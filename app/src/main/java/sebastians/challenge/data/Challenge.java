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
    private long mActivatedTs = 0;

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

    public Challenge() {
        this("unnamed");
    }


    public boolean isActive(){
        return this.mActive;
    }

    public long getActivatedTs(){
        return this.mActivatedTs;
    }

    public void setActivatedTs(long mActivatedTs){
        this.mActivatedTs = mActivatedTs;
    }

    public void setTaskList(List<Task> tasks){
        this.mTaskList = tasks;
    }

    public Challenge(String name, long databaseId) {
        mName = name;
        mDatabaseId = databaseId;
        mTaskList = new ArrayList<Task>();
    }

    /**
     *
     * @return  null if there is no due task at the moment otherwise return Task Object
     */
    public Task getDueTask(){
       int dueTaskId = getDueTaskId();
        if(dueTaskId == -1)
            return null;
        return mTaskList.get(dueTaskId);


    }

    public int getDueTaskId(){
        long activatedTs = this.mActivatedTs;
        long accumulatedTs= activatedTs;

        long currentTs = System.currentTimeMillis() / 1000;

        for(int i = 0; i < mTaskList.size(); i++){
            Task task = mTaskList.get(i);

            accumulatedTs += task.getDuration();

            if(accumulatedTs > currentTs){
                return i;
            }
        }

        return -1;


    }

    /**
     * Activates or deactivates a challenge.
     * @param value
     */
    public void setActive(boolean value){
        this.mActive = value;
        if (mActive)
            setActivatedTs(System.currentTimeMillis() / 1000);
    }

    public void resetDismissedForTasks(){
        for(int i = 0; i < mTaskList.size(); i++){
            mTaskList.get(i).setDismissed(false);
        }
    }

    public String getDescription(){
        return this.mDescription;
    }

    public List<Task> getTaskList(){
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
