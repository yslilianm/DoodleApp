package com.example.android.drawingtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yslilianm on 2018/4/20.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {
    private List<Drawing> drawingList;
    private Context context;


    public GalleryAdapter(List<Drawing> drawingList, Context context) {
        this.drawingList = drawingList;
        this.context = context;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_cardview, parent, false);
        return new GalleryViewHolder(view, context);
    }

    /**
     * Set data to each views in the cardView
     */
    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        Drawing drawing = drawingList.get(position);
        holder.tv_title.setText(drawing.getTitle());
        Log.i("drawing.getTitle()", drawing.getTitle());
        holder.iv_bitmap.setImageBitmap(ImageUtil.byteStringToBitmap(drawing.getBitmap()));
        Log.i("drawing.getBitmap()", drawing.getBitmap());
        holder.tv_timestamp.setText(drawing.getTimestamp());
        Log.i("drawing.getTimestamp()", drawing.getTimestamp());

    }

    @Override
    public int getItemCount() {
        return null != drawingList ? drawingList.size() : 0;
//        return drawingList.size();

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
