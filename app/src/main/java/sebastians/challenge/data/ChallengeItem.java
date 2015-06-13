package sebastians.challenge.data;

import java.util.List;

/**
 * Created by sebastian on 13/06/15.
 */
public class ChallengeItem {
    private String title;
    private long databaseId;
    private int timeAfterPrev;
    private int order;
    private boolean done;
    private ImagePath selfie;
    private List<ImagePath> imagePaths;


    public ChallengeItem(String title, long databaseId, int timeAfterPrev, int order, boolean done, ImagePath selfie, List<ImagePath> images){
        this.title = title;
        this.databaseId = databaseId;
        this.timeAfterPrev = timeAfterPrev;
        this.order = order;
        this.done = done;
        this.selfie = selfie;
        this.imagePaths = images;
    }

    public String getTitle(){
        return this.title;
    }


}
