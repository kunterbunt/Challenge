package sebastians.challenge.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by sebastian on 25/06/15.
 */
public class MyCredentials {
    public final String userid;
    public final String hashedPw;

    public static final String USRID = "userid";
    public static final String PWHSH = "hash";
    public static final String SHRDPREFID = "credentials";
    public static final String REQUESTEDVERIFICATIONCODE = "requestcode";
    private final Context ctx;
    final SharedPreferences sharedPref;
    public MyCredentials(Context ctx){
        this.ctx = ctx;


            sharedPref = this.ctx.getSharedPreferences(SHRDPREFID,Context.MODE_PRIVATE);

            if("".equals(sharedPref.getString(USRID,""))){

                String unumber = promtForUserNumber();
                //save all stuff in shared prefs!
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(USRID, Utis.SHA256(unumber));
                editor.putString(PWHSH, Utis.SHA256(Utis.getRandomString()));
                editor.commit();
                //TODO !!! SUBMIT TO SERVER




            }

        userid = "";

        hashedPw = "";
        //promt user phone number enter!!

    }

    private String promtForUserNumber(){
        final EditText input = new EditText(ctx);
        final String[] nNumber = {""};
        new AlertDialog.Builder(ctx)
                .setTitle("Update Status")
                .setMessage("Please enter your phone number for identification: +49123456789")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.i("input",input.getText().toString());
                        nNumber[0] = input.getText().toString();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // user can not be identified

            }
        }).show();

        return nNumber[0];
    }
}
