package sebastians.challenge;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import sebastians.challenge.adapter.ViewPagerImageAdapter;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.data.DatabaseHelper;
import sebastians.challenge.data.ImagePath;
import sebastians.challenge.data.Task;
import sebastians.challenge.views.ProgressView;

public class ChallengeDetailFragment extends Fragment {
    private ChallengeDetail mActivity;
    private Challenge mChallenge;
    private ViewPagerImageAdapter viewPagerAdapter;

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
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.imageSwiper);
        viewPagerAdapter = new ViewPagerImageAdapter(getActivity());
        for (ImagePath imagePath : mChallenge.getTaskList().get(0).getImagePaths())
            viewPagerAdapter.add(imagePath);
        viewPager.setAdapter(viewPagerAdapter);

        // Set up for zoom-in animation.
        viewPagerAdapter.setUpForZoomAnimation(getView());

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

        // Assign challenge to progressView.
        final ProgressView progressView = (ProgressView) view.findViewById(R.id.progressBar);
        progressView.setChallenge(mChallenge);

        // Set name.
        final TextView nameField = (TextView) view.findViewById(R.id.name);
        nameField.setText(mChallenge.getTaskList().get(0).getTitle().toUpperCase());

        // Set description.
        final TextView descriptionField = (TextView) view.findViewById(R.id.description);
        descriptionField.setText(mChallenge.getTaskList().get(0).getDescription());

        progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get selected task.
                int taskId = progressView.getClosestPointIdToTouch();
                Task selectedTask = mChallenge.getTaskList().get(taskId);

                // Update name.
                nameField.setText(selectedTask.getTitle().toUpperCase());

                // Update description field.
                descriptionField.setText(Html.fromHtml(selectedTask.getDescription()));

                // Update images.
                viewPagerAdapter.clear();
                viewPagerAdapter.add(selectedTask.getImagePaths());
                viewPagerAdapter.notifyDataSetChanged();
            }
        });
    }
}


