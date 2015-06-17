package sebastians.challenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import sebastians.challenge.adapter.ChallengeAdapter;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.tools.DatabaseHelper;
import sebastians.challenge.dialogs.ButtonDialog;


/**
 * Displays list of known challenges.
 */
public class MyChallengesFragment extends Fragment {

    public static final String LOG_TAG = "MyChallengesFragment";
    private  List<Challenge> mChallengeList;
    private ChallengeAdapter mChallengeAdapter;

    private final MyChallengesFragment thisFragment = this;
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
        final ListView challengeList = (ListView) view.findViewById(R.id.challenge_list);
        challengeList.setAdapter(mChallengeAdapter);
        challengeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int listitem = i;

                //instantiate database stuff

                Log.i(LOG_TAG,"Item longclick");
                new ButtonDialog(getActivity(), null, "Remove", "Doublicate", "Share?", null) {
                    @Override
                    public void onPositiveButtonClick() {
                    //completely remove challenge from device
                        new ButtonDialog(getActivity(), "Sure?!", "Okay", "Cancle", null, null) {
                            @Override
                            public void onPositiveButtonClick() {

                                Log.i(LOG_TAG, "Delete Listview Item number -> " + mChallengeList.get(listitem).getName());
                                DatabaseHelper.getInstance().delete(mChallengeList.get(listitem));

                                mChallengeList = getChallengeList();
                                mChallengeAdapter.clear();
                                mChallengeAdapter.addAll(mChallengeList);
                                challengeList.setAdapter(mChallengeAdapter);
                                mChallengeAdapter.notifyDataSetInvalidated();
                                challengeList.invalidateViews();

                            }

                            @Override
                            public void onNeutralButtonClick() {
                                //do nothing
                            }

                        };
                    }

                    @Override
                    public void onNeutralButtonClick() {


                    }

                    @Override
                    public void onNegativeButtonClick() {
                        //
                    }
                };

                //consume longclick, so onclick wont be called
                return true;
            }


        });
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
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // Set add challenge button listener.
        ((ImageButton) view.findViewById(R.id.addChallengeButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity().getApplicationContext(), AddChallengeOverview.class), AddChallengeOverview.REQUEST_NEW_CHALLENGE);
                getActivity().overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
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
