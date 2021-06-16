package com.example.note_avengersgroup_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.note_avengersgroup_android.utils.Constants;

public class NotesActivity extends AppCompatActivity implements View.OnClickListener {
    String category;
    TextView backTV;
    TextView addTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        init();
    }
    public void init() {
        backTV = findViewById(R.id.backTV);
        addTV = findViewById(R.id.addTV);
        backTV.setOnClickListener(this);
        addTV.setOnClickListener(this);
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

        }
    }
}
