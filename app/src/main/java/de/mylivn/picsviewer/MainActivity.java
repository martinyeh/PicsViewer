package de.mylivn.picsviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG= "MainActivity";


    private RecyclerView mRecyclerView;
    private RVAdapter mRVAdapter;

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

        Log.d(LOG_TAG, "onCreate");
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

        mRecyclerView = findViewById(R.id.rv1);
        mRecyclerView.setHasFixedSize(false);

        SpannedGridLayoutManager spannedGridLayoutManager = new SpannedGridLayoutManager(
                SpannedGridLayoutManager.Orientation.VERTICAL, 3);
        mRecyclerView.setLayoutManager(spannedGridLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecorator(10));

        ArrayList<ImageItem> itemsList = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("items");

            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject m_jObj = m_jArry.getJSONObject(i);
                Log.d( LOG_TAG, "uuid: " + m_jObj.getString("uuid"));
                Log.d( LOG_TAG, "url: " + m_jObj.getString("imageUrlString"));

                String uuid = m_jObj.getString("uuid");
                String imgUrlStr = m_jObj.getString("imageUrlString");

                //Add your values in your `ArrayList` as below:
                ImageItem imgItem = new ImageItem();
                imgItem.uuid = uuid;
                imgItem.url = imgUrlStr;

                itemsList.add(imgItem);
            }

        } catch (JSONException e) {
            Log.d(LOG_TAG, "json parsing errors: "+ e.getMessage());
        }

        mRVAdapter = new RVAdapter(MainActivity.this, itemsList);
        mRecyclerView.setAdapter(mRVAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CardsTouchHelperCallback(mRVAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

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
