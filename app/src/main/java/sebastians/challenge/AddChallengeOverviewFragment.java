package sebastians.challenge;

import android.content.Intent;
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

import sebastians.challenge.adapter.ChallengeItemAdapter;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.data.ChallengeItem;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddChallengeOverviewFragment extends Fragment {

    private ListView taskListView;
    private List<ChallengeItem> taskList;
    private ChallengeItemAdapter taskListAdapter;
    private Challenge challenge;

    public AddChallengeOverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_challenge_overview, container, false);

        // Set list view adapter.
        taskListView = (ListView) view.findViewById(R.id.task_list);
        taskList = new ArrayList<>();
        taskListAdapter = new ChallengeItemAdapter(getActivity(), taskList);
        taskListView.setAdapter(taskListAdapter);

        // Add button action.
        ImageButton addTaskButton = (ImageButton) view.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskList.add(new ChallengeItem("Task " + (taskList.size() + 1), "no description"));
                taskListAdapter.notifyDataSetChanged();
            }
        });

        // Go to detail view when task item is clicked.
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AddChallengeTaskDetail.class);

            }
        });

        return view;
    }
}
