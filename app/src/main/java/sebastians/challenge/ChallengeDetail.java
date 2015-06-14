package sebastians.challenge;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import sebastians.challenge.data.Challenge;
import sebastians.challenge.data.DatabaseHelper;


public class ChallengeDetail extends ActionBarActivity {

    /** Put an intent extra into the calling intent to provide the database id of the challenge you want to show. */
    public static final String INTENT_CHALLENGE_ID = "CHALLENGE_ID";
    public static final String LOG_TAG = "ChallengeDetail";
    private long mAssociatedChallengeDatabaseId;
    private DatabaseHelper db;
    private Challenge mChallenge;


    public Challenge getChallenge(){
        return this.mChallenge;
    }

    //views:
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_detail);

        DatabaseHelper.init(getApplicationContext());
        db = DatabaseHelper.getInstance();

        description = (TextView) findViewById(R.id.description);

        mAssociatedChallengeDatabaseId = getIntent().getLongExtra(INTENT_CHALLENGE_ID, -1);


        mChallenge = db.getChallengeById(mAssociatedChallengeDatabaseId);


        if (mAssociatedChallengeDatabaseId == -1)
            throw new IllegalArgumentException("No challenge database ID provided.");



        final ImageButton addTaskButton = (ImageButton) findViewById(R.id.toggleChallengeActivityButton);

        if(mChallenge.isActive()){

            addTaskButton.setImageResource(android.R.drawable.ic_media_pause);
        }else{

            addTaskButton.setImageResource(android.R.drawable.ic_media_play);
        }

        setTitle(mChallenge.getName());
        description.setText(Html.fromHtml(mChallenge.getDescription()));



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
