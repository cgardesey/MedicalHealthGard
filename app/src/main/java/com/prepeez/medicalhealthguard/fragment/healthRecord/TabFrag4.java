package com.prepeez.medicalhealthguard.fragment.healthRecord;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.realm.RealmHealthRecord;

import static com.prepeez.medicalhealthguard.adapter.HealthRecordAdapter.action_helth_rec;
import static com.prepeez.medicalhealthguard.adapter.HealthRecordAdapter.clickedHealthRecord;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.constants.Const.longRandomAlphaNumeric;

import androidx.fragment.app.Fragment;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class TabFrag4 extends Fragment {
    private static final String TAG = "TabFrag4";
    private TextView page;
    private EditText fbc, mps, ltf, widal, kft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.frag_tab4, container, false);

        page = rootView.findViewById(R.id.page);

        fbc = rootView.findViewById(R.id.fbc);
        mps = rootView.findViewById(R.id.mps);
        ltf = rootView.findViewById(R.id.ltf);
        widal = rootView.findViewById(R.id.widal);
        kft = rootView.findViewById(R.id.kft);

        if (clickedPatient.getGender().equals("Female")) {
            page.setText("4 of 6");
        }
        else {
            page.setText("4 of 5");
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
            fbc.setText(clickedHealthRecord.getFbc());
            mps.setText(clickedHealthRecord.getMps());
            ltf.setText(clickedHealthRecord.getLtf());
            widal.setText(clickedHealthRecord.getWidal());
            kft.setText(clickedHealthRecord.getKft());

            fbc.setEnabled(false);
            mps.setEnabled(false);
            ltf.setEnabled(false);
            widal.setEnabled(false);
            kft.setEnabled(false);
        }
        else if (action_helth_rec.equals("add")){
            clickedHealthRecord = new RealmHealthRecord();
            clickedHealthRecord.setHealthrecordid(longRandomAlphaNumeric(12));
        }
    }

    public boolean validate() {
        boolean validated = true;

        if (TextUtils.isEmpty(fbc.getText())) {
            clickedHealthRecord.setFbc(null);
        } else {
            clickedHealthRecord.setFbc(fbc.getText().toString().trim());
        }
        if (TextUtils.isEmpty(mps.getText())) {
            clickedHealthRecord.setMps(null);
        } else {
            clickedHealthRecord.setMps(mps.getText().toString().trim());
        }
        if (TextUtils.isEmpty(ltf.getText())) {
            clickedHealthRecord.setLtf(null);
        } else {
            clickedHealthRecord.setLtf(ltf.getText().toString().trim());
        }
        if (TextUtils.isEmpty(widal.getText())) {

            clickedHealthRecord.setWidal(null);

        } else {

            clickedHealthRecord.setWidal(widal.getText().toString().trim());

        }
        if (TextUtils.isEmpty(kft.getText())) {

            clickedHealthRecord.setKft(null);

        } else {

            clickedHealthRecord.setKft(widal.getText().toString().trim());

        }

        return validated;
    }

    public void clear() {
        fbc.setText(null);
        mps.setText(null);
        ltf.setText(null);
        widal.setText(null);
        kft.setText(null);
    }
}