package sebastians.challenge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import sebastians.challenge.R;
import sebastians.challenge.data.Challenge;

/**
 * Created by kunterbunt on 13.06.15.
 */
public class ChallengeAdapter extends ListAdapter<Challenge> {

    private static final int resource = R.layout.listitem_challenge;

    public ChallengeAdapter(Context context, List<Challenge> objects) {
        super(context, resource, objects);
    }

    @Override
    public void style(View view, int position) {
        ((TextView) view.findViewById(R.id.name)).setText(getItem(position).getName());
    }
}
