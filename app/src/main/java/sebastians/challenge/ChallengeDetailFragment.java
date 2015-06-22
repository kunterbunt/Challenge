package sebastians.challenge;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import sebastians.challenge.adapter.TaskShowListAdapter;
import sebastians.challenge.adapter.ViewPagerImageAdapter;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.tools.DatabaseHelper;
import sebastians.challenge.data.ImagePath;
import sebastians.challenge.data.Task;
import sebastians.challenge.views.ProgressView;

public class ChallengeDetailFragment extends Fragment {
    private ChallengeDetail mActivity;
    private Challenge mChallenge;
    private ViewPagerImageAdapter viewPagerAdapter;
    private int previousSelectedTask;

    public ChallengeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_detail, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set parent activity.
        mActivity = (ChallengeDetail) getActivity();
        // Set challenge.
        mChallenge = mActivity.getChallenge();
        View view = getView();

        // Populate ViewPager.
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.imageSwiper);
        viewPagerAdapter = new ViewPagerImageAdapter(getActivity(), viewPager);
        for (ImagePath imagePath : mChallenge.getTaskList().get(0).getImagePaths())
            viewPagerAdapter.add(imagePath);
        viewPager.setAdapter(viewPagerAdapter);

        // Set up for zoom-in animation.
        viewPagerAdapter.setUpForZoomAnimation(getView(), null);

        // Set toggle active button action.
        final ImageButton toggleActiveButton = (ImageButton) view.findViewById(R.id.toggleChallengeActivityButton);
        toggleActiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//get current challenge from activity
                mChallenge.resetDismissedForTasks();
                mChallenge.setActive(!mChallenge.isActive());
                toggleActiveButton.setImageResource(
                        mChallenge.isActive()
                                ? android.R.drawable.ic_media_rew
                                : android.R.drawable.ic_media_play);
                DatabaseHelper.getInstance().update(mChallenge);
            }
        });

        // Show respective images when a task is clicked.
        ListView taskList = (ListView) view.findViewById(R.id.task_list);
        final TaskShowListAdapter taskListAdapter = new TaskShowListAdapter(getActivity(), mChallenge.getTaskList());
        taskList.setAdapter(taskListAdapter);
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                taskListAdapter.setSelectedPosition(position);
                taskListAdapter.notifyDataSetChanged();

                Task task = mChallenge.getTaskList().get(position);
                viewPagerAdapter.clear();
                viewPagerAdapter.add(task.getImagePaths());
                viewPagerAdapter.notifyDataSetChanged();
            }
        });
    }
}


