package sebastians.challenge.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.R;
import sebastians.challenge.data.ImagePath;
import sebastians.challenge.tools.PhotoManager;

/**
 * Created by kunterbunt on 14.06.15.
 */
public class ViewPagerImageAdapter extends PagerAdapter {

    public static final String LOG_TAG = "ViewPagerImageAdapter";

    private Animator mAnimator;
    private int mAnimationDuration;
    private ImageView mExpandedImageView = null;
    private View mContainerLayout, mExpandedImageViewOverlay;

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
        final ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(mScaleType);

        ImagePath imagePath = mImagePaths.get(position);
        final Bitmap bitmap = PhotoManager.getBitmap(imagePath);

        // Set up animation click event.
        if (mExpandedImageView != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zoomImage(imageView, bitmap);
                }
            });
        }

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

    /**
     * Your layout needs two additional views:
     * 1) An ImageView that is set to INVISIBLE with a size to cover the whole screen
     * 2) A View that is also INVISIBLE that has a semi-transparent background.
     * The ImageView needs to be on top of the background View.
     * @param rootView The same view that is inflated during creation. After creation use getView() to get it.
     */
    public void setUpForZoomAnimation(View rootView) {
        mAnimationDuration = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);
        mExpandedImageView = (ImageView) rootView.findViewById(R.id.expanded_image);
        mContainerLayout = rootView;
        mExpandedImageViewOverlay = rootView.findViewById(R.id.expanded_image_overlay);
        if (mExpandedImageView == null || mExpandedImageViewOverlay == null || mContainerLayout == null)
            throw new IllegalArgumentException("You need to follow the instructions of setupForZoomAnimation!");
    }

    private void zoomImage(final View originalView, Bitmap bitmap) {
        // Cancel current animation and start this one.
        if (mAnimator != null)
            mAnimator.cancel();

        // Load zoomed-in image.
        mExpandedImageView.setImageBitmap(bitmap);

        // Calculate starting and ending bounds for zoomed-in image.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        originalView.getGlobalVisibleRect(startBounds);
        mContainerLayout.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        originalView.setAlpha(0f);
        mExpandedImageViewOverlay.setVisibility(View.VISIBLE);
        mExpandedImageView.setVisibility(View.VISIBLE);
        mExpandedImageView.setPivotX(0f);
        mExpandedImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(mExpandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(mExpandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(mExpandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(mExpandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mAnimator = null;
            }
        });
        set.start();
        mAnimator = set;

        final float startScaleFinal = startScale;
        mExpandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnimator != null) {
                    mAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(mExpandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(mExpandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(mExpandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(mExpandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        originalView.setAlpha(1f);
                        mExpandedImageView.setVisibility(View.GONE);
                        mExpandedImageViewOverlay.setVisibility(View.GONE);
                        mAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        originalView.setAlpha(1f);
                        mExpandedImageView.setVisibility(View.GONE);
                        mExpandedImageViewOverlay.setVisibility(View.GONE);
                        mAnimator = null;
                    }
                });
                set.start();
                mAnimator = set;
            }
        });

    }
}
