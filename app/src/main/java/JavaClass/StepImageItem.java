package JavaClass;

import android.os.Parcel;
import android.os.Parcelable;

// Подкласс для изображения
public class StepImageItem extends StructureClass.StepContent implements Parcelable {
    int id;
    private String imageUrl;

    public StepImageItem(int id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    protected StepImageItem(Parcel in){
        id = in.readInt();
        imageUrl = in.readString();
    }

    public static final Creator<StepImageItem> CREATOR = new Creator<StepImageItem>() {
        @Override
        public StepImageItem createFromParcel(Parcel in) {
            return new StepImageItem(in);
        }

        @Override
        public StepImageItem[] newArray(int size) {
            return new StepImageItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int getType() {
        return IMAGE_TYPE;
    }

    @Override
    public int getId(){return id;}

    @Override
    public Object getContent(){return imageUrl;}
}
