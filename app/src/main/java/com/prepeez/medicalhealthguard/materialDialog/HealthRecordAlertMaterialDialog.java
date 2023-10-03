package com.prepeez.medicalhealthguard.materialDialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.prepeez.medicalhealthguard.R;

import java.util.ArrayList;

/**
 * Created by Nana on 10/22/2017.
 */

public class HealthRecordAlertMaterialDialog extends DialogFragment {
    private static final String TAG = "HealthRecordAlertMaterialDialog";

    public static ArrayList<String> actions = new ArrayList<>();

    private Button mButton;
    private TextView mTextView;
    public String alert;

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.material_dialog_health_record_alert,null);
        mTextView = view.findViewById(R.id.alert);
        mButton = view.findViewById(R.id.takeAction);

        mTextView.setText(alert);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

                android.app.FragmentManager fm = getFragmentManager();

                TakeActionMaterialDialog takeActionMaterialDialog = new TakeActionMaterialDialog();
                if(takeActionMaterialDialog != null && takeActionMaterialDialog.isAdded()) {

                } else {
                    takeActionMaterialDialog.show(fm, "TakeActionMaterialDialog");
                }
            }
        });

       // doneBtn.setOnClickListener(doneAction);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
      //  builder.setCancelable(false);
        return builder.create();
    }


}