package com.prepeez.medicalhealthguard.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.prepeez.medicalhealthguard.R;
import com.prepeez.medicalhealthguard.activity.AddHealthRecordActivity;
import com.prepeez.medicalhealthguard.activity.AddPatientByHealthProfActivity;
import com.prepeez.medicalhealthguard.activity.AddPatientByMisActivity;
import com.prepeez.medicalhealthguard.activity.HealthRecordActivity;
import com.prepeez.medicalhealthguard.constants.Const;
import com.prepeez.medicalhealthguard.pojo.UnsentSms;
import com.prepeez.medicalhealthguard.realm.RealmAgent;
import com.prepeez.medicalhealthguard.realm.RealmHealthRecord;
import com.prepeez.medicalhealthguard.realm.RealmPatient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import io.realm.Realm;

import static com.prepeez.medicalhealthguard.activity.AddPatientByHealthProfActivity.updateRecords;
import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.MYUSERTYPE;
import static com.prepeez.medicalhealthguard.adapter.HealthRecordAdapter.clickedHealthRecord;
import static com.prepeez.medicalhealthguard.adapter.PatientAdapter.clickedPatient;
import static com.prepeez.medicalhealthguard.constants.Const.format;
import static com.prepeez.medicalhealthguard.constants.Const.isNetworkAvailable;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Nana on 9/11/2017.
 */

public class UnsentSmsAdapter extends RecyclerView.Adapter<UnsentSmsAdapter.ViewHolder> {

    private Context mContext;

    public ArrayList<UnsentSms> unsentSmsList;
    public ArrayList<UnsentSms> selected_unsentSmsList;

    public UnsentSmsAdapter(ArrayList<UnsentSms> unsentSmsList, ArrayList<UnsentSms> selected_unsentSmsList) {
        this.unsentSmsList = unsentSmsList;
        this.selected_unsentSmsList = selected_unsentSmsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_unsent_sms, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UnsentSms unsentSms = unsentSmsList.get(position);

//        if(selected_unsentSmsList.contains(unsentSmsList.get(position)))
//            holder.mParentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_selected_state));
//        else
//            holder.mParentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_item_normal_state));

        holder.mcreated_at.setText(unsentSms.getId());


        holder.viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle("SMS Content")
                        .setMessage(unsentSms.getSms())
                        .show();
            }
        });

        holder.sendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendSMS(mContext).execute(unsentSms.getId(), unsentSms.getSms_type(),unsentSms.getNumber(), unsentSms.getSms());
            }
        });

        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.bottomViews.getVisibility() == View.VISIBLE) {
                    holder.bottomViews.setVisibility(View.GONE);
                }
                else {
                    holder.bottomViews.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return unsentSmsList.size();
    }

    public void setFilter(ArrayList<UnsentSms> arrayList) {
        unsentSmsList = new ArrayList<>();
        unsentSmsList.addAll(arrayList);
        notifyDataSetChanged();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mcreated_at;
        final LinearLayout mParentLayout;
        final LinearLayout bottomViews;
        final LinearLayout viewLayout;
        final LinearLayout sendLayout;

        ViewHolder(View view) {
            super(view);
            mcreated_at = view.findViewById(R.id.created_at);
            mParentLayout =  view.findViewById(R.id.parentLayout);
            bottomViews =  view.findViewById(R.id.bottomViews);
            viewLayout =  view.findViewById(R.id.viewLayout);
            sendLayout =  view.findViewById(R.id.sendLayout);
        }
    }

    public static class SendSMS extends AsyncTask<String, Void, String> {
        Context context;
        private String html;

        String id;
        String sms_type;
        String number;
        String sms;

        private ProgressDialog asyncPd;

        public SendSMS(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            asyncPd = new ProgressDialog(context);
            asyncPd.setTitle("Sending sms...");
            asyncPd.setMessage("Please wait...");
            asyncPd.setCancelable(false);
            asyncPd.setIndeterminate(true);

            asyncPd.show();
        }

        @Override
        protected String doInBackground(String... strings) {


//            String url = "https://api.hubtel.com/v1/messages/send?"
//                    + "From=Unity&To=%2B233546101171"
//                    + "&Content=Hello%2C+world&amp"
//                    + "&ClientId=jsaiwsob"
//                    + "&ClientSecret=kgcnvrsi&RegisteredDelivery=true";
            id = strings[0];
            sms_type = strings[1];
            number = strings[2];
            sms = strings[3];
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.hubtel.com")
                    .appendPath("v1")
                    .appendPath("messages")
                    .appendPath("send")
                    .appendQueryParameter("From", "MHG")
                    .appendQueryParameter("To", number)
                    .appendQueryParameter("Content", sms)
                    .appendQueryParameter("ClientId", context.getString(R.string.CLIENT_ID))
                    .appendQueryParameter("ClientSecret", context.getString(R.string.CLIENT_SECRET))
                    .appendQueryParameter("RegisteredDelivery", "true");

            String url = builder.build().toString();

            Log.d("whyoo", url);

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
            asyncPd.dismiss();
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    final String message = jsonObject.getString("Message");
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if (sms_type.equals("PATIENT")) {
                                clickedPatient = realm.where(RealmPatient.class).equalTo("patientid", id).findFirst();
                                clickedPatient.setSmsstatus(message);

                                if (isNetworkAvailable(context)) {
                                    AddPatientByHealthProfActivity.updateRecords(context, true);
                                }
                                else {
                                    try {
                                        java.util.Date date = format.parse(clickedPatient.getUpdated_at());
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(date);
                                        calendar.add(Calendar.SECOND, 1);
                                        String updatedAt = format.format(calendar.getTime());

                                        clickedPatient.setUpdated_at(updatedAt);
                                        realm.copyToRealmOrUpdate(clickedPatient);
                                    } catch (ParseException e) {
                                        Log.d("updateerror", "error: " + e.getMessage());
                                        e.printStackTrace();
                                    }

                                }
                            }
                            else if (sms_type.equals("HEALTHRECORD")) {
                                RealmHealthRecord healthRecord = realm.where(RealmHealthRecord.class).equalTo("healthrecordid", id).findFirst();
                                healthRecord.setSmsstatus(message);

                                if (isNetworkAvailable(context)) {
                                    AddHealthRecordActivity.updateRecords(context, true);
                                }
                                else {
                                    try {
                                        java.util.Date date = format.parse(clickedHealthRecord.getUpdated_at());
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(date);
                                        calendar.add(Calendar.SECOND, 1);
                                        String updatedAt = format.format(calendar.getTime());

                                        clickedHealthRecord.setUpdated_at(updatedAt);
                                        realm.copyToRealmOrUpdate(clickedHealthRecord);
                                    } catch (ParseException e) {
                                        Log.d("updateerror", "error: " + e.getMessage());
                                        e.printStackTrace();
                                    }

                                }
                            }

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("smserror", e.getMessage());
                    Toast.makeText(context, "Error sending sms.", Toast.LENGTH_SHORT).show();
                }
            } else {

            }
        }
    }
}
