package com.ashok.lostfound;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ashok.lostfound.database.DatabaseHelper;
import com.ashok.lostfound.model.LostFound;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Single extends AppCompatActivity {
    TextView title, time, desc, location;
    Button btn_remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        title = findViewById(R.id.lt_title);
        time = findViewById(R.id.time);
        desc = findViewById(R.id.desc);
        location = findViewById(R.id.location);
        btn_remove = findViewById(R.id.remove);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        Intent intent = getIntent();
        LostFound receivedObject = (LostFound) intent.getSerializableExtra("myObject");

        if (receivedObject != null) {
            title.setText(receivedObject.getName());
            location.setText(receivedObject.getLocation());
            desc.setText(receivedObject.getDesc());
            try {
                if (receivedObject.getDate() != null) {
                    Date recordedDate = dateFormat.parse(receivedObject.getDate());
                    Date currentDate = Calendar.getInstance().getTime();

                    long differenceMillis = currentDate.getTime() - recordedDate.getTime();
                    long differenceDays = differenceMillis / (24 * 60 * 60 * 1000);
                    Log.d("receivedObject", ": "+differenceDays);
                    time.setText(differenceDays + " days ago");
                }else{ time.setText("Date not  provided");}
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


        }
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = new DatabaseHelper(Single.this).deleteItemFromDatabase(receivedObject.getName(), receivedObject.getDate());
                if (result) {
                    Toast.makeText(Single.this, "Item deleted successfully ", Toast.LENGTH_SHORT).show();
                    Intent back = new Intent(Single.this, MainActivity.class);
                    back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(back);
                } else {
                    Toast.makeText(Single.this, "Item not deleted please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}