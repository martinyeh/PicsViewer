package de.mylivn.picsviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

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

        ArrayList<ImageItem> itemsList = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("items");

            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject m_jObj = m_jArry.getJSONObject(i);
                //Log.d( LOG_TAG, "uuid: " + m_jObj.getString("uuid"));
                //Log.d( LOG_TAG, "url: " + m_jObj.getString("imageUrlString"));

                String uuid = m_jObj.getString("uuid");
                String imgUrlStr = m_jObj.getString("imageUrlString");

                //Add your values in your `ArrayList` as below:
                ImageItem imgItem = new ImageItem();
                imgItem.uuid = uuid;
                imgItem.url = imgUrlStr;

                itemsList.add(imgItem);
                if(i==0){
                    // put some redundant objects for the first item
                    itemsList.add(imgItem);
                    itemsList.add(imgItem);
                    itemsList.add(imgItem);
                }
            }

        } catch (JSONException e) {
            Log.d(LOG_TAG, "json parsing errors: "+ e.getMessage());
        }


        //customized the order of the gridview; do swap for some rows
        for( int i=6; i<itemsList.size(); i+=3){
            if( (i/3) %2 ==0 ){
                Log.i(LOG_TAG, "index:" +i);
                ImageItem imgItem = itemsList.get(i);
                itemsList.set(i, itemsList.get(i+2));
                itemsList.set(i+2, imgItem);
            }
        }

        GridView gridView = (GridView)findViewById(R.id.gridView1);
        GridViewAdaptor adapter = new GridViewAdaptor(MainActivity.this);
        adapter.setData(itemsList);
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




}
