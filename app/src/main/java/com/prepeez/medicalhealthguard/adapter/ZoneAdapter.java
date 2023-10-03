package com.prepeez.medicalhealthguard.adapter;

/**
 * Created by Nana on 11/10/2017.
 */

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.activity.SigninActivity;
import com.prepeez.medicalhealthguard.activity.ZoneActivity;
import com.prepeez.medicalhealthguard.pojo.Zone;

import java.util.ArrayList;
import static com.prepeez.medicalhealthguard.activity.ZoneActivity.myZone;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Belal on 6/6/2017.
 */

public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ViewHolder> {

    private static final String YOUR_DIALOG_TAG = "";
    private ArrayList<Zone> zones;

    Context context;

    public ZoneAdapter(ArrayList<Zone> zones) {
        this.zones = zones;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_item_list, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Zone zone = zones.get(position);
        holder.zone.setText(zone.getZone());

        holder.zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myZone = zone;
                Intent intent = new Intent(context, SigninActivity.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return zones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        Button zone;

        public ViewHolder(View itemView) {
            super(itemView);

            zone = itemView.findViewById(R.id.zone);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }


    }
}
