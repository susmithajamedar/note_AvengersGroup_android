package com.example.note_avengersgroup_android;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.note_avengersgroup_android.utils.GPSTracker;
import com.example.note_avengersgroup_android.utils.DataBase;
import com.example.note_avengersgroup_android.models.NoteModel;
import com.example.note_avengersgroup_android.utils.Constants;

public class AddNotesActivity extends AppCompatActivity implements View.OnClickListener {

    TextView cameraTV, audioTV, selectedAudioTV, saveTV, tvSpinner;
    TextView tvToolBarTitle;
    Spinner spCategory;
    ImageView selectedIV, selectedTwoIV, ivBack;
    GPSTracker gpsTracker;
    double userLat, userLng;
    MediaPlayer mediaPlayer;
    DataBase dataBase;
    String category;
    NoteModel model;
    EditText titleET, annotationET;
    int REQUEST_CODE = 100;
    ArrayList<String> stringImages;
    String selectedAudio;
    List<String> categoryList = new ArrayList<>();
    private List<NoteModel> list = new ArrayList<>();
    private String flag = null;
    private NoteModel noteModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        tvToolBarTitle = findViewById(R.id.tv_toolbar_title);

        if (getIntent() != null) {

            flag = getIntent().getStringExtra(Constants.FLAG);
            if (flag.equals("edit")) {
                list = (ArrayList<NoteModel>) getIntent().getSerializableExtra(Constants.DATA);
                noteModel = list.get(0);
            } else {
                category = getIntent().getStringExtra("selected_category");
            }

        }

        init();

        if (flag.equals("edit")) {

            categoryList = dataBase.getAllCategories();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddNotesActivity.this,
                    android.R.layout.simple_spinner_item, categoryList);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCategory.setAdapter(adapter);
            int pos = adapter.getPosition(noteModel.getCategory());
            spCategory.setSelection(pos);
            spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    model.setCategory(String.valueOf(adapterView.getItemAtPosition(i)));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            titleET.setText(noteModel.getNote());
            annotationET.setText(noteModel.getAnnotation());
            tvToolBarTitle.setText("Edit Note");
            if (noteModel.getImageOne() != null && !noteModel.getImageOne().equals("")) {
                //Bitmap myBitmap = BitmapFactory.decodeFile(noteModel.getImageOne());
                selectedIV.setImageURI(Uri.parse(noteModel.getImageOne()));
                model.setImageOne(noteModel.getImageOne());
                model.setImageTwo(noteModel.getImageTwo());
            }

            if (noteModel.getAudio() != null && !noteModel.getAudio().equals("")) {
                selectedAudioTV.setVisibility(View.VISIBLE);
                selectedAudio = noteModel.getAudio();
                File file = new File(selectedAudio);
                selectedAudioTV.setText(file.getName());
            }

