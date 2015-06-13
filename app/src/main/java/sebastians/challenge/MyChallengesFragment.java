package sebastians.challenge;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A placeholder fragment containing a simple view.
 */
public class MyChallengesFragment extends Fragment {

    public MyChallengesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Test", "Test");
        return inflater.inflate(R.layout.fragment_my_challenges, container, false);
    }
}
