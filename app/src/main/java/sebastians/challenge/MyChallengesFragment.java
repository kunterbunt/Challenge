package sebastians.challenge;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
    private ChallengeAdapter mChallengeAdapter;

    public MyChallengesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_challenges, container, false);
        // Initialize database connection.
        DatabaseHelper.init(getActivity().getApplicationContext());
        // Populate challenge list.
        mChallengeList = getChallengeList();
        mChallengeAdapter = new ChallengeAdapter(getActivity().getApplicationContext(), mChallengeList);
        ListView challengeList = (ListView) view.findViewById(R.id.challenge_list);
        challengeList.setAdapter(mChallengeAdapter);
        challengeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Pinned headers should not be clickable.
                if (mChallengeList.get(position) == null)
                    return;

                // When a challenge card is clicked, start its detail activity.
                // For that, pass the corresponding database id to the detail activity.
                Intent intent = new Intent(getActivity().getApplicationContext(), ChallengeDetail.class);
                intent.putExtra(ChallengeDetail.INTENT_CHALLENGE_ID, mChallengeList.get(position).getDatabaseId());
                startActivity(intent);
            }
        });

        // Set add challenge button listener.
        ((ImageButton) view.findViewById(R.id.addChallengeButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity().getApplicationContext(), AddChallengeOverview.class), AddChallengeOverview.REQUEST_NEW_CHALLENGE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // This indicates that a challenge has been added to the database.
        if (requestCode == AddChallengeOverview.REQUEST_NEW_CHALLENGE && resultCode == Activity.RESULT_OK) {
            // Re-populate the list.
            mChallengeList.clear();
            mChallengeList.addAll(getChallengeList());
            mChallengeAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @return List of challenges as returned from the database with pinned header indicating objects.
     */
    private List<Challenge> getChallengeList() {
        // Load challenges from database.
        List<Challenge> list = DatabaseHelper.getInstance().getAllChallenges();
        // null indicates a header, add one at start.
        list.add(0, null);
        // Find first inactive challenge.
        for (int i = 1; i < list.size(); i++) {
            if (!list.get(i).isActive()) {
                list.add(i, null);
                break;
            }
        }
        return list;
    }
}
