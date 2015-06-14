package sebastians.challenge;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.adapter.ViewPagerAdapter;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.data.DatabaseHelper;
import sebastians.challenge.data.OnSwipeListener;
import sebastians.challenge.data.Task;

public class ChallengeDetailFragment extends Fragment {
    ChallengeDetail mActivity;

    public ChallengeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_detail, container, false);
        ViewPager imageSwiper = (ViewPager) view.findViewById(R.id.imageSwiper);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());
        imageSwiper.setAdapter(adapter);

        //set activity
        mActivity = (ChallengeDetail) getActivity();

        //set db helper for onclick listener
        final DatabaseHelper db;
        DatabaseHelper.init(mActivity.getApplicationContext());
        db = DatabaseHelper.getInstance();

        final ImageButton addTaskButton = (ImageButton) view.findViewById(R.id.toggleChallengeActivityButton);
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
                    addTaskButton.setImageResource(android.R.drawable.ic_media_play);
                }else{
                    curChallenge.startNow();
                    addTaskButton.setImageResource(android.R.drawable.ic_media_pause);
                }
                db.update(curChallenge);


            }
        });


        return view;
    }
}


