package sebastians.challenge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.R;
import sebastians.challenge.data.Task;

/**
 * Created by kunterbunt on 14.06.15.
 */
public class TaskEditListAdapter extends ListAdapter<Task> {

    private List<Task> list;

    public TaskEditListAdapter(Context context, List<Task> objects) {
        super(context, R.layout.listitem_task, objects);
        list = objects;
    }

    @Override
    public void style(View view, int position) {
        ((TextView) view.findViewById(R.id.name)).setText(getItem(position).getTitle());
        if (position == 0)
            ((TextView) view.findViewById(R.id.number)).setText("" + (position + 1));
        else
            ((TextView) view.findViewById(R.id.number)).setText("" + (position / 2 + 1));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Regular task item.
        if (getItem(position) != null)
            return super.getView(position, convertView, parent);
        // Task Header
        else {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.listitem_task_header, parent, false);

            View tailBefore = view.findViewById(R.id.tailBefore);
            View tailAfter = view.findViewById(R.id.tailAfter);
            int distance_large = (int) getContext().getResources().getDimension(R.dimen.task_distance_large);
            int distance_medium = (int) getContext().getResources().getDimension(R.dimen.task_distance_medium);
            int distance_small = (int) getContext().getResources().getDimension(R.dimen.task_distance_small);

            String durationString;
            int duration = getItem(position - 1).getDuration();
            duration /= 3600;
            if (duration % 24 == 0) {
                duration /= 24;
                if (duration > 1) {
                    durationString = duration + " days";
                    tailBefore.getLayoutParams().height = distance_large;
                    tailAfter.getLayoutParams().height = distance_large;
                } else {
                    durationString = duration + " day";
                    tailBefore.getLayoutParams().height = distance_medium;
                    tailAfter.getLayoutParams().height = distance_medium;
                }
            } else {
                if (duration > 1) {
                    durationString = duration + " hours";
                    tailBefore.getLayoutParams().height = distance_medium;
                    tailAfter.getLayoutParams().height = distance_medium;
                } else {
                    durationString = duration + " hour";
                    tailBefore.getLayoutParams().height = distance_small;
                    tailAfter.getLayoutParams().height = distance_small;
                }
            }
            ((TextView) view.findViewById(R.id.duration)).setText(durationString);
            return view;
        }
    }

    public List<Task> getTaskListWithoutHeaders() {
        List<Task> cleanedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            if (list.get(i) != null)
                cleanedList.add(list.get(i));
        return cleanedList;
    }
}
