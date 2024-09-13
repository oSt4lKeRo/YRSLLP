package JavaClass;

import static JavaClass.HttpHelper.getContent;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app.db";
    private static final int DATABASE_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление схемы базы данных (если необходимо)
//        db.execSQL("DROP TABLE IF EXISTS your_table_name");
//        onCreate(db);
    }

    public static void saveModuleToDB(SQLiteDatabase db, StructureClass.Module module) throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<StructureClass.Lesson> lessons = new ArrayList<>();
                    ArrayList<StructureClass.Step> steps = new ArrayList<>();

                    db.execSQL("INSERT OR REPLACE INTO modules VALUES (" + module.getId() + ", '"+ module.getAttributes().getTitle() + "', '" + module.getAttributes().getDescription() + "')");
                    Log.i("moduleSave", "Module " + module.getId() + " save to db");

                    String json = getContent("http://51.250.97.205:1337/api/modules/" + module.getId() + "?populate=*");
                    try {
                        lessons = JsonHelper.readLectures(json);
                    } catch (JSONException e) {
                        Log.e("JSONTroubleLecture", e.getLocalizedMessage());
                    }

                    for(StructureClass.Lesson lesson : lessons){
                        db.execSQL("INSERT OR REPLACE INTO lessons VALUES (" + lesson.getId() + ", " + module.getId() + ", '" + lesson.getAttributes().getTitle() + "', '" + lesson.getAttributes().getDescription() + "')");
                        Log.i("lectureSave", "Lecture " + lesson.getId() + " save to db");
                        json = getContent("http://51.250.97.205:1337/api/lectures/" + lesson.getId() + "?populate=*");
                        try {
                            steps = JsonHelper.readStepFromLectures(json);
                        } catch (JSONException e){
                            Log.e("JSONTroubleStep", e.getLocalizedMessage());
                        }

                        for(StructureClass.Step step : steps){
                            db.execSQL("INSERT OR REPLACE INTO steps VALUES (" + step.getId() + ", " + lesson.getId() + ", '" + step.getAttributes().getTitle() + "', '" + step.getAttributes().getDescription() + "')");
                            Log.i("stepSave", "Step " + step.getId() + " save to db");

                            ArrayList<StructureClass.StepContent> contents = step.getAttributes().getContent();
                            for(StructureClass.StepContent content : contents){
                                db.execSQL("INSERT OR REPLACE INTO content VALUES (" + content.getId() + ", " + step.getId() + ", '" + content.getType() + "', '" + content.getContent() + "')");
                                Log.i("contentSave", "Content " + content.getId() + " save to db");
                            }
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }).start();
    }

    public static ArrayList<StructureClass.Module> insertModule(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM modules", null);

        ArrayList<StructureClass.Module> moduleList = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            StructureClass.AttributesModule attributesModule = new StructureClass.AttributesModule(title, description, null, null, null, null, null);
            StructureClass.Module module = new StructureClass.Module(id, attributesModule);
            moduleList.add(module);
            Log.i("DBTag", "Прочитано из БД: " + String.valueOf(id) + " " + title + " " + description);
        }
        cursor.close();
        return moduleList;
    }

    public static ArrayList<StructureClass.Lesson> insertLessons(SQLiteDatabase db, String moduleNumber){
        Cursor cursor = db.rawQuery("SELECT * FROM lessons WHERE moduleNumber = ?", new String[]{moduleNumber});
        ArrayList<StructureClass.Lesson> lessonList = new ArrayList<>();
        
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String title = cursor.getString(2);
            String description = cursor.getString(3);
            StructureClass.AttributesLecture attributesLecture = new StructureClass.AttributesLecture(title, description, null, null, null, null, null);
            StructureClass.Lesson lesson = new StructureClass.Lesson(id, attributesLecture);
            lessonList.add(lesson);
            Log.i("DBTag", "Прочитано из БД: " + String.valueOf(id) + " " + title + " " + description);
        }
        cursor.close();
        return lessonList;
    }

    
    public static ArrayList<StructureClass.Step> insertSteps(SQLiteDatabase db, String lessonNumber){
        Cursor cursorStep = db.rawQuery("SELECT * FROM steps WHERE lectureNumber = ?", new String[]{lessonNumber});
        ArrayList<StructureClass.Step> steps = new ArrayList<>();
        
        while (cursorStep.moveToNext()){

            int idStep = cursorStep.getInt(0);
            String titleStep = cursorStep.getString(2);
            String descriptionStep = cursorStep.getString(3);

            Cursor cursorContent = db.rawQuery("SELECT * FROM content WHERE stepNumber = ?", new String[]{String.valueOf(idStep)});
            ArrayList<StructureClass.StepContent> contents = new ArrayList<>();

            while (cursorContent.moveToNext()){
                int contentId = cursorContent.getInt(0);
                String contentType = cursorContent.getString(2);
                String contentText = cursorContent.getString(3);

                switch (contentType){
                    case "text":
                        contents.add(new StepTextItem(contentId, contentText));
                        break;
                    case "video":
//                        contents.add(new StepVideoItem(contentId, contentText));
                        break;
                    case "image":
//                        contents.add(new StepImageItem(contentId, contentText));
                        break;
                }
            }
            cursorContent.close();

            StructureClass.AttributesStep attributesStep = new StructureClass.AttributesStep(titleStep, descriptionStep, null, null, null, contents, null);
            StructureClass.Step step = new StructureClass.Step(idStep, attributesStep);
            steps.add(step);
        }
        cursorStep.close();
        return steps;
    }


    public static String getNameFromDB(SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM user", null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public static void clearModuleTable(SQLiteDatabase db){
        db.execSQL("DELETE FROM modules");
    }
    public static void clearLessonsTable(SQLiteDatabase db){
        db.execSQL("DELETE FROM lessons");
    }
    public static void clearStepTable(SQLiteDatabase db){
        db.execSQL("DELETE FROM steps");
    }
    public static void clearUserTable(SQLiteDatabase db){ db.execSQL("DELETE FROM user");}
    public static void clearDB(SQLiteDatabase db){
        db.execSQL("DELETE FROM modules");
        db.execSQL("DELETE FROM lessons");
        db.execSQL("DELETE FROM steps");
        db.execSQL("DELETE FROM user");
    }

    public static void clearModuleInfo(SQLiteDatabase db){
        db.execSQL("DELETE FROM modules");
        db.execSQL("DELETE FROM lessons");
        db.execSQL("DELETE FROM steps");
        db.execSQL("DELETE FROM content");
    }


    public static void deleteDB(SQLiteDatabase db){
        db.execSQL("DROP TABLE modules");
        db.execSQL("DROP TABLE lessons");
        db.execSQL("DROP TABLE steps");
        db.execSQL("DROP TABLE user");
    }


    public static void dropExtraModule(SQLiteDatabase db, int maxId){
        db.execSQL("DELETE FROM modules WHERE id > ?", new String[]{String.valueOf(maxId)});
    }


    public static boolean isDatabaseNameEmpty(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM user", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        Log.i("countZapis", String.valueOf(count));
        cursor.close();
        return count == 0;
    }

    public static boolean checkModuleExistsById(SQLiteDatabase db, int moduleId) {
        Cursor cursor = db.rawQuery("SELECT id FROM modules WHERE id = ?", new String[] { String.valueOf(moduleId) });
        boolean moduleExists = cursor.getCount() > 0;
        cursor.close();
        return moduleExists;
    }
}
