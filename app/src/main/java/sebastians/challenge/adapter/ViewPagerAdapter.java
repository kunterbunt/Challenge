package sebastians.challenge.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import sebastians.challenge.R;

/**
 * Created by kunterbunt on 14.06.15.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private int[] mImages = new int[] {
            R.drawable.rubicscube_up,
            R.drawable.rubicscube_left
    };

    private List<String> mImagePaths = null;

    private Context context;

    public ViewPagerAdapter(Context context){
        this.context = context;
    }

    public ViewPagerAdapter(Context context, List<String> imagePaths) {
        this.context = context;
        mImagePaths = imagePaths;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public int getCount() {
        return mImagePaths == null ? mImages.length : mImagePaths.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        int padding = context.getResources().getDimensionPixelSize(
                R.dimen.padding_medium);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (mImagePaths == null)
            imageView.setImageResource(mImages[position]);
        else
            imageView.setImageURI(Uri.parse(mImagePaths.get(position)));
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}
