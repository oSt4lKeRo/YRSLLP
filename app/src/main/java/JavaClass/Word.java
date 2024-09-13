package JavaClass;

import java.util.ArrayList;

public class Word {

private int id;
private String spelling;
private String videoURL;

    public Word(int id, String spelling, String videoURL) {
        this.id = id;
        this.spelling = spelling;
        this.videoURL = videoURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpelling() {
        return spelling;
    }

    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    @Override
    public String toString() {
        return   spelling + ", ";
    }

    public static String[] getSpellingArray(ArrayList<Word> wordList) {
        String[] spellings = new String[wordList.size()];
        for (int i = 0; i < wordList.size(); i++) {
            spellings[i] = wordList.get(i).getSpelling();
        }
        return spellings;
    }

    public static Word findWordBySpelling(ArrayList<Word> wordList, String spelling) {
        for (Word word : wordList) {
            if (word.getSpelling().equals(spelling)) {
                return word;
            }
        }
        return null;
    }
}
