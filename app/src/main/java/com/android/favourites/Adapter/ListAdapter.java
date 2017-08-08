package com.android.favourites.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.favourites.Database.ListReaderContract;
import com.android.favourites.Database.ListReaderDbHelper;
import com.android.favourites.ListFlavour;
import com.android.favourites.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<ListFlavour> list;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mDesc;
        public TextView mType;
        public TextView mViewCount;
        public Button mButton;
        public CardView card;
        public ImageView thumb;

        public ViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.title);
            mType = (TextView) view.findViewById(R.id.desc);
            mDesc = (TextView) view.findViewById(R.id.type);
            mViewCount = (TextView) view.findViewById(R.id.view_count);
            mButton = (Button) view.findViewById(R.id.add);
            card = (CardView) view.findViewById(R.id.cardview);
            thumb = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public ListAdapter(List<ListFlavour> mList, Context mContext) {
        this.list = mList;
        this.mContext = mContext;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment1_adapter, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ListFlavour listFlavour = list.get(position);

        holder.mTitle.setText(listFlavour.getTitle());
        holder.mDesc.setText(listFlavour.getDesc());
        holder.mType.setText(listFlavour.getType());
        holder.mViewCount.setText(listFlavour.getView_count());

        final String imgUrl = listFlavour.getImgUrl();
        Picasso.with(mContext).load(imgUrl).into(holder.thumb);

        String desc = listFlavour.getType();
        if (desc.equals("internship"))
            holder.card.setCardBackgroundColor(Color.parseColor("#c0e0e8"));

        else if (desc.equals("offer"))
            holder.card.setCardBackgroundColor(Color.parseColor("#ff9999"));

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListReaderDbHelper mDbHelper = new ListReaderDbHelper(view.getContext());

                // Gets the data repository in write mode
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(ListReaderContract.FeedEntry.COLUMN_NAME_TITLE, holder.mTitle.getText().toString());
                values.put(ListReaderContract.FeedEntry.COLUMN_NAME_DESC, holder.mDesc.getText().toString());
                values.put(ListReaderContract.FeedEntry.COLUMN_NAME_TYPE, holder.mType.getText().toString());
                values.put(ListReaderContract.FeedEntry.COLUMN_NAME_COUNT, holder.mViewCount.getText().toString());
                values.put(ListReaderContract.FeedEntry.COLUMN_NAME_IMGURL, imgUrl);


                long newRowId = db.insert(ListReaderContract.FeedEntry.TABLE_NAME, null, values);

                Toast.makeText(mContext, "Item Added to List!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}