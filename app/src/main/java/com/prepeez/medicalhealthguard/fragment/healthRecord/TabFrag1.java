package com.prepeez.medicalhealthguard.fragment.healthRecord;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.realm.RealmHealthRecord;
import com.prepeez.medicalhealthguard.realm.RealmPatient;

import static com.prepeez.medicalhealthguard.adapter.HealthRecordAdapter.action_helth_rec;
import static com.prepeez.medicalhealthguard.adapter.HealthRecordAdapter.clickedHealthRecord;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.constants.Const.longRandomAlphaNumeric;

import androidx.fragment.app.Fragment;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class TabFrag1 extends Fragment {
    private static final String TAG = "TabFrag1";
    private TextView page;
    private EditText weight, height, pulse, systolicBp, diastolicBp, bodyTemp;
    private TextView bmi;

    public static float BMI = 0.0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.frag_tab1, container, false);

        page = rootView.findViewById(R.id.page);

        weight = rootView.findViewById(R.id.weight);
        height = rootView.findViewById(R.id.height);
        pulse = rootView.findViewById(R.id.pulse);
        systolicBp = rootView.findViewById(R.id.systolicBp);
        diastolicBp = rootView.findViewById(R.id.diastolicBp);
        bodyTemp = rootView.findViewById(R.id.bodyTemp);
        bmi = rootView.findViewById(R.id.bmi);

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                calculateBMI();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                calculateBMI();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (clickedPatient.getGender().equals("Female")) {
            page.setText("1 of 6");
        }
        else {
            page.setText("1 of 5");
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void init() {
        if (action_helth_rec.equals("view")) {
            weight.setText(clickedHealthRecord.getWeight());
            height.setText(clickedHealthRecord.getHeight());
            pulse.setText(clickedHealthRecord.getHeartrate());
            systolicBp.setText(clickedHealthRecord.getSystolicbp());
            diastolicBp.setText(clickedHealthRecord.getDiastolicbp());
            bodyTemp.setText(clickedHealthRecord.getBodytemperature());

            weight.setEnabled(false);
            height.setEnabled(false);
            pulse.setEnabled(false);
            systolicBp.setEnabled(false);
            diastolicBp.setEnabled(false);
            bodyTemp.setEnabled(false);
        }
        else if (action_helth_rec.equals("add")){
            clickedHealthRecord = new RealmHealthRecord();
            clickedHealthRecord.setHealthrecordid(longRandomAlphaNumeric(12));
        }
    }

    public boolean validate() {
        boolean validated = true;

        if (TextUtils.isEmpty(weight.getText())) {
            clickedHealthRecord.setWeight(null);
        } else {
            clickedHealthRecord.setWeight(weight.getText().toString().trim());
        }
        if (TextUtils.isEmpty(height.getText())) {
            clickedHealthRecord.setHeight(null);
        } else {
            clickedHealthRecord.setHeight(height.getText().toString().trim());
        }
        if (TextUtils.isEmpty(pulse.getText())) {

            clickedHealthRecord.setHeartrate(null);

        } else {

            clickedHealthRecord.setHeartrate(pulse.getText().toString().trim());

        }
        if (TextUtils.isEmpty(systolicBp.getText())) {

            clickedHealthRecord.setSystolicbp(null);

        } else {

            clickedHealthRecord.setSystolicbp(systolicBp.getText().toString().trim());

        }
        if (TextUtils.isEmpty(diastolicBp.getText())) {

            clickedHealthRecord.setDiastolicbp(null);

        } else {

            clickedHealthRecord.setDiastolicbp(diastolicBp.getText().toString().trim());

        }

        return validated;
    }

//    public void init() {
//        weight.setText(clickedHealthRecord.getNextofkinothername());
//        height.setText(clickedHealthRecord.getNextofkinothername());
//        pulse.setText(clickedHealthRecord.getNextofkinothername());
//        systolicBp.setText(clickedHealthRecord.getNextofkinothername());
//        diastolicBp.setText(clickedHealthRecord.getNextofkinothername());
//        bodyTemp.setText(clickedHealthRecord.getNextofkinothername());
//        bmi.setText(null);
//    }

    public void clear() {
        weight.setText(null);
        height.setText(null);
        pulse.setText(null);
        systolicBp.setText(null);
        diastolicBp.setText(null);
        bodyTemp.setText(null);
        bmi.setText(null);
    }

    private void calculateBMI() {
        String WEIGHT = weight.getText().toString().trim();
        String HEIGHT = height.getText().toString().trim();

        if (!(TextUtils.isEmpty(HEIGHT)) && !(TextUtils.isEmpty(WEIGHT))) {

            float H = Float.parseFloat(HEIGHT);
            float W = Float.parseFloat(WEIGHT);

            BMI = W / (H * H);

            bmi.setText(Float.toString(BMI));
        } else {
            bmi.setText(null);
        }
    }
}