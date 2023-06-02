package com.example.lostfound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnCreateAdvert;
    private Button btnShowItems;
    private Button btnShowMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateAdvert = findViewById(R.id.btnCreateAdvert);
        btnShowItems = findViewById(R.id.btnShowItems);
        btnShowMap= findViewById(R.id.btnMap);
        btnCreateAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Details.class);
                // Pass any necessary data using intent.putExtra() if needed
                startActivity(intent);
            }
        });

        btnShowItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewItems.class);
                startActivity(intent);
            }
        });
        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShowMap.class);
                startActivity(intent);
            }
        });
    }
}