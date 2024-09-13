package JavaClass;

import static JavaClass.HttpHelper.getContent;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class JsonHelper {

    public static ArrayList<StructureClass.Module> readModule(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray data = jsonObject.getJSONArray("data");

        ArrayList<StructureClass.Module> moduleList = new ArrayList<>();
        for(int i = 0; i < data.length(); i++) {
            JSONObject object = data.getJSONObject(i);
            int id = object.getInt("id");
            JSONObject attributes = object.getJSONObject("attributes");

            String title = attributes.getString("title");
            String description = attributes.getString("description");

            JSONObject statusJSON = attributes.getJSONObject("status");
            int statusId = statusJSON.getInt("id");
            String statusText = statusJSON.getString("status");
            StructureClass.Status status = new StructureClass.Status(statusId, statusText);

            StructureClass.AttributesModule attributesModule = new StructureClass.AttributesModule(title, description, null, null, null, null, status);
            StructureClass.Module module = new StructureClass.Module(id, attributesModule);
            moduleList.add(module);
        }
        return moduleList;
    }

    public static ArrayList<StructureClass.Lesson> readLectures(String response) throws JSONException{
        JSONObject jsonObject = new JSONObject(response);
        ArrayList<StructureClass.Lesson> lessons = new ArrayList<>();

        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject attributeModule = data.getJSONObject("attributes");
        Log.e("LectureRead", attributeModule.toString());
        JSONObject lessonsFromJSON = attributeModule.getJSONObject("lectures");
        JSONArray dataLectures = lessonsFromJSON.getJSONArray("data");
        for(int i = 0; i < dataLectures.length(); i++){
            JSONObject object = dataLectures.getJSONObject(i);
            int id = object.getInt("id");

            JSONObject attribute = object.getJSONObject("attributes");
            String title = attribute.getString("title");
            String description = attribute.getString("description");

            StructureClass.AttributesLecture attributesLesson = new StructureClass.AttributesLecture(title, description, null, null, null, null, null);
            StructureClass.Lesson lesson = new StructureClass.Lesson(id, attributesLesson);

            Log.e("LectureRead", id + " " + title + " " + description);
            lessons.add(lesson);
        }
        return lessons;
    }


    public static ArrayList<StructureClass.Step> readStepFromLectures(String response) throws JSONException, IOException {
        ArrayList<StructureClass.Step> steps = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response);
        JSONObject Data = jsonObject.getJSONObject("data");
        JSONObject Attribute = Data.getJSONObject("attributes");
        JSONObject stepInfo = Attribute.getJSONObject("steps");
        JSONArray stepsData = stepInfo.getJSONArray("data");
        for(int i = 0; i < stepsData.length(); i++){
            JSONObject obj = stepsData.getJSONObject(i);
            int id = obj.getInt("id");

            String stepResponse = getContent("http://51.250.97.205:1337/api/steps/" + id + "?populate=*");
            Log.i("stepContentResponce", stepResponse);
            jsonObject = new JSONObject(stepResponse);
            Data = jsonObject.getJSONObject("data");

            JSONObject attribute = Data.getJSONObject("attributes");
            String title = attribute.getString("title");
            String description = attribute.getString("description");

            JSONArray contentList = attribute.getJSONArray("content");
            ArrayList<StructureClass.StepContent> contents = new ArrayList<>();
            for(int j = 0; j < contentList.length(); j++){
                JSONObject objContent = contentList.getJSONObject(j);

                int contentId = objContent.getInt("id");

                String[] type = objContent.getString("__component").split("s.");
                switch (type[1]){
                    case "text":
                        contents.add(new StepTextItem(contentId, objContent.getString(type[1])));
                        break;
                    case "video":
                        contents.add(new StepVideoItem(contentId, objContent.getString("url")));
                        break;
                    case "image":
                        contents.add(new StepImageItem(contentId, objContent.getString(type[1])));
                        break;
                }
            }

            StructureClass.AttributesStep attributesStep = new StructureClass.AttributesStep(title, description, null, null, null, contents, null);
            StructureClass.Step step = new StructureClass.Step(id, attributesStep);
            steps.add(step);
        }
        return steps;
    }


    public static ArrayList<Category> readCategories(String response) throws JSONException, IOException {
        ArrayList<Category> categories = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(response);
        JSONArray data = jsonObject.getJSONArray("data");
        for(int i = 0; i < data.length(); i++){
            JSONObject category = data.getJSONObject(i);
            int id = category.getInt("id");
            String title = category.getString("title");
            String description = category.getString("description");
            String imageURl = category.getString("imageURL");
            int order = category.getInt("order");
            
            String wordResponse = getContent("http://51.250.97.205:1337/api/categories/" + id + "?publicationState=preview&populate=*");
            jsonObject = new JSONObject(wordResponse);
            JSONObject dataWord = jsonObject.getJSONObject("data");

            ArrayList<Word> wordsList = new ArrayList<>();
            JSONArray words = dataWord.getJSONArray("words");
            for(int j = 0; j < words.length(); j++){
                JSONObject word = words.getJSONObject(j);

                int wordId = word.getInt("id");
                String spelling = word.getString("spelling");
                String videoURL = word.getString("videoURL");

                wordsList.add(new Word(wordId, spelling, videoURL));
            }
            categories.add(new Category(id, title, description, imageURl, order, wordsList));
        }

        return categories;
    }


    public static ArrayList<Word> readWordExampleResponse(String response) throws JSONException {
        ArrayList<Word> wordExample = new ArrayList<>();

        JSONObject resp = new JSONObject(response);
        JSONArray data = resp.getJSONArray("data");
        for(int i = 0; i < data.length(); i++) {
            JSONObject word = data.getJSONObject(i);

            String spelling = word.getString("spelling");
            spelling = spelling.toLowerCase();
            wordExample.add(new Word(word.getInt("id"), spelling, word.getString("videoURL")));
        }
        return wordExample;
    }

    public static PhotoPrediction parsePhotoPrediction(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray predictions = jsonObject.getJSONArray("predictions");

            if (predictions.length() > 0) {
                JSONObject firstPrediction = predictions.getJSONObject(0);
                double confidence = firstPrediction.getDouble("confidence");
                String predictionClass = firstPrediction.getString("class");

                return new PhotoPrediction(confidence, predictionClass);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static StructureClass.Step readStep(String response) throws JSONException {
//        JSONObject json = new JSONObject(response);
//        JSONObject jsonObject = json.getJSONObject("data");
//        int id = jsonObject.getInt("id");
//        JSONObject stepAttribute = jsonObject.getJSONObject("attributes");
//        String title = stepAttribute.getString("title");
//
//        JSONArray contentList = stepAttribute.getJSONArray("content");
//        ArrayList<StructureClass.StepContent> contents = new ArrayList<>();
//        for(int i = 0; i < contentList.length(); i++){
//            JSONObject obj = contentList.getJSONObject(i);
//            String text = obj.getString("text");
//            StructureClass.StepContent content = new StructureClass.StepContent(i, null, text);
//            contents.add(content);
//        }
//        StructureClass.AttributesStep attributesStep = new StructureClass.AttributesStep(title, null, null, null, null, contents, null);
//
//        StructureClass.Step step = new StructureClass.Step(id, attributesStep);
//        return step;
//    }

}
