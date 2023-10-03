package com.prepeez.medicalhealthguard.fragment.healthRecord;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class TabFrag3 extends Fragment {
    private static final String TAG = "TabFrag3";
    private TextView page;
    private EditText cs, rs, gs, cns, dre, ve, oef;
    private LinearLayout veLayout, dreLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.frag_tab3, container, false);

        page = rootView.findViewById(R.id.page);

        cs = rootView.findViewById(R.id.cs);
        rs = rootView.findViewById(R.id.rs);
        gs = rootView.findViewById(R.id.gs);
        cns = rootView.findViewById(R.id.cns);
        dre = rootView.findViewById(R.id.dre);
        ve = rootView.findViewById(R.id.ve);
        oef = rootView.findViewById(R.id.oef);
        veLayout = rootView.findViewById(R.id.veLayout);
        dreLayout = rootView.findViewById(R.id.dreLayout);

        if (clickedPatient.getGender().equals("Female")) {
            veLayout.setVisibility(View.VISIBLE);
            dreLayout.setVisibility(View.GONE);
        }
        else {
            veLayout.setVisibility(View.GONE);
            dreLayout.setVisibility(View.VISIBLE);
        }

        if (clickedPatient.getGender().equals("Female")) {
            page.setText("3 of 6");
        }
        else {
            page.setText("3 of 5");
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
            cs.setText(clickedHealthRecord.getCardiovascularsyst());
            rs.setText(clickedHealthRecord.getRespiratorysys());
            gs.setText(clickedHealthRecord.getGastrointestinalsyst());
            cns.setText(clickedHealthRecord.getCentralnervoussyst());
            dre.setText(clickedHealthRecord.getDigitalrectalexam());
            ve.setText(clickedHealthRecord.getVaginalexam());
            oef.setText(clickedHealthRecord.getOtherexamandfindings());

            cs.setEnabled(false);
            rs.setEnabled(false);
            gs.setEnabled(false);
            cns.setEnabled(false);
            dre.setEnabled(false);
            ve.setEnabled(false);
            oef.setEnabled(false);
        }
        else if (action_helth_rec.equals("add")){
            clickedHealthRecord = new RealmHealthRecord();
            clickedHealthRecord.setHealthrecordid(longRandomAlphaNumeric(12));
        }
    }

    public boolean validate() {
        boolean validated = true;

        if (TextUtils.isEmpty(cs.getText())) {
            clickedHealthRecord.setWeight(null);
        } else {
            clickedHealthRecord.setWeight(cs.getText().toString().trim());
        }
        if (TextUtils.isEmpty(rs.getText())) {
            clickedHealthRecord.setHeight(null);
        } else {
            clickedHealthRecord.setHeight(rs.getText().toString().trim());
        }
        if (TextUtils.isEmpty(gs.getText())) {

            clickedHealthRecord.setHeartrate(null);

        } else {

            clickedHealthRecord.setHeartrate(gs.getText().toString().trim());

        }
        if (clickedPatient.getGender().equals("Female")) {
            if (TextUtils.isEmpty(ve.getText())) {

                clickedHealthRecord.setVaginalexam(null);

            } else {

                clickedHealthRecord.setVaginalexam(ve.getText().toString().trim());

            }
        }
        else {
            if (TextUtils.isEmpty(dre.getText())) {

                clickedHealthRecord.setDigitalrectalexam(null);

            } else {

                clickedHealthRecord.setDigitalrectalexam(dre.getText().toString().trim());

            }
        }

        if (TextUtils.isEmpty(oef.getText())) {

            clickedHealthRecord.setOtherexamandfindings(null);

        } else {

            clickedHealthRecord.setOtherexamandfindings(oef.getText().toString().trim());

        }

        return validated;
    }

    public void clear() {
        cs.setText(null);
        rs.setText(null);
        gs.setText(null);
        cns.setText(null);
        oef.setText(null);
        if (clickedPatient.getGender().equals("Female")) {
            ve.setText(null);
        }
        else {
            dre.setText(null);
        }
    }
}