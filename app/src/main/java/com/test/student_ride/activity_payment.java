package com.test.student_ride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class activity_payment extends AppCompatActivity {

    TextInputLayout txt_card,txt_expiry;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        databaseHelper=new DatabaseHelper(activity_payment.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_card=(TextInputLayout)findViewById(R.id.txt_creditcard);
        txt_expiry=(TextInputLayout)findViewById(R.id.txt_expiry);

        Button btn_save=findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(databaseHelper.add_card(txt_card.getEditText().getText().toString(),txt_expiry.getEditText().getText().toString())){
                    Toast.makeText(activity_payment.this,"Card Info saved",Toast.LENGTH_LONG).show();
                }
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