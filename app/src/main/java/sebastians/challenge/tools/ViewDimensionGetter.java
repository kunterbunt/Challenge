package sebastians.challenge.tools;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by kunterbunt on 16.06.15.
 * Retrieves a view's dimension after its layout has been laid out.
 */
public abstract class ViewDimensionGetter {

    /**
     * Use this tag for logging purposes.
     */
    public static final String LOG_TAG = "ViewDimensionGetter";

    /**
     * Retrieves a view's dimension after its layout has been laid out.
     * @param view
     */
    public ViewDimensionGetter(final View view) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    int viewWidth = view.getWidth();
                    int viewHeight = view.getHeight();
                    sizeWasSet(viewWidth, viewHeight);
                }
            });
        }
    }

    /**
     * Called when the view's size was set, e.g. its layout laid out.
     * @param width
     * @param height
     */
    public abstract void sizeWasSet(int width, int height);
}
