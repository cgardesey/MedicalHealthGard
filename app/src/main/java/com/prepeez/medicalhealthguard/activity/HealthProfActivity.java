package com.prepeez.medicalhealthguard.activity;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.prepeez.medicalhealthguard.R;

import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.HEALTHPROF;
import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.MYUSERTYPE;
import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.userType;

import androidx.appcompat.app.AppCompatActivity;

public class HealthProfActivity extends AppCompatActivity {
    
    public static final String DOCTOR = "doctor";
    public static final String LABTECH = "labTech";
    Button doctor, labTech;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_prof);

        doctor = findViewById(R.id.doctor);
        labTech = findViewById(R.id.labTech);

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //staticZone = "A";
                Intent intent = new Intent(HealthProfActivity.this, SigninActivity.class);
                userType = "3";
                PreferenceManager
                        .getDefaultSharedPreferences(HealthProfActivity.this)
                        .edit()
                        .putString(MYUSERTYPE, "3")
                        .apply();
                startActivity(intent);
            }
        });
        labTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HealthProfActivity.this, SigninActivity.class);

                userType = "4";
                PreferenceManager
                        .getDefaultSharedPreferences(HealthProfActivity.this)
                        .edit()
                        .putString(MYUSERTYPE, "4")
                        .apply();
                startActivity(intent);
            }
        });
    }
}
