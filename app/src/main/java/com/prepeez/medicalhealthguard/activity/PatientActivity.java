package com.prepeez.medicalhealthguard.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.fragment.patient.CompaniesPatientFragment;
import com.prepeez.medicalhealthguard.fragment.patient.FamiliesPatientFragment;
import com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment;
import com.prepeez.medicalhealthguard.fragmentPagerAdapter.PatientPagerAdapter;
import com.prepeez.medicalhealthguard.realm.RealmPatient;

import java.lang.reflect.Field;

import static com.prepeez.medicalhealthguard.activity.SigninActivity.AGENTID;
import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.MYUSERTYPE;
import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.userType;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.action;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.fragment.patient.CompaniesPatientFragment.filter_companies;
import static com.prepeez.medicalhealthguard.fragment.patient.FamiliesPatientFragment.filter_families;
import static com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment.filter_individuals;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;

public class PatientActivity extends AppCompatActivity {
    public static boolean search;
    public static final int RC_PASSWORD_CHANGE = 22;
    public static String PATIENT_FRAGMENT = "Individual";

    FloatingActionButton fab;
    private TabHost mTabHost;
    TextView individual, family, company;

    Toolbar toolbar, searchtollbar;
    private static Menu search_menu;

    static Context mContext;

    IndividualsPatientFragment individualsPatientFragment;
    FamiliesPatientFragment familiesPatientFragment;
    CompaniesPatientFragment companiesPatientFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        mContext = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.searchtoolbar);
        individual = (TextView) findViewById(R.id.individuals);
        family = (TextView) findViewById(R.id.family);
        company = (TextView) findViewById(R.id.company);
        fab = findViewById(R.id.fab);
        setSearchToolbar();
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PatientPagerAdapter adapter = new PatientPagerAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickedPatient = new RealmPatient();
                clickedPatient.setGrouptype(PATIENT_FRAGMENT);
                action = "add";

                String myUserType = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MYUSERTYPE, "");

                if (myUserType.equals("1")) {
                    mContext.startActivity(new Intent(PatientActivity.this, AddPatientByHealthProfActivity.class));
                }
                else if (myUserType.equals("2")) {
                    mContext.startActivity(new Intent(PatientActivity.this, AddPatientByMisActivity.class));
                }
                else{
                    mContext.startActivity(new Intent(PatientActivity.this, AddPatientByHealthProfActivity.class));
                }
            }
        });

        individualsPatientFragment = new IndividualsPatientFragment();
        familiesPatientFragment = new FamiliesPatientFragment();
        companiesPatientFragment = new CompaniesPatientFragment();

        individual.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PATIENT_FRAGMENT = "Individual";
                viewPager.setCurrentItem(0);
                individual.setBackgroundResource(R.color.colorAccent);
                individual.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                family.setTextColor(Color.rgb(63, 208, 215));
                family.setBackgroundResource(R.color.transparent);
                company.setTextColor(Color.rgb(63, 208, 215));
                company.setBackgroundResource(R.color.transparent);
            }
        });
        family.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PATIENT_FRAGMENT = "Family";
                viewPager.setCurrentItem(1);
                family.setBackgroundResource(R.color.colorAccent);
                family.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                individual.setTextColor(Color.rgb(63, 208, 215));
                individual.setBackgroundResource(R.color.transparent);
                company.setTextColor(Color.rgb(63, 208, 215));
                company.setBackgroundResource(R.color.transparent);
            }
        });
        company.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PATIENT_FRAGMENT = "Company";
                viewPager.setCurrentItem(2);
                company.setBackgroundResource(R.color.colorAccent);
                company.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                individual.setTextColor(Color.rgb(63, 208, 215));
                individual.setBackgroundResource(R.color.transparent);
                family.setTextColor(Color.rgb(63, 208, 215));
                family.setBackgroundResource(R.color.transparent);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    PATIENT_FRAGMENT = "Individual";
                    individual.setBackgroundResource(R.color.colorAccent);
                    individual.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    family.setTextColor(Color.rgb(63, 208, 215));
                    family.setBackgroundResource(R.color.transparent);
                    company.setTextColor(Color.rgb(63, 208, 215));
                    company.setBackgroundResource(R.color.transparent);
                } else if (position == 1) {
                    PATIENT_FRAGMENT = "Family";
                    family.setBackgroundResource(R.color.colorAccent);
                    family.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    individual.setTextColor(Color.rgb(63, 208, 215));
                    individual.setBackgroundResource(R.color.transparent);
                    company.setTextColor(Color.rgb(63, 208, 215));
                    company.setBackgroundResource(R.color.transparent);
                } else if (position == 2) {
                    PATIENT_FRAGMENT = "Company";
                    company.setBackgroundResource(R.color.colorAccent);
                    company.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    individual.setTextColor(Color.rgb(63, 208, 215));
                    individual.setBackgroundResource(R.color.transparent);
                    family.setTextColor(Color.rgb(63, 208, 215));
                    family.setBackgroundResource(R.color.transparent);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setSupportActionBar(toolbar);

        initSearchView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.sign_out_menu) {
            Toast.makeText(this, "Signed out!", Toast.LENGTH_SHORT).show();

            PreferenceManager
                    .getDefaultSharedPreferences(PatientActivity.this)
                    .edit()
                    .putBoolean(userType, false)
                    .putString(AGENTID, "")
                    .apply();
            Intent i = new Intent(PatientActivity.this, UserTypeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            return true;
        } else if (itemId == R.id.change_password_menu) {
            startActivityForResult(new Intent(
                    PatientActivity.this,
                    UpdatePasswordActivity.class), RC_PASSWORD_CHANGE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PASSWORD_CHANGE) {
            switch (resultCode) {
                case RESULT_OK:
                    // Password change succeeded

                    Intent i = new Intent(PatientActivity.this, UserTypeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    break;

                default:
                    // Password change failed
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow) {
        final View myView = findViewById(viewID);

        int width = myView.getWidth();

        if (posFromRight > 0)
            width -= (posFromRight * getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        if (containsOverflow)
            width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = myView.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);

        anim.setDuration((long) 220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if (isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();


    }

    public void setSearchToolbar() {

        searchtollbar = (Toolbar) findViewById(R.id.searchtoolbar);

        if (searchtollbar != null) {
            searchtollbar.inflateMenu(R.menu.menu_search);
            search_menu = searchtollbar.getMenu();

            searchtollbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        getSupportActionBar().show();
                    } else {
                        getSupportActionBar().show();
                    }

                }
            });

            MenuItem item_search = search_menu.findItem(R.id.action_search);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(R.id.searchtoolbar, 1, true, false);
                    } else
                        searchtollbar.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            });

        } else {
            Log.d("toolbar", "setSearchtollbar: NULL");
            // initSearchView(events,adapter);
        }

    }

    public void initSearchView() {
        MenuItem item_signout = search_menu.findItem(R.id.sign_out_menu);
        item_signout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(mContext, "Signed out!", Toast.LENGTH_SHORT).show();

                PreferenceManager
                        .getDefaultSharedPreferences(PatientActivity.this)
                        .edit()
                        .putBoolean(userType, false)
                        .putString(AGENTID, "")
                        .apply();
                Intent i = new Intent(PatientActivity.this, UserTypeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                return true;
            }
        });

        MenuItem item_changepassword = search_menu.findItem(R.id.change_password_menu);
        item_changepassword.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivityForResult(new Intent(
                        PatientActivity.this,
                        UpdatePasswordActivity.class), RC_PASSWORD_CHANGE);
                return true;
            }
        });

        MenuItem item_unsentsms = search_menu.findItem(R.id.unsent_sms);
        item_unsentsms.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(PatientActivity.this, UnsentSmsActivity.class));
                return true;
            }
        });

        final SearchView searchView =
                (SearchView) search_menu.findItem(R.id.action_search).getActionView();


        searchView.setSubmitButtonEnabled(true);

        // set hint and the text colors

        // EditText txtSearch = ((EditText) searchView.findViewById(R.id.search_src_text));
        // txtSearch.setHint("Search...");
        // txtSearch.setHintTextColor(Color.DKGRAY);
        // txtSearch.setText("Search....");
        //txtSearch.setTextColor(context.getResources().getColor(R.color.colorPrimary));


        // set the cursor

        /*AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            // mCursorDrawableRes.set(searchTextView, R.drawable.bg_gradient); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                filter_families(query);
                filter_companies(query);
                filter_individuals(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                filter_families(newText);
                filter_companies(newText);
                filter_individuals(newText);
                return true;
            }
        });*/
    }
}
