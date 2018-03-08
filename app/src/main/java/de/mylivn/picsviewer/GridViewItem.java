package de.mylivn.picsviewer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by martinyeh on 2018/3/6.
 */

public class GridViewItem extends android.support.v7.widget.AppCompatImageView {
    public GridViewItem(Context context) {
        super(context);
    }

    public GridViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec); // This is the key that will make the height equivalent to its width
    }
}
