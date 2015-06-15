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
    private List<Challenge> mChallengeListViewList;
    private ChallengeAdapter mChallengeAdapter;

    public MyChallengesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_challenges, container, false);
        // Get ListView.
        ListView challengeList = (ListView) view.findViewById(R.id.challenge_list);
        // Initialize database connection.
        DatabaseHelper.init(getActivity().getApplicationContext());
        DatabaseHelper db = DatabaseHelper.getInstance();
        // Load challenges from database.
        mChallengeList = db.getAllChallenges();

        mChallengeListViewList = new ArrayList<>();
        mChallengeListViewList.add(null);
        //add "VOID" Challenges to Challengelist.
        boolean notActive = false;
        for(int i = 0; i < mChallengeList.size(); i++){
            if(mChallengeList.get(i).isActive() == false && notActive == false){
                notActive = true;
                mChallengeListViewList.add(null);
            }
            mChallengeListViewList.add(mChallengeList.get(i));
        }
        mChallengeAdapter = new ChallengeAdapter(getActivity().getApplicationContext(), mChallengeListViewList);
        challengeList.setAdapter(mChallengeAdapter);
        challengeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mChallengeListViewList.get(position) == null)
                    return;

                // When a challenge card is clicked, start its detail activity.
                // For that, pass the corresponding database id to the detail activity.
                Intent intent = new Intent(getActivity().getApplicationContext(), ChallengeDetail.class);
                intent.putExtra(ChallengeDetail.INTENT_CHALLENGE_ID, mChallengeListViewList.get(position).getDatabaseId());
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
        if (requestCode == AddChallengeOverview.REQUEST_NEW_CHALLENGE && resultCode == Activity.RESULT_OK) {
            Log.i("test", "intent recived");
            mChallengeList = DatabaseHelper.getInstance().getAllChallenges();
            mChallengeListViewList.clear();
            mChallengeListViewList.add(null);
            //add "VOID" Challenges to Challengelist.
            boolean notActive = false;
            for(int i = 0; i < mChallengeList.size(); i++){
                if(mChallengeList.get(i).isActive() == false && notActive == false){
                    notActive = true;
                    mChallengeListViewList.add(null);
                }
                mChallengeListViewList.add(mChallengeList.get(i));
            }
            mChallengeAdapter.notifyDataSetChanged();
        }
    }
}
