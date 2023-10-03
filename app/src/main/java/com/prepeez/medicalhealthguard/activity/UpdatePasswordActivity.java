package com.prepeez.medicalhealthguard.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.prepeez.medicalhealthguard.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.prepeez.medicalhealthguard.activity.SigninActivity.AGENTID;
import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.userType;

import static com.prepeez.medicalhealthguard.constants.keyConst.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

public class UpdatePasswordActivity extends AppCompatActivity {


    EditText oldPassword, newPassword, confirmPassword;
    Button UpdatePassword;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        UpdatePassword = findViewById(R.id.UpdatePassword);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        UpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String oldPass = oldPassword.getText().toString();
                    String newPass = newPassword.getText().toString();
                    String confirmPass = confirmPassword.getText().toString();
                    if (newPass.equals(confirmPass)) {
                        new UpdatePasswordAsync().execute(oldPass, newPass);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean validate() {
        boolean validate = true;

        String oldPass = oldPassword.getText().toString();
        String newPass = newPassword.getText().toString();
        String confirmPass = confirmPassword.getText().toString();

        oldPassword.setError(null);
        newPassword.setError(null);
        confirmPassword.setError(null);

        if ((TextUtils.isEmpty(oldPass))) {
            oldPassword.setError(getString(R.string.error_field_required));
            validate = false;
        }
        if ((TextUtils.isEmpty(newPass))) {
            newPassword.setError(getString(R.string.error_field_required));
            validate = false;
        }
        if ((TextUtils.isEmpty(confirmPass))) {
            confirmPassword.setError(getString(R.string.error_field_required));
            validate = false;
        }
        return validate;
    }

    public class UpdatePasswordAsync extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {

                String oldPass = (String) arg0[0];
                String newPass = (String) arg0[1];

                String link = BASE_URL + "update_password.php";
                String data = URLEncoder.encode("Old_password", "UTF-8") + "=" +
                        URLEncoder.encode(oldPass, "UTF-8");
                data += "&" + URLEncoder.encode("New_password", "UTF-8") + "=" +
                        URLEncoder.encode(newPass, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return sb.toString();
            } catch (Exception e) {
                return new String("" + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            mProgress.dismiss();
            if (result != null) {
                Toast.makeText(UpdatePasswordActivity.this, result, Toast.LENGTH_SHORT).show();
                if (result != null) {
                    if (result.equals("Password successfully updated!")) {
                        PreferenceManager
                                .getDefaultSharedPreferences(UpdatePasswordActivity.this)
                                .edit()
                                .putBoolean(userType, false)
                                .apply();
                        Intent i = new Intent();
                        setResult(Activity.RESULT_OK, i);
                        finish();
                    }
                }
            }
        }
    }

}
