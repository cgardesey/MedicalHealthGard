package com.prepeez.medicalhealthguard.materialDialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.prepeez.medicalhealthguard.R;

import java.util.ArrayList;


/**
 * Created by Nana on 10/22/2017.
 */

public class AlertMaterialDialog extends DialogFragment {

    private String alertText;

    public String getAlertText() {
        return alertText;
    }

    public void setAlertText(String alertText) {
        this.alertText = alertText;
    }

    TextView alert;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.material_health_record_threshold,null);
        alert = view.findViewById(R.id.alert);
        alert.setText(alertText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
      //  builder.setCancelable(false);
        return builder.create();
    }
}