            if (noteModel.getAudio() != null && !noteModel.getAudio().equals("")) {
                model.setAudio(noteModel.getAudio());
            }
        } else {
            spCategory.setVisibility(View.GONE);
            tvSpinner.setVisibility(View.GONE);
        }
    }

    private void init() {
        model = new NoteModel();
        model.setCategory(category);
        dataBase = new DataBase(this);
        mediaPlayer = new MediaPlayer();
        cameraTV = findViewById(R.id.cameraTV);
        selectedIV = findViewById(R.id.selectedIV);
        selectedTwoIV = findViewById(R.id.selectedTwoIV);
        audioTV = findViewById(R.id.audioTV);
        selectedAudioTV = findViewById(R.id.selectedAudioTV);
        ivBack = findViewById(R.id.iv_back);
        tvSpinner = findViewById(R.id.tv_spinner);
        spCategory = findViewById(R.id.sp_category);
        saveTV = findViewById(R.id.saveTV);
        titleET = findViewById(R.id.titleET);
        annotationET = findViewById(R.id.annotationET);
        gpsTracker = new GPSTracker(this);
        userLat = gpsTracker.getLatitude();
        userLng = gpsTracker.getLongitude();
        model.setLocation("" + userLat + " " + userLng);
        model.setDate(getCurrentTime());
        Toast.makeText(this, "lat " + userLat + "  lng" + userLng, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "date " + getCurrentTime(), Toast.LENGTH_LONG).show();
        cameraTV.setOnClickListener(this);
        audioTV.setOnClickListener(this);
        saveTV.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.cameraTV:
                openBottomDialog();
                break;

            case R.id.audioTV:
                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                intent_upload.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent_upload, 1);
                break;
            case R.id.saveTV:
                if (flag.equals("edit")) {
                    if (titleET.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Enter note title", Toast.LENGTH_LONG).show();
                    } else if (annotationET.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Enter note annotation", Toast.LENGTH_LONG).show();
                    } else {
                        model.setNote(titleET.getText().toString());
                        model.setAnnotation(annotationET.getText().toString());
                        model.setLocation("" + userLat + " " + userLng);
                        model.setDate(getCurrentTime());
                        dataBase.updateData(model, noteModel.getId());
                        finish();
                    }
                } else {
                    if (titleET.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Enter note title", Toast.LENGTH_LONG).show();
                    } else if (annotationET.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Enter note annotation", Toast.LENGTH_LONG).show();
                    } else {
                        model.setNote(titleET.getText().toString());
                        model.setAnnotation(annotationET.getText().toString());
                        dataBase.insertNote(model);
                        finish();
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            stringImages = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            setImages();
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                //the selected audio.
                Uri uri = data.getData();
                selectedAudioTV.setVisibility(View.VISIBLE);
                selectedAudio = uri.toString();
                try {
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    model.setAudio(writeStreamToFile(is));
                } catch (Exception e) {

                }

                File file = new File(selectedAudio);
                selectedAudioTV.setText(file.getName());

                Uri playUri = Uri.parse(selectedAudio);


                try {
                    mediaPlayer.setDataSource(this, playUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        }
        if (requestCode == Constants.PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                selectedIV.setImageURI(data.getData());
                //File f = new File(getRealPathFromURI(AddNotesActivity.this,data.getData()));
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    model.setImageOne(saveToInternalStorage(bitmap));
                } catch (Exception e) {

                }


            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void setImages() {
        for (int i = 0; i < stringImages.size(); i++) {
            File imgFile = new File(stringImages.get(i));
            if (imgFile.exists()) {
                if (i == 0) {
                    model.setImageOne(stringImages.get(i));
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    selectedIV.setImageBitmap(myBitmap);
                } else if (i == 1) {
                    model.setImageTwo(stringImages.get(i));
                    selectedTwoIV.setVisibility(View.VISIBLE);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    selectedTwoIV.setImageBitmap(myBitmap);
                }
            }
        }
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

    private void openBottomDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(AddNotesActivity.this, R.style.bottom_dialog_theme);
        dialog.setContentView(R.layout.dialog_add_attachment);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        TextView tvCamera = dialog.findViewById(R.id.tv_camera);
        TextView tvFromPhotos = dialog.findViewById(R.id.tv_photos);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);

        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Options options = Options.init()
                        .setRequestCode(REQUEST_CODE)                                           //Request code for activity results
                        .setCount(2)                                                   //Number of images to restict selection count
                        .setFrontfacing(false)           //Front Facing camera on start
                        .setSpanCount(4)//Span count for gallery min 1 & max 5
                        .setExcludeVideos(true)//Option to exclude videos
                        .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT);     //Orientaion;                                       //Custom Path For media Storage

                Pix.start(AddNotesActivity.this, options);
                dialog.dismiss();
            }
        });
        tvFromPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Constants.PICK_IMAGE);
                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE);*/
                dialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {

        Cursor cursor = null;

        try {

            String[] proj = {
                    MediaStore.Images.Media.DATA};

            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);

            int column_index = cursor.getColumnIndexOrThrow(
                    MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            return cursor.getString(column_index);

        } catch (Exception e) {

            Log.e("Path", "getRealPathFromURI Exception : " + e.toString());

            return "";

        } finally {

            if (cursor != null) {

                cursor.close();

            }

        }

    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        //File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                + "/note_photo");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "cropped_" + timeStamp + ".jpeg";
        File mypath = new File(directory, imageFileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 70, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    private String writeStreamToFile(InputStream input) {

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                + "/note_audio");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = timeStamp + ".mp3";
        File file = new File(directory, imageFileName);
        try {
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;
                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }

}