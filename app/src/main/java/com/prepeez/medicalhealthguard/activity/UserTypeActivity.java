package com.prepeez.medicalhealthguard.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.pojo.UserType;
import com.prepeez.medicalhealthguard.realm.RealmHealthRecord;
import com.prepeez.medicalhealthguard.receiver.NetworkReceiver;

import java.util.ArrayList;

import io.realm.Realm;

import static com.prepeez.medicalhealthguard.activity.PatientActivity.RC_PASSWORD_CHANGE;
import static com.prepeez.medicalhealthguard.activity.PatientActivity.search;
import static com.prepeez.medicalhealthguard.activity.SigninActivity.AGENTID;

import androidx.appcompat.app.AppCompatActivity;


public class UserTypeActivity extends AppCompatActivity {

    public  static final String MYUSERTYPE = "myuserype";

    public static String userType;
    public static final String FIELDAGENT = "fieldAgent";
    public static final String MISAGENT = "misAgent";
    public static final String HEALTHPROF = "healthProf";
    Button fieldAgent, misAgent, healthProf;
    static TextView tvState;

    NetworkReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        Realm.init(this);

        //tvState = (TextView) findViewById(R.id.state);

        receiver = new NetworkReceiver();

        fieldAgent = findViewById(R.id.fieldAgent);
        misAgent = findViewById(R.id.misAgent);
        healthProf = findViewById(R.id.healthProf);

        fieldAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = false;
                Intent intent = new Intent(UserTypeActivity.this, ZoneActivity.class);
                userType = "1";
                PreferenceManager
                        .getDefaultSharedPreferences(UserTypeActivity.this)
                        .edit()
                        .putString(MYUSERTYPE, "1")
                        .apply();

                startActivity(intent);
            }
        });
        misAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = false;
                Intent intent = new Intent(UserTypeActivity.this, SigninActivity.class);
                userType = "2";
                PreferenceManager
                        .getDefaultSharedPreferences(UserTypeActivity.this)
                        .edit()
                        .putString(MYUSERTYPE, "2")
                        .apply();
                startActivity(intent);
            }
        });
        healthProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = false;
                Intent intent = new Intent(UserTypeActivity.this, HealthProfActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public static void toastMain(boolean b){

        if (b){
            //tvState.setText("CONNECTED");

        }else{
            //tvState.setText("DISCONNECTED");

        }
    }
}
