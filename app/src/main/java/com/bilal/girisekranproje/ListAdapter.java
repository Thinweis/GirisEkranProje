package com.bilal.girisekranproje;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    ArrayList<Haber> haberler;
    Context context;

    public ListAdapter(ArrayList<Haber> haberler, Context context) {
        this.haberler = haberler;
        this.context = context;
    }
    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        Haber haber = haberler.get(position);
        holder.baslik.setText(haber.baslik);
        holder.yazar.setText(haber.yazar);
        if(haber.imagepath!=null) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(haber.imagepath);
            holder.resim.setImageBitmap(imageBitmap);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return haberler.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView baslik = itemView.findViewById(R.id.haberbaslik);
        TextView yazar = itemView.findViewById(R.id.haberyazar);
        ImageView resim = itemView.findViewById(R.id.haberresim);

        public ViewHolder(View itemView){
            super(itemView);

        }
    }
}
