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
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.adapter.TaskListAdapter;
import sebastians.challenge.data.ImagePath;
import sebastians.challenge.data.Task;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddChallengeOverviewFragment extends Fragment {

    private ListView mTaskListView;
    private List<Task> mTaskList;
    private TaskListAdapter mTaskListAdapter;
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
        mTaskListAdapter = new TaskListAdapter(getActivity(), mTaskList);
        mTaskListView.setAdapter(mTaskListAdapter);

        // Add button action.
        ImageButton addTaskButton = (ImageButton) view.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskList.add(new Task("Task " + (mTaskList.size() + 1), ""));
                mTaskListAdapter.notifyDataSetChanged();
            }
        });

        // Go to detail view when task item is clicked.
        mTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = mTaskList.get(position);
                mPositionOfLastEditedTask = position;
                Intent intent = new Intent(getActivity(), AddChallengeTaskDetail.class);
                intent.putExtra(AddChallengeTaskDetail.INTENT_TITLE, task.getTitle());
                intent.putExtra(AddChallengeTaskDetail.INTENT_DESCRIPTION, task.getDescription());
                intent.putExtra(AddChallengeTaskDetail.INTENT_TIMEAFTERPREV, task.getDurationValidity());
                intent.putStringArrayListExtra(AddChallengeTaskDetail.INTENT_IMAGEPATHLIST, (ArrayList) ImagePath.convertToStringList(task.getImagePaths()));
                startActivityForResult(intent, AddChallengeTaskDetail.REQUEST_SET_DETAIL);
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
                    Task task = mTaskList.get(mPositionOfLastEditedTask);
                    task.setTitle(data.getStringExtra(AddChallengeTaskDetail.INTENT_TITLE));
                    task.setDescription(data.getStringExtra(AddChallengeTaskDetail.INTENT_DESCRIPTION));
                    task.setDurationValidity(data.getIntExtra(AddChallengeTaskDetail.INTENT_TIMEAFTERPREV, 1));
                    task.setImagePaths(ImagePath.convertToImagePathList(data.getStringArrayListExtra(AddChallengeTaskDetail.INTENT_IMAGEPATHLIST)));
                    mTaskListAdapter.notifyDataSetChanged();
                }
        }
    }
}
