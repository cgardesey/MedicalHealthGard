package com.prepeez.medicalhealthguard.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.pojo.Phone;

import java.util.ArrayList;

/**
 * Created by Nana on 9/11/2017.
 */

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder>  {


   ArrayList <Phone> contacts;
    private Context mContext;

    public PhoneAdapter(ArrayList<Phone> contacts)
    {
        this.contacts = contacts;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_phone,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Phone cur_event = contacts.get(position);
        String number = cur_event.getNumber();
        holder.phonenumber.setText(number);
        holder.phonetype.setText(cur_event.getType());
        mContext =   holder.phonetype.getContext();}
    @Override
    public int getItemCount() {
        return  contacts.size();
    }
    public  class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        int itemPosition = -1;
        TextView phonenumber, phonetype;
        ImageView deletepost;
        private Context mContext;
        public void onAttach(final Activity activity) {
            mContext = activity;
        }

        public ViewHolder(View view)
        {
         super(view);
            deletepost = view.findViewById(R.id.close);
            phonenumber = view.findViewById(R.id.phoneNumber);
            phonetype = view.findViewById(R.id.contactType);
          //  institutename = (TextView) view.findViewById(R.id.institutename);
            deletepost.setOnClickListener(this);
          //  itemView.setOnLongClickListener(this);
        }
        @Override
        public void onClick(final View v) {
            Snackbar snackbar=     Snackbar.make(v,"Delete Entry?", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("YES", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    // check if item still exists
                    if(pos != RecyclerView.NO_POSITION){
                        contacts.remove(pos);
                    }

                    notifyDataSetChanged();
                }
            });
            snackbar.show();
        }
    }
}
