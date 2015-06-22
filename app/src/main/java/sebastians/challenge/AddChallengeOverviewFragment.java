package sebastians.challenge;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.adapter.TaskEditListAdapter;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.data.ImagePath;
import sebastians.challenge.data.Task;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddChallengeOverviewFragment extends Fragment {

    private ListView mTaskListView;
    private List<Task> mTaskList;
    private TaskEditListAdapter mTaskEditListAdapter;
    /** Keeps track of the last challenge the user has clicked on to edit. */
    private int mPositionOfLastEditedTask;

    public AddChallengeOverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_challenge_overview, container, false);

        // Set list view adapter.
        mTaskListView = (ListView) view.findViewById(R.id.task_list);
        mTaskList = new ArrayList<>();
        mTaskEditListAdapter = new TaskEditListAdapter(getActivity(), mTaskList);
        mTaskListView.setAdapter(mTaskEditListAdapter);

        // Pass all changes of the task dataset on to the parent activity.
        mTaskEditListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                AddChallengeOverview activity = (AddChallengeOverview) getActivity();
                Challenge challenge = activity.getChallenge();
                challenge.setTaskList(mTaskEditListAdapter.getTaskListWithoutHeaders());
                activity.setChallenge(challenge);
            }
        });
        // Add button action.
        ImageButton addTaskButton = (ImageButton) view.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // null indicates arrow.
                if (mTaskList.size() > 0)
                    mTaskList.add(null);
                String name;
                if (mTaskList.size() == 0)
                    name = "Task 1";
                else
                    name = "Task " + (mTaskList.size() / 2 + 1);
                mTaskList.add(new Task(name, ""));
                mTaskEditListAdapter.notifyDataSetChanged();
            }
        });

        // Go to detail view when task item is clicked.
        mTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Every second item is an arrow.
                if (position % 2 == 1)
                    return;
                Task task = mTaskList.get(position);
                mPositionOfLastEditedTask = position;
                Intent intent = new Intent(getActivity(), AddChallengeTaskDetail.class);
                intent.putExtra(AddChallengeTaskDetail.INTENT_TITLE, task.getTitle());
                intent.putExtra(AddChallengeTaskDetail.INTENT_DESCRIPTION, task.getDescription());
                intent.putExtra(AddChallengeTaskDetail.INTENT_DURATION, task.getDuration());
                intent.putStringArrayListExtra(AddChallengeTaskDetail.INTENT_IMAGEPATHLIST, (ArrayList) ImagePath.convertToStringList(task.getImagePaths()));
                startActivityForResult(intent, AddChallengeTaskDetail.REQUEST_SET_DETAIL);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (AddChallengeTaskDetail.REQUEST_SET_DETAIL):
                if (resultCode == Activity.RESULT_OK) {
                    // Get the last task that the user clicked and set its attributes to what the intent specifies.
                    Task task = mTaskList.get(mPositionOfLastEditedTask);
                    task.setTitle(data.getStringExtra(AddChallengeTaskDetail.INTENT_TITLE));
                    task.setDescription(data.getStringExtra(AddChallengeTaskDetail.INTENT_DESCRIPTION));
                    task.setDuration(data.getIntExtra(AddChallengeTaskDetail.INTENT_DURATION, 1));
                    task.setImagePaths(ImagePath.convertToImagePathList(data.getStringArrayListExtra(AddChallengeTaskDetail.INTENT_IMAGEPATHLIST)));
                    mTaskEditListAdapter.notifyDataSetChanged();
                }
        }
    }
}
