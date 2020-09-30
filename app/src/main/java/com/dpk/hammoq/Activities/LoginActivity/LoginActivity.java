package com.dpk.hammoq.Activities.LoginActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.dpk.hammoq.Activities.CameraActivity.CameraActivity;
import com.dpk.hammoq.Activities.Dashbaord.DashBoardActivity;
import com.dpk.hammoq.Activities.ForgetPasswordActivity.ForgetPasswordActivity;
import com.dpk.hammoq.Activities.RegisterActivity.RegisterActivity;
import com.dpk.hammoq.GsonModule.LoginModule.LoginPkg;
import com.dpk.hammoq.R;
import com.dpk.hammoq.Utils.Appcontants;
import com.dpk.hammoq.utilsNetwork.CheckNetworkAccess;
import com.dpk.hammoq.utilsNetwork.RetrofitClient;
import com.dpk.hammoq.utilsNetwork.UrlsApi;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    private TextInputEditText et_email, et_password;
    private UrlsApi urlsApi;
    private CardView progress_bar;
    private Button btnLogin, btnReset, btnRegister;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        @SuppressLint("WrongViewCast")
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        init();

        if (CheckNetworkAccess.isNetAvailable(LoginActivity.this)) {
            urlsApi = RetrofitClient.getClient().create(UrlsApi.class);
        } else {
            Toast.makeText(LoginActivity.this, "Check Network Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void init() {
        textInputLayoutEmail = findViewById(R.id.Email);
        textInputLayoutPassword = findViewById(R.id.Password);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        progress_bar = findViewById(R.id.progress_bar);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::onClick);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this::onClick);

        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                boolean isValid = validateInputs();
                if (isValid) {
                    progress_bar.setVisibility(View.VISIBLE);
                    if (CheckNetworkAccess.isNetAvailable(LoginActivity.this)) {
                        String mUsername = et_email.getText().toString().trim();
                        String mPassword = et_password.getText().toString().trim();
                        loginMethod(mUsername, mPassword);
                    } else {
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.btnRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                CustomIntent.customType(LoginActivity.this, "fadein-to-fadeout");
                break;

            case R.id.btnReset:
                Intent resetIntent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(resetIntent);
                CustomIntent.customType(LoginActivity.this, "fadein-to-fadeout");
                break;
        }
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(et_email.getText().toString().trim())) {
            et_email.setError("Enter Email");
            et_email.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
            et_password.setError("Enter Password");
            et_password.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void loginMethod(String mUsername, String mPassword) {

        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("email", mUsername);
        stringMap.put("password", mPassword);

        Log.e("api", mUsername);
        Log.e("api", mPassword);

        urlsApi.LOGIN_PKG_CALL(stringMap).enqueue(new Callback<LoginPkg>() {

            @Override
            public void onResponse(Call<LoginPkg> call, Response<LoginPkg> response) {

                try {

                    if (!response.isSuccessful()) {
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    LoginPkg loginPkg = response.body();
                    assert loginPkg != null;

                    try {
                        if (loginPkg.getErr().length() > 1) {
                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, loginPkg.getErr(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Appcontants.LOGIN_USER_TOKEN, String.valueOf(loginPkg.getToken()));
                        editor.apply();

                        /*  SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    mToken = sharedPreferences.getString(AppsContants.EXAM_TYPE_ID, ""); */

                        emptyInputEditText();

                        progress_bar.setVisibility(View.GONE);

                        startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                        CustomIntent.customType(LoginActivity.this, "fadein-to-fadeout");
                        finish();

                    }

                } catch (Exception e) {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginPkg> call, Throwable t) {
            }
        });
    }

    private void emptyInputEditText() {
        et_email.setText(null);
        et_password.setText(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
