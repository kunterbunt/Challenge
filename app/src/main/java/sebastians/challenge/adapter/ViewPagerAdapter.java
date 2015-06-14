package sebastians.challenge.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import sebastians.challenge.R;

/**
 * Created by kunterbunt on 14.06.15.
 */
public class ViewPagerAdapter extends PagerAdapter {

    public static final String LOG_TAG = "ViewPagerAdapter";

    private int[] mImages = new int[] {
            R.drawable.rubicscube_up,
            R.drawable.rubicscube_left
    };

    protected List<Uri> mImageUris = null;

    protected Context context;

    public ViewPagerAdapter(Context context){
        Log.i(LOG_TAG, "Using test-mode of ViewPagerAdapter. Use other constructor for own images.");
        this.context = context;
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
        return mImageUris == null ? mImages.length : mImageUris.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        int padding = context.getResources().getDimensionPixelSize(
                R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (mImageUris == null) {
            imageView.setImageResource(mImages[position]);
        } else {
            imageView.setImageURI(mImageUris.get(position));
        }
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}
