package org.gdg_lome.codelab_connectapptocloud;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.gdg_lome.codelab_connectapptocloud.data.ProgrammeDbHelper;
import org.gdg_lome.codelab_connectapptocloud.data.ProgrammeContract.MatiereEntry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * programme scolaire : liste des matieres :)
 */

public class MainActivity extends AppCompatActivity {

    private ListView list;
    private ArrayList<HashMap<String, String>> programmes =
            new ArrayList<HashMap<String, String>>();
    private ListAdapter adapter;
    private ProgrammeDbHelper db;
    private String [] keys = {
            Config.LOGO_KEY,
            Config.NOM_KEY,
            Config.DESCRIPTION_KEY};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new ProgrammeDbHelper(this);
        adapter = new ListAdapter(this,programmes);

        new AsyncTask<Void,Void,Cursor>(){
            @Override
            protected Cursor doInBackground(Void... params) {
                try{
                    if(isConnected()) {
                        JSONObject json = new JSONObject(syncronisation());
                        JSONArray json_programme =
                                json.getJSONArray("Programmes");
                        ContentValues values[] =
                                new ContentValues[json_programme.length()];
                        for (int i = 0; i < json_programme.length(); i++) {
                            ContentValues value = new ContentValues();
                            value.put(MatiereEntry.COLUMN_MATIERE_LOGO,
                                    json_programme.getJSONObject(i)
                                            .getString(Config.LOGO_KEY));

                            value.put(MatiereEntry.COLUMN_MATIERE_NOM,
                                    json_programme.getJSONObject(i)
                                            .getString(Config.NOM_KEY));

                            value.put(MatiereEntry.COLUMN_MATIERE_DESCRIPTION,
                                    json_programme.getJSONObject(i)
                                            .getString(Config.DESCRIPTION_KEY));
                            values[i] = value;
                        }
                        long count = db.insert(values);
                    }
                    return db.get();
                }catch (Exception e){

                }
                return null;
            }

            @Override
            protected void onPostExecute(Cursor s) {
                programmes.clear();
                while (s.moveToNext()){
                    HashMap<String, String> programme =
                            new HashMap<String, String>();
                    for (int j = 0; j < keys.length; j++) {
                        programme.put(keys[j],s.getString(j+1));
                    }
                    programmes.add(programme);
                }

                adapter.notifyDataSetChanged();
                super.onPostExecute(s);

            }
        }.execute();

        list = (ListView)findViewById(R.id.list);



        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, programmes.get(position).get(Config.NOM_KEY), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public String syncronisation() throws IOException{
        OkHttpClient okHttpClient = new OkHttpClient();
        String result;
        Request request = new Request
                .Builder()
                .url(Config.BACKEND_URL)
                .build();
        Response response = okHttpClient.
                newCall(request).
                execute();
        result = response.body().string();
        return result;

    }

    public Boolean isConnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager)
               getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if(networkInfo!=null)
            return true;
        return false;
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
