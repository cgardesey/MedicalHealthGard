package com.prepeez.medicalhealthguard.fragment.misagent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.activity.HealthRecordActivity;
import com.prepeez.medicalhealthguard.activity.PatientActivity;
import com.prepeez.medicalhealthguard.realm.RealmPatient;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.prepeez.medicalhealthguard.activity.PatientActivity.PATIENT_FRAGMENT;
import static com.prepeez.medicalhealthguard.activity.PatientActivity.search;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.action;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.constants.Const.randomAlphaNumeric;

import androidx.fragment.app.Fragment;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class TabFragment7 extends Fragment {
    public static final int RC_GROUP_ID = 88;
    FloatingActionButton fab;
    EditText userId, groupId;
    Button generateId;
    private Spinner account_type_spinner;
    private Spinner group_type_spinner;

    SendMessage SM;
    private String my_var;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab7, container, false);

        userId = rootView.findViewById(R.id.userId);
        groupId = rootView.findViewById(R.id.groupId);
        generateId = rootView.findViewById(R.id.generateID);
        account_type_spinner = rootView.findViewById(R.id.account_type_spinner);
        group_type_spinner = rootView.findViewById(R.id.group_type_spinner);

        generateId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId.setError(null);
                userId.setText(randomAlphaNumeric(6));
            }
        });

        groupId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search = true;
                Intent intent = new Intent(getActivity(), PatientActivity.class);
                startActivityForResult(intent, RC_GROUP_ID);
            }
        });

        fab = rootView.findViewById(R.id.done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SM.sendData();
            }
        });

        group_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                PATIENT_FRAGMENT = parent.getItemAtPosition(pos).toString();
                if (pos > 1) {
                    groupId.setVisibility(View.VISIBLE);
                } else {
                    groupId.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootView;
    }

    public void init() {
        if (action.equals("edit")) {
            userId.setText(clickedPatient.getPatientid());
            groupId.setText(clickedPatient.getGroupid());
            generateId.setVisibility(View.GONE);
            account_type_spinner.setSelection(((ArrayAdapter) account_type_spinner.getAdapter()).getPosition(clickedPatient.getAccounttype()));
            group_type_spinner.setSelection(((ArrayAdapter) group_type_spinner.getAdapter()).getPosition(clickedPatient.getGrouptype()));
        }

        if (clickedPatient.getPatientid() == null) {
            generateId.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_GROUP_ID:
                switch (resultCode) {
                    case RESULT_OK:
                        groupId.setError(null);
                        String contact = data.getStringExtra("contact");
                        groupId.setText(contact);
                        break;

                    case RESULT_CANCELED:

                        break;
                }
                break;

            default:
                break;

        }
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

    public boolean validate() {
        boolean validated = true;
        if (TextUtils.isEmpty(userId.getText())) {

            userId.setError(getString(R.string.error_field_required));
            validated = false;
        } else {
            userId.setError(null);
            clickedPatient.setPatientid(userId.getText().toString().trim());
        }
        if (groupId.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(groupId.getText())) {

                groupId.setError(getString(R.string.error_field_required));
                validated = false;
            } else {
                groupId.setError(null);
                clickedPatient.setGroupid(groupId.getText().toString().trim());
            }
        } else {
            if (TextUtils.isEmpty(groupId.getText())) {
                clickedPatient.setGroupid("");
            } else {
                clickedPatient.setGroupid(groupId.getText().toString().trim());
            }
        }
        if (account_type_spinner.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) account_type_spinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        } else {

            clickedPatient.setAccounttype(account_type_spinner.getSelectedItem().toString());

        }
        if (group_type_spinner.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) group_type_spinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        } else {

            clickedPatient.setGrouptype(group_type_spinner.getSelectedItem().toString());

        }
        return validated;
    }

    public void clear() {
        account_type_spinner.setSelection(0);
        group_type_spinner.setSelection(0);
        userId.setText(null);
        groupId.setText(null);
    }
}