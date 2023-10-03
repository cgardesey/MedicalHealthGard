package com.prepeez.medicalhealthguard.fragment.healthRecord;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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

public class TabFrag5 extends Fragment {
    private static final String TAG = "TabFrag5";
    private EditText roc, doc, termPregnancies, mensturalPeriod, prematureBirth, liveBirth;
    private Spinner flow_spinner, pain_spinner, gestation_spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.frag_tab5, container, false);

        roc = rootView.findViewById(R.id.roc);
        doc = rootView.findViewById(R.id.doc);
        termPregnancies = rootView.findViewById(R.id.termPregnancies);
        mensturalPeriod = rootView.findViewById(R.id.mensturalPeriod);
        prematureBirth = rootView.findViewById(R.id.prematureBirth);
        liveBirth = rootView.findViewById(R.id.liveBirth);
        flow_spinner = rootView.findViewById(R.id.flow_spinner);
        pain_spinner = rootView.findViewById(R.id.pain_spinner);
        gestation_spinner = rootView.findViewById(R.id.gestation_spinner);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void init() {
        if (action_helth_rec.equals("view")) {
            roc.setText(clickedHealthRecord.getRegulariyofcycle());
            doc.setText(clickedHealthRecord.getDurationofcycle());
            termPregnancies.setText(clickedHealthRecord.getTermpregnancies());
            mensturalPeriod.setText(clickedHealthRecord.getMenstralperiod());
            prematureBirth.setText(clickedHealthRecord.getPrematurebirth());
            liveBirth.setText(clickedHealthRecord.getLivebirth());

            flow_spinner.setSelection(((ArrayAdapter)flow_spinner.getAdapter()).getPosition(clickedHealthRecord.getFlow()));
            pain_spinner.setSelection(((ArrayAdapter)pain_spinner.getAdapter()).getPosition(clickedHealthRecord.getPain()));
            gestation_spinner.setSelection(((ArrayAdapter)gestation_spinner.getAdapter()).getPosition(clickedHealthRecord.getMultiplegestations()));

            roc.setEnabled(false);
            doc.setEnabled(false);
            termPregnancies.setEnabled(false);
            mensturalPeriod.setEnabled(false);
            prematureBirth.setEnabled(false);
            liveBirth.setEnabled(false);

            flow_spinner.setEnabled(false);
            pain_spinner.setEnabled(false);
            gestation_spinner.setEnabled(false);

        }
        else if (action_helth_rec.equals("add")){
            clickedHealthRecord = new RealmHealthRecord();
            clickedHealthRecord.setHealthrecordid(longRandomAlphaNumeric(12));
        }
    }

    public boolean validate() {
        boolean validated = true;

        if (TextUtils.isEmpty(roc.getText())) {
            clickedHealthRecord.setRegulariyofcycle(null);
        } else {
            clickedHealthRecord.setRegulariyofcycle(roc.getText().toString().trim());
        }
        if (TextUtils.isEmpty(doc.getText())) {
            clickedHealthRecord.setDurationofcycle(null);
        } else {
            clickedHealthRecord.setDurationofcycle(doc.getText().toString().trim());
        }
        if (TextUtils.isEmpty(termPregnancies.getText())) {

            clickedHealthRecord.setTermpregnancies(null);

        } else {

            clickedHealthRecord.setTermpregnancies(termPregnancies.getText().toString().trim());

        }
        if (TextUtils.isEmpty(mensturalPeriod.getText())) {

            clickedHealthRecord.setMenstralperiod(null);

        } else {

            clickedHealthRecord.setMenstralperiod(mensturalPeriod.getText().toString().trim());

        }
        if (TextUtils.isEmpty(prematureBirth.getText())) {

            clickedHealthRecord.setPrematurebirth(null);

        } else {

            clickedHealthRecord.setPrematurebirth(prematureBirth.getText().toString().trim());

        }
        if (TextUtils.isEmpty(liveBirth.getText())) {

            clickedHealthRecord.setLivebirth(null);

        } else {

            clickedHealthRecord.setLivebirth(liveBirth.getText().toString().trim());

        }
        clickedHealthRecord.setFlow(flow_spinner.getSelectedItem().toString());
        clickedHealthRecord.setPain(pain_spinner.getSelectedItem().toString());
        clickedHealthRecord.setMultiplegestations(gestation_spinner.getSelectedItem().toString());

        return validated;
    }

    public void clear() {
        roc.setText(null);
        doc.setText(null);
        termPregnancies.setText(null);
        mensturalPeriod.setText(null);
        prematureBirth.setText(null);
        liveBirth.setText(null);
        flow_spinner.setSelection(0);
        pain_spinner.setSelection(0);
        gestation_spinner.setSelection(0);
    }
}