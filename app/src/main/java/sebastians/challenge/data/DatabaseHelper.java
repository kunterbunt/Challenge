package sebastians.challenge.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by sebastian on 13/06/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String LOG_TAG = "DB";

    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        writableDatabase = super.getWritableDatabase();
        readableDatabase = super.getReadableDatabase();

        //create tables:
        db.execSQL(Contract.SQL_CREATE_TABLE_CHALLENGES);


        this.createDefaultData();

    }

    /**
     * populate 
     */
    private void createDefaultData(){

    }




    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }




    private static class Contract {
        private static final String TYPE_TEXT = " TEXT ";
        private static final String TYPE_INTEGER = " INTEGER ";
        private static final String TYPE_FLOAT = " FLOAT ";
        private static final String COMMA_SEP = ",";
        private static final String DEFAULT = " DEFAULT ";
        private static final String NOT_NULL = " NOT NULL ";





        protected static final String SQL_CREATE_TABLE_CHALLENGES =
                "CREATE TABLE " + ChallengeEntry.TABLE_NAME + " (" +
                        ChallengeEntry._ID + " INTEGER PRIMARY KEY, " +
                        ChallengeEntry.COLUMN_NAME_TITLE + TYPE_TEXT + NOT_NULL  + COMMA_SEP +
                        ChallengeEntry.COLUMN_NAME_DESCRIPTION + TYPE_TEXT + NOT_NULL  +
                        " )";

        protected static final String SQL_DROP_TABLE_CHALLENGES =
                "DROP TABLE IF EXISTS " + ChallengeEntry.TABLE_NAME;

        protected static abstract class ChallengeEntry implements BaseColumns {
            public static final String TABLE_NAME = "challenge";
            public static final String COLUMN_NAME_TITLE = "title";
            public static final String COLUMN_NAME_DESCRIPTION = "title";
        }
    }


}
