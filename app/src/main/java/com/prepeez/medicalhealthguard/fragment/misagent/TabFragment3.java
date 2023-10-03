package com.prepeez.medicalhealthguard.fragment.misagent;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.activity.AddPatientByMisActivity;
import com.prepeez.medicalhealthguard.realm.RealmPatient;

import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.action;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.constants.Const.retrieveGSON;
import static com.prepeez.medicalhealthguard.constants.Const.storeGSON;

import androidx.fragment.app.Fragment;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class TabFragment3 extends Fragment {
    private static final String TAG = "TabFragment3";
    private EditText firstName, lastName, otherNames;
    private Spinner title, gender;
    Context mContext;
    private ImageView opendate;

    private DatePicker datePicker;
    private EditText eventlocation;
    private Button pickplace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);

        //

        firstName = rootView.findViewById(R.id.first_name);
        lastName = rootView.findViewById(R.id.last_name);
        otherNames = rootView.findViewById(R.id.other_names);
        title = rootView.findViewById(R.id.title_spinner);
        gender = rootView.findViewById(R.id.gender_spinner);
        opendate = rootView.findViewById(R.id.dateselect);

        mContext = getContext();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void init() {
        if (action.equals("edit")) {
            title.setSelection(((ArrayAdapter)title.getAdapter()).getPosition(clickedPatient.getNextofkintitle()));
            firstName.setText(clickedPatient.getNextofkinfirstname());
            lastName.setText(clickedPatient.getNextofkinfirstname());
            otherNames.setText(clickedPatient.getNextofkinothername());
            gender.setSelection(((ArrayAdapter)gender.getAdapter()).getPosition(clickedPatient.getNextofkingender()));
        }
    }

    public boolean validate (){
        boolean validated = true;

        if(TextUtils.isEmpty(firstName.getText())){
            firstName.setError(getString(R.string.error_field_required));
            validated = false;
        }
        else {

            clickedPatient.setNextofkinfirstname(firstName.getText().toString().trim());

        }
        if( TextUtils.isEmpty(lastName.getText())){
            lastName.setError(getString(R.string.error_field_required));
            validated = false;
        }
        else {

            clickedPatient.setNextofkinlastname(lastName.getText().toString().trim());

        }
        if( title.getSelectedItemPosition() == 0){
            TextView errorText = (TextView)title.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        }
        else {

            clickedPatient.setNextofkintitle(title.getSelectedItem().toString());

        }
        if( gender.getSelectedItemPosition() == 0){
            TextView errorText = (TextView)gender.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        }
        else {

            clickedPatient.setNextofkingender(gender.getSelectedItem().toString());

        }
        clickedPatient.setNextofkinothername(otherNames.getText().toString().trim());
        return validated;
    }

    public RealmPatient getPatient() {
        return clickedPatient;
    }

    public void clear() {
        title.setSelection(0);
        firstName.setText(null);
        lastName.setText(null);
        otherNames.setText(null);
        gender.setSelection(0);
    }
}