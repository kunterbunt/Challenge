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
public class ChallengeAdapter extends ListAdapterHeader<Challenge> {

    private static final int resource = R.layout.listitem_challenge;
    private static final int resource2 = R.layout.listitem_challenge_header;

    public ChallengeAdapter(Context context, List<Challenge> objects) {
        super(context, resource, resource2, objects);
    }

    @Override
    public void style(View view, int position) {
        ((TextView) view.findViewById(R.id.name)).setText(getItem(position).getName());
    }

    @Override
    public void styleHeader(View view, boolean active) {
        if(active){
            ((TextView) view.findViewById(R.id.name)).setText("Active");
        }else{
            ((TextView) view.findViewById(R.id.name)).setText("InActive");
        }
    }
}
