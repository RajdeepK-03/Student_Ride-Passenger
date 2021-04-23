package com.test.student_ride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class activity_profile extends AppCompatActivity {
    TextInputLayout txt_fname,txt_lname,txt_id,txt_university;

    ImageView img;
    Uri mCapturedImageURI;
    byte[] inputData;
    byte[] byte_arr;
    private static final int PERMISSION_REQUEST_CODE = 200;
    Bitmap bitmap;
    Uri imageUri;
    final static  int CAMERA_RESULT=200;
    boolean flag=false;
    DatabaseHelper databaseHelper;
    Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        requestPermission(null);
         databaseHelper=new DatabaseHelper(activity_profile.this);
        txt_fname=(TextInputLayout)findViewById(R.id.txt_firstname);
        txt_lname=(TextInputLayout)findViewById(R.id.txt_lastname);
        txt_id=(TextInputLayout)findViewById(R.id.txt_studentid);
        txt_university=(TextInputLayout)findViewById(R.id.txt_university);
        btn_save=findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag) {
                    if (databaseHelper.update_info(txt_fname.getEditText().getText().toString(), txt_lname.getEditText().getText().toString(), txt_id.getEditText().getText().toString(), txt_university.getEditText().getText().toString(), byte_arr)) {
                        Toast.makeText(activity_profile.this, "Info updated successfully !", Toast.LENGTH_LONG).show();

                    }

                }  else {

                        if (databaseHelper.addUser(txt_fname.getEditText().getText().toString(), txt_lname.getEditText().getText().toString(), txt_id.getEditText().getText().toString(), txt_university.getEditText().getText().toString(), byte_arr)) {
                            Toast.makeText(activity_profile.this, "Info saved", Toast.LENGTH_LONG).show();
                        }


                }
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img=findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capture();
            }
        });

        List<Profile> _profile = databaseHelper.get_user_info();

        if(_profile.size()>0){
            btn_save.setText("Update Info");
            flag=true;

            for (Profile profile : _profile) {
                txt_fname.getEditText().setText(profile.getFnameName());
                txt_lname.getEditText().setText(profile.getLname());
                txt_id.getEditText().setText(String.valueOf(profile.getId()));
                txt_university.getEditText().setText(profile.getuniversity());
                loadImageFromDB();


            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public  void capture(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_RESULT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case CAMERA_RESULT:
                if (requestCode == CAMERA_RESULT)
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            img.setImageBitmap(bitmap);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream); //compress to which format you want.
                            byte_arr = stream.toByteArray();
                            InputStream iStream = getContentResolver().openInputStream(mCapturedImageURI);
                            inputData = Utils.getBytes(iStream);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

        }
    }





    private void requestPermission(ArrayList<String> list)
    {
        try {
            if(list == null) {
                PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
                ActivityCompat.requestPermissions(activity_profile.this, info.requestedPermissions, PERMISSION_REQUEST_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(activity_profile.this, (String[])list.toArray(), PERMISSION_REQUEST_CODE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0)
                {
                    try
                    {
                        int index = 0;
                        ArrayList<String> temp = new ArrayList<>();
                        for(int result : grantResults)
                        {
                            if(result == PackageManager.PERMISSION_DENIED) {
                                temp.add(permissions[index]);
                            }
                            index++;
                        }

                        if(temp.size() > 0)
                            requestPermission(temp);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }




    Boolean loadImageFromDB() {
        try {
            byte_arr = databaseHelper.retreiveImageFromDB("");
            // Show Image from DB in ImageView
            img.setImageBitmap(Utils.getImage(byte_arr));
            return true;

        } catch (Exception e) {
            Log.e("TAG", "<loadImageFromDB> Error : " + e.getLocalizedMessage());
            databaseHelper.close();
            return false;
        }
    }
}