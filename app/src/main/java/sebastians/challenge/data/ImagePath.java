package sebastians.challenge.data;

import android.media.Image;

/**
 * Created by sebastian on 13/06/15.
 */
public class ImagePath {

    private String path;
    private long databaseId;

    public ImagePath(String path, long databaseId){
        this.path = path;
        this.databaseId = databaseId;
    }

    public ImagePath(String path){
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }

    public long getDatabaseId(){
        return this.databaseId;
    }

}
