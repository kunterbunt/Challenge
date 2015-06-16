package sebastians.challenge.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
//import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.data.ImagePath;
import sebastians.challenge.tools.PhotoManager;

/**
 * Created by kunterbunt on 14.06.15.
 */
public class ViewPagerImageAdapter extends PagerAdapter {

    public static final String LOG_TAG = "ViewPagerImageAdapter";

    private List<ImagePath> mImagePaths;
    private Context mContext;
    private ImageView.ScaleType mScaleType;
    private int mCurrentPosition;
    private boolean needsToRefresh;

    public ViewPagerImageAdapter(Context context) {
        this(context, new ArrayList<ImagePath>());
        mScaleType = ImageView.ScaleType.CENTER_CROP;
        mCurrentPosition = 0;
        needsToRefresh = false;
    }

    public ViewPagerImageAdapter(Context context, List<ImagePath> imagePaths) {
        mContext = context;
        mImagePaths = imagePaths;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mImagePaths.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(mScaleType);

        ImagePath imagePath = mImagePaths.get(position);
        Bitmap bitmap = PhotoManager.getBitmap(imagePath);

        imageView.setImageBitmap(bitmap);
        container.addView(imageView, 0);
        mCurrentPosition = position;
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getItemPosition(Object object) {
        if (!needsToRefresh)
            return super.getItemPosition(object);
        else {
            needsToRefresh = false;
            return POSITION_NONE;
        }

    }

    /**
     * Change the scale type used to show the images.
     * @param scaleType
     */
    public void setScaleType(ImageView.ScaleType scaleType) {
        this.mScaleType = scaleType;
    }

    public void add(ImagePath path) {
        mImagePaths.add(path);
    }

    public void add(List<ImagePath> paths) {
        for (ImagePath path : paths)
            add(path);
    }

    public void remove(ImagePath path) {
        mImagePaths.remove(path);
    }

    public void remove(int position) {
        mImagePaths.remove(position);
    }

    public void clear() { mImagePaths.clear(); needsToRefresh = true;}

    public void removeCurrentImage() {
        mImagePaths.remove(mCurrentPosition);
    }

    public List<ImagePath> getImagePaths() {
        return mImagePaths;
    }
}
