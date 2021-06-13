package com.example.note_avengersgroup_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note_avengersgroup_android.adapter.CategoryAdapter;
import com.example.note_avengersgroup_android.utils.DataBase;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView addTV;
    DataBase dataBase;
    RecyclerView categoryRV;

    List<String> dataList;
    CategoryAdapter adapter;

    private static final int STORAGE_PERMISSION_CODE = 101;

    // start point of the default view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_sort_by);
        //initialize all action item views & set  Permissions
        //init();
    }

    private void init()
    {
        dataBase=new DataBase(this);
        addTV=findViewById(R.id.addTV);
        categoryRV=findViewById(R.id.categoryRV);
        addTV.setOnClickListener(this);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        setAdapter();
    }

    private void setAdapter()
    {
        categoryRV.setLayoutManager(new LinearLayoutManager(this));
        dataList=dataBase.getAllCategories();
        adapter=new CategoryAdapter(this,dataList);
        categoryRV.setAdapter(adapter);
    }

    private void addCategoryDialog()
    {
        Dialog dialog =new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_category_dialog);
        TextView cancelTV = dialog.findViewById(R.id.cancelTV);
        TextView okTV = dialog.findViewById(R.id.okTV);
        EditText categoryET = dialog.findViewById(R.id.categoryET);

        okTV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(categoryET.getText().toString().length()>0)
                {
                    dataBase.insertCategory(categoryET.getText().toString());
                    dialog.dismiss();
                    setAdapter();
                    dataSaved();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Please Enter Category",Toast.LENGTH_LONG).show();
                }
            }
        });


        cancelTV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void dataSaved(){
        Dialog dialog =new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.data_saved_success);
        TextView okTV = dialog.findViewById(R.id.okTV);

        okTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.addTV:
                addCategoryDialog();
                break;
        }

    }
}