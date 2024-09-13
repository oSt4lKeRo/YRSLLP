package com.example.yrsllp;

import static JavaClass.JsonHelper.parsePhotoPrediction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import JavaClass.PhotoPrediction;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PhotoMakeFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private ImageView imageView;
    private Bitmap capturedImage;
    private ImageView historyButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_make_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.photo_make_image_view);
        Button captureButton = view.findViewById(R.id.photo_make_photoCaptureButton);
        Button pickButton = view.findViewById(R.id.photo_make_photoPickButton);
        Button uploadButton = view.findViewById(R.id.photo_make_photoUploadButton);
        historyButton = view.findViewById(R.id.photo_make_historyButton);

        captureButton.setOnClickListener(v -> dispatchTakePictureIntent());
        pickButton.setOnClickListener(v -> dispatchPickPictureIntent());
        uploadButton.setOnClickListener(v -> uploadPhoto());
        historyButton.setOnClickListener(v -> navigateToHistoryFragment());
    }

    private void navigateToHistoryFragment() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_photoMakeFragment_to_photoHistoryFragment);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchPickPictureIntent() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPictureIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                handleImageCapture(data);
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                handleImagePick(data);
            }
        }
    }

    private void handleImageCapture(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        if (imageBitmap != null) {
            capturedImage = imageBitmap;
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void handleImagePick(Intent data) {
        Uri selectedImageUri = data.getData();
        if (selectedImageUri != null) {
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    capturedImage = bitmap;
                    imageView.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                Log.e("PhotoFragment", "Error loading image from gallery", e);
            }
        }
    }

    private void uploadPhoto() {
        if (capturedImage != null) {
            sendPhotoToServer(capturedImage);
        } else {
            Toast.makeText(getActivity(), "Изначально выберите или сделайте фото!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendPhotoToServer(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), encodedImage);

        Request request = new Request.Builder()
                .url("https://detect.roboflow.com/sign-language-recognition-mmbok/1?api_key=Bp3lQ8Ly0fYmqeLRavPN")
                .post(requestBody)
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String serverResponse = response.body().string();
                    PhotoPrediction photoPrediction = parsePhotoPrediction(serverResponse);
                    String resp = "Вероятность: " + photoPrediction.getConfidence() + ", Слово: " + photoPrediction.getPredictionClass();
                    saveImageAndResponse(bitmap, resp);
                    Log.i("PhotoFragment", serverResponse);
                } else {
                    Log.e("PhotoFragment", response.toString());
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Ошибка при отправке", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                Log.e("PhotoFragment", "Проблема при отправке", e);
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void saveImageAndResponse(Bitmap bitmap, String response) {
        try {
            File imageFile = createImageFile();
            try (FileOutputStream out = new FileOutputStream(imageFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            File responseFile = new File(imageFile.getParent(), imageFile.getName().replace(".jpg", ".txt"));
            try (FileOutputStream out = new FileOutputStream(responseFile)) {
                out.write(response.getBytes());
            }
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Изображение и ответ успешно сохранены!", Toast.LENGTH_SHORT).show());
        } catch (IOException e) {
            Log.e("PhotoFragment", "Ошибка при сохранении изображения или ответа", e);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
}
