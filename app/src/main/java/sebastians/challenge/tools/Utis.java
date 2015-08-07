package sebastians.challenge.tools;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by sebastian on 25/06/15.
 */
public class Utis {
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * THIS IS STUDIP BASE64
     * @param text
     * @return
     */
    public static String SHA256 (String text)  {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

        md.update(text.getBytes());
        byte[] digest = md.digest();

        return Base64.encodeToString(digest, Base64.DEFAULT);
    }

    public static String getRandomString(){



        Random rnd = new Random(System.currentTimeMillis());

            StringBuilder sb = new StringBuilder( 20 );
            for( int i = 0; i < 20; i++ )
                sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
            return sb.toString();



    }
}
