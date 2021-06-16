package com.example.note_avengersgroup_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.note_avengersgroup_android.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener {
    String category;
    TextView backTV;
    TextView sortBy;
    TextView addTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        category = getIntent().getStringExtra("selected_category");
        init();
    }
    public void init() {
        backTV = findViewById(R.id.backTV);
        addTV = findViewById(R.id.addTV);
        backTV.setOnClickListener(this);
        addTV.setOnClickListener(this);
        sortBy = findViewById(R.id.sort_by_date);
        sortBy.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backTV:
                finish();
                break;
            case R.id.addTV:
                Intent intent = new Intent(this, AddNotesActivity.class);
                intent.putExtra("selected_category", category);
                intent.putExtra(Constants.FLAG, "add");
                startActivity(intent);
                break;

            case R.id.sort_by_date:
                openBottomDialog();
                break;

        }
    }

    private void openBottomDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(NotesActivity.this, R.style.bottom_dialog_theme);
        dialog.setContentView(R.layout.select_sort_by);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        TextView tvSortByDate = dialog.findViewById(R.id.tv_sort_by_date);
        TextView tvSortByName = dialog.findViewById(R.id.tv_sort_by_name);

        tvSortByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy.setText("Sort by date");
                dialog.dismiss();
            }
        });
        tvSortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBy.setText("Sort by name");
                dialog.dismiss();
            }
        });

    }
}
