package sebastians.challenge.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import sebastians.challenge.R;
import sebastians.challenge.data.ChallengeItem;

/**
 * Created by kunterbunt on 14.06.15.
 */
public class ChallengeItemAdapter extends ListAdapter<ChallengeItem> {

    public ChallengeItemAdapter(Context context, List<ChallengeItem> objects) {
        super(context, R.layout.listitem_subchallenge, objects);
    }

    @Override
    public void style(View view, int position) {
        ((TextView) view.findViewById(R.id.name)).setText(getItem(position).getTitle());
        ((TextView) view.findViewById(R.id.number)).setText("" + (position + 1));
    }
}
