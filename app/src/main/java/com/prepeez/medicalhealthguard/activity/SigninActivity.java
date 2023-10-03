package com.prepeez.medicalhealthguard.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.prepeez.medicalhealthguard.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.prepeez.medicalhealthguard.activity.UserTypeActivity.userType;
import static com.prepeez.medicalhealthguard.activity.ZoneActivity.myZone;

import static com.prepeez.medicalhealthguard.constants.keyConst.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;


public class SigninActivity extends AppCompatActivity {

    public static final String AGENTID = "agentid";

    static final String USER_NOT_FOUND = "0";
    static final String SUCCESS = "1";
    static final String INCORRECT_PASSWORD = "2";
    private EditText usernameField, passwordField;
    ImageView passwordIcon;
    private Button button;
    private ProgressDialog mProgress;
    boolean passwordShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        passwordIcon = findViewById(R.id.passwordIcon);

        button = findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        boolean signedIn = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(userType, false);
        if (signedIn) {
            onSignInInit();
        }
        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordShow = !passwordShow;
                if (passwordShow) {
                    passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hide_password);
                    passwordIcon.setImageBitmap(bitmap);
                }
                else {
                    passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.see_password);
                    passwordIcon.setImageBitmap(bitmap);
                }
            }
        });
    }

    private void onSignInInit() {
        startActivity(new Intent(SigninActivity.this, PatientActivity.class));
        finish();
    }

    public void login() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        boolean canLogin = true;

        if (TextUtils.isEmpty(username)) {
            usernameField.setError(getString(R.string.error_field_required));
            canLogin = false;
        } else {
            usernameField.setError(null);
        }
        if (TextUtils.isEmpty(password)) {
            passwordField.setError(getString(R.string.error_field_required));
            canLogin = false;
        } else {
            passwordField.setError(null);
        }

        if (canLogin) {
            new SigninAsync(this).execute(username, password, userType);
        }
    }

    public class SigninAsync extends AsyncTask<String, Void, String> {

        private Context context;

        public SigninAsync(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String username = (String) arg0[0];
                String password = (String) arg0[1];
                String UserTypeID = (String) arg0[2];

                String link = BASE_URL + "public/api/login";
                String data = URLEncoder.encode("username", "UTF-8") + "=" +
                        URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8");
                data += "&" + URLEncoder.encode("UserTypeID", "UTF-8") + "=" +
                        URLEncoder.encode(UserTypeID, "UTF-8");
                if (UserTypeID.equals("1")) {
                    data += "&" + URLEncoder.encode("zone", "UTF-8") + "=" +
                            URLEncoder.encode(myZone.getId(), "UTF-8");
                }

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

                 if (result.equals(INCORRECT_PASSWORD)){
                    Toast.makeText(context, "Incorrect password!", Toast.LENGTH_SHORT).show();
                }
                else if (result.equals(USER_NOT_FOUND)){
                    Toast.makeText(context, "User not found!", Toast.LENGTH_SHORT).show();
                }
                else if (result.substring(0, 7).equals("AgentID")) {
                    Toast.makeText(context, "Successfully signed in!", Toast.LENGTH_SHORT).show();
                    String agentid = result.substring(7);
                     PreferenceManager
                            .getDefaultSharedPreferences(context)
                            .edit()
                            .putBoolean(userType, true)
                            .putString(AGENTID, agentid)
                            .apply();

                    onSignInInit();
                }
                else {
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}