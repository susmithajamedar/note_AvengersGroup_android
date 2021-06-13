package com.example.note_avengersgroup_android.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.note_avengersgroup_android.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {

    Activity activity;
    List<String> dataList;

    public CategoryAdapter(Activity activity, List<String> dataList){
        this.activity=activity;
        this.dataList=dataList;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(activity);
        View view=inflater.inflate(R.layout.category_item,null);

        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull  Viewholder holder, int position) {
        holder.textTV.setText(dataList.get(position));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        TextView textTV;
        View view;
        public Viewholder(View view){
            super(view);
            textTV=view.findViewById(R.id.textTV);
            this.view=view;
        }
    }
}
