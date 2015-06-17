package sebastians.challenge.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sebastians.challenge.R;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.data.ImagePath;
import sebastians.challenge.data.Task;
import sebastians.challenge.tools.PhotoManager;

/**
 * Created by kunterbunt on 13.06.15.
 */
public class ChallengeAdapter extends ListAdapterHeader<Challenge> {

    private static final int RESOURCE_ITEM = R.layout.listitem_challenge;
    private static final int RESOURCE_HEADER = R.layout.listitem_challenge_header;

    public ChallengeAdapter(Context context, List<Challenge> objects) {
        super(context, RESOURCE_ITEM, RESOURCE_HEADER, objects);
    }

    @Override
    public void style(View view, int position) {
        Challenge challenge = getItem(position);
        ((TextView) view.findViewById(R.id.name)).setText(challenge.getName());
        ((TextView) view.findViewById(R.id.tasks)).setText(challenge.getTaskList().size() + " tasks");
        // Find first image path.
        ImagePath path = null;
        for (Task task : challenge.getTaskList()) {
            if (task.getImagePaths().size() > 0) {
                path = task.getImagePaths().get(0);
                break;
            }
        }

        if (path != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            PhotoManager.setFittingBitmap(path, imageView, 96, 96);
        }
    }

    @Override
    public void styleHeader(View view, boolean active) {
        if (active) {
            ((TextView) view.findViewById(R.id.name)).setText("Active");
            ((TextView) view.findViewById(R.id.name)).setTextColor(getContext().getResources().getColor(android.R.color.black));
        } else {
            ((TextView) view.findViewById(R.id.name)).setText("Inactive");
        }
    }
}
