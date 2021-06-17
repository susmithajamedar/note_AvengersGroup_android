package com.example.note_avengersgroup_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_avengersgroup_android.adapter.NoteAdapter;
import com.example.note_avengersgroup_android.models.NoteModel;
import com.example.note_avengersgroup_android.utils.Constants;
import com.example.note_avengersgroup_android.utils.DataBase;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener {
    String category;
    TextView backTV;
    TextView sortBy;
    TextView addTV;
    EditText searchET;
    DataBase dataBase;
    RecyclerView notesRV;

    ArrayList<NoteModel> dataList;

    List<NoteModel> sortedList = new ArrayList<>();
    List<NoteModel> searchingList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        category = getIntent().getStringExtra("selected_category");
        init();
        searchET.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = String.valueOf(charSequence);
                if (searchingList.size() > 0) {
                    searchingList.clear();
                }
                for (int j = 0; j < sortedList.size(); j++) {
                    if (sortedList.get(j).getNote().contains(searchText)) {
                        searchingList.add(sortedList.get(j));
                    }
                }
                NoteAdapter adapter = new NoteAdapter(NotesActivity.this, searchingList);
                notesRV.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void init() {
        dataBase = new DataBase(this);
        backTV = findViewById(R.id.backTV);
        backTV.setOnClickListener(this);
        addTV = findViewById(R.id.addTV);
        addTV.setOnClickListener(this);
        searchET = findViewById(R.id.searchET);
        sortBy = findViewById(R.id.sort_by_date);
        sortBy.setOnClickListener(this);
        notesRV = findViewById(R.id.notesRV);
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
