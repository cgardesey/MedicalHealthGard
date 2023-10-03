package com.prepeez.medicalhealthguard.fragment.healthRecord;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.realm.RealmHealthRecord;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.prepeez.medicalhealthguard.adapter.HealthRecordAdapter.action_helth_rec;
import static com.prepeez.medicalhealthguard.adapter.HealthRecordAdapter.clickedHealthRecord;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.constants.Const.longRandomAlphaNumeric;

import androidx.fragment.app.Fragment;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class TabFrag6 extends Fragment {
    private static final String TAG = "TabFrag6";
    private TextView page;
    private EditText diffDiag, diag, treatment;
    private TextView nextvisit;
    ImageView opendate;
    FloatingActionButton fab;
    private int mYear, mMonth, mDay, mHour, mMinute;

    SendMessage SM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.frag_tab6, container, false);

        page = rootView.findViewById(R.id.page);

        diffDiag = rootView.findViewById(R.id.diffDiag);
        diag = rootView.findViewById(R.id.diag);
        treatment = rootView.findViewById(R.id.treatment);
        nextvisit = rootView.findViewById(R.id.nextvisit);
        opendate = rootView.findViewById(R.id.opendate);

        fab = rootView.findViewById(R.id.done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SM.sendData();
            }
        });

        opendate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDate(1980, 0, 1, R.style.DatePickerSpinner);

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                                nextvisit.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                nextvisit.setError(null);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

        if (clickedPatient.getGender().equals("Female")) {
            page.setText("6 of 6");
        }
        else {
            page.setText("5 of 5");
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
            diffDiag.setText(clickedHealthRecord.getDifferentialdiagnosis());
            diag.setText(clickedHealthRecord.getDiagnosis());
            treatment.setText(clickedHealthRecord.getTreatment());
            nextvisit.setText(clickedHealthRecord.getNextscheduledvisit());

            diffDiag.setEnabled(false);
            diag.setEnabled(false);
            treatment.setEnabled(false);
            opendate.setEnabled(false);

            fab.setVisibility(View.GONE);
        }
        else if (action_helth_rec.equals("add")){
            clickedHealthRecord = new RealmHealthRecord();
            clickedHealthRecord.setHealthrecordid(longRandomAlphaNumeric(12));
        }
    }

    public boolean validate() {
        boolean validated = true;

        if (TextUtils.isEmpty(diffDiag.getText())) {
            clickedHealthRecord.setDifferentialdiagnosis(null);
        } else {
            clickedHealthRecord.setDifferentialdiagnosis(diffDiag.getText().toString().trim());
        }
        if (TextUtils.isEmpty(diag.getText())) {
            clickedHealthRecord.setDiagnosis(null);
        } else {
            clickedHealthRecord.setDiagnosis(diag.getText().toString().trim());
        }
        if (TextUtils.isEmpty(treatment.getText())) {

            clickedHealthRecord.setTreatment(null);

        } else {

            clickedHealthRecord.setTreatment(treatment.getText().toString().trim());

        }
        if( TextUtils.isEmpty(nextvisit.getText())){
            nextvisit.setError(getString(R.string.error_field_required));
            validated = false;
        }
        else {

            clickedHealthRecord.setNextscheduledvisit(nextvisit.getText().toString().trim());

        }

        return validated;
    }

    public void clear() {
        diffDiag.setText(null);
        diag.setText(null);
        treatment.setText(null);
        nextvisit.setText(null);

    }

    public interface SendMessage {
        void sendData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendMessage) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }
}