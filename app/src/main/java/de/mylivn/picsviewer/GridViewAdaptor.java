package de.mylivn.picsviewer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by martinyeh on 2018/3/6.
 */

class GridViewAdaptor extends BaseAdapter {
    final String LOG_TAG= "GridViewAdaptor";

    ArrayList<ImageItem> itemsList = new ArrayList<>();
    Context context;
    private LayoutInflater inflater;

    public GridViewAdaptor(Context context){
        this.context= context;

        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<ImageItem> itemsList){
        this.itemsList = itemsList;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.gridview_item_image, parent, false);
        }

        Log.d(LOG_TAG, position + " load url: "+itemsList.get(position).url);

        switch(position){
            case 0:
                Picasso.with(context).load(itemsList.get(0).url).transform(new CropSquareTransformation(0,0)).fit().into((ImageView)convertView);
                return convertView;
            case 1:
                Picasso.with(context).load(itemsList.get(0).url).transform(new CropSquareTransformation(1,0)).fit().into((ImageView)convertView);
                return convertView;
            case 3:
                Picasso.with(context).load(itemsList.get(0).url).transform(new CropSquareTransformation(0,1)).fit().into((ImageView)convertView);
                return convertView;
            case 4:
                Picasso.with(context).load(itemsList.get(0).url).transform(new CropSquareTransformation(1,1)).fit().into((ImageView)convertView);
                return convertView;
            default:
                if(position==2){
                    Picasso.with(context).load(itemsList.get(position+2).url).fit().into((ImageView)convertView);
                }else{
                    Picasso.with(context).load(itemsList.get(position).url).fit().into((ImageView)convertView);
                }
                return convertView;
        }
    }

}