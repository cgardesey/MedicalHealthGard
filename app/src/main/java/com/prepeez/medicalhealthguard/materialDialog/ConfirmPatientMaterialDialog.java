package com.prepeez.medicalhealthguard.materialDialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.realm.RealmPatient;

import static com.prepeez.medicalhealthguard.activity.AddHealthRecordActivity.onConfirmInit;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by Nana on 10/22/2017.
 */

public class ConfirmPatientMaterialDialog extends DialogFragment {
    private static final String TAG = "ConfirmPatientMaterialDialog";

    TextView name, contact;

    Button deny, confirm;
    RoundedImageView profilePic;

    RealmPatient realmPatient;

    public RealmPatient getRealmPatient() {
        return realmPatient;
    }

    public void setRealmPatient(RealmPatient realmPatient) {
        this.realmPatient = realmPatient;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.material_dialog_confirm_patient,null);
        name = view.findViewById(R.id.name);
        contact = view.findViewById(R.id.contact);
        profilePic = view.findViewById(R.id.profilePic);
        deny = view.findViewById(R.id.deny);
        confirm = view.findViewById(R.id.confirm);

        name.setText(realmPatient.getTitle() + " " + realmPatient.getLastname() + " " + realmPatient.getFirstname());
        contact.setText(realmPatient.getContact());
        String encoded_picture = realmPatient.getPicture();
        if (encoded_picture != null) {
            byte[] decodedBytes = Base64.decode(encoded_picture, 0);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            profilePic.setImageBitmap(decodedBitmap);

        } else {
            profilePic.setImageBitmap(null);
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onConfirmInit();
            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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