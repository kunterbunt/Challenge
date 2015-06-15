package sebastians.challenge.tools;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sebastians.challenge.data.ImagePath;

/**
 * Created by kunterbunt on 14.06.15.
 */
public class PhotoManager {

    public static final String LOG_TAG = "PhotoManager";

    public static final int REQUEST_TAKE_PHOTO = 451,
                            REQUEST_PICK_PHOTO = 452;

    private static final String PHOTO_DIR = File.separator + ".photos" + File.separator;

    /**
     * Chooses external or internal storage according to availability and user choice.
     * Then fires an intent to launch the camera, which will store the full-sized image in the file that is returned.
     * The passed activity will be used to launch that intent and needs to implement onActivityResult method
     * and listen for requestCode == PhotoManager.REQUEST_TAKE_PHOTO to be notified when the image is taken.
     * No intent will be returned, use the path returned from this method to do stuff with the file instead.
     * @param activity Will be used to launch the intent and will be notified when the photo is taken.
     * @return Absolute path to image file.
     */
    public static String requestToTakePhoto(Activity activity) {
        File saveToPath;
        File file;
        boolean saveToExternalStorage = true;

        // Check if external storage is available.
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        Log.d(LOG_TAG, "External Storage Available: " + externalStorageAvailable);
        // If it is available and the user wants to save data there, then use it.
        if (externalStorageAvailable && saveToExternalStorage) {
            saveToPath = new File(activity.getExternalFilesDir(null).getAbsolutePath() + PHOTO_DIR);
            Log.d(LOG_TAG, "Using external storage path: " + saveToPath.getAbsolutePath());

        } else {
            // Warn that it is not available if the user wanted to use it.
            if (saveToExternalStorage)
                Toast.makeText(activity, "External storage is not available. Using internal storage instead.", Toast.LENGTH_SHORT).show();
            saveToPath = new File(activity.getFilesDir().getAbsolutePath() + PHOTO_DIR);
            Log.d(LOG_TAG, "Using internal storage path: " + saveToPath.getAbsolutePath());
        }
        saveToPath.mkdirs();

        // Set .nomedia flag telling the Media Scanner to ignore images in this folder, if it has not been set.
        File noMediaFlag = new File(saveToPath.getAbsolutePath() + File.separator + ".nomedia");
        boolean hadToCreateNoMediaFlag = false;
        if (!noMediaFlag.exists()) {
            hadToCreateNoMediaFlag = true;
            try {
                Log.d(LOG_TAG, "No .nomedia flag found - creating \"" + noMediaFlag.getAbsolutePath() + "\"... " + noMediaFlag.createNewFile());
            } catch (IOException ex) {
                Log.e(LOG_TAG, "Error creating .nomedia flag: " + ex.toString());
            }
        }
        // Apparently a .nomedia flag sometimes only works when it is placed in the parent directory BEFORE any media.
        noMediaFlag = new File(saveToPath.getParentFile().getAbsolutePath() + File.separator + ".nomedia");
        if (!noMediaFlag.exists()) {
            hadToCreateNoMediaFlag = true;
            try {
                Log.d(LOG_TAG, "No .nomedia flag found in parent directory - creating \"" + noMediaFlag.getAbsolutePath() + "\"... " + noMediaFlag.createNewFile());
            } catch (IOException ex) {
                Log.e(LOG_TAG, "Error creating .nomedia flag: " + ex.toString());
            }
        }

        // Create unique file.
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String fileName = "challengeApp_" + timeStamp;
        try {
            file = File.createTempFile(fileName, ".jpg", saveToPath);
        } catch (IOException ex) {
            Log.e(LOG_TAG, ex.toString());
            Toast.makeText(activity, "Couldn't get a file to save your image to. See your developer.", Toast.LENGTH_SHORT).show();
            return null;
        }

        // Take photo.
        // Set flag to indicate we want to take a picture.
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Check if there is a camera app installed.
        if (photoIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Set flag to indicate we want to save the full-size image, too,
            // and pass a file to save it to.
            photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            activity.startActivityForResult(photoIntent, REQUEST_TAKE_PHOTO);
        } else {
            Log.e(LOG_TAG, "No camera app.");
            Toast.makeText(activity, "Couldn't find a camera app. You kinda need one to take a picture.", Toast.LENGTH_LONG).show();
        }
        Log.d(LOG_TAG, "Photo will be saved in: " + file.getAbsolutePath());

        return file.getAbsolutePath();
    }

    /**
     * Fires off an intent that launches a gallery app. The user can then pick an image.
     * The passed activity needs to implement onActivityResult
     * and listen for requestCode == PhotoManager.REQUEST_PICK_PHOTO.
     * The recieved intent contains the CONTENT_URI in data.getData().
     * For ease of use pass that Uri to convertContentUriToImagePath().
     * @param activity
     */
    public static void requestToPickPhotoFromGallery(Activity activity) {
        // We want to PICK a data item and the result should be a CONTENT_URI.
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        activity.startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
    }

    public static ImagePath convertContentUriToImagePath(Activity activity, Uri uri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        // Get the cursor
        Cursor cursor = activity.getContentResolver().query(uri,
                filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();

        return new ImagePath(imgDecodableString);
    }

    /**
     * @param imagePath
     * @param width
     * @param height
     * @return Bitmap that fits given dimensions decoded from the file.
     */
    public static Bitmap getScaledBitmap(ImagePath imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath.getPath(), options);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return bitmap;
    }

    public static Bitmap getBitmap(ImagePath imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath.getPath(), options);
        return bitmap;
    }

}
