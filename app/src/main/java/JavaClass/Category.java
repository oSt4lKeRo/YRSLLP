package JavaClass;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Category {

    private int id;
    private String title;
    private String description;
    private String imageURL;
    private int order;
    private List<Word> words;

    public Category(int id, String title, String description, String imageURL, int order, List<Word> words) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.order = order;
        this.words = words;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<Word> getWords(){
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public static ArrayList<Word> getAllWord(List<Category> categories){
        ArrayList<Word> wordsGet = new ArrayList<>();

        for(Category category : categories){
            wordsGet.addAll(category.getWords());
        }

        return wordsGet;
    }

}
