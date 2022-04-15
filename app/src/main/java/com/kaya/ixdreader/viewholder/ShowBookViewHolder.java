package com.kaya.ixdreader.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaya.ixdreader.R;


public class ShowBookViewHolder  extends RecyclerView.ViewHolder {
    public TextView bookshow_text;
    public ShowBookViewHolder(@NonNull View itemView) {
        super(itemView);
        bookshow_text = itemView.findViewById(R.id.book_name_show_tx);
    }
}
