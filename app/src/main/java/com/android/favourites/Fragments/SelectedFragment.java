package com.android.favourites.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.favourites.Adapter.SelectedListAdapter;
import com.android.favourites.Database.ListReaderContract;
import com.android.favourites.Database.ListReaderDbHelper;
import com.android.favourites.ListFlavour;
import com.android.favourites.R;

import java.util.ArrayList;
import java.util.List;

public class SelectedFragment extends Fragment {

    View root;
    List<ListFlavour> list = new ArrayList<>();
    SelectedListAdapter mAdapter;

    public SelectedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new FetchListTask().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_2, container, false);

        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) root.findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new FetchListTask().execute();
                        mAdapter.notifyDataSetChanged();
                        swipe.setRefreshing(false);
                    }
                }, 500);

            }
        });
        return root;
    }

    public void attachViews() {
        if (list != null) {
            RecyclerView mRecyclerView;
            RecyclerView.LayoutManager mLayoutManager;

            // use a linear layout manager
            mRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view2);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new SelectedListAdapter(list, getContext());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            TextView emptyView = (TextView) root.findViewById(R.id.empty);

            if (list.isEmpty()) {
                mRecyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
        }

    }

    private class FetchListTask extends AsyncTask<Void, Void, Void> {
        Cursor cursor;

        @Override
        protected Void doInBackground(Void... voids) {

            list = new ArrayList<>();

            try {
                ListReaderDbHelper mDbHelper = new ListReaderDbHelper(getContext());
                SQLiteDatabase db = mDbHelper.getReadableDatabase();

                String[] projection = {
                        ListReaderContract.FeedEntry._ID,
                        ListReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                        ListReaderContract.FeedEntry.COLUMN_NAME_DESC,
                        ListReaderContract.FeedEntry.COLUMN_NAME_TYPE,
                        ListReaderContract.FeedEntry.COLUMN_NAME_COUNT,
                        ListReaderContract.FeedEntry.COLUMN_NAME_IMGURL
                };


                cursor = db.query(
                        ListReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        null,                                // The columns for the WHERE clause
                        null,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );
                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {

                    String t = cursor.getString(cursor.getColumnIndex("title"));
                    String d = cursor.getString(cursor.getColumnIndex("desc"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
                    String vc = cursor.getString(cursor.getColumnIndex("view_count"));
                    String imgUrl = cursor.getString(cursor.getColumnIndex("imageUrl"));

                    cursor.moveToNext();

                    ListFlavour lf = new ListFlavour(t, d, type, vc, imgUrl);
                    list.add(lf);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            attachViews();
        }
    }
}