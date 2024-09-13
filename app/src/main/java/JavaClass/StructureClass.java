package JavaClass;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class StructureClass {
    public static class Module{
        private int id;
        private AttributesModule attributes;

        public Module(int id, AttributesModule attributes) {
            this.id = id;
            this.attributes = attributes;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public AttributesModule getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesModule attributes) {
            this.attributes = attributes;
        }

        @Override
        public String toString() {
            if(attributes == null){
                return "Truuuuuuuoooooooble";
            } else {
                return "Module{" +
                        "id=" + id +
                        ", attributes=" + attributes.toString() +
                        '}';
            }
        }
    }

    public static class Lesson{
        private int id;
        private AttributesLecture attributes;
        public Lesson(int id, AttributesLecture attributes) {
            this.id = id;
            this.attributes = attributes;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public AttributesLecture getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesLecture attributes) {
            this.attributes = attributes;
        }
    }

    public static class Step{
        private int id;
        private AttributesStep attributes;

        public Step(int id, AttributesStep attributes) {
            this.id = id;
            this.attributes = attributes;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public AttributesStep getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesStep attributes) {
            this.attributes = attributes;
        }
    }

    public static class AttributesStep{
        private String title;
        private String description;
        private String createdAt;
        private String updatedAt;
        private String publishedAt;
        private ArrayList<StepContent> content;
        private Type type;

        public  AttributesStep(String title, String description, String createdAt, String updatedAt, String publishedAt, ArrayList<StepContent> content, Type type) {
            this.title = title;
            this.description = description;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.publishedAt = publishedAt;
            this.content = content;
            this.type = type;
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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public ArrayList<StepContent> getContent() {
            return content;
        }

        public void setContent(ArrayList<StepContent> content) {
            this.content = content;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "AttributesStep{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", content=" + content +
                    ", type=" + type +
                    '}';
        }
    }

    public static class AttributesLecture{
        private String title;
        private String description;
        private String createdAt;
        private String updatedAt;
        private String publishedAt;
        List<Step> steps;
        private Status status;

        public AttributesLecture(String title, String description, String createdAt, String updatedAt, String publishedAt, List<Step> steps, Status status) {
            this.title = title;
            this.description = description;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.publishedAt = publishedAt;
            this.steps = steps;
            this.status = status;
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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public List<Step> getStep(){return steps;}

        public void setLectures(List<Step> steps){this.steps = steps;}

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }
    }

    public static class  AttributesModule{
        private String title;
        private String description;
        private String createdAt;
        private String updatedAt;
        private String publishedAt;
        private List<Lesson> lectures;
        private Status status;
        private int imageResource;

        private int progressStatus;

        public  AttributesModule(String title, String description, String createdAt, String updatedAt, String publishedAt, List<Lesson> lectures, Status status) {
            this.title = title;
            this.description = description;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.publishedAt = publishedAt;
            this.lectures = lectures;
            this.status = status;
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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public List<Lesson> getLectures(){return lectures;}

        public void setLectures(List<Lesson> lectures){this.lectures = lectures;}

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public int getImageResource() { return imageResource; }

        public void setImageResource(int imageResource) { this.imageResource = imageResource; }

        public int getProgressStatus() { return progressStatus; }

        public void setProgressStatus(int progressStatus) {
            this.progressStatus = progressStatus;
        }


        @Override
        public String toString() {
            return "AttributesModule{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", lectures=" + lectures +
                    ", status=" + status.status +
                    '}';
        }
    }

    public static class Status{
        private int id;
        private String status;

        public Status(int id, String status) {
            this.id = id;
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


    public static abstract class StepContent {
        int id;
        public static final int TEXT_TYPE = 0;
        public static final int IMAGE_TYPE = 1;
        public static final int VIDEO_TYPE = 2;
        abstract public int getType();
        abstract public int getId();
        abstract public Object getContent();
    }
  
  
//    public static class StepContent{
//        private int id;
//        private String type;
//        private String content;
//
//        public StepContent(int id, String type, String content) {
//            this.id = id;
//            this.type = type;
//            this.content = content;
//        }
//
//        public StepContent(){}
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getContent() {
//            return content;
//        }
//
//        public void setContent(String content) {
//            this.content = content;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//    }


    public class Type{
        private int id;
        private String type;

        public Type(int id, String type) {
            this.id = id;
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
