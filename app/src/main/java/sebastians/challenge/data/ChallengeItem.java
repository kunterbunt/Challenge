package sebastians.challenge.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastian on 13/06/15.
 */
public class ChallengeItem {
    private String title;
    private String description;
    private long databaseId;
    private int timeAfterPrev;
    private int order;
    private boolean done;
    private ImagePath selfie;
    private List<ImagePath> imagePaths;


    public ChallengeItem(String title, long databaseId, String description, int timeAfterPrev, int order, boolean done, ImagePath selfie, List<ImagePath> images){
        this.title = title;
        this.databaseId = databaseId;
        this.timeAfterPrev = timeAfterPrev;
        this.order = order;
        this.done = done;
        this.selfie = selfie;
        this.imagePaths = images;
        this.description = description;
    }

    public ChallengeItem(String title, String description){
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

    public int getTimeAfterPrev(){
        return this.timeAfterPrev;
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
}
