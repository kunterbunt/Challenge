package sebastians.challenge.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastian on 13/06/15.
 */
public class ImagePath {

    private String path;
    private long databaseId;

    public ImagePath(String path, long databaseId) {
        this.path = path;
        this.databaseId = databaseId;
    }

    public static List<ImagePath> convertToImagePathList(List<String> list) {
        List<ImagePath> imagePathList = new ArrayList<>(list.size());
        for (String path : list)
            imagePathList.add(new ImagePath(path));
        return imagePathList;
    }

    public static List<String> convertToStringList(List<ImagePath> list) {
        List<String> stringList = new ArrayList<>(list.size());
        for (ImagePath path : list)
            stringList.add(path.getPath());
        return stringList;
    }

    public ImagePath(String path){
        this.path = path;
    }

    public ImagePath(){
        this("");
    }

    public String getPath(){
        return this.path;
    }

    public long getDatabaseId(){
        return this.databaseId;
    }

    /**
     * @return Whether this image exists in storage.
     */
    public boolean valdiate() {
        return new File(path).exists();
    }

}
