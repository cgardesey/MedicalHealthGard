package com.prepeez.medicalhealthguard.materialDialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.activity.HealthRecordActivity;
import com.prepeez.medicalhealthguard.fragment.patient.CompaniesPatientFragment;
import com.prepeez.medicalhealthguard.fragment.patient.FamiliesPatientFragment;
import com.prepeez.medicalhealthguard.realm.RealmPatient;

import java.util.ArrayList;

import io.realm.Realm;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.prepeez.medicalhealthguard.fragment.misagent.TabFragment7.RC_GROUP_ID;
import static com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment.isMultiSelect;
import static com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment.mActionMode;
import static com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment.multiselect_list;
import static com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment.populatePatients;
import static com.prepeez.medicalhealthguard.fragment.patient.IndividualsPatientFragment.refreshAdapter;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by Nana on 10/22/2017.
 */

public class CreateGroupMaterialDialog extends DialogFragment {
    private static final String TAG = "CreateGroupMaterialDialog";

    Spinner group_type_spinner;
    EditText groupId;
    TextView cancel, ok;

    String chosenContact;

//    RealmPatient realmPatient;
//
//    public RealmPatient getRealmPatient() {
//        return realmPatient;
//    }
//
//    public void setRealmPatient(RealmPatient realmPatient) {
//        this.realmPatient = realmPatient;
//    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.material_dialog_create_group,null);
        group_type_spinner = view.findViewById(R.id.group_type_spinner);
        groupId = view.findViewById(R.id.groupId);
        cancel = view.findViewById(R.id.cancel);
        ok = view.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            for (RealmPatient realmPatient : multiselect_list) {
                                realmPatient.setGroupid(chosenContact);
                                realmPatient.setGrouptype(group_type_spinner.getSelectedItem().toString());;
                            }
                            if (mActionMode != null) {
                                mActionMode.finish();
                            }
                            isMultiSelect = false;
                            multiselect_list = new ArrayList<RealmPatient>();
                            populatePatients();
                            refreshAdapter();

                            FamiliesPatientFragment familiesPatientFragment = new FamiliesPatientFragment();
                            CompaniesPatientFragment companiesPatientFragment = new CompaniesPatientFragment();

                            familiesPatientFragment.populatePatients();
                            companiesPatientFragment.populatePatients();

                            familiesPatientFragment.refreshAdapter();
                            companiesPatientFragment.refreshAdapter();

                            dismiss();
                        }
                    });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        groupId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HealthRecordActivity.class);
                startActivityForResult(intent, RC_GROUP_ID);
            }
        });


       // doneBtn.setOnClickListener(doneAction);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
      //  builder.setCancelable(false);
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_GROUP_ID:
                switch (resultCode) {
                    case RESULT_OK:
                        groupId.setError(null);
                        final String contact = data.getStringExtra("contact");
                        groupId.setText(contact);
                        chosenContact = contact;
                        break;

                    case RESULT_CANCELED:

                        break;
                }
                break;

            default:
                break;

        }
    }

    public boolean validate (){
        boolean validated = true;

        if( TextUtils.isEmpty(groupId.getText())){

            groupId.setError(getString(R.string.error_field_required));
            validated = false;
        }
        else {
            groupId.setError(null);
            //clickedPatient.setPatientid(groupId.getText().toString().trim());
        }
        if( group_type_spinner.getSelectedItemPosition() == 0){
            TextView errorText = (TextView)group_type_spinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        }
        else {

            //clickedPatient.setAccounttype(group_type_spinner.getSelectedItem().toString());

        }
        return validated;
    }
}