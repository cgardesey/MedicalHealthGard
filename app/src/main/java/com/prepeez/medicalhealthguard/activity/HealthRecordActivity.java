package com.prepeez.medicalhealthguard.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.adapter.HealthRecordAdapter;
import com.prepeez.medicalhealthguard.adapter.PatientAdapter;
import com.prepeez.medicalhealthguard.realm.RealmHealthRecord;
import com.prepeez.medicalhealthguard.realm.RealmPatient;

import java.lang.reflect.Field;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.prepeez.medicalhealthguard.activity.PatientActivity.RC_PASSWORD_CHANGE;
import static com.prepeez.medicalhealthguard.activity.SigninActivity.AGENTID;
import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.MYUSERTYPE;
import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.userType;
import static com.prepeez.medicalhealthguard.adapter.HealthRecordAdapter.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HealthRecordActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    HealthRecordAdapter healthRecordAdapter;
    ArrayList<RealmHealthRecord> healthRecords = new ArrayList<>();
    static Menu search_menu;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_record);

        mContext = getApplicationContext();

        Realm.init(this);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        populateHealthRecords();
        healthRecordAdapter = new HealthRecordAdapter(healthRecords);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(healthRecordAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickedHealthRecord = new RealmHealthRecord();
                action_helth_rec = "add";

                mContext.startActivity(new Intent(HealthRecordActivity.this, AddHealthRecordActivity.class));
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(final Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//
//        MenuItem mSearch = menu.findItem(R.id.action_search);
//
//        SearchView mSearchView = (SearchView) mSearch.getActionView();
//        mSearchView.setQueryHint("Search...");
//
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                ArrayList<RealmHealthRecord> filteredModelList = new ArrayList<>();
//                filteredModelList = filter(unsentSmsList, query);
//                healthRecordAdapter.setFilter(filteredModelList);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                ArrayList<RealmHealthRecord> filteredModelList = new ArrayList<>();
//                filteredModelList = filter(unsentSmsList, newText);
//                healthRecordAdapter.setFilter(filteredModelList);
//                //com.map.intgh.finde.fragment.TabFragment1.filter(filteredModelList);
//                //Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_LONG).show();
//                return true;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//        //return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.sign_out_menu) {
            Toast.makeText(this, "Signed out!", Toast.LENGTH_SHORT).show();

            PreferenceManager
                    .getDefaultSharedPreferences(HealthRecordActivity.this)
                    .edit()
                    .putBoolean(userType, false)
                    .putString(AGENTID, "")
                    .apply();
            Intent i = new Intent(HealthRecordActivity.this, UserTypeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            return true;
        } else if (itemId == R.id.change_password_menu) {
            startActivityForResult(new Intent(
                    HealthRecordActivity.this,
                    UpdatePasswordActivity.class), RC_PASSWORD_CHANGE);
            return true;
        } else if (itemId == R.id.action_search) {
            MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    if (healthRecords == null) {
                        Toast.makeText(HealthRecordActivity.this, "Providers is null", Toast.LENGTH_SHORT).show();
                    }
                    if (healthRecordAdapter == null) {
                        Toast.makeText(HealthRecordActivity.this, "healthRecordAdapter is null", Toast.LENGTH_SHORT).show();
                    }
                    if ((healthRecords != null) && (healthRecordAdapter != null)) {
                        initSearchView1(healthRecords, healthRecordAdapter);
                        filter(healthRecords, "");
                    }
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {

                    if (healthRecords == null) {
                        Toast.makeText(HealthRecordActivity.this, "Providers is null", Toast.LENGTH_SHORT).show();
                    }
                    if (healthRecordAdapter == null) {
                        Toast.makeText(HealthRecordActivity.this, "healthRecordAdapter is null", Toast.LENGTH_SHORT).show();
                    }
                    if ((healthRecords != null) && (healthRecordAdapter != null)) {
                        Toast.makeText(HealthRecordActivity.this, "asd", Toast.LENGTH_SHORT).show();
                        initSearchView1(healthRecords, healthRecordAdapter);
                        filter(healthRecords, "");
                    }
                    initSearchView1(healthRecords, healthRecordAdapter);
                    filter(healthRecords, "");
                    return true;
                }
            });
            //startActivity(new Intent(HealthRecordActivity.this, AccountActivity.class));
            return true;
        } else if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateHealthRecords();
        healthRecordAdapter = new HealthRecordAdapter(healthRecords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(healthRecordAdapter);
    }

    private ArrayList<RealmHealthRecord> filter(ArrayList<RealmHealthRecord> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<RealmHealthRecord> filteredModelList = new ArrayList<>();

        for (RealmHealthRecord model : models) {

            String name = model.getCreated_at();

            name = name.toLowerCase();

            if (name.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public static void initSearchView1(final ArrayList<RealmHealthRecord> searchevents, final HealthRecordAdapter seAdapter) {
        final SearchView searchView =
                (SearchView) search_menu.findItem(R.id.action_search).getActionView();

        searchView.setSubmitButtonEnabled(true);

        // set hint and the text colors

        /*EditText txtSearch = searchView.findViewById(R.id.search_src_text);
        txtSearch.setHint(mContext.getString(R.string.search));
        txtSearch.setHintTextColor(Color.DKGRAY);
        // txtSearch.setText("Search....");
        //   txtSearch.setTextColor(getResources().getColor(R.color.colorPrimary));


        // set the cursor

        AutoCompleteTextView searchTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            // mCursorDrawableRes.set(searchTextView, R.drawable.bg_gradient); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void populateHealthRecords() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmHealthRecord> results = realm.where(RealmHealthRecord.class).equalTo("patientid", PatientAdapter.clickedPatient.getPatientid()).findAll();

        healthRecords.clear();
        for(RealmHealthRecord healthRecord:results){
            healthRecords.add(healthRecord);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case RC_PASSWORD_CHANGE:
                switch (resultCode) {
                    case RESULT_OK:
                        // Password change succeeded

                        Intent i = new Intent(HealthRecordActivity.this, UserTypeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                        break;

                    default:
                        // Password change failed
                        break;
                }
                break;

            default:
                break;
        }
    }
}
