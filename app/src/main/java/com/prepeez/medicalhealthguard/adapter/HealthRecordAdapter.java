package com.prepeez.medicalhealthguard.adapter;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.activity.AddHealthRecordActivity;
import com.prepeez.medicalhealthguard.realm.RealmHealthRecord;

import java.util.ArrayList;

import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.MYUSERTYPE;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Nana on 9/11/2017.
 */

public class HealthRecordAdapter extends RecyclerView.Adapter<HealthRecordAdapter.ViewHolder> {
    public static RealmHealthRecord clickedHealthRecord;

    public static String action_helth_rec;
    private Context mContext;

    public ArrayList<RealmHealthRecord> healthRecords;

    public HealthRecordAdapter(ArrayList<RealmHealthRecord> healthRecords) {
        this.healthRecords = healthRecords;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_realm_health_record, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RealmHealthRecord healthRecord = healthRecords.get(position);

        holder.mcreated_at.setText(healthRecord.getCreated_at());
        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myUserType = PreferenceManager.getDefaultSharedPreferences(mContext).getString(MYUSERTYPE, "");

                clickedHealthRecord = healthRecord;
                action_helth_rec = "view";

                mContext.startActivity(new Intent(mContext, AddHealthRecordActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return healthRecords.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mcreated_at;
        final LinearLayout mParentLayout;

        ViewHolder(View view) {
            super(view);
            mcreated_at = view.findViewById(R.id.created_at);
            mParentLayout = view.findViewById(R.id.parentLayout);
        }
    }

    public void setFilter(ArrayList<RealmHealthRecord> arrayList) {
        healthRecords = new ArrayList<>();
        healthRecords.addAll(arrayList);
        notifyDataSetChanged();

    }
}
