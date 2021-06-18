package com.example.note_avengersgroup_android.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_avengersgroup_android.NotesDisplayActivity;
import com.example.note_avengersgroup_android.R;
import com.example.note_avengersgroup_android.models.NoteModel;
import com.example.note_avengersgroup_android.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    Activity activity;
    List<NoteModel> dataList;

    public NoteAdapter(Activity activity, List<NoteModel> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.notes_item, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.noteTV.setText(dataList.get(position).getNote());
        holder.dateTV.setText(dataList.get(position).getDate());

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, NotesDisplayActivity.class);
                ArrayList<NoteModel> list=new ArrayList<>();
                list.add(dataList.get(position));
                intent.putExtra(Constants.DATA,list);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTV, dateTV;
        RelativeLayout llContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTV = itemView.findViewById(R.id.noteTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            llContainer = itemView.findViewById(R.id.ll_container);
        }
    }
}
