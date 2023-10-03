package com.prepeez.medicalhealthguard.fragment.misagent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.adapter.PhoneAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.makeramen.roundedimageview.RoundedImageView;
import com.prepeez.medicalhealthguard.realm.RealmPatient;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import net.rimoto.intlphoneinput.IntlPhoneInput;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.action;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.constants.Const.retrieveGSON;
import static com.prepeez.medicalhealthguard.constants.Const.storeGSON;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class TabFragment4 extends Fragment implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {
    private static final String TAG = "TabFragment4";
    LinearLayout parentLayout;
    TextView clicktoaddTextView;
    EditText emailAddress, homeAddress;
    EditText location;
    IntlPhoneInput intlPhoneInput;
    ImageView pickplace;
    ImageView opendate;
    RoundedImageView imageView;
    RecyclerView recyclerView;
    Spinner spinner, maritalstatus;
    PhoneAdapter phoneAdapter;
    TextView dateofbirth;
    Context mContext;
    SimpleDateFormat simpleDateFormat;
    public static int PLACE_PICKER_REQUEST = 100;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab4, container, false);


        //

        emailAddress = rootView.findViewById(R.id.emailaddress);
        homeAddress = rootView.findViewById(R.id.homeaddress);
        pickplace = rootView.findViewById(R.id.locPicker);
        mContext = getContext();
        opendate = rootView.findViewById(R.id.dateselect);
        imageView = rootView.findViewById(R.id.profile_imgview);
        dateofbirth = rootView.findViewById(R.id.dateofbirth);
        intlPhoneInput = rootView.findViewById(R.id.phonenumber);
        maritalstatus = rootView.findViewById(R.id.spinner3);
        recyclerView = rootView.findViewById(R.id.recyclerView);
//        final RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        location = rootView.findViewById(R.id.homeaddress);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        opendate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate(1980, 0, 1, R.style.DatePickerSpinner);
            }

        });

        pickplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build((Activity) mContext);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intlPhoneInput.setDefault();
        init();
    }
    
    public void init() {
        if (action.equals("edit")) {
            maritalstatus.setSelection(((ArrayAdapter)maritalstatus.getAdapter()).getPosition(clickedPatient.getNextofkinmaritalstatus()));
            dateofbirth.setText(clickedPatient.getDateofbirth());
            homeAddress.setText(clickedPatient.getNextofkinlocation());
            emailAddress.setText(clickedPatient.getNextofkinaddress());
            intlPhoneInput.setNumber(clickedPatient.getNextofkincontact());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // txtData = (TextView)view.findViewById(R.id.txtData);
    }

    public void displayReceivedData(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        // txtData.setText("Data received: "+message);
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

    public boolean validate (){
        boolean validated = true;
//        if( TextUtils.isEmpty(emailAddress.getText())){
//            emailAddress.setError(getString(R.string.error_field_required));
//            validated = false;
//        }
//        else {
//
//            clickedPatient.setNextofkinaddress(emailAddress.getText().toString().trim());
//
//        }
        if( TextUtils.isEmpty(dateofbirth.getText())){
            dateofbirth.setError(getString(R.string.error_field_required));
            validated = false;
        }
        else {

            clickedPatient.setNextofkindateofbirth(dateofbirth.getText().toString().trim());

        }
        if( TextUtils.isEmpty(homeAddress.getText())){
            homeAddress.setError(getString(R.string.error_field_required));
            validated = false;
        }
        else {

            clickedPatient.setNextofkinaddress(homeAddress.getText().toString().trim());

        }
        if( maritalstatus.getSelectedItemPosition() == 0){
            TextView errorText = (TextView)maritalstatus.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        }
        else {

            clickedPatient.setNextofkinmaritalstatus(maritalstatus.getSelectedItem().toString());

        }
//        if( TextUtils.isEmpty(intlPhoneInput.getText())){
//            Toast.makeText(mContext, "Phone number is required!", Toast.LENGTH_SHORT).show();
//            validated = false;
//        }
        if (!intlPhoneInput.isValid()) {
            Toast.makeText(mContext, "Invalid number!", Toast.LENGTH_SHORT).show();
            validated = false;
        }
        else {

            clickedPatient.setNextofkincontact(intlPhoneInput.getNumber());

        }
        if (!isEmailValid(emailAddress.getText().toString().trim())){
            emailAddress.setError("Invalid email!");
            validated = false;
        }
        else {

            clickedPatient.setNextofkinaddress(emailAddress.getText().toString().trim());

        }
        return validated;
    }

    public static boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    public RealmPatient getPatient() {
        return clickedPatient;
    }

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        dateofbirth.setText(simpleDateFormat.format(calendar.getTime()));
        dateofbirth.setError(null);
    }

    @VisibleForTesting
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(getContext())
                .callback(this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    public void clear() {
        emailAddress.setText(null);
        ((EditText) ((LinearLayout) intlPhoneInput.getChildAt(0)).getChildAt(1)).setText("");
        dateofbirth.setText(null);
        location.setText(null);
        maritalstatus.setSelection(0);
    }
}
