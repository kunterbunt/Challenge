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

    private List<ChallengeItem> mChallengeItemList;

    public Challenge(String name, long databaseId, List<ChallengeItem> challengeItemList) {
        mName = name;
        mDatabaseId = databaseId;
        mChallengeItemList = challengeItemList;
    }

    public Challenge(String name, long databaseId) {
        mName = name;
        mDatabaseId = databaseId;
        mChallengeItemList = new ArrayList<ChallengeItem>();
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
