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

public class TabFrag2 extends Fragment {
    private static final String TAG = "TabFrag2";
    private TextView page;
    private EditText cpc, hpc, odq, gpef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.frag_tab2, container, false);

        page = rootView.findViewById(R.id.page);

        cpc = rootView.findViewById(R.id.cpc);
        hpc = rootView.findViewById(R.id.hpc);
        odq = rootView.findViewById(R.id.odq);
        gpef = rootView.findViewById(R.id.gpef);

        if (clickedPatient.getGender().equals("Female")) {
            page.setText("2 of 6");
        }
        else {
            page.setText("2 of 5");
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
            cpc.setText(clickedHealthRecord.getChiefpresentingcomplaint());
            hpc.setText(clickedHealthRecord.getHistoryofpreseningcomplaint());
            odq.setText(clickedHealthRecord.getOndiriectquestioning());
            gpef.setText(clickedHealthRecord.getGeneralphysicalexam());

            cpc.setEnabled(false);
            hpc.setEnabled(false);
            odq.setEnabled(false);
            gpef.setEnabled(false);
        }
        else if (action_helth_rec.equals("add")){
            clickedHealthRecord = new RealmHealthRecord();
            clickedHealthRecord.setHealthrecordid(longRandomAlphaNumeric(12));
        }
    }

    public boolean validate() {
        boolean validated = true;

        if (TextUtils.isEmpty(cpc.getText())) {
            clickedHealthRecord.setChiefpresentingcomplaint(null);
        } else {
            clickedHealthRecord.setChiefpresentingcomplaint(cpc.getText().toString().trim());
        }
        if (TextUtils.isEmpty(hpc.getText())) {
            clickedHealthRecord.setHistoryofpreseningcomplaint(null);
        } else {
            clickedHealthRecord.setHistoryofpreseningcomplaint(hpc.getText().toString().trim());
        }
        if (TextUtils.isEmpty(odq.getText())) {

            clickedHealthRecord.setOndiriectquestioning(null);

        } else {

            clickedHealthRecord.setOndiriectquestioning(odq.getText().toString().trim());

        }

        return validated;
    }

    public void clear() {
        cpc.setText(null);
        hpc.setText(null);
        odq.setText(null);
        gpef.setText(null);
    }
}