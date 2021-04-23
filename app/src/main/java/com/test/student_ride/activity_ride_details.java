package com.test.student_ride;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class activity_ride_details extends AppCompatActivity {

    TextInputLayout txt_destination, txt_price, txt_distnace;

    String destination,current_lat,current_lon,destination_lat,destination_lon;
    private String[] cars_name;
     // 28.311218793140295, 70.12683681275315

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cars_name = getResources().getStringArray(R.array.cars);
        txt_destination = (TextInputLayout) findViewById(R.id.txt_destination);
        txt_distnace = (TextInputLayout) findViewById(R.id.txt_distance);
        txt_price = (TextInputLayout) findViewById(R.id.txt_price);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            destination   = extras.getString("destination");
            current_lat   = extras.getString("current_lat");
            current_lon   = extras.getString("current_lon");
            destination_lat   = extras.getString("destination_lat");
            destination_lon   = extras.getString("destination_lon");

        double kms=Helper.distance(Double.parseDouble(current_lat),Double.parseDouble(current_lon),Double.parseDouble(destination_lat),Double.parseDouble(destination_lon));
        txt_destination.getEditText().setText(destination);
        txt_distnace.getEditText().setText(String.valueOf(kms)+" Kms");
        txt_price.getEditText().setText(String.valueOf(kms*1)+"$");


        }


        Button btn_map=findViewById(R.id.btn_map);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_ride_details.this, map_activity.class);
                intent.putExtra("current_lat", current_lat);
                intent.putExtra("current_lon", current_lon);
                intent.putExtra("destination_lat", destination_lat);
                intent.putExtra("destination_lon", destination_lon);
                startActivity(intent);
            }
        });
        Button btn_confirm=findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomIndex = new Random().nextInt(cars_name.length);
                String randomName = cars_name[randomIndex];

                new AlertDialog.Builder(activity_ride_details.this)
                        .setTitle("Student Ride")
                        .setMessage("You have successfully booked "+randomName+".Click Ok to see your route Thank you so much !")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                Intent intent = new Intent(activity_ride_details.this, map_activity.class);
                                intent.putExtra("current_lat", current_lat);
                                intent.putExtra("current_lon", current_lon);
                                intent.putExtra("destination_lat", destination_lat);
                                intent.putExtra("destination_lon", destination_lon);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
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
}