package sebastians.challenge.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.data.interfaces.TitleDescriptionColumns;

/**
 * Created by sebastian on 13/06/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 21;
    public static final String DATABASE_NAME = "challenge_db";
    public static final String LOG_TAG = "DB";

    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;
    public static DatabaseHelper instance = null;


    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        writableDatabase = super.getWritableDatabase();
        readableDatabase = super.getReadableDatabase();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);

            if(instance.getAllChallenges().size() == 0)
                instance.createDefaultData();
            Log.i(LOG_TAG, "Database instantiated.");
        } else
            Log.e(LOG_TAG, "Attempted to re-instantiate database.");
    }

    public static DatabaseHelper getInstance() {
        return instance;
    }

    public static void stop() {
        instance.writableDatabase.close();
        instance.readableDatabase.close();
        Log.i(LOG_TAG, "Database connection closed.");
    }

    public List<Challenge> getActiveChallenges(){
        List<Challenge> challenges = new ArrayList<>();
        Cursor cursor = readableDatabase.query(
                Contract.ChallengeEntry.TABLE_NAME,
                new String[]{Contract.ChallengeEntry._ID},
                Contract.ChallengeEntry.ACTIVE + "= ?",
                new String[]{"1"},
                null,
                null,
                Contract.ChallengeEntry.ACTIVE +" DESC");
        while (cursor.moveToNext()) {
            challenges.add(getChallengeById(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeEntry._ID))));
        }
        return challenges;
    }

    /**
     * Fetch alle Challenges from database
     * @return List of Challenges
     */
    public List<Challenge> getAllChallenges(){

        List<Challenge> challenges = new ArrayList<>();
        Cursor cursor = readableDatabase.query(
                Contract.ChallengeEntry.TABLE_NAME,
                new String[]{Contract.ChallengeEntry._ID},
                null,
                null,
                null,
                null,
                Contract.ChallengeEntry.ACTIVE +" DESC");
        while (cursor.moveToNext()) {
            challenges.add(getChallengeById(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeEntry._ID))));
        }
        return challenges;
    }

    /**
     * request challenge by id
     * @param id
     * @return challenge object from database
     */
    public Challenge getChallengeById(long id){
        Cursor cursor = readableDatabase.query(
                Contract.ChallengeEntry.TABLE_NAME,
                new String[]{Contract.ChallengeEntry._ID, Contract.ChallengeEntry.TITLE, Contract.ChallengeEntry.DESCRIPTION, Contract.ChallengeEntry.ACTIVE, Contract.ChallengeEntry.ACTIVATEDTS},
                Contract.ChallengeEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);
        cursor.moveToFirst();



        return cursor2Challenge(cursor);
    }

    public Task getTaskById(long id){
        Cursor cursor = readableDatabase.query(
                Contract.ChallengeItemEntry.TABLE_NAME,
                new String[]{Contract.ChallengeItemEntry._ID,
                        Contract.ChallengeItemEntry.TITLE,
                        Contract.ChallengeItemEntry.DESCRIPTION,
                        Contract.ChallengeItemEntry.TIME_AFTER_PREV,
                        Contract.ChallengeItemEntry.ORDER,
                        Contract.ChallengeItemEntry.DONE,
                        Contract.ChallengeItemEntry.SELFIE,
                        Contract.ChallengeItemEntry.DISMISSED,

                },
                Contract.ChallengeItemEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);
        cursor.moveToFirst();
        List<ImagePath> imagePaths = new ArrayList<>();
        long itemId = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry._ID));

        Cursor imgsIdsCursor = readableDatabase.query(
                Contract.Item2ImageEntry.TABLE_NAME,
                new String[]{Contract.Item2ImageEntry.CHALLENGEITEM,
                        Contract.Item2ImageEntry.IMAGE,
                },
                Contract.Item2ImageEntry.CHALLENGEITEM + " = ?",
                new String[]{String.valueOf(itemId)},
                null,
                null,
                null);


        while (imgsIdsCursor.moveToNext()){
            long imageId  = imgsIdsCursor.getInt(imgsIdsCursor.getColumnIndexOrThrow(Contract.Item2ImageEntry.IMAGE));
            Cursor imgsCursor = readableDatabase.query(
                    Contract.ImageEntry.TABLE_NAME,
                    new String[]{Contract.ImageEntry.PATH,Contract.ImageEntry._ID
                    },
                    Contract.ImageEntry._ID + " = ?",
                    new String[]{String.valueOf(imageId)},
                    null,
                    null,
                    null);
            imgsCursor.moveToFirst();
            String imgPath = imgsCursor.getString(imgsCursor.getColumnIndexOrThrow(Contract.ImageEntry.PATH));
            long imgDbId = imgsCursor.getInt(imgsCursor.getColumnIndexOrThrow(Contract.ImageEntry._ID));
            ImagePath imagePath = new ImagePath(imgPath,imgDbId);
            imagePaths.add(imagePath);
        }



        Task task = new Task(
                cursor.getString(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry.TITLE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry._ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry.DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry.TIME_AFTER_PREV)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry.ORDER)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry.DONE)) > 0,
                cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry.DISMISSED)) > 0,
                new ImagePath(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry.SELFIE))),
                imagePaths);

        return task;
    }

    /**
     * map cursor to challenge
     * @param cursor
     * @return
     */
    public Challenge cursor2Challenge(Cursor cursor){
        Challenge challenge;
        long challengeDbId = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeEntry._ID));
        String challengeTitle = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ChallengeEntry.TITLE));
        String challengeDescription = cursor.getString(cursor.getColumnIndexOrThrow(Contract.ChallengeEntry.DESCRIPTION));
        boolean isActive = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeEntry.ACTIVE)) > 0;
        long activatedTs = cursor.getLong(cursor.getColumnIndexOrThrow(Contract.ChallengeEntry.ACTIVATEDTS));

        challenge = new Challenge(
                challengeTitle,
                challengeDbId,
                challengeDescription,
                isActive,
                this.getChallengeItemsForChallengeId(challengeDbId)
        );
        challenge.setActivatedTs(activatedTs);

        return challenge;
    }

    /**
     *
     * @param id
     * @return
     */
    public List<Task> getChallengeItemsForChallengeId(long id){
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = readableDatabase.query(
                Contract.ChallengeItemEntry.TABLE_NAME,
                new String[]{Contract.ChallengeItemEntry._ID,


                },
                Contract.ChallengeItemEntry.CHALLENGE + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                Contract.ChallengeItemEntry.ORDER);

        while (cursor.moveToNext()) {
            long itemId = cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry._ID));
            tasks.add(this.getTaskById(itemId));
        }

        return tasks;
    }

    /**
     * save new Challenge entry in database
     * @param challenge
     * @return created id!
     */
    public Challenge create(Challenge challenge){

        //create challenge in database
        ContentValues cv = new ContentValues();
        cv.put(Contract.ChallengeEntry.TITLE, challenge.getName());
        cv.put(Contract.ChallengeEntry.DESCRIPTION, challenge.getDescription());
        cv.put(Contract.ChallengeEntry.ACTIVE,challenge.isActive());
        cv.put(Contract.ChallengeEntry.ACTIVATEDTS, challenge.getActivatedTs());
        long challengeId = writableDatabase.insert(Contract.ChallengeEntry.TABLE_NAME, null, cv);

        //create challenge itemss
        for(int i = 0; i < challenge.getChallengeItemList().size(); i++){
            Task task = challenge.getChallengeItemList().get(i);
            cv = new ContentValues();
            cv.put(Contract.ChallengeItemEntry.CHALLENGE,challengeId);
            cv.put(Contract.ChallengeItemEntry.TITLE, task.getTitle());
            cv.put(Contract.ChallengeItemEntry.DESCRIPTION, task.getDescription());
            cv.put(Contract.ChallengeItemEntry.ORDER, task.getOrder());
            cv.put(Contract.ChallengeItemEntry.TIME_AFTER_PREV, task.getDurationValidity());
            cv.put(Contract.ChallengeItemEntry.SELFIE, task.getSelfie().getPath());
            long itemId = writableDatabase.insert(Contract.ChallengeItemEntry.TABLE_NAME,null,cv);

            for(int j = 0; j < task.getImagePaths().size(); j++){
                //insert all image paths to database
                ImagePath imagePath = task.getImagePaths().get(j);
                cv = new ContentValues();
                cv.put(Contract.ImageEntry.PATH,imagePath.getPath());
                long imageId = writableDatabase.insert(Contract.ImageEntry.TABLE_NAME,null,cv);

                cv = new ContentValues();
                cv.put(Contract.Item2ImageEntry.IMAGE,imageId);
                cv.put(Contract.Item2ImageEntry.CHALLENGEITEM,itemId);
                writableDatabase.insert(Contract.Item2ImageEntry.TABLE_NAME, null, cv);
            }

        }


        return this.getChallengeById(challengeId);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        //create tables:
        db.execSQL(Contract.SQL_CREATE_TABLE_CHALLENGES);
        db.execSQL(Contract.SQL_CREATE_TABLE_CHALLENGEITEMS);
        db.execSQL(Contract.SQL_CREATE_TABLE_IMAGES);
        db.execSQL(Contract.SQL_CREATE_TABLE_ITEMS2IMAGES);




    }

    /**
     * populate database with default Challenges etc.
     */
    private void createDefaultData() {

        //add first challenge to database

        Challenge smoothieChallenge = new Challenge("Smoothie Challenge");
        smoothieChallenge.setActivatedTs(System.currentTimeMillis() / 1000);
        smoothieChallenge.setActive(true);
        //add some ChallengeItems
        ArrayList<Task> tasks = new ArrayList<>();

        Task task;
        task = new Task("Day 1", "Stuff to do");
        ArrayList<ImagePath> imgs = new ArrayList<>();
        imgs.add(new ImagePath("d1img1"));
        imgs.add(new ImagePath("d1img2"));
        task.setImagePaths(imgs);
        task.setDurationValidity(120);
        task.setOrder(0);
        tasks.add(task);

        task = new Task("Day 2", "Stuff to do");
        imgs = new ArrayList<>();
        imgs.add(new ImagePath("d2img1"));
        imgs.add(new ImagePath("d2img2"));
        task.setImagePaths(imgs);
        task.setOrder(1);
        task.setDurationValidity(120);
        tasks.add(task);

        task = new Task("Day 3", " even more Stuff to do");
        imgs = new ArrayList<>();
        imgs.add(new ImagePath("d2img1"));
        imgs.add(new ImagePath("d2img2"));
        task.setImagePaths(imgs);
        task.setOrder(2);
        task.setDurationValidity(120);
        tasks.add(task);

        task = new Task("Day 4", " dude this is lots of Stuff to do");
        imgs = new ArrayList<>();
        imgs.add(new ImagePath("d2img1"));
        imgs.add(new ImagePath("d2img2"));
        task.setImagePaths(imgs);
        task.setOrder(3);
        task.setDurationValidity(120);
        tasks.add(task);

        smoothieChallenge.setTaskList(tasks);
        this.create(smoothieChallenge);


        ContentValues cv = new ContentValues();

        cv = new ContentValues();
        cv.put(Contract.ChallengeEntry.TITLE, "Running Challenge");
        cv.put(Contract.ChallengeEntry.DESCRIPTION, "Run Forrest");
        cv.put(Contract.ChallengeEntry.ACTIVE, 0);
        writableDatabase.insert(Contract.ChallengeEntry.TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(Contract.ChallengeEntry.TITLE, "Running Challenge 1");
        cv.put(Contract.ChallengeEntry.DESCRIPTION, "Run Forrest");
        cv.put(Contract.ChallengeEntry.ACTIVE, 1);
        writableDatabase.insert(Contract.ChallengeEntry.TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(Contract.ChallengeEntry.TITLE, "Running Challenge 2 ");
        cv.put(Contract.ChallengeEntry.DESCRIPTION, "Run Forrest");
        cv.put(Contract.ChallengeEntry.ACTIVE, 0);
        writableDatabase.insert(Contract.ChallengeEntry.TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(Contract.ChallengeEntry.TITLE, "Running Challenge 3");
        cv.put(Contract.ChallengeEntry.DESCRIPTION, "Run Forrest");
        cv.put(Contract.ChallengeEntry.ACTIVE, 0);
        writableDatabase.insert(Contract.ChallengeEntry.TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(Contract.ChallengeEntry.TITLE, "Running Challenge 4");
        cv.put(Contract.ChallengeEntry.DESCRIPTION, "Run Forrest");
        cv.put(Contract.ChallengeEntry.ACTIVE, 0);
        writableDatabase.insert(Contract.ChallengeEntry.TABLE_NAME, null, cv);

        cv = new ContentValues();
        cv.put(Contract.ChallengeEntry.TITLE, "Running Challenge 5");
        cv.put(Contract.ChallengeEntry.DESCRIPTION, "Run Forrest");
        cv.put(Contract.ChallengeEntry.ACTIVE, 0);
        writableDatabase.insert(Contract.ChallengeEntry.TABLE_NAME, null, cv);


        cv = new ContentValues();
        cv.put(Contract.ChallengeEntry.TITLE, "Running Challenge 5");
        cv.put(Contract.ChallengeEntry.DESCRIPTION, "Run Forrest");
        cv.put(Contract.ChallengeEntry.ACTIVE, 0);
        writableDatabase.insert(Contract.ChallengeEntry.TABLE_NAME, null, cv);


        cv = new ContentValues();
        cv.put(Contract.ChallengeEntry.TITLE, "Running Challenge 5");
        cv.put(Contract.ChallengeEntry.DESCRIPTION, "Run Forrest");
        cv.put(Contract.ChallengeEntry.ACTIVE, 0);
        writableDatabase.insert(Contract.ChallengeEntry.TABLE_NAME, null, cv);


        cv = new ContentValues();
        cv.put(Contract.ChallengeEntry.TITLE, "Running Challenge 5");
        cv.put(Contract.ChallengeEntry.DESCRIPTION, "Run Forrest");
        cv.put(Contract.ChallengeEntry.ACTIVE, 0);
        writableDatabase.insert(Contract.ChallengeEntry.TABLE_NAME, null, cv);

    }

    /**
     * update challenge
     * IMPORTANT: -> TODO deep update imagepaths!!
     * @param challenge
     */
    public void update(Challenge challenge){
        //TODO ADD LOGIC DUDE!
        ContentValues cv = new ContentValues();
        cv.put(Contract.ChallengeEntry.TITLE, challenge.getName());
        cv.put(Contract.ChallengeEntry.DESCRIPTION, challenge.getDescription());
        cv.put(Contract.ChallengeEntry.ACTIVE,challenge.isActive());
        cv.put(Contract.ChallengeEntry.ACTIVATEDTS, challenge.getActivatedTs());

        writableDatabase.update(Contract.ChallengeEntry.TABLE_NAME, cv, Contract.ChallengeEntry._ID + " = ?",
                new String[] { String.valueOf(challenge.getDatabaseId()) });

        for(int i = 0; i < challenge.getChallengeItemList().size(); i++){
            this.update(challenge.getChallengeItemList().get(i));
        }

    }

    public void update(Task task){
        ContentValues cv = new ContentValues();
        cv.put(Contract.ChallengeItemEntry.TITLE, task.getTitle());
        cv.put(Contract.ChallengeItemEntry.DESCRIPTION, task.getDescription());
        cv.put(Contract.ChallengeItemEntry.DISMISSED, task.isDismissed());
        cv.put(Contract.ChallengeItemEntry.DONE, task.isDone());
        cv.put(Contract.ChallengeItemEntry.SELFIE, task.getSelfie().getPath());
        cv.put(Contract.ChallengeItemEntry.ORDER, task.getOrder());
        cv.put(Contract.ChallengeItemEntry.TIME_AFTER_PREV, task.getDurationValidity());

        writableDatabase.update(Contract.ChallengeItemEntry.TABLE_NAME, cv, Contract.ChallengeItemEntry._ID + " = ?",
                new String[] { String.valueOf(task.getDatabaseId()) });
    }

    /**
     * TODO add some logic
     * @param db
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL(Contract.SQL_DROP_TABLE_CHALLENGES);
        db.execSQL(Contract.SQL_DROP_TABLE_CHALLENGEITEMS);
        db.execSQL(Contract.SQL_DROP_TABLE_IMAGES);
        db.execSQL(Contract.SQL_DROP_TABLE_ITEMS2IMAGES);


        this.onCreate(db);

    }




    private static class Contract {

        private static final String TYPE_TEXT = " TEXT ";
        private static final String TYPE_INTEGER = " INTEGER ";
        private static final String TYPE_DATETIME = " DATETIME ";
        private static final String TYPE_FLOAT = " FLOAT ";
        private static final String TYPE_PRIMARY = " INTEGER PRIMARY KEY ";
        private static final String COMMA_SEP = ",";
        private static final String DEFAULT = " DEFAULT ";
        private static final String NOT_NULL = " NOT NULL ";




        // Challenges

        protected static abstract class ChallengeEntry implements BaseColumns, TitleDescriptionColumns {
            public static final String TABLE_NAME = "challenges";
            public static final String ACTIVE = "isactive";
            public static final String ACTIVATEDTS = "activatedts";
        }

        protected static final String SQL_CREATE_TABLE_CHALLENGES =
                "CREATE TABLE " + ChallengeEntry.TABLE_NAME + " (" +
                        ChallengeEntry._ID + TYPE_PRIMARY + ", " +
                        ChallengeEntry.ACTIVATEDTS + TYPE_DATETIME   + COMMA_SEP +
                        ChallengeEntry.TITLE + TYPE_TEXT + NOT_NULL  + COMMA_SEP +
                        ChallengeEntry.ACTIVE + TYPE_INTEGER   + COMMA_SEP +
                        ChallengeEntry.DESCRIPTION + TYPE_TEXT + NOT_NULL   +

                        " )";

        protected static final String SQL_DROP_TABLE_CHALLENGES =
                "DROP TABLE IF EXISTS " + ChallengeEntry.TABLE_NAME;



        //Task

        protected static abstract class ChallengeItemEntry implements BaseColumns, TitleDescriptionColumns {
            public static final String TABLE_NAME = "challengeitems";
            public static final String CHALLENGE = "challengeid";
            public static final String TIME_AFTER_PREV = "timeafterprev";
            public static final String ORDER = "position";
            public static final String SELFIE = "selfie";
            public static final String DONE = "done";
            public static final String DISMISSED = "dismissed";

        }

        protected static final String SQL_CREATE_TABLE_CHALLENGEITEMS =
                "CREATE TABLE " + ChallengeItemEntry.TABLE_NAME + "(" +
                        ChallengeItemEntry._ID + TYPE_PRIMARY + ", " +
                        ChallengeItemEntry.TITLE + TYPE_TEXT + NOT_NULL + COMMA_SEP +
                        ChallengeItemEntry.DESCRIPTION + TYPE_TEXT + NOT_NULL + COMMA_SEP +
                        ChallengeItemEntry.CHALLENGE + TYPE_INTEGER + NOT_NULL + COMMA_SEP +
                        ChallengeItemEntry.TIME_AFTER_PREV + TYPE_INTEGER  + COMMA_SEP +
                        ChallengeItemEntry.ORDER + TYPE_INTEGER + NOT_NULL + COMMA_SEP +
                        ChallengeItemEntry.SELFIE + TYPE_TEXT  + COMMA_SEP +
                        ChallengeItemEntry.DONE + TYPE_INTEGER + DEFAULT + " 0 " + COMMA_SEP +
                        ChallengeItemEntry.DISMISSED + TYPE_INTEGER + DEFAULT + " 0 " + COMMA_SEP +
                        "FOREIGN KEY (" + ChallengeItemEntry.CHALLENGE + ") REFERENCES " + ChallengeEntry.TABLE_NAME + "(" + ChallengeEntry ._ID + ")"  +
                        ")";

        protected static final String SQL_DROP_TABLE_CHALLENGEITEMS =
                "DROP TABLE IF EXISTS " + ChallengeItemEntry.TABLE_NAME;


        //images

        protected static abstract class ImageEntry implements BaseColumns {
            public static final String TABLE_NAME = "images";
            public static final String PATH = "path";

        }

        protected static final String SQL_CREATE_TABLE_IMAGES =
                "CREATE TABLE " + ImageEntry.TABLE_NAME + "(" +
                        ImageEntry._ID + TYPE_PRIMARY + ", " +
                        ImageEntry.PATH + TYPE_TEXT +
                ")";

        protected static final String SQL_DROP_TABLE_IMAGES =
                "DROP TABLE IF EXISTS " + ImageEntry.TABLE_NAME;


        protected static abstract class Item2ImageEntry implements BaseColumns {
            public static final String TABLE_NAME = "items2images";
            public static final String IMAGE = "imageid";
            public static final String CHALLENGEITEM = "itemid";
        }

        protected static final String SQL_CREATE_TABLE_ITEMS2IMAGES =
                "CREATE TABLE " + Item2ImageEntry.TABLE_NAME + "(" +
                        Item2ImageEntry._ID + TYPE_PRIMARY + ", " +
                        Item2ImageEntry.IMAGE + TYPE_INTEGER + NOT_NULL + COMMA_SEP +
                        Item2ImageEntry.CHALLENGEITEM + TYPE_INTEGER + NOT_NULL + COMMA_SEP +
                        "FOREIGN KEY (" + Item2ImageEntry.IMAGE + ") REFERENCES " + ImageEntry.TABLE_NAME + "(" + ImageEntry ._ID + "),"  +
                        "FOREIGN KEY (" + Item2ImageEntry.CHALLENGEITEM + ") REFERENCES " + ChallengeItemEntry.TABLE_NAME + "(" + ChallengeItemEntry ._ID + ")"  +
                        ")";

        protected static final String SQL_DROP_TABLE_ITEMS2IMAGES =
                "DROP TABLE IF EXISTS " + Item2ImageEntry.TABLE_NAME;


    }


}
