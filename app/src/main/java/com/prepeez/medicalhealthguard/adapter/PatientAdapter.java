package com.prepeez.medicalhealthguard.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.activity.AddPatientByHealthProfActivity;
import com.prepeez.medicalhealthguard.activity.AddPatientByMisActivity;
import com.prepeez.medicalhealthguard.activity.HealthRecordActivity;
import com.prepeez.medicalhealthguard.realm.RealmPatient;


import java.util.ArrayList;

import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.MYUSERTYPE;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Nana on 9/11/2017.
 */

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {
    public static RealmPatient clickedPatient;

    public static String action;
    private Context mContext;

    public ArrayList<RealmPatient> patients;
    public ArrayList<RealmPatient> selected_patients;
    public boolean actionSearch;

    public PatientAdapter(ArrayList<RealmPatient> patients, ArrayList<RealmPatient> selected_patients, boolean actionSearch) {
        this.patients = patients;
        this.selected_patients = selected_patients;
        this.actionSearch = actionSearch;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_realm_patient, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RealmPatient realmPatient = patients.get(position);



        if(selected_patients.contains(patients.get(position)))
            holder.mParentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
        else
            holder.mParentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));

        holder.mName.setText(realmPatient.getTitle() + " " + realmPatient.getLastname() + " " + realmPatient.getFirstname());
        if (realmPatient.getGroupid() != null && realmPatient.getGroupid().equals(realmPatient.getContact())) {
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        }
        else {
            holder.mName.setTextColor(mContext.getResources().getColor(R.color.secondary_text));
        }
        holder.mContact.setText(realmPatient.getContact());

        String encoded_picture = realmPatient.getPicture();
        if (encoded_picture != null) {
            byte[] decodedBytes = Base64.decode(encoded_picture, 0);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            holder.mProfileImg.setImageBitmap(decodedBitmap);

        } else {
            holder.mProfileImg.setImageBitmap(null);
        }

        holder.editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myUserType = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MYUSERTYPE, "");

                clickedPatient = realmPatient;
                action = "edit";

                if (myUserType.equals("1")) {
                    mContext.startActivity(new Intent(mContext, AddPatientByHealthProfActivity.class));
                }
                else if (myUserType.equals("2")) {
                    mContext.startActivity(new Intent(mContext, AddPatientByMisActivity.class));
                }
                else{
                    mContext.startActivity(new Intent(mContext, AddPatientByHealthProfActivity.class));
                }
            }
        });

        holder.healthRecLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myUserType = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MYUSERTYPE, "");

                clickedPatient = realmPatient;
                mContext.startActivity(new Intent(mContext, HealthRecordActivity.class));
            }
        });

        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //notifyDataSetChanged();

                if (actionSearch) {
                    Intent intent = new Intent();
                    intent.putExtra("contact", realmPatient.getContact());
                    ((Activity)mContext).setResult(Activity.RESULT_OK, intent);
                    ((Activity)mContext).finish();
                }
                else {
                    if (holder.bottomViews.getVisibility() == View.VISIBLE) {
                        holder.bottomViews.setVisibility(View.GONE);
                    }
                    else {
                        holder.bottomViews.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public void setFilter(ArrayList<RealmPatient> arrayList) {
        patients = new ArrayList<>();
        patients.addAll(arrayList);
        notifyDataSetChanged();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mName;
        final TextView mContact;
        final RoundedImageView mProfileImg;
        final LinearLayout mParentLayout;
        final LinearLayout bottomViews;
        final LinearLayout editLayout;
        final LinearLayout healthRecLayout;
        final LinearLayout groupLayout;

        ViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.contact);
            mContact = (TextView) view.findViewById(R.id.name);
            mProfileImg = view.findViewById(R.id.profile_imgview);
            mParentLayout =  view.findViewById(R.id.parentLayout);
            bottomViews =  view.findViewById(R.id.bottomViews);
            editLayout =  view.findViewById(R.id.viewLayout);
            healthRecLayout =  view.findViewById(R.id.sendLayout);
            groupLayout =  view.findViewById(R.id.groupLayout);
        }
    }
}
