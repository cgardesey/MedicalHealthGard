package com.prepeez.medicalhealthguard.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.adapter.UserTypeAdapter;

import java.util.ArrayList;

public class SubscriptionActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    private ArrayList<String> userTypes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        UserTypeAdapter SuscriptionAdapter = new UserTypeAdapter(getUserTypes());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.setAdapter(SuscriptionAdapter);
        //recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private ArrayList<String> getUserTypes() {
        userTypes.clear();
        userTypes.add("I AM A FIELD AGENT");
        userTypes.add("I AM AN MIS AGENT");
        userTypes.add("I AM A HEALTH PROFESSIONAL");

        return userTypes;
    }
}
