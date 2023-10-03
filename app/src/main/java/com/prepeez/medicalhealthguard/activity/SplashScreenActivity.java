package com.prepeez.medicalhealthguard.activity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.preference.PreferenceManager;
        import android.text.TextUtils;
        import android.util.Log;
        import android.widget.ProgressBar;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
        import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
        import com.prepeez.medicalhealthguard.BuildConfig;
        import com.prepeez.medicalhealthguard.R;
        import com.prepeez.medicalhealthguard.pojo.Zone;


        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "SplashScreenActivity";

    public static final String PREF_ZONES_KEY = "pref_zones_key";
    public static final String ZONES = "zones";

    public static ArrayList<Zone> zones = new ArrayList<>();

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    String fall_back_json = "";

    public static final String DEFAULT_JSON = "{\n" +
            "\t\"1\":\"ZONE 1\",\n" +
            "\t\"2\":\"ZONE 2\",\n" +
            "\t\"3\":\"ZONE 3\",\n" +
            "\t\"4\":\"ZONE 4\",\n" +
            "\t\"5\":\"ZONE 5\"\n" +
            "}";




    private int progressStatus = 0;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //Realm.init(this);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        final ProgressBar pb = findViewById(R.id.pb);

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        fall_back_json = PreferenceManager.getDefaultSharedPreferences(this).getString(PREF_ZONES_KEY, DEFAULT_JSON);

        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put(ZONES, fall_back_json);
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap);
        fetchConfig();

        progressStatus = 0;

                /*
                    A Thread is a concurrent unit of execution. It has its own call stack for
                    methods being invoked, their arguments and local variables. Each application
                    has at least one thread running when it is started, the main thread,
                    in the main ThreadGroup. The runtime keeps its own threads
                    in the system thread group.
                */
        // Start the lengthy operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 100){
                    // Update the progress status
                    progressStatus +=1;

                    // Try to sleep the thread for 20 milliseconds
                    try{
                        Thread.sleep(40);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setProgress(progressStatus);
                        }
                    });
                }
                if(progressStatus ==  100)
                {
                    startActivity(new Intent(SplashScreenActivity.this, UserTypeActivity.class));
                    finish();
                    return;
                }
            }
        }).start(); // Start the operation

    }

    public void fetchConfig() {
        long cacheExpiration = 3600; // 1 hour in seconds
        // If developer mode is enabled reduce cacheExpiration to 0 so that each fetch goes to the
        // server. This should not be used in release builds.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        applyRetrievedLengthLimit();
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Make the fetched config available
                        // via FirebaseRemoteConfig get<type> calls, e.g., getLong, getString.
                        mFirebaseRemoteConfig.activateFetched();

                        // Update the EditText length limit with
                        // the newly retrieved values from Remote Config.
                        applyRetrievedLengthLimit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // An error occurred when fetching the config.
                        Log.w(TAG, "Error fetching config", e);

                        // Update the EditText length limit with
                        // the newly retrieved values from Remote Config.
                        applyRetrievedLengthLimit();
                    }
                });
    }

    private void applyRetrievedLengthLimit() {
        String rc_json = mFirebaseRemoteConfig.getString(ZONES);

        if (!TextUtils.isEmpty(rc_json) && !rc_json.equals(fall_back_json)) {
            PreferenceManager
                    .getDefaultSharedPreferences(SplashScreenActivity.this)
                    .edit()
                    .putString(PREF_ZONES_KEY, rc_json)
                    .apply();
        }

        try {
            JSONObject jsonObject = new JSONObject(rc_json);
            Iterator iterator = jsonObject.keys();
            zones.clear();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                zones.add(new Zone(key, jsonObject.getString(key)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
