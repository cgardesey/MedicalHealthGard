package com.prepeez.medicalhealthguard.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.greysonparrelli.permiso.Permiso;
import com.greysonparrelli.permiso.PermisoActivity;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.constants.Const;
import com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag1;
import com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag2;
import com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag3;
import com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag4;
import com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag5;
import com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag6;
import com.prepeez.medicalhealthguard.fragmentPagerAdapter.HealthRecordPageAdapter;
import com.prepeez.medicalhealthguard.http.healthrecord.FetchHealthRecordsSingleton;
import com.prepeez.medicalhealthguard.http.healthrecord.HealthRecordSingleton;
import com.prepeez.medicalhealthguard.http.healthrecord.UpdateHealthRecordSingleton;
import com.prepeez.medicalhealthguard.http.patient.UpdatePatientSingleton;
import com.prepeez.medicalhealthguard.materialDialog.AlertMaterialDialog;
import com.prepeez.medicalhealthguard.materialDialog.ConfirmPatientMaterialDialog;
import com.prepeez.medicalhealthguard.materialDialog.HealthRecordAlertMaterialDialog;
import com.prepeez.medicalhealthguard.pojo.HealthRecord;
import com.prepeez.medicalhealthguard.pojo.Patient;
import com.prepeez.medicalhealthguard.realm.RealmAgent;
import com.prepeez.medicalhealthguard.realm.RealmHealthRecord;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.INTERNET;
import static com.prepeez.medicalhealthguard.activity.SigninActivity.AGENTID;
import static com.prepeez.medicalhealthguard.adapter.HealthRecordAdapter.clickedHealthRecord;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.constants.Const.format;
import static com.prepeez.medicalhealthguard.constants.Const.isNetworkAvailable;
import static com.prepeez.medicalhealthguard.fragment.healthRecord.TabFrag1.BMI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

public class AddHealthRecordActivity extends PermisoActivity implements TabFrag6.SendMessage {

    static Context context;

    private static final String TAG = "AddHealthRecordActivity";

    static final int ALL_PERMISSIONS_RESULT = 107;
    ArrayList permissionsToRequest;
    ArrayList permissionsRejected = new ArrayList();
    ArrayList permissions = new ArrayList();

    static String smsContent = "";


    boolean close = false;
    ViewPager mViewPager;
    HealthRecordPageAdapter healthRecordPageAdapter;


    RelativeLayout rootview;
    ProgressBar progressBar;
    private ProgressDialog mProgressDialog;

    private static ProgressDialog mProgress;

    static android.app.FragmentManager fm;

    static HealthRecordAlertMaterialDialog healthRecordAlertMaterialDialog;
    AlertMaterialDialog alertMaterialDialog;

    static String name, contact, netVisit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        healthRecordAlertMaterialDialog = new HealthRecordAlertMaterialDialog();
        context = getApplicationContext();

        fm = getFragmentManager();

        alertMaterialDialog = new AlertMaterialDialog();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Sending sms...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        //permissions.add(CAMERA);
        permissions.add(INTERNET);
        //permissions.add(READ_EXTERNAL_STORAGE);
        //permissions.add(WRITE_EXTERNAL_STORAGE);
        //permissions.add(READ_PHONE_STATE);
        //permissions.add(ACCESS_COARSE_LOCATION);
        //permissions.add(ACCESS_FINE_LOCATION);
        //permissions.add(GET_ACCOUNTS);


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

