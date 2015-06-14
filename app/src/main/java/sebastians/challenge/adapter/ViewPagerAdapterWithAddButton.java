package sebastians.challenge.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

import sebastians.challenge.R;

/**
 * Created by kunterbunt on 14.06.15.
 * Implements swipable images. Last image is always an add button.
 */
public abstract class ViewPagerAdapterWithAddButton extends ViewPagerAdapter {

    public ViewPagerAdapterWithAddButton(Context context, List<Uri> imageUris) {
        super(context, imageUris);
    }

    /**
     * Define what should happen when the add button is pressed.
     */
    public abstract void onAddButtonPressed();

    @Override
    public int getCount() {
        // Number of images plus add button.
        return mImageUris.size() + 1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position < getCount() - 1)
            return super.instantiateItem(container, position);
        else {
            ImageButton addButton = new ImageButton(context, null, R.style.FAB_Final);
            addButton.setBackgroundResource(R.drawable.button_fab);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddButtonPressed();
                }
            });
//            addButton.setImageDrawable(context.getResources().getDrawable(R.drawable.button_fab));

            container.addView(addButton, 0);
            return addButton;
        }


    }
}
