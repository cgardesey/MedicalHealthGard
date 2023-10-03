package com.prepeez.medicalhealthguard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.adapter.ZoneAdapter;
import com.prepeez.medicalhealthguard.pojo.Zone;


import org.json.JSONObject;

import java.util.ArrayList;

import static com.prepeez.medicalhealthguard.activity.SplashScreenActivity.zones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ZoneActivity extends AppCompatActivity {
    public static Zone myZone;
    RecyclerView recyclerView;
    TextView editTextSearch;
    ZoneAdapter zoneAdapter;
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.setText(title);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        zoneAdapter = new ZoneAdapter(zones);

        recyclerView.setAdapter(zoneAdapter);
    }
}