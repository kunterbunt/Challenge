package sebastians.challenge;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.adapter.ChallengeAdapter;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.data.DatabaseHelper;


/**
 * Displays list of known challenges.
 */
public class MyChallengesFragment extends Fragment {

    private List<Challenge> mChallengeList;

    public MyChallengesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_challenges, container, false);
        ListView challengeList = (ListView) view.findViewById(R.id.challenge_list);
        mChallengeList = new ArrayList<>();
        mChallengeList.add(new Challenge("Test", 1));

//        DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
//        mChallengeList.addAll(db.getAllChallenges());

        // Set challenge list view adapter.
        challengeList.setAdapter(new ChallengeAdapter(getActivity().getApplicationContext(), mChallengeList));
        challengeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When a challenge card is clicked, start its detail activity.
                // For that, pass the corresponding database id to the detail activity.
                Intent intent = new Intent(getActivity().getApplicationContext(), ChallengeDetail.class);
                intent.putExtra(ChallengeDetail.INTENT_CHALLENGE_ID, mChallengeList.get(position).getDatabaseId());
                startActivity(intent);
            }
        });
        return view;
    }
}
