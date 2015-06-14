package sebastians.challenge.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastian on 13/06/15.
 */
public class Challenge {

    private String mName;
    private long mDatabaseId;
    private String mDescription;
    private List<Task> mTaskList;
    private boolean mActive = false;
    private long activatedTs = 0;

    public Challenge(String name, long databaseId, String description, boolean active, List<Task> taskList) {
        mName = name;
        mDatabaseId = databaseId;
        mTaskList = taskList;
        mDescription = description;
        mActive = active;
    }

    public Challenge(String name){
        this(name,-1,"",false,new ArrayList<Task>());
    }


    public boolean isActive(){
        return this.mActive;
    }

    public long getActivatedTs(){
        return this.activatedTs;
    }

    public void setActivatedTs(long activatedTs){
        this.activatedTs = activatedTs;
    }

    public Challenge(String name, long databaseId) {
        mName = name;
        mDatabaseId = databaseId;
        mTaskList = new ArrayList<Task>();
    }


    public String getDescription(){
        return this.mDescription;
    }

    public List<Task> getChallengeItemList(){
        return mTaskList;
    }


    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public long getDatabaseId() {
        return mDatabaseId;
    }

    public void setDatabaseId(long mDatabaseId) {
        this.mDatabaseId = mDatabaseId;
    }
}
