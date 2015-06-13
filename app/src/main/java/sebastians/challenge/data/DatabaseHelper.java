package sebastians.challenge.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.data.interfaces.TitleDescriptionColumns;

/**
 * Created by sebastian on 13/06/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
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

    public Challenge getChallenge(int id){
        return null;
    }

    /**
     * Fetch alle Challenges from database
     * @return List of Challenges
     */
    public List<Challenge> getAllChallenges(){

        List<Challenge> challenges = new ArrayList<>();
        Cursor cursor = readableDatabase.query(
                Contract.ChallengeEntry.TABLE_NAME,
                new String[]{Contract.ChallengeEntry._ID,Contract.ChallengeEntry.TITLE},
                null,
                null,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            Challenge challenge;
            long challengeDbId = cursor.getColumnIndexOrThrow(Contract.ChallengeEntry._ID);
            challenge = new Challenge(
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.ChallengeEntry.TITLE)),
                    challengeDbId,
                    this.getChallengeItemsForChallengeId(challengeDbId)
                    );
            challenges.add(challenge);
        }
        return challenges;
    }


    public List<ChallengeItem> getChallengeItemsForChallengeId(long id){
        List<ChallengeItem> challengeItems = new ArrayList<>();
        Cursor cursor = readableDatabase.query(
                Contract.ChallengeItemEntry.TABLE_NAME,
                new String[]{Contract.ChallengeItemEntry._ID,
                        Contract.ChallengeItemEntry.TITLE,
                        Contract.ChallengeItemEntry.DESCRIPTION,

                },
                Contract.ChallengeItemEntry.CHALLENGE + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                Contract.ChallengeItemEntry.ORDER);

        while (cursor.moveToNext()) {

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
                long imageId  = imgsIdsCursor.getInt(cursor.getColumnIndexOrThrow(Contract.Item2ImageEntry.IMAGE));
                Cursor imgsCursor = readableDatabase.query(
                        Contract.ImageEntry.TABLE_NAME,
                        new String[]{Contract.ImageEntry.PATH
                        },
                        Contract.ImageEntry._ID + " = ?",
                        new String[]{String.valueOf(imageId)},
                        null,
                        null,
                        null);

                String imgPath = imgsCursor.getString(cursor.getColumnIndexOrThrow(Contract.ImageEntry.PATH));
                long imgDbId = imgsCursor.getInt(cursor.getColumnIndexOrThrow(Contract.ImageEntry._ID));
                ImagePath imagePath = new ImagePath(imgPath,imgDbId);
                imagePaths.add(imagePath);
            }



            ChallengeItem challengeItem = new ChallengeItem(
                    cursor.getString(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry.TITLE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry._ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry.TIME_AFTER_PREV)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry.ORDER)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry.DONE)) > 0,
                    new ImagePath(cursor.getString(cursor.getColumnIndexOrThrow(Contract.ChallengeItemEntry.SELFIE))),
                    imagePaths);

            challengeItems.add(challengeItem);

        }

        return challengeItems;
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
    private void createDefaultData(){

        //add first challenge to database
        ContentValues cv = new ContentValues();
        cv.put(Contract.ChallengeEntry.TITLE, "Smoothie Challenge");
        cv.put(Contract.ChallengeEntry.DESCRIPTION, "Smoothie Challenge <b>Description</b>");
        long id = writableDatabase.insert(Contract.ChallengeEntry.TABLE_NAME,null,cv);

        //add item to challenge
        cv = new ContentValues();
        cv.put(Contract.ChallengeItemEntry.CHALLENGE,id);
        cv.put(Contract.ChallengeItemEntry.TITLE,"DAY ONE");
        cv.put(Contract.ChallengeItemEntry.ORDER, 0);
        cv.put(Contract.ChallengeItemEntry.TIME_AFTER_PREV,0);
        long itemId = writableDatabase.insert(Contract.ChallengeItemEntry.TABLE_NAME,null,cv);

        //add image to challenge item
        cv = new ContentValues();
        cv.put(Contract.ImageEntry.PATH,"IMAGEPATH1");
        long imageId = writableDatabase.insert(Contract.ImageEntry.TABLE_NAME,null,cv);

        //link challenge item and image!
        cv = new ContentValues();
        cv.put(Contract.Item2ImageEntry.IMAGE,imageId);
        cv.put(Contract.Item2ImageEntry.CHALLENGEITEM,itemId);
        writableDatabase.insert(Contract.Item2ImageEntry.TABLE_NAME, null,cv);



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
        private static final String TYPE_FLOAT = " FLOAT ";
        private static final String TYPE_PRIMARY = " INTEGER PRIMARY KEY ";
        private static final String COMMA_SEP = ",";
        private static final String DEFAULT = " DEFAULT ";
        private static final String NOT_NULL = " NOT NULL ";




        // Challenges

        protected static abstract class ChallengeEntry implements BaseColumns, TitleDescriptionColumns {
            public static final String TABLE_NAME = "challenges";

        }

        protected static final String SQL_CREATE_TABLE_CHALLENGES =
                "CREATE TABLE " + ChallengeEntry.TABLE_NAME + " (" +
                        ChallengeEntry._ID + TYPE_PRIMARY + ", " +
                        ChallengeEntry.TITLE + TYPE_TEXT + NOT_NULL  + COMMA_SEP +
                        ChallengeEntry.DESCRIPTION + TYPE_TEXT + NOT_NULL  +
                        " )";

        protected static final String SQL_DROP_TABLE_CHALLENGES =
                "DROP TABLE IF EXISTS " + ChallengeEntry.TABLE_NAME;



        //ChallengeItem

        protected static abstract class ChallengeItemEntry implements BaseColumns, TitleDescriptionColumns {
            public static final String TABLE_NAME = "challengeitems";
            public static final String CHALLENGE = "challengeid";
            public static final String TIME_AFTER_PREV = "timeafterprev";
            public static final String ORDER = "position";
            public static final String SELFIE = "selfie";
            public static final String DONE = "done";
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
