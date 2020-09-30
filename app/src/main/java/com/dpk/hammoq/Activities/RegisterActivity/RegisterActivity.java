package com.dpk.hammoq.Activities.RegisterActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.dpk.hammoq.Activities.LoginActivity.LoginActivity;
import com.dpk.hammoq.GsonModule.RegisterModule.RegisterPkg;
import com.dpk.hammoq.Helper.InputValidation;
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

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText mFirstName, mLastName, mEmail, mMobile, mPassword, mPassConfig, mCoupon;

    private TextInputLayout TextFirstName, TextLastName, TextEmail, TextMobile, TextPassword, TextPassConfg, TextCouponCode;

    private TextView tvPrivacyPolicy;

    private Button mRegister;

    private CheckBox mCheckbox;

    private UrlsApi urlsApi;

    private CardView progress_bar;

    private InputValidation inputValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        @SuppressLint("WrongViewCast")
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        if (CheckNetworkAccess.isNetAvailable(RegisterActivity.this)) {
            urlsApi = RetrofitClient.getClient().create(UrlsApi.class);
            onClick();
        } else {
            Toast.makeText(RegisterActivity.this, "Check Network Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void init() {

        mCheckbox = (CheckBox) findViewById(R.id.mCheckbox);
        mRegister = (Button) findViewById(R.id.btnRegister);
        tvPrivacyPolicy = (TextView) findViewById(R.id.tvPrivacyPolicy);

        TextFirstName = (TextInputLayout) findViewById(R.id.tvFirstName);
        TextLastName = (TextInputLayout) findViewById(R.id.tvLastName);
        TextEmail = (TextInputLayout) findViewById(R.id.tvEmail);
        TextMobile = (TextInputLayout) findViewById(R.id.tvPhoneNumber);
        TextPassword = (TextInputLayout) findViewById(R.id.tvPassword);
        TextPassConfg = (TextInputLayout) findViewById(R.id.tvPasswordConfg);
        TextCouponCode = (TextInputLayout) findViewById(R.id.tvCouponCode);

        progress_bar = findViewById(R.id.progress_bar);

        mFirstName = (TextInputEditText) findViewById(R.id.etFirstName);
        mLastName = (TextInputEditText) findViewById(R.id.etLastName);
        mEmail = (TextInputEditText) findViewById(R.id.etEmail);
        mMobile = (TextInputEditText) findViewById(R.id.etPhoneNumber);
        mPassword = (TextInputEditText) findViewById(R.id.etPassword);
        mPassConfig = (TextInputEditText) findViewById(R.id.etPasswordConfg);
        mCoupon = (TextInputEditText) findViewById(R.id.etCouponCode);

    }

    private boolean checkValidateInputs() {

        if (TextUtils.isEmpty(mFirstName.getText().toString().trim())) {
            mFirstName.setError("Enter First Name");
            mFirstName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(mLastName.getText().toString().trim())) {
            mLastName.setError("Enter Last Name");
            mLastName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(mEmail.getText().toString().trim())) {
            mEmail.setError("Enter Valid Email");
            mEmail.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(mMobile.getText().toString().trim())) {
            mMobile.setError("Enter Valid Mobile Number");
            mMobile.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(mPassword.getText().toString().trim())) {
            mPassword.setError("Enter Password");
            mPassword.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(mPassConfig.getText().toString().trim())) {
            mPassConfig.setError("Enter Confirmation Password");
            mPassConfig.requestFocus();
            return false;
        } else if (!mCheckbox.isChecked()) {
            mCheckbox.setError("Please check TERMS & CONDITIONS it");
            mCheckbox.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    public void onClick() {
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean inputFlag = checkValidateInputs();
                if (inputFlag == true) {
                    progress_bar.setVisibility(View.VISIBLE);
                    String mGetFirstName = mFirstName.getText().toString();
                    String mGetLastName = mLastName.getText().toString();
                    String mGetEmail = mEmail.getText().toString();
                    String mGetPhone = mMobile.getText().toString();
                    String mGetPassword = mPassword.getText().toString();
                    String mGetPasswordConfig = mPassConfig.getText().toString();
                    String mGetCoupon = mCoupon.getText().toString();

                    updatersUsersInfo(mGetFirstName,
                            mGetLastName,
                            mGetEmail,
                            mGetPhone,
                            mGetPassword,
                            mGetPasswordConfig,
                            mGetCoupon);
                }
            }
        });
    }

    private void updatersUsersInfo(String mGetFirstName,
                                   String mGetLastName,
                                   String mGetEmail,
                                   String mGetPhone,
                                   String mGetPassword,
                                   String mGetPasswordConfig,
                                   String mGetCoupon) {

        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("firstName", mGetFirstName);
        stringMap.put("lastName", mGetLastName);
        stringMap.put("email", mGetEmail);
        stringMap.put("phoneno", mGetPhone);
        stringMap.put("password", mGetPassword);

        urlsApi.REGISTER_PKG_CALL(stringMap).enqueue(new Callback<RegisterPkg>() {

            @Override
            public void onResponse(Call<RegisterPkg> call, Response<RegisterPkg> response) {

                try {

                    if (!response.isSuccessful()) {
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    RegisterPkg registerPkg = response.body();

                    Log.e("api:", registerPkg.getToken());

                    try {
                        if (registerPkg.getErr().length() > 1) {
                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, registerPkg.getErr(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Appcontants.REGISTER_USER_TOKEN, String.valueOf(registerPkg.getToken()));
                        editor.apply();

                        /*  SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    mToken = sharedPreferences.getString(AppsContants.EXAM_TYPE_ID, ""); */

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                        emptyInputEditText();

                        progress_bar.setVisibility(View.GONE);

                        Toast.makeText(RegisterActivity.this, registerPkg.getToken(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterPkg> call, Throwable t) {
            }
        });
    }

    private void emptyInputEditText() {
        mFirstName.setText(null);
        mLastName.setText(null);
        mEmail.setText(null);
        mMobile.setText(null);
        mPassword.setText(null);
        mPassConfig.setText(null);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        CustomIntent.customType(RegisterActivity.this, "fadein-to-fadeout");
        finish();
    }
}