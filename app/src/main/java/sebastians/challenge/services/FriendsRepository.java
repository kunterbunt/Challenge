package sebastians.challenge.services;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

import sebastians.challenge.tools.Utis;

/**
 * Created by sebastian on 25/06/15.
 */
public class FriendsRepository {
    public static final String LOG_TAG = "firends";
    Context ctx;


    private Cursor mCursor;
    String[] projection = new String[] { ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };

    public ArrayList<String> allFriends = new ArrayList<>();

    public FriendsRepository(Context ctx){
        this.ctx = ctx;

        mCursor = ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "=?", new String[]{"1"},
                ContactsContract.Contacts.DISPLAY_NAME);

        mCursor.moveToFirst();


        while(mCursor.moveToNext()){
            Log.i(LOG_TAG, mCursor.getString(mCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));

            Cursor cursorPhone = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER},

                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
                    new String[]{mCursor.getString(mCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))},
                    null);


            if (cursorPhone.moveToFirst()) {
               // while(cursorPhone.moveToNext())
                    String pNum = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                    if(pNum != null)
                        try {
                            Log.i(LOG_TAG, Utis.SHA256(pNum));
                            allFriends.add(Utis.SHA256(pNum));
                        }catch (Exception e){
                            ;
                        }

            }

            cursorPhone.close();

        }


    }


}
