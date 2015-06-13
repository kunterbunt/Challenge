package sebastians.challenge;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class ChallengeDetail extends ActionBarActivity {

    /** Put an intent extra into the calling intent to provide the database id of the challenge you want to show. */
    public static final String INTENT_CHALLENGE_ID = "CHALLENGE_ID";
    public static final String LOG_TAG = "ChallengeDetail";
    private long mAssociatedChallengeDatabaseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_detail);
        mAssociatedChallengeDatabaseId = getIntent().getLongExtra(INTENT_CHALLENGE_ID, -1);
        if (mAssociatedChallengeDatabaseId == -1)
            throw new IllegalArgumentException("No challenge database ID provided.");
        setTitle("Challenge title");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_challenge_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
