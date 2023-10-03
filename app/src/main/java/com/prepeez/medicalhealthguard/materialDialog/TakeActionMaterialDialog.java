package com.prepeez.medicalhealthguard.materialDialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.activity.AddHealthRecordActivity;
import com.prepeez.medicalhealthguard.activity.AddPatientByHealthProfActivity;
import com.prepeez.medicalhealthguard.activity.AgentActivity;
import com.prepeez.medicalhealthguard.activity.HealthRecordActivity;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by Nana on 10/22/2017.
 */

public class TakeActionMaterialDialog extends DialogFragment {
    private static final String TAG = "TakeActionMaterialDialog";
    public static final int RC_AGENT_CONTACT = 991;

    public static ArrayList<String> actions = new ArrayList<>();

    private RadioGroup mRadioGroup;
    public String selectedAction;

    public String getselectedAction() {
        return selectedAction;
    }

    public void setselectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.material_dialog_take_action,null);
        mRadioGroup = view.findViewById(R.id.radioGroup);

        actions.clear();
        actions.add("Offer advice");
        actions.add("Refer to healthcare agent");

        int size = actions.size();
        RadioButton[] rb = new RadioButton[size];
        for (int i = 0; i < size; i++) {
            rb[i]  = new RadioButton(getActivity());
            mRadioGroup.addView(rb[i]);
            rb[i].setId(i);
            rb[i].setText(actions.get(i));
        }

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedAction = actions.get(mRadioGroup.getCheckedRadioButtonId());

                if (selectedAction.equals("Refer to healthcare agent")) {
                    Intent intent = new Intent(getActivity(), AgentActivity.class);
                    startActivity(intent);
            }

                dismiss();
            }
        });

       // doneBtn.setOnClickListener(doneAction);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
      //  builder.setCancelable(false);
        return builder.create();
    }
}