package sebastians.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.log4j.BasicConfigurator;

import sebastians.challenge.services.PeriodicWakeupReceiver;


public class MyChallenges extends ActionBarActivity {
    public static final String LOG_TAG = "MyChallenges";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_challenges);
        setTitle(getResources().getString(R.string.overview_title));
        PeriodicWakeupReceiver.setAlarm(this);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().setReenterTransition(new Slide(Gravity.LEFT));
            getWindow().setExitTransition(new Fade(Fade.OUT));
        }

        //this needs to be done for jlogger
        BasicConfigurator.configure();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_challenges, menu);
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

        if(id == R.id.action_verification) {
            //start verification activity
            Intent intent = new Intent(this, IdentitiyVerificationActivity.class);
            startActivity(intent);

        }

        if(id == R.id.action_profile) {
            //start verification activity
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

}