        healthRecordPageAdapter = new HealthRecordPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.pageques);
        mViewPager.setAdapter(healthRecordPageAdapter);
        if (clickedPatient.getGender().equals("Female")) {
            mViewPager.setOffscreenPageLimit(5);
        } else {
            mViewPager.setOffscreenPageLimit(4);
        }
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


        if (clickedPatient.getGender().equals("Female")) {

            String tag1 = "android:switcher:" + R.id.pageques + ":" + 0;
            String tag2 = "android:switcher:" + R.id.pageques + ":" + 1;
            String tag3 = "android:switcher:" + R.id.pageques + ":" + 2;
            String tag4 = "android:switcher:" + R.id.pageques + ":" + 3;
            String tag5 = "android:switcher:" + R.id.pageques + ":" + 4;
            String tag6 = "android:switcher:" + R.id.pageques + ":" + 5;

            final TabFrag1 tabFrag1 = (TabFrag1) getSupportFragmentManager().findFragmentByTag(tag1);
            final TabFrag2 tabFrag2 = (TabFrag2) getSupportFragmentManager().findFragmentByTag(tag2);
            final TabFrag3 tabFrag3 = (TabFrag3) getSupportFragmentManager().findFragmentByTag(tag3);
            final TabFrag4 tabFrag4 = (TabFrag4) getSupportFragmentManager().findFragmentByTag(tag4);
            final TabFrag5 tabFrag5 = (TabFrag5) getSupportFragmentManager().findFragmentByTag(tag5);
            final TabFrag6 tabFrag6 = (TabFrag6) getSupportFragmentManager().findFragmentByTag(tag6);

            Realm.init(context);
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    boolean tabsNotNull;
                    tabsNotNull = tabFrag1 != null && tabFrag2 != null && tabFrag3 != null && tabFrag4 != null && tabFrag5 != null && tabFrag6 != null;
                    if (tabsNotNull) {
                        tabFrag1.validate();
                        tabFrag2.validate();
                        tabFrag3.validate();
                        tabFrag4.validate();
                        tabFrag5.validate();
                        tabFrag6.validate();

                        boolean validateTabs;
                        validateTabs = tabFrag1.validate() && tabFrag2.validate() && tabFrag3.validate() && tabFrag4.validate() && tabFrag5.validate() && tabFrag6.validate();

                        if (validateTabs) {

                            contact = clickedPatient.getContact();
                            netVisit = clickedHealthRecord.getNextscheduledvisit();
                            name = clickedPatient.getTitle() + " " + clickedPatient.getFirstname();
                            //new SendSMS().execute();
//                            tabFrag1.clear();
//                            tabFrag2.clear();
//                            tabFrag3.clear();
//                            tabFrag4.clear();
//                            tabFrag5.clear();
//                            tabFrag6.clear();

                            android.app.FragmentManager fm = getFragmentManager();
                            ConfirmPatientMaterialDialog confirmPatientMaterialDialog = new ConfirmPatientMaterialDialog();
                            if(confirmPatientMaterialDialog != null && confirmPatientMaterialDialog.isAdded()) {
                            } else {
                                confirmPatientMaterialDialog.setRealmPatient(clickedPatient);
                                confirmPatientMaterialDialog.show(fm, "ConfirmPatientMaterialDialog");
                            }
                        } else {
                            Toast.makeText(AddHealthRecordActivity.this, R.string.required_field_message, Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

        }
        else {

            String tag1 = "android:switcher:" + R.id.pageques + ":" + 0;
            String tag2 = "android:switcher:" + R.id.pageques + ":" + 1;
            String tag3 = "android:switcher:" + R.id.pageques + ":" + 2;
            String tag4 = "android:switcher:" + R.id.pageques + ":" + 3;
            String tag6 = "android:switcher:" + R.id.pageques + ":" + 4;


            final TabFrag1 tabFrag1 = (TabFrag1) getSupportFragmentManager().findFragmentByTag(tag1);
            final TabFrag2 tabFrag2 = (TabFrag2) getSupportFragmentManager().findFragmentByTag(tag2);
            final TabFrag3 tabFrag3 = (TabFrag3) getSupportFragmentManager().findFragmentByTag(tag3);
            final TabFrag4 tabFrag4 = (TabFrag4) getSupportFragmentManager().findFragmentByTag(tag4);
            final TabFrag6 tabFrag6 = (TabFrag6) getSupportFragmentManager().findFragmentByTag(tag6);

            Realm.init(context);
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    boolean tabsNotNull;
                    tabsNotNull = tabFrag1 != null && tabFrag2 != null && tabFrag3 != null && tabFrag4 != null && tabFrag6 != null;

                    if (tabsNotNull) {
                        tabFrag1.validate();
                        tabFrag2.validate();
                        tabFrag3.validate();
                        tabFrag4.validate();
                        tabFrag6.validate();

                        boolean validateTabs;
                        validateTabs = tabFrag1.validate() && tabFrag2.validate() && tabFrag3.validate() && tabFrag4.validate() && tabFrag6.validate();

                        if (validateTabs) {

                            contact = clickedPatient.getContact();
                            netVisit = clickedHealthRecord.getNextscheduledvisit();
                            name = clickedPatient.getTitle() + " " + clickedPatient.getFirstname();
                            //new SendSMS().execute();
//                            tabFrag1.clear();
//                            tabFrag2.clear();
//                            tabFrag3.clear();
//                            tabFrag4.clear();
//                            tabFrag6.clear();

                            android.app.FragmentManager fm = getFragmentManager();
                            ConfirmPatientMaterialDialog confirmPatientMaterialDialog = new ConfirmPatientMaterialDialog();
                            if(confirmPatientMaterialDialog != null && confirmPatientMaterialDialog.isAdded()) {
                            } else {
                                confirmPatientMaterialDialog.setRealmPatient(clickedPatient);
                                confirmPatientMaterialDialog.show(fm, "ConfirmPatientMaterialDialog");
                            }
                        } else {
                            Toast.makeText(AddHealthRecordActivity.this, R.string.required_field_message, Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });


        }

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
                AddHealthRecordActivity.this.onBackPressed();

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

    public static void onConfirmInit() {
        String contact = clickedPatient.getContact();
        new SendSMS().execute(contact);
    }

    private static void saveToRecords() {
        HealthRecordSingleton singleton = new HealthRecordSingleton();

        Call<HealthRecord> recordCall = singleton.getHealthRecordInterface().addRecord(
                clickedHealthRecord.getHealthrecordid(),
                clickedHealthRecord.getWeight(),
                clickedHealthRecord.getHeight(),
                clickedHealthRecord.getHeartrate(),
                clickedHealthRecord.getSystolicbp(),
                clickedHealthRecord.getDiastolicbp(),
                clickedHealthRecord.getChiefpresentingcomplaint(),
                clickedHealthRecord.getHistoryofpreseningcomplaint(),
                clickedHealthRecord.getOndiriectquestioning(),
                clickedHealthRecord.getGeneralphysicalexam(),
                clickedHealthRecord.getCardiovascularsyst(),
                clickedHealthRecord.getRespiratorysys(),
                clickedHealthRecord.getGastrointestinalsyst(),
                clickedHealthRecord.getCentralnervoussyst(),
                clickedHealthRecord.getDigitalrectalexam(),
                clickedHealthRecord.getVaginalexam(),
                clickedHealthRecord.getOtherexamandfindings(),
                clickedHealthRecord.getFbc(),
                clickedHealthRecord.getMps(),
                clickedHealthRecord.getLtf(),
                clickedHealthRecord.getWidal(),
                clickedHealthRecord.getKft(),
                clickedHealthRecord.getMenstralperiod(),
                clickedHealthRecord.getRegulariyofcycle(),
                clickedHealthRecord.getDurationofcycle(),
                clickedHealthRecord.getFlow(),
                clickedHealthRecord.getPain(),
                clickedHealthRecord.getTermpregnancies(),
                clickedHealthRecord.getAbortion(),
                clickedHealthRecord.getLivebirth(),
                clickedHealthRecord.getPrematurebirth(),
                clickedHealthRecord.getMultiplegestations(),
                clickedHealthRecord.getDifferentialdiagnosis(),
                clickedHealthRecord.getDiagnosis(),
                clickedHealthRecord.getTreatment(),
                clickedHealthRecord.getBodytemperature(),
                clickedPatient.getLocation(),
                clickedHealthRecord.getNextscheduledvisit(),
                clickedHealthRecord.getSms(),
                clickedHealthRecord.getSmsstatus(),
                clickedPatient.getPatientid(),
                clickedHealthRecord.getAgentid()
        );

        recordCall.enqueue(new Callback<HealthRecord>() {
            @Override
            public void onResponse(Call<HealthRecord> call, Response<HealthRecord> response) {
                //progressDialog.dismiss();
                if (response.isSuccessful()) {
                    HealthRecord healthRecord = response.body();

                    RealmHealthRecord realmHealthRecord = new RealmHealthRecord(
                            healthRecord.getHealthrecordid(),
                            healthRecord.getWeight(),
                            healthRecord.getHeight(),
                            healthRecord.getHeartrate(),
                            healthRecord.getSystolicbp(),
                            healthRecord.getDiastolicbp(),
                            healthRecord.getChiefpresentingcomplaint(),
                            healthRecord.getHistoryofpreseningcomplaint(),
                            healthRecord.getOndiriectquestioning(),
                            healthRecord.getGeneralphysicalexam(),
                            healthRecord.getCardiovascularsyst(),
                            healthRecord.getRespiratorysys(),
                            healthRecord.getGastrointestinalsyst(),
                            healthRecord.getCentralnervoussyst(),
                            healthRecord.getDigitalrectalexam(),
                            healthRecord.getVaginalexam(),
                            healthRecord.getOtherexamandfindings(),
                            healthRecord.getFbc(),
                            healthRecord.getMps(),
                            healthRecord.getLtf(),
                            healthRecord.getWidal(),
                            healthRecord.getKft(),
                            healthRecord.getMenstralperiod(),
                            healthRecord.getRegulariyofcycle(),
                            healthRecord.getDurationofcycle(),
                            healthRecord.getFlow(),
                            healthRecord.getPain(),
                            healthRecord.getTermpregnancies(),
                            healthRecord.getAbortion(),
                            healthRecord.getLivebirth(),
                            healthRecord.getPrematurebirth(),
                            healthRecord.getMultiplegestations(),
                            healthRecord.getDifferentialdiagnosis(),
                            healthRecord.getDiagnosis(),
                            healthRecord.getTreatment(),
                            healthRecord.getBodytemperature(),
                            healthRecord.getLocation(),
                            healthRecord.getNextscheduledvisit(),
                            healthRecord.getSms(),
                            healthRecord.getSmsstatus(),
                            healthRecord.getPatientid(),
                            healthRecord.getPatientid(),
                            healthRecord.getCreated_at(),
                            healthRecord.getUpdated_at()
                    );
                    saveToRealm(realmHealthRecord);
                    ////Const.showToast(getApplicationContext(),"Data Added Successfully");
                /*}else if (response.code() == 403){
                    Const.showToast(getApplicationContext(),"Data Already Inserted");*/
                } else {
                    String agentid = PreferenceManager.getDefaultSharedPreferences(context).getString(AGENTID, "");
;
                    //clickedHealthRecord.setLocation(clickedPatient.getLocation());
                    //clickedHealthRecord.setPatientid(clickedPatient.getPatientid());
                    clickedHealthRecord.setAgentid(agentid);
                    clickedHealthRecord.setSms(smsContent);
                    saveToRealm(clickedHealthRecord);

                    Log.d("mosquito", response.errorBody().byteStream().toString());
                }
            }

            @Override
            public void onFailure(Call<HealthRecord> call, Throwable t) {
                //progressDialog.dismiss();
                Const.showToast(context, "You may not have internet connection");

            }
        });

    }

    public static void saveToRealm(final RealmHealthRecord realmHealthRecord) {
        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm mRealm) {
                                              // mRealm.createObject(RealmHealthRecord.class, clickedHealthRecord);
                                              Number num = mRealm.where(RealmHealthRecord.class).max("id");
                                              int id = 0;
                                              if (num == null) {
                                                  id = 1;
                                              } else {
                                                  id = num.intValue() + 1;
                                              }
                                              realmHealthRecord.setId(id);
                                              mRealm.copyToRealmOrUpdate(realmHealthRecord);
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
                        Const.showToast(context, error.getMessage());
                        Log.d("asd", error.toString());
                    }
                }
        );
    }

    public static class SendSMS extends AsyncTask<String, Void, String> {

        private String html;

        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected String doInBackground(String... strings) {
//            String url = "https://api.hubtel.com/v1/messages/send?"
//                    + "From=Unity&To=%2B233546101171"
//                    + "&Content=Hello%2C+world&amp"
//                    + "&ClientId=jsaiwsob"
//                    + "&ClientSecret=kgcnvrsi&RegisteredDelivery=true";
            smsContent = "Dear " + name + ", Your next visit is scheduled for " + netVisit + ". " + "Your bill is " + getPatientBill();
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.hubtel.com")
                    .appendPath("v1")
                    .appendPath("messages")
                    .appendPath("send")
                    .appendQueryParameter("From", "MHG")
                    .appendQueryParameter("To", contact)
                    .appendQueryParameter("Content", smsContent)
                    .appendQueryParameter("ClientId", context.getString(R.string.CLIENT_ID))
                    .appendQueryParameter("ClientSecret", context.getString(R.string.CLIENT_SECRET))
                    .appendQueryParameter("RegisteredDelivery", "true");

            String url = builder.build().toString();

            try {
                html = new Scanner(new DefaultHttpClient().execute(new HttpGet(url)).getEntity().getContent(), "UTF-8").useDelimiter("\\A").next();
            } catch (IOException e) {
                html = "" + e.toString();
            }
            String content = null;
            return html;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgress.dismiss();
            super.onPostExecute(result);
            if (result != null) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    final String message = jsonObject.getString("Message");
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            clickedHealthRecord.setSmsstatus(message);
                            String agentid = PreferenceManager.getDefaultSharedPreferences(context).getString(AGENTID, "");
                            clickedHealthRecord.setAgentid(agentid);
                            clickedHealthRecord.setSms(smsContent);
                            if (isNetworkAvailable(context)) {
                                saveToRecords();
                            } else {

                                saveToRealm(clickedHealthRecord);
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("smserror", e.getMessage());
                    Toast.makeText(context, "Error sending sms.", Toast.LENGTH_SHORT).show();
                }
            } else {

            }
            String alert = alertBuilder();
            if (!alert.equals("")) {

                if (healthRecordAlertMaterialDialog != null && healthRecordAlertMaterialDialog.isAdded()) {

                } else {
                    healthRecordAlertMaterialDialog.setAlert(alert);
                    healthRecordAlertMaterialDialog.show(fm, "HealthRecordAlertMaterialDialog");
                }

            }
        }
    }

    private static String getPatientBill() {
        return "Service charge: 50 cedis\nEquipments used: 40cedis";
    }

    private static String alertBuilder() {
        String alert = "";

        if (BMI > 0.0f) {
            if (BMI > 24.9) {
                alert += "Patient is overweight\n\n";
            } else if (BMI < 18.5) {
                alert += "Patient is underweight\n\n";
            }
        }

        String SYSTOLICBP = clickedHealthRecord.getSystolicbp();
        if (!(TextUtils.isEmpty(SYSTOLICBP))) {

            if (Float.parseFloat(SYSTOLICBP) <= 90) {
                alert += "Patient has low blood pressure\n\n";
            } else if (Float.parseFloat(SYSTOLICBP) > 120 && Float.parseFloat(SYSTOLICBP) < 140) {
                alert += "Patient has pre-high blood pressure\n\n";
            } else if (Float.parseFloat(SYSTOLICBP) >= 140) {
                alert += "Patient has high blood pressure\n\n";
            }
        }
        String DIASTOLICBP = clickedHealthRecord.getDiastolicbp();
        if (!(TextUtils.isEmpty(DIASTOLICBP))) {

            if (Float.parseFloat(DIASTOLICBP) <= 60) {
                if (!alert.contains("Patient has low blood pressure")) {
                    alert += "Patient has low blood pressure\n\n";
                }
            } else if (Float.parseFloat(DIASTOLICBP) > 80 && Float.parseFloat(DIASTOLICBP) < 90) {
                if (!alert.contains("Patient has pre-high blood pressure")) {
                    alert += "Patient has pre-high blood pressure\n\n";
                }
            } else if (Float.parseFloat(DIASTOLICBP) >= 90) {
                if (!alert.contains("Patient has high blood pressure")) {
                    alert += "Patient has high blood pressure\n\n";
                }
            }
        }

        String BODYTEMP = clickedHealthRecord.getBodytemperature();
        if (!(TextUtils.isEmpty(BODYTEMP))) {

            if (Float.parseFloat(BODYTEMP) < 36.1) {
                alert += "Body temperature is below normal range\n\n";
            } else if (Float.parseFloat(BODYTEMP) > 37.2) {
                alert += "Body temperature is above normal range\n\n";
            }
        }
        String ROC = clickedHealthRecord.getRegulariyofcycle();
        if (!(TextUtils.isEmpty(ROC))) {

            if (Float.parseFloat(ROC) < 26) {
                alert += "Body temperature is below normal range\n\n";
            } else if (Float.parseFloat(ROC) > 29) {
                alert += "Body temperature is above normal range\n\n";
            }
        }
        String DOC = clickedHealthRecord.getDurationofcycle();
        if (!(TextUtils.isEmpty(DOC))) {

            if (Float.parseFloat(DOC) < 3) {
                alert += "Body temperature is below normal range\n\n";
            } else if (Float.parseFloat(DOC) > 7) {
                alert += "Body temperature is above normal range\n\n";
            }
        }
        String TERMPREG = clickedHealthRecord.getTermpregnancies();
        if (!(TextUtils.isEmpty(TERMPREG))) {

            if (Float.parseFloat(TERMPREG) < 38) {
                alert += "Body temperature is below normal range\n\n";
            } else if (Float.parseFloat(TERMPREG) > 42) {
                alert += "Body temperature is above normal range\n\n";
            }
        }
        String[] split = clickedPatient.getDateofbirth().split("/");
        int day = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int year = Integer.parseInt(split[2]);

        int age = Const.getAge(year, month, day);

        String HEARTRATE = clickedHealthRecord.getHeartrate();
        if (!(TextUtils.isEmpty(HEARTRATE))) {
            if (age > 4 && age < 7) {
                if (Float.parseFloat(HEARTRATE) < 75) {
                    alert += "Heart rate is below normal range for patient's age\n\n";
                } else if (Float.parseFloat(HEARTRATE) > 115) {
                    alert += "Heart rate level is above normal range for patient's age\n\n";
                }
            } else if (age > 6 && age < 10) {
                if (Float.parseFloat(HEARTRATE) < 70) {
                    alert += "Heart rate is below normal range for patient's age\n\n";
                } else if (Float.parseFloat(HEARTRATE) > 110) {
                    alert += "Heart rate is above normal range for patient's age\n\n";
                }
            } else if (age> 9) {
                if (Float.parseFloat(HEARTRATE) < 60) {
                    alert += "Heart rate is below normal range for patient's age\n\n";
                } else if (Float.parseFloat(HEARTRATE) > 100) {
                    alert += "Heart rate is above normal range for patient's age\n\n";
                }
            }
        }
        return alert;
    }

    private void showAlertMaterialDialogFrag(String alert) {
        android.app.FragmentManager fm = getFragmentManager();
        alertMaterialDialog.setCancelable(true);
        alertMaterialDialog.setAlertText(alert);

        if (alertMaterialDialog != null && alertMaterialDialog.isAdded()) {
            // no need to call dialog.show(ft, "DatePicker");
        } else {
            alertMaterialDialog.show(fm, "AlertMaterialDialog");
        }
    }

    public static void updateRecords(final Context recordContext, final boolean silentUpdate) {

        final ProgressDialog progressDialog = new ProgressDialog(recordContext);
        progressDialog.setTitle("Updating Record");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        if (!silentUpdate) {
            progressDialog.show();
        }

        HealthRecord healthRecord = new HealthRecord(
                clickedHealthRecord.getHealthrecordid(),
                clickedHealthRecord.getWeight(),
                clickedHealthRecord.getHeight(),
                clickedHealthRecord.getHeartrate(),
                clickedHealthRecord.getSystolicbp(),
                clickedHealthRecord.getDiastolicbp(),
                clickedHealthRecord.getChiefpresentingcomplaint(),
                clickedHealthRecord.getHistoryofpreseningcomplaint(),
                clickedHealthRecord.getOndiriectquestioning(),
                clickedHealthRecord.getGeneralphysicalexam(),
                clickedHealthRecord.getCardiovascularsyst(),
                clickedHealthRecord.getRespiratorysys(),
                clickedHealthRecord.getGastrointestinalsyst(),
                clickedHealthRecord.getCentralnervoussyst(),
                clickedHealthRecord.getDigitalrectalexam(),
                clickedHealthRecord.getVaginalexam(),
                clickedHealthRecord.getOtherexamandfindings(),
                clickedHealthRecord.getFbc(),
                clickedHealthRecord.getMps(),
                clickedHealthRecord.getLtf(),
                clickedHealthRecord.getWidal(),
                clickedHealthRecord.getKft(),
                clickedHealthRecord.getMenstralperiod(),
                clickedHealthRecord.getRegulariyofcycle(),
                clickedHealthRecord.getDurationofcycle(),
                clickedHealthRecord.getFlow(),
                clickedHealthRecord.getPain(),
                clickedHealthRecord.getTermpregnancies(),
                clickedHealthRecord.getAbortion(),
                clickedHealthRecord.getLivebirth(),
                clickedHealthRecord.getPrematurebirth(),
                clickedHealthRecord.getMultiplegestations(),
                clickedHealthRecord.getDifferentialdiagnosis(),
                clickedHealthRecord.getDiagnosis(),
                clickedHealthRecord.getTreatment(),
                clickedHealthRecord.getBodytemperature(),
                clickedHealthRecord.getLocation(),
                clickedHealthRecord.getNextscheduledvisit(),
                clickedHealthRecord.getSms(),
                clickedHealthRecord.getSmsstatus(),
                clickedHealthRecord.getPatientid(),
                clickedHealthRecord.getAgentid()
        );

        UpdateHealthRecordSingleton singleton = new UpdateHealthRecordSingleton();
        Call<HealthRecord> recordCall = singleton.updateRecordsInterface().update(clickedHealthRecord.getHealthrecordid(), healthRecord);

        recordCall.enqueue(new Callback<HealthRecord>() {
            @Override
            public void onResponse(Call<HealthRecord> call, final Response<HealthRecord> response) {

                if (!silentUpdate) {
                    progressDialog.dismiss();
                }
                if (response.isSuccessful()) {
                    HealthRecord healthRecord = response.body();

//                    final RealmPatient realmPatient_callback = new RealmPatient(
//                            patient.getPatientid(),
//                            patient.getPicture(),
//                            patient.getTitle(),
//                            patient.getFirstname(),
//                            patient.getLastname(),
//                            patient.getOthername(),
//                            patient.getGender(),
//                            patient.getDateofbirth(),
//                            patient.getMaritalstatus(),
//                            patient.getContact(),
//                            patient.getAddress(),
//                            patient.getLocation(),
//                            patient.getOccupation(),
//                            patient.getNextofkintitle(),
//                            patient.getNextofkinfirstname(),
//                            patient.getNextofkinlastname(),
//                            patient.getNextofkinothername(),
//                            patient.getNextofkingender(),
//                            patient.getNextofkindateofbirth(),
//                            patient.getNextofkinmaritalstatus(),
//                            patient.getNextofkincontact(),
//                            patient.getNextofkinaddress(),
//                            patient.getNextofkinlocation(),
//                            patient.getNhisnumber(),
//                            patient.getNhispicture(),
//                            patient.getAbobloodgroup(),
//                            patient.getSicklecellbloodgroup(),
//                            patient.getAllergies(),
//                            patient.getSpecializedcondition(),
//                            patient.getAccounttype(),
//                            patient.getActive(),
//                            1,
//                            1,
//                            patient.getCreated_at(),
//                            patient.getUpdated_at()
//                    );
                    if (!silentUpdate) {
                        Const.showToast(recordContext, "Data Updated Successfully");
                    }

                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            clickedHealthRecord.setUpdated_at(response.body().getUpdated_at());
                            realm.copyToRealmOrUpdate(clickedHealthRecord);
                        }
                    });
                } else {
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(clickedHealthRecord);
                            if (!silentUpdate) {
                                Const.showToast(recordContext, "Data Updated Successfully");
                            }
                        }
                    });
                    Log.d("mosquito", response.message());
                }
            }

            @Override
            public void onFailure(Call<HealthRecord> call, Throwable t) {
                progressDialog.dismiss();
                if (!silentUpdate) {
                    Const.showToast(recordContext, "You may not have internet connection");
                }
            }
        });

    }
}
