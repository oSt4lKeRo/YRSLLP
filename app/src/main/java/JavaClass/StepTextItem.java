package JavaClass;

import android.os.Parcel;
import android.os.Parcelable;

// Подкласс для текста
public class StepTextItem extends StructureClass.StepContent {
    int id;
    private String text;

    public StepTextItem(int id, String text) {
        this.text = text;
    }

    protected StepTextItem(Parcel in) {
        id = in.readInt();
        text = in.readString();
    }
    @Override
    public int getType() {
        return TEXT_TYPE;
    }
    public int getId() { return id; }

    public Object getContent(){
        return text;
    }
}
