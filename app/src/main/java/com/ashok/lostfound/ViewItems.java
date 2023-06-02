package com.ashok.lostfound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ashok.lostfound.database.DatabaseHelper;
import com.ashok.lostfound.model.LostFound;

import java.util.List;

public class ViewItems extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private List<LostFound> itemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Retrieve the data from the database using your DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        itemList = databaseHelper.getAllData();
        adapter = new ListAdapter(itemList);
        recyclerView.setAdapter(adapter);

    }
}