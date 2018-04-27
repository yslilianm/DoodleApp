package com.example.android.drawingtest;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by yslilianm on 2018/4/20.
 */

public class GalleryViewHolder extends RecyclerView.ViewHolder {
    public CardView cardView;
    public TextView tv_title;
    public ImageView iv_bitmap;
    public TextView tv_timestamp;
    public Button bt_love;
    public int count = 0;

    public GalleryViewHolder(View view, final Context context) {
        super(view);
        cardView = (CardView) view.findViewById(R.id.cardView);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        iv_bitmap = (ImageView) view.findViewById(R.id.iv_drawingResult);
        tv_timestamp = (TextView) view.findViewById(R.id.tv_timestamp);
        bt_love = (Button) view.findViewById(R.id.bt_love);

        /**
         * Like the drawing in the gallery
         */
        bt_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Click the "Love It" tag
                count++;
                if (count % 2 == 1) {
                    bt_love.setBackgroundColor(Color.parseColor(Key.COLOR_ACCENT));
                }
                //Click the "Love It" tag
                else {
                    bt_love.setBackgroundColor(Color.parseColor(Key.COLOR_BTN_DEFAULT));
                }


            }
        });
    }

}

