package com.prepeez.medicalhealthguard.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.prepeez.medicalhealthguard.R;

import java.util.ArrayList;

/**
 * Created by Nana on 9/11/2017.
 */

public class UserTypeAdapter extends RecyclerView.Adapter<UserTypeAdapter.ViewHolder>  {


   ArrayList <String> userTypes;
    private Context mContext;

    public UserTypeAdapter(ArrayList<String> userTypes)
    {
        this.userTypes = userTypes;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user_type, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final String usertype = userTypes.get(position);
        holder.button.setText(usertype);

        switch (position) {
            case 0:
                holder.parentLayout.setBackground(mContext.getDrawable(R.drawable.field_agent));
                break;
            case 1:
                holder.parentLayout.setBackground(mContext.getDrawable(R.drawable.mis_agent));
                break;
            case 2:
                holder.parentLayout.setBackground(mContext.getDrawable(R.drawable.health_prof));
                break;
             default:
                break;
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return  userTypes.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        int itemPosition = -1;
        Button button;
        RelativeLayout parentLayout;
        private Context mContext;
        public void onAttach(final Activity activity) {
            mContext = activity;
        }

        public ViewHolder(View view)
        {
         super(view);

            button = view.findViewById(R.id.button);
            parentLayout = view.findViewById(R.id.parentLayout);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

        }
    }
}
