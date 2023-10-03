package com.prepeez.medicalhealthguard.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.greysonparrelli.permiso.Permiso;
import com.greysonparrelli.permiso.PermisoActivity;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.constants.Const;
import com.prepeez.medicalhealthguard.fragment.healthProf.Tab1;
import com.prepeez.medicalhealthguard.fragment.healthProf.Tab2;
import com.prepeez.medicalhealthguard.fragment.healthProf.Tab3;
import com.prepeez.medicalhealthguard.fragment.patient.CompaniesPatientFragment;
import com.prepeez.medicalhealthguard.fragment.patient.FamiliesPatientFragment;
import com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment;
import com.prepeez.medicalhealthguard.fragmentPagerAdapter.HealthProfPatientPageAdapter;
import com.prepeez.medicalhealthguard.http.patient.PatientSingleton;
import com.prepeez.medicalhealthguard.http.patient.UpdatePatientSingleton;
import com.prepeez.medicalhealthguard.pojo.Patient;
import com.prepeez.medicalhealthguard.realm.RealmPatient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

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
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.action;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.constants.Const.format;
import static com.prepeez.medicalhealthguard.constants.Const.isNetworkAvailable;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

public class AddPatientByHealthProfActivity extends PermisoActivity implements Tab3.SendMessage {

    static Context context;

    private static final String TAG = "AddPatientByMisActivity";

    static final int ALL_PERMISSIONS_RESULT = 107;
    ArrayList permissionsToRequest;
    ArrayList permissionsRejected = new ArrayList();
    ArrayList permissions = new ArrayList();

    boolean close = false;
    ViewPager mViewPager;
    HealthProfPatientPageAdapter healthProfPatientPageAdapter;


    RelativeLayout rootview;
    ProgressBar progressBar;
    private ProgressDialog mProgressDialog;

    static String smsContent = "";

    String tag1 = "android:switcher:" + R.id.pageques + ":" + 0;
    String tag2 = "android:switcher:" + R.id.pageques + ":" + 1;
    String tag3 = "android:switcher:" + R.id.pageques + ":" + 2;


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

