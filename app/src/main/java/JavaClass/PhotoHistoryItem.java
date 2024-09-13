package JavaClass;
public class PhotoHistoryItem {
    private String photoPath;
    private String response;

    public PhotoHistoryItem(String photoPath, String response) {
        this.photoPath = photoPath;
        this.response = response;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public String getResponse() {
        return response;
    }
}

