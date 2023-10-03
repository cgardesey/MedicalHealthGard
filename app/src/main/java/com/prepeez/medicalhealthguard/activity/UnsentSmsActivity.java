package com.prepeez.medicalhealthguard.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.adapter.UnsentSmsAdapter;
import com.prepeez.medicalhealthguard.adapter.PatientAdapter;
import com.prepeez.medicalhealthguard.pojo.UnsentSms;
import com.prepeez.medicalhealthguard.realm.RealmAgent;
import com.prepeez.medicalhealthguard.realm.RealmHealthRecord;
import com.prepeez.medicalhealthguard.realm.RealmPatient;


import java.lang.reflect.Field;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.prepeez.medicalhealthguard.activity.PatientActivity.RC_PASSWORD_CHANGE;
import static com.prepeez.medicalhealthguard.activity.SigninActivity.AGENTID;
import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.userType;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UnsentSmsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UnsentSmsAdapter unsentSmsAdapter;
    ArrayList<UnsentSms> unsentSmsList = new ArrayList<>();
    static Menu search_menu;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsent_sms);

        mContext = getApplicationContext();

        Realm.init(this);

        recyclerView = findViewById(R.id.recyclerView);
        populateUnsentSmsList();
        unsentSmsAdapter = new UnsentSmsAdapter(unsentSmsList, unsentSmsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(unsentSmsAdapter);
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
//                ArrayList<UnsentSms> filteredModelList = new ArrayList<>();
//                filteredModelList = filter(unsentSmsList, query);
//                unsentSmsAdapter.setFilter(filteredModelList);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                ArrayList<UnsentSms> filteredModelList = new ArrayList<>();
//                filteredModelList = filter(unsentSmsList, newText);
//                unsentSmsAdapter.setFilter(filteredModelList);
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
                    .getDefaultSharedPreferences(UnsentSmsActivity.this)
                    .edit()
                    .putBoolean(userType, false)
                    .putString(AGENTID, "")
                    .apply();
            Intent i = new Intent(UnsentSmsActivity.this, UserTypeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            return true;
        } else if (itemId == R.id.change_password_menu) {
            startActivityForResult(new Intent(
                    UnsentSmsActivity.this,
                    UpdatePasswordActivity.class), RC_PASSWORD_CHANGE);
            return true;
        } else if (itemId == R.id.action_search) {
            MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    if (unsentSmsList == null) {
                        Toast.makeText(UnsentSmsActivity.this, "Providers is null", Toast.LENGTH_SHORT).show();
                    }
                    if (unsentSmsAdapter == null) {
                        Toast.makeText(UnsentSmsActivity.this, "unsentSmsAdapter is null", Toast.LENGTH_SHORT).show();
                    }
                    if ((unsentSmsList != null) && (unsentSmsAdapter != null)) {
                        initSearchView1(unsentSmsList, unsentSmsAdapter);
                        filter(unsentSmsList, "");
                    }
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {

                    if (unsentSmsList == null) {
                        Toast.makeText(UnsentSmsActivity.this, "Providers is null", Toast.LENGTH_SHORT).show();
                    }
                    if (unsentSmsAdapter == null) {
                        Toast.makeText(UnsentSmsActivity.this, "unsentSmsAdapter is null", Toast.LENGTH_SHORT).show();
                    }
                    if ((unsentSmsList != null) && (unsentSmsAdapter != null)) {
                        Toast.makeText(UnsentSmsActivity.this, "asd", Toast.LENGTH_SHORT).show();
                        initSearchView1(unsentSmsList, unsentSmsAdapter);
                        filter(unsentSmsList, "");
                    }
                    initSearchView1(unsentSmsList, unsentSmsAdapter);
                    filter(unsentSmsList, "");
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
        populateUnsentSmsList();
        unsentSmsAdapter = new UnsentSmsAdapter(unsentSmsList, unsentSmsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(unsentSmsAdapter);
    }

    private ArrayList<UnsentSms> filter(ArrayList<UnsentSms> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<UnsentSms> filteredModelList = new ArrayList<>();

        for (UnsentSms model : models) {

            String name = model.getId();

            name = name.toLowerCase();

            if (name.contains(search_txt)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public static void initSearchView1(final ArrayList<UnsentSms> searchevents, final UnsentSmsAdapter seAdapter) {
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

    public void populateUnsentSmsList() {
        unsentSmsList.clear();
        Realm realm = Realm.getDefaultInstance();

        RealmResults<RealmHealthRecord> healthRecords = realm.where(RealmHealthRecord.class).notEqualTo("smsstatus", "Sent").findAll();
        for(RealmHealthRecord healthRecord:healthRecords){
            String patientid = healthRecord.getPatientid();
            RealmPatient patient = realm.where(RealmPatient.class).equalTo("patientid", patientid).findFirst();
            String patientContact = patient.getContact();
            unsentSmsList.add(new UnsentSms(healthRecord.getHealthrecordid(),"HEALTHRECORD", patientContact, healthRecord.getSms() ));
        }

        RealmResults<RealmPatient> patients = realm.where(RealmPatient.class).notEqualTo("smsstatus", "Sent").findAll();
        for(RealmPatient patient:patients){
            unsentSmsList.add(new UnsentSms(patient.getPatientid(), "PATIENT", patient.getContact(), patient.getSms()));
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

                        Intent i = new Intent(UnsentSmsActivity.this, UserTypeActivity.class);
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
