package com.prepeez.medicalhealthguard.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.activity.AddHealthRecordActivity;
import com.prepeez.medicalhealthguard.activity.AddPatientByHealthProfActivity;
import com.prepeez.medicalhealthguard.activity.AddPatientByMisActivity;
import com.prepeez.medicalhealthguard.activity.AgentActivity;
import com.prepeez.medicalhealthguard.activity.HealthRecordActivity;
import com.prepeez.medicalhealthguard.realm.RealmAgent;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import io.realm.Realm;

import static com.prepeez.medicalhealthguard.activity.AgentActivity.*;
import static com.prepeez.medicalhealthguard.activity.SigninActivity.AGENTID;
import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.MYUSERTYPE;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Nana on 9/11/2017.
 */

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.ViewHolder> {
    public static RealmAgent clickedAgent;

    public static String action;
    private Context mContext;

    public ArrayList<RealmAgent> agents;
    public boolean actionSearch;

    public AgentAdapter(Context mContext, ArrayList<RealmAgent> agents, boolean actionSearch) {
        this.mContext = mContext;
        this.agents = agents;
        this.actionSearch = actionSearch;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_realm_patient, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RealmAgent realmAgent = agents.get(position);


        holder.mName.setText(realmAgent.getTitle() + " " + realmAgent.getLastname() + " " + realmAgent.getFirstname());
        holder.mContact.setText(realmAgent.getContact());

        String encoded_picture = realmAgent.getPicture();
        if (encoded_picture != null) {
            byte[] decodedBytes = Base64.decode(encoded_picture, 0);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            holder.mProfileImg.setImageBitmap(decodedBitmap);

        } else {
            holder.mProfileImg.setImageBitmap(null);
        }

        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //notifyDataSetChanged();

                if (actionSearch) {
                    String receipientid = realmAgent.getAgentid();
                    String patientid = clickedPatient.getPatientid();
                    new SendSMStoAgent(mContext).execute(receipientid, patientid);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return agents.size();
    }

    public void setFilter(ArrayList<RealmAgent> arrayList) {
        agents = new ArrayList<>();
        agents.addAll(arrayList);
        notifyDataSetChanged();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mName;
        final TextView mContact;
        final RoundedImageView mProfileImg;
        final LinearLayout mParentLayout;

        ViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.contact);
            mContact = (TextView) view.findViewById(R.id.name);
            mProfileImg = view.findViewById(R.id.profile_imgview);
            mParentLayout =  view.findViewById(R.id.parentLayout);
        }
    }

    public class SendSMStoAgent extends AsyncTask<String, Void, String> {

        private String html;
        ProgressDialog mProgress;
        Context mContext;

        public SendSMStoAgent(Context mContext) {
            this.mContext = mContext;
            mProgress = new ProgressDialog(mContext);
        }

        @Override
        protected void onPreExecute() {
            mProgress.setTitle("Sending sms...");
            mProgress.setMessage("Please wait...");
            mProgress.setCancelable(false);
            mProgress.setIndeterminate(true);
            mProgress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String agentId_recepient = strings[0];
            String patientid = strings[1];
            RealmAgent agent = Realm.getDefaultInstance().where(RealmAgent.class).equalTo("agentid", agentId_recepient).findFirst();
            RealmAgent me = Realm.getDefaultInstance().where(RealmAgent.class).equalTo("agentid", PreferenceManager.getDefaultSharedPreferences(mContext).getString(AGENTID, "")).findFirst();

            String myname = me.getTitle() + " " + me.getLastname() + " " + me.getFirstname();

            String agentContact = agent.getContact();
            String agentName = agent.getTitle() + " " + agent.getLastname() + " " + agent.getFirstname();


            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.hubtel.com")
                    .appendPath("v1")
                    .appendPath("messages")
                    .appendPath("send")
                    .appendQueryParameter("From", "MHG")
                    .appendQueryParameter("To", agentContact)
                    .appendQueryParameter("Content", "Dear " + agentName + ", " + myname + " has refered patient with ID  " + patientid + " to you.")
                    .appendQueryParameter("ClientId", mContext.getString(R.string.CLIENT_ID))
                    .appendQueryParameter("ClientSecret", mContext.getString(R.string.CLIENT_SECRET))
                    .appendQueryParameter("RegisteredDelivery", "true");

            String url = builder.build().toString();

            try {
                html = new Scanner(new DefaultHttpClient().execute(new HttpGet(url)).getEntity().getContent(), "UTF-8").useDelimiter("\\A").next();
            } catch (IOException e) {
                html = "" + e.toString();
            }
            String content = null;
            return html;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgress.dismiss();
            super.onPostExecute(result);
            //((Activity)mContext).finish();
            if (result != null) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("Message");
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("smserror", e.getMessage());
                    Toast.makeText(mContext, "Error sending sms.", Toast.LENGTH_SHORT).show();
                }
            } else {

            }
        }
    }
}
