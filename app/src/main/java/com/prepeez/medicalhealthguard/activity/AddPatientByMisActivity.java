package com.prepeez.medicalhealthguard.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.fragmentPagerAdapter.MisAgentPatientPageAdapter;
import com.prepeez.medicalhealthguard.constants.Const;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment1;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment2;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment3;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment4;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment5;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment6;
import com.prepeez.medicalhealthguard.fragment.misagent.TabFragment7;
import com.prepeez.medicalhealthguard.http.patient.PatientSingleton;
import com.prepeez.medicalhealthguard.http.patient.UpdatePatientSingleton;
import com.prepeez.medicalhealthguard.pojo.Patient;
import com.prepeez.medicalhealthguard.realm.RealmPatient;
import com.greysonparrelli.permiso.Permiso;
import com.greysonparrelli.permiso.PermisoActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.prepeez.medicalhealthguard.activity.AddPatientByHealthProfActivity.saveToRealm;
import static com.prepeez.medicalhealthguard.activity.AddPatientByHealthProfActivity.saveToRecords;
import static com.prepeez.medicalhealthguard.activity.AddPatientByHealthProfActivity.updateRecords;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.action;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.constants.Const.format;
import static com.prepeez.medicalhealthguard.constants.Const.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

public class AddPatientByMisActivity extends PermisoActivity implements TabFragment7.SendMessage {

    static Context context;

    private static final String TAG = "AddPatientByMisActivity";

    static final int ALL_PERMISSIONS_RESULT = 107;
    ArrayList permissionsToRequest;
    ArrayList permissionsRejected = new ArrayList();
    ArrayList permissions = new ArrayList();


    boolean close = false;
    ViewPager mViewPager;
    MisAgentPatientPageAdapter accountPageAdapter;


    RelativeLayout rootview;
    ProgressBar progressBar;
    private ProgressDialog mProgressDialog;

    String tag1 = "android:switcher:" + R.id.pageques + ":" + 0;
    String tag2 = "android:switcher:" + R.id.pageques + ":" + 1;
    String tag3 = "android:switcher:" + R.id.pageques + ":" + 2;
    String tag4 = "android:switcher:" + R.id.pageques + ":" + 3;
    String tag5 = "android:switcher:" + R.id.pageques + ":" + 4;
    String tag6 = "android:switcher:" + R.id.pageques + ":" + 5;
    String tag7 = "android:switcher:" + R.id.pageques + ":" + 6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        permissions.add(CAMERA);
        permissions.add(INTERNET);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_PHONE_STATE);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(GET_ACCOUNTS);


        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        Permiso.getInstance().setActivity(this);

        setContentView(R.layout.activity_add_patient_by_mis_agent);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Processing...");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);

        rootview = findViewById(R.id.root);

        progressBar = findViewById(R.id.pbar_pic);

        accountPageAdapter = new MisAgentPatientPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.pageques);
        mViewPager.setAdapter(accountPageAdapter);
        mViewPager.setOffscreenPageLimit(6);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_account, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.sign_out_menu:
