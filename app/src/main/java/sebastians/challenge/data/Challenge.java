package sebastians.challenge.data;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastian on 13/06/15.
 */
public class Challenge {

    private String mName;
    private long mDatabaseId;
    private String mDescription;
    private List<ChallengeItem> mChallengeItemList;
    private boolean mActive = false;

    public Challenge(String name, long databaseId, String description, boolean active, List<ChallengeItem> challengeItemList) {
        mName = name;
        mDatabaseId = databaseId;
        mChallengeItemList = challengeItemList;
        mDescription = description;
        mActive = active;
    }

    public Challenge(String name){
        this(name,-1,"",false,new ArrayList<ChallengeItem>());
    }


    public boolean isActive(){
        return this.mActive;
    }

    public Challenge(String name, long databaseId) {
        mName = name;
        mDatabaseId = databaseId;
        mChallengeItemList = new ArrayList<ChallengeItem>();
    }


    public String getDescription(){
        return this.mDescription;
    }

    public List<ChallengeItem> getChallengeItemList(){
        return mChallengeItemList;
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
