package JavaClass;

import java.util.ArrayList;

public class ListObject {

    private String name;

    public ListObject(){}

    public ListObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toString();
    }
    
    public ListObject valueOf(StructureClass.Lesson lesson){
        return new ListObject(lesson.getAttributes().getTitle());
    }

    public ListObject valueOf(StructureClass.Step step){
        return new ListObject(step.getAttributes().getTitle());
    }

    public ArrayList<ListObject> valueOfToArray(ArrayList<StructureClass.Step> steps){
        ArrayList<ListObject> listObjects = new ArrayList<>();
        for(StructureClass.Step step : steps){
            ListObject listObject = new ListObject(step.getAttributes().getTitle());
            listObjects.add(listObject);
        }
        return listObjects;
    }

}
