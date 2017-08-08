package com.android.favourites.Fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.favourites.Adapter.ListAdapter;
import com.android.favourites.Database.ListReaderDbHelper;
import com.android.favourites.ListFlavour;
import com.android.favourites.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {

    Context mContext;
    List<ListFlavour> list = new ArrayList<>();
    View rootView;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
        new FetchListTask1().execute();

        ListReaderDbHelper mDbHelper = new ListReaderDbHelper(mContext);

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String DELETE_LIST = "DELETE FROM list;";
        db.execSQL(DELETE_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_1, container,
                false);

        return rootView;
    }

    public void attachViews() {
        if (list != null) {

            RecyclerView mRecyclerView;
            RecyclerView.Adapter mAdapter;
            RecyclerView.LayoutManager mLayoutManager;

            // use a linear layout manager
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
            mLayoutManager = new LinearLayoutManager(rootView.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new ListAdapter(list, getContext());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

    }

    private class FetchListTask1 extends AsyncTask<Void, Void, Void> {

        String JsonString = null;

        @Override
        protected Void doInBackground(Void[] unused) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                final String built_url =
                        "http://54.254.198.83:8080/favourite.json";

                URL url = new URL(built_url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                JsonString = buffer.toString();
            } catch (IOException e) {
                Log.e(FetchListTask1.class.getSimpleName(), "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(FetchListTask1.class.getSimpleName(), "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            try {
                getListData();
            } catch (JSONException e) {
                Log.e(FetchListTask1.class.getSimpleName(), e.getMessage(), e);
                e.printStackTrace();
            }
        }

        public void getListData() throws JSONException {

            // JSON List information
            final String TITLE = "title";
            final String DESC = "desc";
            final String TYPE = "type";
            final String VIEW_COUNT = "view-count";
            final String IMG_URL = "imageUrl";

            try {

                JSONArray jsonArray = new JSONArray(JsonString);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject listObject = jsonArray.getJSONObject(i);

                    String t = listObject.getString(TITLE);
                    String d = listObject.getString(DESC);
                    String type = listObject.getString(TYPE);
                    String imgUrl = listObject.getString(IMG_URL);

                    int view_count = listObject.getInt(VIEW_COUNT);
                    int part1 = view_count / 1000;
                    int part2 = (int) Math.floor((view_count % 1000) * 0.01);
                    String vc = part1 + "." + part2 + "k";

                    ListFlavour lf = new ListFlavour(t, d, type, vc, imgUrl);
                    list.add(lf);
                }
            } catch (JSONException e) {
                Log.e(FetchListTask1.class.getSimpleName(), e.getMessage(), e);
                e.printStackTrace();
            } finally {
                attachViews();
            }
        }
    }
}

