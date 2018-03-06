package de.mylivn.picsviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG= "MainActivity";

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("images.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        ArrayList<ImageItem> formList = new ArrayList<ImageItem>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("items");

            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject item = m_jArry.getJSONObject(i);
                Log.d( LOG_TAG, "uuid: " + item.getString("uuid"));
                Log.d( LOG_TAG, "url: " + item.getString("imageUrlString"));

                String uuid = item.getString("uuid");
                String imgUrlStr = item.getString("imageUrlString");

                //Add your values in your `ArrayList` as below:
                ImageItem imgItem = new ImageItem();
                imgItem.uuid = uuid;
                imgItem.url = imgUrlStr;

                formList.add(imgItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GridView gridView = (GridView)findViewById(R.id.gridView1);
        GridViewAdaptor adapter = new GridViewAdaptor(MainActivity.this);
        adapter.setData(formList);
        gridView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class GridViewAdaptor extends BaseAdapter {

        ArrayList<ImageItem> itemsList = new ArrayList<>();
        Context context;
        private LayoutInflater inflater;
        boolean loaded = false;


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

            switch(position){
                case 0:
                    if(loaded){
                        return convertView;
                    }
                    loaded = true;
                    Log.d(LOG_TAG, "load position 0");
                    Picasso.with(context).load(itemsList.get(0).url).transform(new CropSquareTransformation(0,0)).fit().into((ImageView)convertView);
                    //Picasso.with(context).invalidate(itemsList.get(0).url);
                    return convertView;
                case 1:
                    Log.d(LOG_TAG, "load position 1");
                    //imageView.setImageBitmap(Bitmap.createBitmap(bitmap, bitmap.getWidth()/2, 0, bitmap.getWidth()/2, bitmap.getHeight()/2));
                    //convertView.setBackgroundColor(Color.GREEN);
                    Picasso.with(context).load(itemsList.get(0).url).transform(new CropSquareTransformation(1,0)).fit().into((ImageView)convertView);
                    //Picasso.with(context).invalidate(itemsList.get(0).url);
                    return convertView;
                case 3:
                    Log.d(LOG_TAG, "load position 3");
                    //imageView.setImageBitmap(Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/2, bitmap.getWidth()/2, bitmap.getHeight()/2));
                    //imageView.setBackgroundColor(Color.YELLOW);
                    Picasso.with(context).load(itemsList.get(0).url).transform(new CropSquareTransformation(0,1)).fit().into((ImageView)convertView);
                    //Picasso.with(context).invalidate(itemsList.get(0).url);
                    return convertView;
                case 4:
                    Log.d(LOG_TAG, "load position 4");
                    //imageView.setImageBitmap(Bitmap.createBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, bitmap.getHeight()/2));
                    //imageView.setBackgroundColor(Color.MAGENTA);
                    Picasso.with(context).load(itemsList.get(0).url).transform(new CropSquareTransformation(1,1)).fit().into((ImageView)convertView);
                    return convertView;
                default:
                    Log.d(LOG_TAG, "load url: "+itemsList.get(position).url);
                    if(position==2){
                        Picasso.with(context).load(itemsList.get(position-1).url).fit().into((ImageView)convertView);
                    }else{
                        Picasso.with(context).load(itemsList.get(position-3).url).fit().into((ImageView)convertView);
                    }
                    return convertView;
            }
        }

    }


}
