package com.prepeez.medicalhealthguard.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.adapter.AgentAdapter;
import com.prepeez.medicalhealthguard.adapter.AgentAdapter;
import com.prepeez.medicalhealthguard.constants.Const;
import com.prepeez.medicalhealthguard.realm.RealmAgent;
import com.prepeez.medicalhealthguard.realm.RealmAgent;
import com.prepeez.medicalhealthguard.realm.RealmHealthRecord;

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

public class AgentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    AgentAdapter agentAdapter;
    ArrayList<RealmAgent> agents = new ArrayList<>();
    static Menu search_menu;
    static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        mContext = getApplicationContext();

        Realm.init(this);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        populateAgents();
        agentAdapter = new AgentAdapter(this,agents, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(agentAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search...");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<RealmAgent> filteredModelList = new ArrayList<>();
                filteredModelList = filter(agents, query);
                agentAdapter.setFilter(filteredModelList);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<RealmAgent> filteredModelList = new ArrayList<>();
                filteredModelList = filter(agents, newText);
                agentAdapter.setFilter(filteredModelList);
                //com.map.intgh.finde.fragment.TabFragment1.filter(filteredModelList);
                //Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_LONG).show();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    private ArrayList<RealmAgent> filter(ArrayList<RealmAgent> models, String search_txt) {

        search_txt = search_txt.toLowerCase();
        final ArrayList<RealmAgent> filteredModelList = new ArrayList<>();

        for (RealmAgent model : models) {
            String agentId = model.getAgentid();
            String name = model.getTitle() + " " + model.getLastname() + " " + model.getFirstname();
            String contact = model.getContact();
            String address = model.getAddress();

            agentId = agentId.toLowerCase();
            name = name.toLowerCase();
            contact = contact.toLowerCase();


            if (
                    agentId.contains(search_txt) ||
                    name.contains(search_txt) ||
                    address.contains(search_txt) ||
                    contact.contains(search_txt)
                    ) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.sign_out_menu) {
            Toast.makeText(this, "Signed out!", Toast.LENGTH_SHORT).show();

            PreferenceManager
                    .getDefaultSharedPreferences(AgentActivity.this)
                    .edit()
                    .putBoolean(userType, false)
                    .putString(AGENTID, "")
                    .apply();
            Intent i = new Intent(AgentActivity.this, UserTypeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            return true;
        } else if (itemId == R.id.change_password_menu) {
            startActivityForResult(new Intent(
                    AgentActivity.this,
                    UpdatePasswordActivity.class), RC_PASSWORD_CHANGE);
            return true;
        } else if (itemId == R.id.action_search) {
            MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    if (agents == null) {
                        Toast.makeText(AgentActivity.this, "Providers is null", Toast.LENGTH_SHORT).show();
                    }
                    if (agentAdapter == null) {
                        Toast.makeText(AgentActivity.this, "agentAdapter is null", Toast.LENGTH_SHORT).show();
                    }
                    if ((agents != null) && (agentAdapter != null)) {
                        initSearchView1(agents, agentAdapter);
                        filter(agents, "");
                    }
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {

                    if (agents == null) {
                        Toast.makeText(AgentActivity.this, "Providers is null", Toast.LENGTH_SHORT).show();
                    }
                    if (agentAdapter == null) {
                        Toast.makeText(AgentActivity.this, "agentAdapter is null", Toast.LENGTH_SHORT).show();
                    }
                    if ((agents != null) && (agentAdapter != null)) {
                        Toast.makeText(AgentActivity.this, "asd", Toast.LENGTH_SHORT).show();
                        initSearchView1(agents, agentAdapter);
                        filter(agents, "");
                    }
                    initSearchView1(agents, agentAdapter);
                    filter(agents, "");
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        populateAgents();
//        agentAdapter = new AgentAdapter(agents, true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        recyclerView.setAdapter(agentAdapter);
//    }

    public static void initSearchView1(final ArrayList<RealmAgent> searchevents, final AgentAdapter seAdapter) {
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

    public void populateAgents() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmAgent> results = realm.where(RealmAgent.class).findAll();

        agents.clear();
        for(RealmAgent agent:results){
            agents.add(agent);
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

                        Intent i = new Intent(AgentActivity.this, UserTypeActivity.class);
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

    public static void saveToRealm(final RealmAgent realmAgent) {
        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm mRealm) {
                                              // mRealm.createObject(RealmAgent.class, clickedAgent);
                                              Number num = mRealm.where(RealmAgent.class).max("id");
                                              int id = 0;
                                              if (num == null) {
                                                  id = 1;
                                              } else {
                                                  id = num.intValue() + 1;
                                              }
                                              realmAgent.setId(id);
                                              mRealm.copyToRealmOrUpdate(realmAgent);
                                          }
                                      },
                new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        realm.close();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d("asd", error.toString());
                    }
                }
        );
    }
}
