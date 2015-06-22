package sebastians.challenge.adapter;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import sebastians.challenge.R;
import sebastians.challenge.data.Task;
import sebastians.challenge.tools.TimeManager;

/**
 * Created by kunterbunt on 19.06.15.
 */
public class TaskShowListAdapter extends ListAdapter<Task> {

    /**
     * Use this tag for logging purposes.
     */
    public static final String LOG_TAG = "TaskShowListAdapter";
    private int mSelectedPosition;

    public TaskShowListAdapter(Context context, List<Task> tasks) {
        super(context, R.layout.listitem_challenge_detail, tasks);
        mSelectedPosition = 0;
    }

    @Override
    public void style(View view, final int position) {
        // Set name.
        final TextView nameField = (TextView) view.findViewById(R.id.name);
        nameField.setText(getItem(position).getTitle());

        // Set description.
        final TextView descriptionField = (TextView) view.findViewById(R.id.description);
        descriptionField.setText(getItem(position).getDescription());

        // Color the circle green if it is the selected task.
        if (position == mSelectedPosition) {
            final View circle = view.findViewById(R.id.circle);
            nameField.setTextColor(getContext().getResources().getColor(R.color.app_color_accent));
            circle.setPressed(true);
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN) {
                        circle.setPressed(true);
                    }
                    return true;
                }
            });
        }

        // Set duration.
        ((TextView) view.findViewById(R.id.duration)).setText(TimeManager.convertMillisecondsToDateString(getItem(position).getDuration()));
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }
}
