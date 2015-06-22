package sebastians.challenge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;

import java.util.List;

import sebastians.challenge.R;

/**
 * Created by kunterbunt on 14.06.15.
 */
public abstract class ListAdapter<T> extends ArrayAdapter<T> {

    private int mResource;

    public ListAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        mResource = resource;
    }

    public abstract void style(View view, int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(mResource, parent, false);
        // Have implementation add listeners and stuff.
        style(view, position);
        return view;
    }
}