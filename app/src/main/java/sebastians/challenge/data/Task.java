package sebastians.challenge.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastian on 13/06/15.
 */
public class Task {

    private String title;
    private String description;
    private long databaseId;
    /** How long, in seconds, is this task valid. */
    private int durationValidity;
    private int order;
    private boolean done;
    private ImagePath selfie;
    private List<ImagePath> imagePaths;


    public Task(String title, long databaseId, String description, int durationValidity, int order, boolean done, ImagePath selfie,
                List<ImagePath> images){
        this.title = title;
        this.databaseId = databaseId;
        this.durationValidity = durationValidity;
        this.order = order;
        this.done = done;
        this.selfie = selfie;
        this.imagePaths = images;
        this.description = description;
    }

    public Task(String title, String description){
        this(title,-1,description,0,0,false,new ImagePath(), new ArrayList<ImagePath>());
    }

    public void setImagePaths(List<ImagePath> imagePaths){
        this.imagePaths = imagePaths;
    }

    public String getTitle(){
        return this.title;
    }

    public long getDatabaseId(){
        return this.databaseId;
    }

    public int getDurationValidity(){
        return this.durationValidity;
    }

    public boolean isDone(){
        return this.done;
    }

    public String getDescription(){
        return this.description;
    }

    public int getOrder(){
        return this.order;
    }

    public List<ImagePath> getImagePaths(){
        if(this.imagePaths == null)
            this.imagePaths = new ArrayList<>();
        return this.imagePaths;
    }

    public ImagePath getSelfie(){
        return this.selfie;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * time after prev has to be in SECONDS
     * @param durationValidity
     */
    public void setDurationValidity(int durationValidity) {
        this.durationValidity = durationValidity;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String toString(){
        String string = "";
        string += "title:" + getTitle() + "\n";
        string += "description:" + getDescription() + "\n";
        string += "order:" + getOrder() + "\n";
        string += "time:" + getDurationValidity() + "\n";
        return string;
    }
}
