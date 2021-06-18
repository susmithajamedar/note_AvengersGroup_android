package com.example.note_avengersgroup_android;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.note_avengersgroup_android.models.NoteModel;
import com.example.note_avengersgroup_android.utils.Constants;
import com.example.note_avengersgroup_android.utils.DataBase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class NotesDisplayActivity extends AppCompatActivity implements OnMapReadyCallback {
    NoteModel noteModel;
    private ArrayList<NoteModel> noteList = new ArrayList<>();
    private TextView tvTitle, tvDateTime, tvDescription;
    private RelativeLayout rlAudio;
    private ImageView imageView,ivBack,ivPlayPause;
    private Button btDelete, btEdit;
    private GoogleMap mMap;
    private Marker curr_marker;
    private MediaPlayer mediaPlayer;

    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_display);

        init();
        setDataInViews();

        dataBase = new DataBase(this);

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDeleteDialog();
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NotesDisplayActivity.this,AddNotesActivity.class);
                intent.putExtra(Constants.DATA,noteList);
                intent.putExtra(Constants.FLAG,"edit");
                startActivity(intent);
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    ivPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                }else{
                    mediaPlayer.start();
                    ivPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                }
            }
        });
    }

    private void setDataInViews() {
        if (getIntent() != null) {
            noteList = (ArrayList<NoteModel>) getIntent().getSerializableExtra(Constants.DATA);
            noteModel = noteList.get(0);
        }
        if (noteModel.getNote() != null && !noteModel.getNote().equals("")) {
            tvTitle.setText(noteModel.getNote());
        } else {
            tvTitle.setText("");
        }
        if (noteModel.getDate() != null && !noteModel.getDate().equals("")) {
            tvDateTime.setText(noteModel.getDate());
        } else {
            tvDateTime.setText("");
        }
        if (noteModel.getAnnotation() != null && !noteModel.getAnnotation().equals("")) {
            tvDescription.setText(noteModel.getAnnotation());
        } else {
            tvDescription.setText("");
        }
        if (noteModel.getImageOne() != null && !noteModel.getImageOne().equals("")) {
            imageView.setImageURI(Uri.parse(noteModel.getImageOne()));
        } else {
            imageView.setVisibility(View.GONE);
        }

        if(noteModel.getAudio()!=null && !noteModel.getAudio().equals("")){
            try {
                mediaPlayer.setDataSource(NotesDisplayActivity.this, Uri.parse(noteModel.getAudio()));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            rlAudio.setVisibility(View.GONE);
        }
    }

    private void init() {
        tvTitle = findViewById(R.id.tv_title);
        tvDateTime = findViewById(R.id.tv_date_time);
        tvDescription = findViewById(R.id.tv_description);
        imageView = findViewById(R.id.image_view);
        btDelete = findViewById(R.id.bt_delete);
        btEdit = findViewById(R.id.bt_edit);
        ivBack=findViewById(R.id.iv_back);
        rlAudio=findViewById(R.id.rl_audio);
        ivPlayPause=findViewById(R.id.iv_play_pause);

        mediaPlayer = new MediaPlayer();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        mMap = googleMap;
        if (noteModel.getLocation() != null && !noteModel.getLocation().equals("")) {
            setLocation(noteModel.getLocation());
        }
    }

    private void setLocation(String location) {
        String[] loc = location.split(" ");
        LatLng latLng = new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1]));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("Your Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .snippet("You are here");
        curr_marker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8));
    }

    private void addDeleteDialog(){
        Dialog dialog =new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_dialog);
        TextView cancelTV = dialog.findViewById(R.id.cancelTV);
        TextView okTV = dialog.findViewById(R.id.okTV);

        okTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBase.deleteNote(noteModel.getId());
                dialog.dismiss();
                finish();
            }
        });


        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
