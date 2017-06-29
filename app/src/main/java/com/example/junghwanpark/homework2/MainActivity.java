package com.example.junghwanpark.homework2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.junghwanpark.homework2.model.NewsItem;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progress;
    private EditText search;
    private RecyclerView rv;
    private static final String TAG = "mainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        search = (EditText) findViewById(R.id.searchQuery);
        rv = (RecyclerView) findViewById(R.id.recyclerView);

        rv.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();

        if (itemNumber == R.id.search) {
            String s = search.getText().toString();
            NetworkTask task = new NetworkTask(s);
            task.execute();
        }

        return true;
    }

    class NetworkTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> {
        String query;

        NetworkTask(String s){
            query = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<NewsItem> doInBackground(URL... params)  {
            ArrayList<NewsItem> result = null;
            URL url = NetworkUtils.makeURL("the-next-web", "latest"," "); //put the api to make it work
            Log.d(TAG, "aysnTaskurl: " + url.toString());
            try {
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                result = NetworkUtils.parseJSON(json);
                //Log.d(TAG, "resultSize: " + result.size());
            } catch (IOException e) {
                e.printStackTrace();
                //Log.d(TAG, "threw io exception");
            } catch (JSONException e) {
                e.printStackTrace();
                //Log.d(TAG, "threw json exeption");
            }
            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<NewsItem> data) {
            super.onPostExecute(data);
            progress.setVisibility(View.GONE);
            if (data != null) {
                // NewsItem item = data.get(0);
                // System.out.println("Title: " + item.getTitle());
                //System.out.println("Description: " + item.getDescription());
                //System.out.println("Date: " + item.getPublishedAt());

                GithubAdapter adapter = new GithubAdapter(data, new GithubAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int clickedItemIndex) {
                        String url = data.get(clickedItemIndex).getUrl();
                        Log.d(TAG, String.format("Url %s", url));
                    }
                });
                rv.setAdapter(adapter);


            }else{
                Log.d(TAG, "no data");
            }
        }
    }
}

