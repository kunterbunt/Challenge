package sebastians.challenge;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import sebastians.challenge.adapter.ViewPagerAdapterDummy;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.data.DatabaseHelper;

public class ChallengeDetailFragment extends Fragment {
    ChallengeDetail mActivity;

    public ChallengeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_detail, container, false);
        ViewPager imageSwiper = (ViewPager) view.findViewById(R.id.imageSwiper);
        ViewPagerAdapterDummy adapter = new ViewPagerAdapterDummy(getActivity());

        mActivity = (ChallengeDetail) getActivity();

        imageSwiper.setAdapter(adapter);

        ImageButton addTaskButton = (ImageButton) view.findViewById(R.id.toggleChallengeActivityButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get current challenge from activity
                Challenge curChallenge = mActivity.getChallenge();
                DatabaseHelper db;
                DatabaseHelper.init(mActivity.getApplicationContext());
                
                db = DatabaseHelper.getInstance();

                if(curChallenge.isActive()){
                    curChallenge.setActive(false);
                }else{
                    curChallenge.startNow();
                }
                db.update(curChallenge);


            }
        });


        return view;
    }
}


