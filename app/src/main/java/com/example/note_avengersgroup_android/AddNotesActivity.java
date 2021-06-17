package com.example.note_avengersgroup_android;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.note_avengersgroup_android.models.NoteModel;
import com.example.note_avengersgroup_android.utils.Constants;
import com.example.note_avengersgroup_android.utils.DataBase;
import com.example.note_avengersgroup_android.utils.GPSTracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddNotesActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner spCategory;
    TextView tvSpinner;
    DataBase dataBase;
    String category;
    NoteModel model;
    ImageView  ivBack;
    GPSTracker gpsTracker;
    double userLat, userLng;
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
        gpsTracker = new GPSTracker(this);
        userLat = gpsTracker.getLatitude();
        userLng = gpsTracker.getLongitude();
        model.setLocation("" + userLat + " " + userLng);
        model.setDate(getCurrentTime());
        Toast.makeText(this, "lat " + userLat + "  lng" + userLng, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "date " + getCurrentTime(), Toast.LENGTH_LONG).show();

    }
    private String getCurrentTime() {
        String formatedDate = "";
        Date currentTime = Calendar.getInstance().getTime();
        String newFormat = "dd-MM-yyyy hh:mm aaa";
        SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
        formatedDate = timeFormat.format(currentTime);
        //tvCurrentDateTime.setText(getString(R.string.text_current_time_is) + "  " + formatedDate)
        return formatedDate;
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