        healthProfPatientPageAdapter = new HealthProfPatientPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.pageques);
        mViewPager.setAdapter(healthProfPatientPageAdapter);
        mViewPager.setOffscreenPageLimit(2);
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

        final Tab1 tab1 = (Tab1) getSupportFragmentManager().findFragmentByTag(tag1);
        final Tab2 tab2 = (Tab2) getSupportFragmentManager().findFragmentByTag(tag2);
        final Tab3 tab3 = (Tab3) getSupportFragmentManager().findFragmentByTag(tag3);

        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (tab1 != null && tab2 != null && tab3 != null) {

                    tab1.validate();
                    tab2.validate();
                    tab3.validate();

                    if (tab1.validate() && tab2.validate() && tab3.validate()) {

                        if (action.equals("add")) {
                            //clickedPatient.setCreated_at(calendar.getTime().toString());
                            //clickedPatient.setUpdated_at(calendar.getTime().toString());
                            new SendSMS(AddPatientByHealthProfActivity.this).execute();
                            tab1.clear();
                            tab2.clear();
                            tab3.clear();
                        } else if (action.equals("edit")) {
                            if (isNetworkAvailable(AddPatientByHealthProfActivity.this)) {
                                updateRecords(AddPatientByHealthProfActivity.this, false);
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
                                    Const.showToast(AddPatientByHealthProfActivity.this, "Data Updated Successfully");
                                } catch (ParseException e) {
                                    Log.d("updateerror", "error: " + e.getMessage());
                                    e.printStackTrace();
                                }

                                IndividualsPatientFragment individualsPatientFragment = new IndividualsPatientFragment();
                                FamiliesPatientFragment familiesPatientFragment = new FamiliesPatientFragment();
                                CompaniesPatientFragment companiesPatientFragment = new CompaniesPatientFragment();

                                individualsPatientFragment.populatePatients();
                                familiesPatientFragment.populatePatients();
                                companiesPatientFragment.populatePatients();

                                //individualsPatientFragment.refreshAdapter();
                                //familiesPatientFragment.refreshAdapter();
                                //companiesPatientFragment.refreshAdapter();
                            }
                        }
                    } else {
                        Toast.makeText(AddPatientByHealthProfActivity.this, R.string.required_field_message, Toast.LENGTH_SHORT).show();
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
                AddPatientByHealthProfActivity.this.onBackPressed();

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

    public static void saveToRecords(final Context recordContext) {
        PatientSingleton singleton = new PatientSingleton();
        Call<Patient> recordCall = singleton.getPatientInterface().addRecord(
                clickedPatient.getPatientid(),
                clickedPatient.getZone(),
                clickedPatient.getPicture(),
                clickedPatient.getTitle(),
                clickedPatient.getFirstname(),
                clickedPatient.getLastname(),
                clickedPatient.getOthername(),
                clickedPatient.getGender(),
                clickedPatient.getDateofbirth(),
                clickedPatient.getMaritalstatus(),
                clickedPatient.getContact(),
                clickedPatient.getAddress(),
                clickedPatient.getLocation(),
                clickedPatient.getOccupation(),
                clickedPatient.getNextofkintitle(),
                clickedPatient.getNextofkinfirstname(),
                clickedPatient.getNextofkinlastname(),
                clickedPatient.getNextofkinothername(),
                clickedPatient.getNextofkingender(),
                clickedPatient.getNextofkindateofbirth(),
                clickedPatient.getNextofkinmaritalstatus(),
                clickedPatient.getNextofkincontact(),
                clickedPatient.getNextofkinaddress(),
                clickedPatient.getNextofkinlocation(),
                clickedPatient.getNhisnumber(),
                clickedPatient.getNhispicture(),
                clickedPatient.getAbobloodgroup(),
                clickedPatient.getSicklecellbloodgroup(),
                clickedPatient.getAllergichistory(),
                clickedPatient.getDrughistory(),
                clickedPatient.getPastsurgicalhistory(),
                clickedPatient.getPasthistory(),
                clickedPatient.getFamilyhistory(),
                clickedPatient.getAgeofmenarche(),
                clickedPatient.getGravidity(),
                clickedPatient.getAccounttype(),
                clickedPatient.getGrouptype(),
                clickedPatient.getGroupid(),
                clickedPatient.getActive(),
                smsContent,
                clickedPatient.getSmsstatus()
        );

        recordCall.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                //progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Patient patient = response.body();

                    RealmPatient realmPatient = new RealmPatient(
                            patient.getPatientid(),
                            patient.getZone(),
                            patient.getPicture(),
                            patient.getTitle(),
                            patient.getFirstname(),
                            patient.getLastname(),
                            patient.getOthername(),
                            patient.getGender(),
                            patient.getDateofbirth(),
                            patient.getMaritalstatus(),
                            patient.getContact(),
                            patient.getAddress(),
                            patient.getLocation(),
                            patient.getOccupation(),
                            patient.getNextofkintitle(),
                            patient.getNextofkinfirstname(),
                            patient.getNextofkinlastname(),
                            patient.getNextofkinothername(),
                            patient.getNextofkingender(),
                            patient.getNextofkindateofbirth(),
                            patient.getNextofkinmaritalstatus(),
                            patient.getNextofkincontact(),
                            patient.getNextofkinaddress(),
                            patient.getNextofkinlocation(),
                            patient.getNhisnumber(),
                            patient.getNhispicture(),
                            patient.getAbobloodgroup(),
                            patient.getSicklecellbloodgroup(),
                            patient.getAllergichistory(),
                            patient.getDrughistory(),
                            patient.getPastsurgicalhistory(),
                            patient.getPasthistory(),
                            patient.getFamilyhistory(),
                            patient.getAgeofmenarche(),
                            patient.getGravidity(),
                            patient.getAccounttype(),
                            patient.getGrouptype(),
                            patient.getGroupid(),
                            patient.getActive(),
                            patient.getSms(),
                            patient.getSmsstatus(),
                            patient.getCreated_at(),
                            patient.getUpdated_at()
                            );
                    //Toast.makeText(recordContext, "Data successfully saved!", Toast.LENGTH_SHORT).show();
                    saveToRealm(realmPatient);
                    //Const.showToast(getApplicationContext(), "Data Added Successfully");
                /*}else if (response.code() == 403){
                    Const.showToast(getApplicationContext(),"Data Already Inserted");*/
                } else {
                    clickedPatient.setSms(smsContent);
                    saveToRealm(clickedPatient);
                    Log.d("mosquito", response.errorBody().byteStream().toString());
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                //progressDialog.dismiss();
                //Const.showToast(recordContext, "You may not have internet connection");
                Log.d("mosquito", t.getMessage());
            }
        });

    }

    public static void saveToRealm(final RealmPatient realmPatient) {
        final Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm mRealm) {
                                              long countBefore = mRealm.where(RealmPatient.class).count();
                                              // mRealm.createObject(RealmPatient.class, realmPatient);
                                              Number num = mRealm.where(RealmPatient.class).max("id");
                                              int id = 0;
                                              if (num == null) {
                                                  id = 1;
                                              } else {
                                                  id = num.intValue() + 1;
                                              }
                                              realmPatient.setId(id);
                                              mRealm.copyToRealmOrUpdate(realmPatient);
                                              long countAfter = mRealm.where(RealmPatient.class).count();
                                              //Log.d("panacosa", " countAfter: " + String.valueOf(countAfter));

                                              IndividualsPatientFragment individualsPatientFragment = new IndividualsPatientFragment();
                                              FamiliesPatientFragment familiesPatientFragment = new FamiliesPatientFragment();
                                              CompaniesPatientFragment companiesPatientFragment = new CompaniesPatientFragment();

                                              individualsPatientFragment.populatePatients();
                                              familiesPatientFragment.populatePatients();
                                              companiesPatientFragment.populatePatients();

                                              //individualsPatientFragment.refreshAdapter();
                                              //familiesPatientFragment.refreshAdapter();
                                              //companiesPatientFragment.refreshAdapter();
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

    public static void updateRecords(final Context recordContext, final boolean silentUpdate) {

        final ProgressDialog progressDialog = new ProgressDialog(recordContext);
        progressDialog.setTitle("Updating Record");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        if (!silentUpdate) {
            progressDialog.show();
        }

        Patient patient = new Patient(
                clickedPatient.getId(),
                clickedPatient.getPatientid(),
                clickedPatient.getZone(),
                clickedPatient.getPicture(),
                clickedPatient.getTitle(),
                clickedPatient.getFirstname(),
                clickedPatient.getLastname(),
                clickedPatient.getOthername(),
                clickedPatient.getGender(),
                clickedPatient.getDateofbirth(),
                clickedPatient.getMaritalstatus(),
                clickedPatient.getContact(),
                clickedPatient.getAddress(),
                clickedPatient.getLocation(),
                clickedPatient.getOccupation(),
                clickedPatient.getNextofkintitle(),
                clickedPatient.getNextofkinfirstname(),
                clickedPatient.getNextofkinlastname(),
                clickedPatient.getNextofkinothername(),
                clickedPatient.getNextofkingender(),
                clickedPatient.getNextofkindateofbirth(),
                clickedPatient.getNextofkinmaritalstatus(),
                clickedPatient.getNextofkincontact(),
                clickedPatient.getNextofkinaddress(),
                clickedPatient.getNextofkinlocation(),
                clickedPatient.getNhisnumber(),
                clickedPatient.getNhispicture(),
                clickedPatient.getAbobloodgroup(),
                clickedPatient.getSicklecellbloodgroup(),
                clickedPatient.getAllergichistory(),
                clickedPatient.getDrughistory(),
                clickedPatient.getPastsurgicalhistory(),
                clickedPatient.getPasthistory(),
                clickedPatient.getFamilyhistory(),
                clickedPatient.getAgeofmenarche(),
                clickedPatient.getGravidity(),
                clickedPatient.getAccounttype(),
                clickedPatient.getGrouptype(),
                clickedPatient.getGroupid(),
                clickedPatient.getActive(),
                clickedPatient.getSms(),
                clickedPatient.getSmsstatus()
        );

        UpdatePatientSingleton singleton = new UpdatePatientSingleton();
        Call<Patient> recordCall = singleton.updateRecordsInterface().update(clickedPatient.getPatientid(), patient);

        recordCall.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, final Response<Patient> response) {

                if (!silentUpdate) {
                    progressDialog.dismiss();
                }
                if (response.isSuccessful()) {
                    Patient patient = response.body();

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
                            clickedPatient.setUpdated_at(response.body().getUpdated_at());
                            realm.copyToRealmOrUpdate(clickedPatient);
                        }
                    });
                } else {
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(clickedPatient);
                            if (!silentUpdate) {
                                Const.showToast(recordContext, "Data Updated Successfully");
                            }
                        }
                    });
                    Log.d("mosquito", response.message());
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                progressDialog.dismiss();
                if (!silentUpdate) {
                    Const.showToast(recordContext, "You may not have internet connection");
                }
            }
        });

    }

    public static class SendSMS extends AsyncTask<String, Void, String> {
        Context context;
        private String html;

        private ProgressDialog asyncPd;

        public SendSMS(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            asyncPd = new ProgressDialog(context);
            asyncPd.setTitle("Sending sms...");
            asyncPd.setMessage("Please wait...");
            asyncPd.setCancelable(false);
            asyncPd.setIndeterminate(true);

            asyncPd.show();
        }

        @Override
        protected String doInBackground(String... strings) {


//            String url = "https://api.hubtel.com/v1/messages/send?"
//                    + "From=Unity&To=%2B233546101171"
//                    + "&Content=Hello%2C+world&amp"
//                    + "&ClientId=jsaiwsob"
//                    + "&ClientSecret=kgcnvrsi&RegisteredDelivery=true";
            String name = clickedPatient.getTitle() + " " + clickedPatient.getFirstname();
            smsContent = "Congratulations " + name + "!" + " You are registered on Medical Health Guard(MHG).An MHG personnel will call you on 0554723487 to complete your registration. Thank you.";
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.hubtel.com")
                    .appendPath("v1")
                    .appendPath("messages")
                    .appendPath("send")
                    .appendQueryParameter("From", "MHG")
                    .appendQueryParameter("To", clickedPatient.getContact())
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
            asyncPd.dismiss();
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    final String message = jsonObject.getString("Message");
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            clickedPatient.setSmsstatus(message);
                            clickedPatient.setSms(smsContent);
                            if (isNetworkAvailable(context)) {
                                saveToRecords(context);
                            } else {
                                saveToRealm(clickedPatient);
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
        }
    }
}
