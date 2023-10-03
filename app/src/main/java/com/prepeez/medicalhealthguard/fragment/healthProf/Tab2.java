package com.prepeez.medicalhealthguard.fragment.healthProf;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.realm.RealmPatient;
import com.prepeez.medicalhealthguard.util.PixelUtil;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.action;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class Tab2 extends Fragment {
    private static final String TAG = "Tab2";
    private EditText allergicHistory, specializedCondition, gravidity, aom, drugHistory, pastSurgicalHistoty;
    //private Spinner sicklecell_spinner, bloodgroup_spinner;
    private Spinner gender;
    private LinearLayout aomLayout, gravidityLayout;
    private CheckBox ph_dm, ph_ast, ph_dm2, ph_dm3, fh_dm, fh_htn, fh_scd, fh_dm2;

    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2, container, false);

        mContext = getContext();

        aomLayout = rootView.findViewById(R.id.aomLayout);
        gravidityLayout = rootView.findViewById(R.id.gravidityLayout);
        allergicHistory = rootView.findViewById(R.id.allergicHistory);
        pastSurgicalHistoty = rootView.findViewById(R.id.pastSurgicalHistoty);
        drugHistory = rootView.findViewById(R.id.drugHistory);
        gender = rootView.findViewById(R.id.gender_spinner);
        gravidity = rootView.findViewById(R.id.gravidity);
        aom = rootView.findViewById(R.id.aom);
        ph_dm = rootView.findViewById(R.id.ph_dm);
        ph_ast = rootView.findViewById(R.id.ph_ast);
        ph_dm2 = rootView.findViewById(R.id.ph_dm2);
        ph_dm3 = rootView.findViewById(R.id.ph_dm3);
        fh_dm = rootView.findViewById(R.id.fh_dm);
        fh_htn = rootView.findViewById(R.id.fh_htn);
        fh_scd = rootView.findViewById(R.id.fh_scd);
        fh_dm2 = rootView.findViewById(R.id.fh_dm2);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public boolean validate() {
        boolean validated = true;
        if (gender.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) gender.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        } else {

            clickedPatient.setGender(gender.getSelectedItem().toString());

        }
        if (gravidityLayout.getVisibility() == View.VISIBLE) {
            clickedPatient.setGravidity(gravidity.getText().toString().trim());
        }
        if (aom.getVisibility() == View.VISIBLE) {
            clickedPatient.setAgeofmenarche(aom.getText().toString().trim());
        }
        clickedPatient.setAllergichistory(allergicHistory.getText().toString().trim());
        clickedPatient.setDrughistory(drugHistory.getText().toString().trim());
        clickedPatient.setPastsurgicalhistory(pastSurgicalHistoty.getText().toString().trim());
        JSONObject phJsonObj =new JSONObject();
        JSONObject fhJsonObj =new JSONObject();
        try {
            fhJsonObj.put("fh_dm",   fh_dm.isChecked());
            fhJsonObj.put("fh_htn", fh_htn.isChecked());
            fhJsonObj.put("fh_scd", fh_scd.isChecked());
            fhJsonObj.put("fh_dm2", fh_dm2.isChecked());

            phJsonObj.put("ph_dm",   ph_dm.isChecked());
            phJsonObj.put("ph_ast", ph_ast.isChecked());
            phJsonObj.put("ph_dm2", ph_dm2.isChecked());
            phJsonObj.put("ph_dm3", ph_dm3.isChecked());

            clickedPatient.setFamilyhistory(fhJsonObj.toString());
            clickedPatient.setPasthistory(phJsonObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        if( sicklecell_spinner.getSelectedItemPosition() == 0){
//            clickedPatient.setSicklecellbloodgroup(null);
//        }
//        else {
//
//            clickedPatient.setSicklecellbloodgroup(sicklecell_spinner.getSelectedItem().toString());
//
//        }
//        if( bloodgroup_spinner.getSelectedItemPosition() == 0){
//            clickedPatient.setAbobloodgroup(null);
//        }
//        else {
//
//            clickedPatient.setAbobloodgroup(bloodgroup_spinner.getSelectedItem().toString());
//
//        }

        return validated;
    }

    public void init() {
        if (action.equals("edit")) {
            if (gender.getSelectedItemPosition() == 1) {
                aomLayout.setVisibility(View.GONE);
                gravidityLayout.setVisibility(View.GONE);
            } else if (gender.getSelectedItemPosition() == 2) {
                aomLayout.setVisibility(View.VISIBLE);
                gravidityLayout.setVisibility(View.VISIBLE);
                aom.setText(clickedPatient.getAgeofmenarche());
                gravidity.setText(clickedPatient.getGravidity());
            }
            if (TextUtils.isEmpty(clickedPatient.getFamilyhistory())) {
                fh_dm.setChecked(false);
                fh_htn.setChecked(false);
                fh_scd.setChecked(false);
                fh_dm2.setChecked(false);
            } else {
                try {
                    JSONObject obj = new JSONObject(clickedPatient.getFamilyhistory());
                     fh_dm.setChecked(obj.getBoolean("fh_dm"));
                    fh_htn.setChecked(obj.getBoolean("fh_htn"));
                    fh_scd.setChecked(obj.getBoolean("fh_scd"));
                    fh_dm2.setChecked(obj.getBoolean("fh_dm2"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (TextUtils.isEmpty(clickedPatient.getPasthistory())) {
                ph_dm.setChecked(false);
                ph_ast.setChecked(false);
                ph_dm2.setChecked(false);
                ph_dm3.setChecked(false);
            } else {
                try {
                    JSONObject obj = new JSONObject(clickedPatient.getPasthistory());
                     ph_dm.setChecked(obj.getBoolean("ph_dm"));
                    ph_ast.setChecked(obj.getBoolean("ph_ast"));
                    ph_dm2.setChecked(obj.getBoolean("ph_dm2"));
                    ph_dm3.setChecked(obj.getBoolean("ph_dm3"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            sicklecell_spinner.setSelection(((ArrayAdapter)sicklecell_spinner.getAdapter()).getPosition(clickedPatient.getSicklecellbloodgroup()));
//            bloodgroup_spinner.setSelection(((ArrayAdapter)bloodgroup_spinner.getAdapter()).getPosition(clickedPatient.getAbobloodgroup()));
            allergicHistory.setText(clickedPatient.getAllergichistory());
            pastSurgicalHistoty.setText(clickedPatient.getPastsurgicalhistory());
            drugHistory.setText(clickedPatient.getDrughistory());
            gender.setSelection(((ArrayAdapter) gender.getAdapter()).getPosition(clickedPatient.getGender()));
        }
    }

    public RealmPatient getPatient() {
        return clickedPatient;
    }

    private UCrop.Options imgOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        options.setCropFrameColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        options.setCropFrameStrokeWidth(PixelUtil.dpToPx(getContext(), 4));
        options.setCropGridColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        options.setCropGridStrokeWidth(PixelUtil.dpToPx(getContext(), 2));
        options.setActiveWidgetColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        options.setToolbarTitle("Crop Image");

        // set rounded cropping guide
        options.setCircleDimmedLayer(true);
        return options;
    }

    public void clear() {
        gender.setSelection(0);
//        sicklecell_spinner.setSelection(0);
//        bloodgroup_spinner.setSelection(0);
        allergicHistory.setText(null);
        drugHistory.setText(null);
        pastSurgicalHistoty.setText(null);
        ph_dm.setChecked(false);
        ph_ast.setChecked(false);
        ph_dm2.setChecked(false);
        ph_dm3.setChecked(false);
        fh_dm.setChecked(false);
        fh_htn.setChecked(false);
        fh_scd.setChecked(false);
        fh_dm2.setChecked(false);
        aom.setText(null);
        gravidity.setText(null);
    }
}