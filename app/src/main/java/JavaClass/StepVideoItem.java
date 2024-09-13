package JavaClass;

import android.os.Parcel;
import android.os.Parcelable;

// Подкласс для видео
public class StepVideoItem extends StructureClass.StepContent implements Parcelable {
    int id;
    private String videoUrl;
    public StepVideoItem(int id, String videoUrl) {
        this.videoUrl = videoUrl;
    }

    protected StepVideoItem(Parcel in) {
        id = in.readInt();
        videoUrl = in.readString();
    }

    public static final Creator<StepVideoItem> CREATOR = new Creator<StepVideoItem>() {
        @Override
        public StepVideoItem createFromParcel(Parcel in) {
            return new StepVideoItem(in);
        }

        @Override
        public StepVideoItem[] newArray(int size) {
            return new StepVideoItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(videoUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int getType() {
        return VIDEO_TYPE;
    }
    public int getId(){
        return id;
    }
    public Object getContent(){
        return videoUrl;
    }
}
