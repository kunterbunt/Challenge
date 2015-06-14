package sebastians.challenge.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
//import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.R;

/**
 * Created by kunterbunt on 14.06.15.
 */
public class ViewPagerAdapter extends PagerAdapter {

    public static final String LOG_TAG = "ViewPagerAdapter";

    protected List<Uri> mImageUris = null;

    protected Context context;

    public ViewPagerAdapter(Context context) {
        this(context, new ArrayList<Uri>());
    }

    public ViewPagerAdapter(Context context, List<Uri> imageUris) {
        this.context = context;
        mImageUris = imageUris;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mImageUris.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
//        int padding = context.getResources().getDimensionPixelSize(
//                R.dimen.padding_medium);
//        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageURI(mImageUris.get(position));
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    public void add(Uri uri) {
        mImageUris.add(uri);
    }

    public List<Uri> getImageUris() {
        return mImageUris;
    }
}
