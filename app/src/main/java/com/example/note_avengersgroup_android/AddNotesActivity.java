package com.example.note_avengersgroup_android;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.note_avengersgroup_android.models.NoteModel;
import com.example.note_avengersgroup_android.utils.Constants;
import com.example.note_avengersgroup_android.utils.DataBase;

import java.util.ArrayList;

public class AddNotesActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner spCategory;
    TextView tvSpinner;
    DataBase dataBase;
    String category;
    NoteModel model;
    ImageView  ivBack;
    private String flag = null;
    private NoteModel noteModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        if (getIntent() != null) {

            flag = getIntent().getStringExtra(Constants.FLAG);
            category = getIntent().getStringExtra("selected_category");
        }
        init();
        spCategory.setVisibility(View.GONE);
        tvSpinner.setVisibility(View.GONE);
    }
    private void init() {
        model = new NoteModel();
        model.setCategory(category);
        dataBase = new DataBase(this);
        spCategory = findViewById(R.id.sp_category);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        tvSpinner = findViewById(R.id.tv_spinner);

    }
        @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }

    }
}
