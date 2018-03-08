package de.mylivn.picsviewer;

import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.picasso.Transformation;

/**
 * Created by martinyeh on 2018/3/6.
 */

public class CropSquareTransformation implements Transformation {

    private int x;
    private int y;

    CropSquareTransformation(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override public Bitmap transform(Bitmap source) {
        Bitmap result = Bitmap.createBitmap(source, x*source.getWidth()/2, y*source.getHeight()/2, source.getWidth()/2, source.getHeight()/2);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override public String key() { return String.valueOf(this.hashCode()); }
}
