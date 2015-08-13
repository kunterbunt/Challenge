package sebastians.challenge.data;

import android.content.SharedPreferences;

import sebastians.challenge.tools.Prefs;

/**
 * little wrapper for userinformation!
 */
public class UserCredentials {
    private String username = "";
    private String password = "";

    /**
     * read userinformation from preferences!
     * @param frompref
     */
    public UserCredentials(SharedPreferences frompref){
        username = frompref.getString(Prefs.USER,"");
        password = frompref.getString(Prefs.PW,"");
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

}
