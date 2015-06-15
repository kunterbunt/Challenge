package sebastians.challenge;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import sebastians.challenge.adapter.ViewPagerImageAdapter;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.data.DatabaseHelper;
import sebastians.challenge.data.Task;
import sebastians.challenge.views.ProgressView;

public class ChallengeDetailFragment extends Fragment {
    ChallengeDetail mActivity;

    public ChallengeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_detail, container, false);
        ViewPager imageSwiper = (ViewPager) view.findViewById(R.id.imageSwiper);
        ViewPagerImageAdapter adapter = new ViewPagerImageAdapter(getActivity());
        imageSwiper.setAdapter(adapter);

        //set activity
        mActivity = (ChallengeDetail) getActivity();

        //set db helper for onclick listener
        final DatabaseHelper db;
        DatabaseHelper.init(mActivity.getApplicationContext());
        db = DatabaseHelper.getInstance();




        final ImageButton addTaskButton = (ImageButton) view.findViewById(R.id.toggleChallengeActivityButton);

        final ProgressView progressView = (ProgressView) view.findViewById(R.id.progressBar);

        final TextView description = (TextView) view.findViewById(R.id.description);
        progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int taskId = progressView.getClosestPointIdToTouch();
                Log.i("POINT", taskId + "");
                Challenge curChallenge = mActivity.getChallenge();
                Task selectedTask = curChallenge.getTaskList().get(taskId);
                description.setText(Html.fromHtml(selectedTask.getDescription()));
                //TODO ADD IMAGE STUFF

            }
        });





        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get current challenge from activity
                Challenge curChallenge = mActivity.getChallenge();
                curChallenge.resetDismissedForTasks();

                if(curChallenge.isActive()){
                    curChallenge.setActive(false);
                    addTaskButton.setImageResource(android.R.drawable.ic_media_play);
                }else{
                    curChallenge.startNow();
                    addTaskButton.setImageResource(android.R.drawable.ic_media_rew);
                }
                db.update(curChallenge);


            }
        });


        return view;
    }
}


