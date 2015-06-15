package sebastians.challenge.tools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kunterbunt on 14.06.15.
 */
public class PhotoManager {

    public static final String LOG_TAG = "PhotoManager";

    public static final int REQUEST_TAKE_PHOTO = 451;

    /**
     * Launches the camera app and stores the image that the user might take in the Uri it returns.
     * Implement an onActivityResult and listen for requestCode == PhotoManager.REQUEST_TAKE_PHOTO
     * in the activity you pass to this method to get the returning intent
     * and to do stuff with it or just know when the operation is finished.
     * @param activity Activity that will be used to launch the camera intent. This will recieve an resulting intent, too.
     * @return Uri to the file the image is saved to.
     */
    public static Uri requestToTakeImage(Activity activity) {
        // Create image file.
        String timestamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss").format(new Date());
        String filename = "challengeApp_" + timestamp;
        File image;
        try {
            // Save to private storage.
            File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!storageDir.exists())
                storageDir.mkdirs();
            image = File.createTempFile(filename, ".jpg", storageDir);
        } catch (IOException ex) {
            Log.e(LOG_TAG, ex.toString());
            Toast.makeText(activity, "Taking picture failed, sorry mate.", Toast.LENGTH_SHORT).show();
            return null;
        }
        // Start camera.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
            activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        } else {
            Log.e(LOG_TAG, "No camera app.");
            Toast.makeText(activity, "Couldn't find a camera app. You kinda need one to take a picture.", Toast.LENGTH_LONG).show();
        }
        return Uri.fromFile(image);
    }

}