//                //FirebaseAuth.getInstance().signOut();
//                Toast.makeText(this, "Signed out!", Toast.LENGTH_SHORT).show();
//                finish();
//                return true;
//            case R.id.change_password_menu:
//                //startActivity(new Intent(AddPatientByMisActivity.this, UpdatePasswordActivity.class));
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    // * must add this *
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissions) {
                    if (hasPermission(perms)) {

                    } else {

                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(String.valueOf(permissionsRejected.get(0)))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }
    }

    @Override
    public void sendData() {

        final TabFragment1 tabFrag1 = (TabFragment1) getSupportFragmentManager().findFragmentByTag(tag1);
        final TabFragment2 tabFrag2 = (TabFragment2) getSupportFragmentManager().findFragmentByTag(tag2);
        final TabFragment3 tabFrag3 = (TabFragment3) getSupportFragmentManager().findFragmentByTag(tag3);
        final TabFragment4 tabFrag4 = (TabFragment4) getSupportFragmentManager().findFragmentByTag(tag4);
        final TabFragment5 tabFrag5 = (TabFragment5) getSupportFragmentManager().findFragmentByTag(tag5);
        final TabFragment6 tabFrag6 = (TabFragment6) getSupportFragmentManager().findFragmentByTag(tag6);
        final TabFragment7 tabFrag7 = (TabFragment7) getSupportFragmentManager().findFragmentByTag(tag7);


        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (tabFrag1 != null && tabFrag2 != null && tabFrag3 != null && tabFrag4 != null && tabFrag5 != null && tabFrag6 != null && tabFrag7 != null) {
                    tabFrag1.validate();
                    tabFrag2.validate();
                    tabFrag3.validate();
                    tabFrag4.validate();
                    tabFrag5.validate();
                    tabFrag6.validate();
                    tabFrag7.validate();
                    if (tabFrag1.validate() && tabFrag2.validate() && tabFrag3.validate() && tabFrag4.validate() && tabFrag5.validate() && tabFrag6.validate() && tabFrag7.validate()) {

                        if (action.equals("add")) {
                            //clickedPatient.setCreated_at(calendar.getTime().toString());
                            //clickedPatient.setUpdated_at(calendar.getTime().toString());
                            new AddPatientByHealthProfActivity.SendSMS(AddPatientByMisActivity.this).execute();
                            tabFrag1.clear();
                            tabFrag2.clear();
                            tabFrag3.clear();
                            tabFrag4.clear();
                            tabFrag5.clear();
                            tabFrag6.clear();
                            tabFrag7.clear();
                        }
                        else if (action.equals("edit")) {
                            if (isNetworkAvailable(AddPatientByMisActivity.this)) {
                                updateRecords(AddPatientByMisActivity.this, false);
                            }
                            else {
                                try {
                                    java.util.Date date = format.parse(clickedPatient.getUpdated_at());
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    calendar.add(Calendar.SECOND, 1);
                                    String updatedAt = format.format(calendar.getTime());

                                    clickedPatient.setUpdated_at(updatedAt);
                                    realm.copyToRealmOrUpdate(clickedPatient);
                                    Const.showToast(AddPatientByMisActivity.this, "Data Updated Successfully");
                                } catch (ParseException e) {
                                    Log.d("updateerror", "error: " + e.getMessage());
                                    e.printStackTrace();
                                }

                            }
                        }
                    } else {
                        Toast.makeText(AddPatientByMisActivity.this, R.string.required_field_message, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }


    private void showTwoButtonSnackbar() {

        // Create the Snackbar
        LinearLayout.LayoutParams objLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final Snackbar snackbar = Snackbar.make(rootview, "Exit?", Snackbar.LENGTH_INDEFINITE);

        // Get the Snackbar layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        // Inflate our courseListMaterialDialog viewBitmap bitmap = ((RoundedDrawable)profilePic.getDrawable()).getSourceBitmap();
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);


        TextView textViewOne = snackView.findViewById(R.id.first_text_view);
        textViewOne.setText(this.getResources().getString(R.string.yes));
        textViewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                close = true;
                AddPatientByMisActivity.this.onBackPressed();

                //  finish();
            }
        });

        final TextView textViewTwo = snackView.findViewById(R.id.second_text_view);

        textViewTwo.setText(this.getResources().getString(R.string.no));
        textViewTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Deny", "showTwoButtonSnackbar() : deny clicked");
                snackbar.dismiss();


            }
        });

        // Add our courseListMaterialDialog view to the Snackbar's layout
        layout.addView(snackView, objLayoutParams);

        // Show the Snackbar
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        if (close)
            super.onBackPressed();
//        if (statusMsg.getVisibility() == View.GONE) {
//            showTwoButtonSnackbar();
//        } else {
//            super.onBackPressed();
//        }

        showTwoButtonSnackbar();
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
}
