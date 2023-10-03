package com.prepeez.medicalhealthguard.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greysonparrelli.permiso.Permiso;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;
import com.noelchew.multipickerwrapper.library.MultiPickerWrapper;
import com.noelchew.multipickerwrapper.library.ui.MultiPickerWrapperAppCompatActivity;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.constants.Const;
import com.prepeez.medicalhealthguard.http.patient.PatientSingleton;
import com.prepeez.medicalhealthguard.http.patient.UpdatePatientSingleton;
import com.prepeez.medicalhealthguard.pojo.Patient;
import com.prepeez.medicalhealthguard.realm.RealmPatient;
import com.prepeez.medicalhealthguard.util.PixelUtil;
import com.yalantis.ucrop.UCrop;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
import static com.prepeez.medicalhealthguard.constants.Const.isNetworkAvailable;
import static com.prepeez.medicalhealthguard.constants.Const.randomAlphaNumeric;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment2.BOUNDS_MOUNTAIN_VIEW;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment2.PLACE_PICKER_REQUEST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public class AddPatientByFieldAgenActivity extends MultiPickerWrapperAppCompatActivity {

    RoundedImageView profilePic;
    ProgressBar progressBar;
    LinearLayout controls;
    FloatingActionButton gal;
    FloatingActionButton cam;
    FloatingActionButton rem;
    FloatingActionButton accept;
    FloatingActionButton addimage;
    Spinner title;
    Spinner gender;
    EditText firstName;
    EditText lastName;
    EditText otherNames;
    IntlPhoneInput intlPhoneInput;
    Button generateID;
    EditText userId;
    EditText location;
    ImageView locPicker;
    private Spinner account_type_spinner;
    

    static Context mContext;

    static final int ALL_PERMISSIONS_RESULT = 107;
    ArrayList permissionsToRequest;
    ArrayList permissionsRejected = new ArrayList();
    ArrayList permissions = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_by_field_agen);

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

        profilePic = findViewById(R.id.profile_imgview);
        controls = findViewById(R.id.add);
        gal = findViewById(R.id.gal);
        cam = findViewById(R.id.cam);
        rem = findViewById(R.id.rem);
        accept = findViewById(R.id.accept);
        addimage = findViewById(R.id.addimage);
        title = findViewById(R.id.title_spinner);
        gender = findViewById(R.id.gender_spinner);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        otherNames = findViewById(R.id.other_names);
        intlPhoneInput = (IntlPhoneInput) findViewById(R.id.intlPhoneInput);
        generateID = findViewById(R.id.generateID);
        userId = findViewById(R.id.userId);
        locPicker = findViewById(R.id.locPicker);
        location = findViewById(R.id.location);
        account_type_spinner = findViewById(R.id.account_type_spinner);

        mContext = getApplicationContext();
        generateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId.setError(null);
                userId.setText(randomAlphaNumeric(6));
            }
        });
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controls.getVisibility() == View.VISIBLE) {
                    controls.setVisibility(View.GONE);

                } else {
                    controls.setVisibility(View.VISIBLE);
                }
            }
        });
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                multiPickerWrapper.getPermissionAndPickSingleImageAndCrop(imgOptions(), 1, 1);
            }
        });
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                multiPickerWrapper.getPermissionAndTakePictureAndCrop(imgOptions(), 1, 1);
            }
        });
        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePic.setImageDrawable(null);
            }
        });

        locPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build(AddPatientByFieldAgenActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (action.equals("edit")) {
                    clickedPatient.getRealm().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if (validate()) {
                                if (isNetworkAvailable(AddPatientByFieldAgenActivity.this)) {
                                    updateRecords(AddPatientByFieldAgenActivity.this, false);
                                } else {
                                    clickedPatient.setSmsstatus("");
                                    realm.copyToRealmOrUpdate(clickedPatient);
                                }
                            } else {
                                Toast.makeText(AddPatientByFieldAgenActivity.this, R.string.required_field_message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (action.equals("add")){
                    if (validate()) {
                        Calendar calendar = Calendar.getInstance(Locale.UK);
//                        clickedPatient.setCreated_at(calendar.getTime().toString());
//                        clickedPatient.setUpdated_at(calendar.getTime().toString());
                        userId.setText(null);
                        if (isNetworkAvailable(mContext)) {

                            saveToRecords(AddPatientByFieldAgenActivity.this);
                        } else {
                            saveToRealm(clickedPatient);
                        }
                    } else {
                        Toast.makeText(mContext, R.string.required_field_message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST)
        {

            if(resultCode == RESULT_OK)
            {

                final Place place = PlacePicker.getPlace(mContext, data);
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                String attributions = (String) place.getAttributions();
                if (attributions == null) {
                    attributions = "";
                }


                location.setText("");
                location.setText(name);
                location.setError(null);
                //  mAddress.setText(address);
                //  mAttributions.setText(Html.fromHtml(attributions));


            }
            else
            {
                Toast.makeText(mContext, "No location selected",
                        Toast.LENGTH_LONG).show();
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);

        intlPhoneInput.setDefault();
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

    @Override
    protected MultiPickerWrapper.PickerUtilListener getMultiPickerWrapperListener() {
        return multiPickerWrapperListener;
    }

    MultiPickerWrapper.PickerUtilListener multiPickerWrapperListener = new MultiPickerWrapper.PickerUtilListener() {
        @Override
        public void onPermissionDenied() {
            // do something here
        }

        @Override
        public void onImagesChosen(List<ChosenImage> list) {
            controls.setVisibility(View.GONE);
            String imagePath = list.get(0).getOriginalPath();
            profilePic.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }

        @Override
        public void onVideosChosen(List<ChosenVideo> list) {

        }

        @Override
        public void onError(String s) {
            Toast.makeText(mContext, "Error choosing image", Toast.LENGTH_SHORT).show();
            Log.d("PatientAccForFieldAgent", s);
        }
    };

    public boolean validate() {
        boolean validated = true;

        if (profilePic.getDrawable() == null) {
            clickedPatient.setPicture(null);
        }
        else {
            Bitmap bitmap = ((RoundedDrawable)profilePic.getDrawable()).getSourceBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayImage = baos.toByteArray();
            final String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

            clickedPatient.setPicture(encodedImage);
        }
        if (profilePic.getDrawable() == null) {
            clickedPatient.setPicture(null);
        } else {
            Bitmap bitmap = ((RoundedDrawable) profilePic.getDrawable()).getSourceBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayImage = baos.toByteArray();
            String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

            clickedPatient.setPicture(encodedImage);
        }

        if (TextUtils.isEmpty(firstName.getText())) {
            firstName.setError(getString(R.string.error_field_required));
            validated = false;
        } else {
            clickedPatient.setFirstname(firstName.getText().toString().trim());
        }
        if (TextUtils.isEmpty(lastName.getText())) {
            lastName.setError(getString(R.string.error_field_required));
            validated = false;
        } else {
            clickedPatient.setLastname(lastName.getText().toString().trim());
        }
        if (title.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) title.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        } else {
            clickedPatient.setTitle(title.getSelectedItem().toString());
        }
        if (gender.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) gender.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        } else {
            clickedPatient.setGender(gender.getSelectedItem().toString());
        }
        if (TextUtils.isEmpty(otherNames.getText())) {
            clickedPatient.setOthername(null);
        } else {
            clickedPatient.setOthername(otherNames.getText().toString().trim());
        }
        if (!intlPhoneInput.isValid()) {
            Toast.makeText(mContext, "Invalid number!", Toast.LENGTH_SHORT).show();
            validated = false;
        } else {
            clickedPatient.setContact(intlPhoneInput.getNumber());
        }
        if (TextUtils.isEmpty(userId.getText())) {
            userId.setError(getString(R.string.error_field_required));
            validated = false;
        } else {
            userId.setError(null);
            clickedPatient.setPatientid(userId.getText().toString().trim());
        }
        if (TextUtils.isEmpty(location.getText())) {
            location.setError(getString(R.string.error_field_required));
            validated = false;
        } else {
            clickedPatient.setLocation(location.getText().toString().trim());
        }
        if (account_type_spinner.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) account_type_spinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        } else {
            clickedPatient.setAccounttype(account_type_spinner.getSelectedItem().toString());
        }
        return validated;
    }

    public void init() {
        if (action.equals("edit")) {
            String encoded_picture = clickedPatient.getPicture();
            if (encoded_picture != null) {
                byte[] decodedBytes = Base64.decode(encoded_picture, 0);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                profilePic.setImageBitmap(decodedBitmap);

            } else {
                profilePic.setImageBitmap(null);
            }
            title.setSelection(((ArrayAdapter) title.getAdapter()).getPosition(clickedPatient.getTitle()));
            firstName.setText(clickedPatient.getFirstname());
            lastName.setText(clickedPatient.getLastname());
            otherNames.setText(clickedPatient.getOthername());
            gender.setSelection(((ArrayAdapter) gender.getAdapter()).getPosition(clickedPatient.getGender()));
            location.setText(clickedPatient.getLocation());
            intlPhoneInput.setNumber(clickedPatient.getContact());
            account_type_spinner.setSelection(((ArrayAdapter) account_type_spinner.getAdapter()).getPosition(clickedPatient.getAccounttype()));

            userId.setText(clickedPatient.getPatientid());
            generateID.setVisibility(View.GONE);
        } else if (action.equals("add")) {
            clickedPatient = new RealmPatient();

            generateID.setVisibility(View.VISIBLE);
        }
    }

    private UCrop.Options imgOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setStatusBarColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        options.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        options.setCropFrameColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        options.setCropFrameStrokeWidth(PixelUtil.dpToPx(mContext, 4));
        options.setCropGridColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        options.setCropGridStrokeWidth(PixelUtil.dpToPx(mContext, 2));
        options.setActiveWidgetColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        options.setToolbarTitle("Crop Image");

        // set rounded cropping guide
        options.setCircleDimmedLayer(true);
        return options;
    }

//    public void updateRecords() {
//
//        final ProgressDialog progressDialog = new ProgressDialog(AddPatientByFieldAgenActivity.this);
//        progressDialog.setTitle("Updating Record");
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        Patient patient = new Patient(
//                clickedPatient.getId(),
//                clickedPatient.getPatientid(),
//                clickedPatient.getPicture(),
//                clickedPatient.getTitle(),
//                clickedPatient.getFirstname(),
//                clickedPatient.getLastname(),
//                clickedPatient.getOthername(),
//                clickedPatient.getGender(),
//                clickedPatient.getDateofbirth(),
//                clickedPatient.getMaritalstatus(),
//                clickedPatient.getContact(),
//                clickedPatient.getAddress(),
//                clickedPatient.getLocation(),
//                clickedPatient.getOccupation(),
//                clickedPatient.getNextofkintitle(),
//                clickedPatient.getNextofkinfirstname(),
//                clickedPatient.getNextofkinlastname(),
//                clickedPatient.getNextofkinothername(),
//                clickedPatient.getNextofkingender(),
//                clickedPatient.getNextofkindateofbirth(),
//                clickedPatient.getNextofkinmaritalstatus(),
//                clickedPatient.getNextofkincontact(),
//                clickedPatient.getNextofkinaddress(),
//                clickedPatient.getNextofkinlocation(),
//                clickedPatient.getNhisnumber(),
//                clickedPatient.getNhispicture(),
//                clickedPatient.getAbobloodgroup(),
//                clickedPatient.getSicklecellbloodgroup(),
//                clickedPatient.getAllergies(),
//                clickedPatient.getSpecializedcondition(),
//                clickedPatient.getAccounttype(),
//                clickedPatient.getActive(),
//                clickedPatient.getSmssent(),
//                1
//        );
//        UpdatePatientSingleton singleton = new UpdatePatientSingleton();
//        Call<Patient> recordCall = singleton.updateRecordsInterface().update(clickedPatient.getPatientid(), patient);
//
//        recordCall.enqueue(new Callback<Patient>() {
//            @Override
//            public void onResponse(Call<Patient> call, final Response<Patient> response) {
//                progressDialog.dismiss();
//                if (response.isSuccessful()) {
//                    Patient patient = response.body();
//
////                    final RealmPatient realmPatient_callback = new RealmPatient(
////                            patient.getPatientid(),
////                            patient.getPicture(),
////                            patient.getTitle(),
////                            patient.getFirstname(),
////                            patient.getLastname(),
////                            patient.getOthername(),
////                            patient.getGender(),
////                            patient.getDateofbirth(),
////                            patient.getMaritalstatus(),
////                            patient.getContact(),
////                            patient.getAddress(),
////                            patient.getLocation(),
////                            patient.getOccupation(),
////                            patient.getNextofkintitle(),
////                            patient.getNextofkinfirstname(),
////                            patient.getNextofkinlastname(),
////                            patient.getNextofkinothername(),
////                            patient.getNextofkingender(),
////                            patient.getNextofkindateofbirth(),
////                            patient.getNextofkinmaritalstatus(),
////                            patient.getNextofkincontact(),
////                            patient.getNextofkinaddress(),
////                            patient.getNextofkinlocation(),
////                            patient.getNhisnumber(),
////                            patient.getNhispicture(),
////                            patient.getAbobloodgroup(),
////                            patient.getSicklecellbloodgroup(),
////                            patient.getAllergies(),
////                            patient.getSpecializedcondition(),
////                            patient.getAccounttype(),
////                            patient.getActive(),
////                            1,
////                            1,
////                            patient.getCreated_at(),
////                            patient.getUpdated_at()
////                    );
//                    Const.showToast(getApplicationContext(), "Data Updated Successfully");
//
//                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
//                        @Override
//                        public void execute(Realm realm) {
//                            clickedPatient.setUpdated_at(response.body().getUpdated_at());
//                            realm.copyToRealmOrUpdate(clickedPatient);
//                        }
//                    });
//                } else {
//                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
//                        @Override
//                        public void execute(Realm realm) {
//                            clickedPatient.setSmssent(0);
//                            realm.copyToRealmOrUpdate(clickedPatient);
//                        }
//                    });
//                    Log.d("mosquito", response.message());
//                }
//            }
//
//            private void updateRealmRecord(RealmPatient clickedPatient, int i) {
//            }
//
//            @Override
//            public void onFailure(Call<Patient> call, Throwable t) {
//                progressDialog.dismiss();
//
//                Const.showToast(getApplicationContext(), "You may not have internet connection");
//
//            }
//        });
//
//    }
//
//    public void saveToRecords() {
//
//        final ProgressDialog progressDialog = new ProgressDialog(AddPatientByFieldAgenActivity.this);
//        progressDialog.setTitle("Adding Record");
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//
//        PatientSingleton singleton = new PatientSingleton();
//        Call<Patient> recordCall = singleton.getPatientInterface().addRecord(
//                clickedPatient.getPatientid(),
//                clickedPatient.getPicture(),
//                clickedPatient.getTitle(),
//                clickedPatient.getFirstname(),
//                clickedPatient.getLastname(),
//                clickedPatient.getOthername(),
//                clickedPatient.getGender(),
//                clickedPatient.getDateofbirth(),
//                clickedPatient.getMaritalstatus(),
//                clickedPatient.getContact(),
//                clickedPatient.getAddress(),
//                clickedPatient.getLocation(),
//                clickedPatient.getOccupation(),
//                clickedPatient.getNextofkintitle(),
//                clickedPatient.getNextofkinfirstname(),
//                clickedPatient.getNextofkinlastname(),
//                clickedPatient.getNextofkinothername(),
//                clickedPatient.getNextofkingender(),
//                clickedPatient.getNextofkindateofbirth(),
//                clickedPatient.getNextofkinmaritalstatus(),
//                clickedPatient.getNextofkincontact(),
//                clickedPatient.getNextofkinaddress(),
//                clickedPatient.getNextofkinlocation(),
//                clickedPatient.getNhisnumber(),
//                clickedPatient.getNhispicture(),
//                clickedPatient.getAbobloodgroup(),
//                clickedPatient.getSicklecellbloodgroup(),
//                clickedPatient.getAllergies(),
//                clickedPatient.getSpecializedcondition(),
//                clickedPatient.getAccounttype(),
//                clickedPatient.getActive(),
//                0,
//                1
//        );
//
//        recordCall.enqueue(new Callback<Patient>() {
//            @Override
//            public void onResponse(Call<Patient> call, Response<Patient> response) {
//                progressDialog.dismiss();
//                if (response.isSuccessful()) {
//                    Patient patient = response.body();
//
//                    RealmPatient realmPatient = new RealmPatient(
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
//                            patient.getSmssent(),
//                            1,
//                            patient.getCreated_at(),
//                            patient.getUpdated_at()
//                    );
//                    Toast.makeText(AddPatientByFieldAgenActivity.this, "Data successfully saved!", Toast.LENGTH_SHORT).show();
//                    saveToRealm(realmPatient, 0);
//
//                    //Const.showToast(getApplicationContext(), "Data Added Successfully");
//                /*}else if (response.code() == 403){
//                    Const.showToast(getApplicationContext(),"Data Already Inserted");*/
//                } else {
//                    clickedPatient.setSynced(0);
//                    saveToRealm(clickedPatient, 1);
//                    Log.d("mosquito", response.errorBody().byteStream().toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Patient> call, Throwable t) {
//                progressDialog.dismiss();
//                Const.showToast(getApplicationContext(), "You may not have internet connection");
//
//            }
//        });
//
//    }
//
//    public void saveToRealm(final RealmPatient realmPatient, final int action) {
//        final Realm realm = Realm.getDefaultInstance();
//
//        realm.executeTransactionAsync(new Realm.Transaction() {
//                                          @Override
//                                          public void execute(Realm mRealm) {
//                                              // mRealm.createObject(RealmPatient.class, realmPatient);
//                                              Number num = mRealm.where(RealmPatient.class).max("id");
//                                              int id = 0;
//                                              if (num == null) {
//                                                  id = 1;
//                                              } else {
//                                                  id = num.intValue() + 1;
//                                              }
//                                              realmPatient.setId(id);
//                                              mRealm.copyToRealmOrUpdate(realmPatient);
//                                          }
//                                      },
//                new Realm.Transaction.OnSuccess() {
//                    @Override
//                    public void onSuccess() {
//                        if (action == 1) {
//                            Toast.makeText(mContext, "Data successfully saved!", Toast.LENGTH_SHORT).show();
//                        }
//
//                        realm.close();
//                    }
//                }, new Realm.Transaction.OnError() {
//                    @Override
//                    public void onError(Throwable error) {
//                        Const.showToast(mContext, error.getMessage());
//                        Log.d("asd", error.toString());
//                    }
//                }
//        );
//    }

    public void clear() {
        profilePic.setImageBitmap(null);
        title.setSelection(0);
        firstName.setText(null);
        lastName.setText(null);
        otherNames.setText(null);
        gender.setSelection(0);
        location.setText(null);
        ((EditText) ((LinearLayout) intlPhoneInput.getChildAt(0)).getChildAt(1)).setText("");
    }
}
