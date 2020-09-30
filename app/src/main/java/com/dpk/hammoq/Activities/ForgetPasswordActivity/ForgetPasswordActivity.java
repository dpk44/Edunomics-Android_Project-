package com.dpk.hammoq.Activities.ForgetPasswordActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.dpk.hammoq.Activities.LoginActivity.LoginActivity;
import com.dpk.hammoq.GsonModule.ForgetPasswordModule.ForgetPasswordPkg;
import com.dpk.hammoq.GsonModule.ForgetPasswordModule.NewPasswordModule.NewPasswordPkg;
import com.dpk.hammoq.GsonModule.ForgetPasswordModule.OtpModule.ForgetPasswordOtpVerifyPkg;
import com.dpk.hammoq.R;
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

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout textInputLayoutEmail, textInputLayoutNewPassword, textInputLayoutNewPasswordConfg;
    private TextInputEditText et_email, et_newpassword, et_newpasswordConfg;
    private UrlsApi urlsApi;
    private CardView cvReset, cvNewPassword;
    private Button btnReset, btnNewPassword;
    private String mEmail, mPassword, mAccessToken;
    private Dialog dialog;
    private CardView progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        @SuppressLint("WrongViewCast")
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reset Password");

        init();

        if (CheckNetworkAccess.isNetAvailable(ForgetPasswordActivity.this)) {

            urlsApi = RetrofitClient.getClient().create(UrlsApi.class);

        } else {

            Toast.makeText(ForgetPasswordActivity.this, "Check Network Connection", Toast.LENGTH_LONG).show();

        }

    }

    private void init() {

        textInputLayoutEmail = findViewById(R.id.Email);
        textInputLayoutNewPassword = findViewById(R.id.textInputLayoutNewPassword);
        textInputLayoutNewPasswordConfg = findViewById(R.id.textInputLayoutNewPasswordConfg);

        et_email = findViewById(R.id.et_email);
        et_newpassword = findViewById(R.id.et_newpassword);
        et_newpasswordConfg = findViewById(R.id.et_newpasswordConfg);

        progress_bar = findViewById(R.id.progress_bar);

        cvReset = findViewById(R.id.cvReset);
        cvNewPassword = findViewById(R.id.cvNewPassword);

        btnReset = findViewById(R.id.btnReset);
        btnNewPassword = findViewById(R.id.btnNewPassword);

        btnReset.setOnClickListener(this);
        btnNewPassword.setOnClickListener(this);

    }

    private boolean validNewPassword() {

        if (TextUtils.isEmpty(et_newpassword.getText().toString().trim())) {
            et_newpassword.setError("Enter Password");
            et_newpassword.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(et_newpasswordConfg.getText().toString().trim())) {
            et_newpasswordConfg.setError("Enter Password");
            et_newpasswordConfg.requestFocus();
            return false;
        }
        return true;

    }

    private boolean validPassword() {

        if (TextUtils.isEmpty(et_email.getText().toString().trim())) {
            et_email.setError("Enter Email");
            et_email.requestFocus();
            return false;
        }
        return true;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnReset:

                mEmail = et_email.getText().toString().trim();

                boolean validPassword = validPassword();
                if (validPassword) {
                    progress_bar.setVisibility(View.VISIBLE);
                    forgetPasswordMethod(mEmail);
                }

                break;

            case R.id.btnNewPassword:

                mPassword = et_newpassword.getText().toString().trim();

                boolean validNewPassword = validNewPassword();

                if (validNewPassword) {
                    progress_bar.setVisibility(View.VISIBLE);
                    NewPasswordMethod();
                }

                break;

        }

    }

    private void forgetPasswordMethod(String mEmail) {

        urlsApi.FORGET_PASSWORD_PKG_CALL(mEmail).enqueue(new Callback<ForgetPasswordPkg>() {

            @Override
            public void onResponse(Call<ForgetPasswordPkg> call, Response<ForgetPasswordPkg> response) {

                try {

                    if (!response.isSuccessful()) {
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(ForgetPasswordActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ForgetPasswordPkg forgetPasswordPkg = response.body();
                    assert forgetPasswordPkg != null;

                    try {
                        if (forgetPasswordPkg.getErr().length() > 1) {
                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(ForgetPasswordActivity.this, forgetPasswordPkg.getErr(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                      /*  SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ForgetPasswordActivity.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Appcontants.LOGIN_USER_TOKEN, String.valueOf(forgetPasswordPkg.getToken()));
                        editor.apply();

                      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                      mToken = sharedPreferences.getString(AppsContants.EXAM_TYPE_ID, ""); */

                        emptyInputEditText();

                        createOTPDialog();

                        progress_bar.setVisibility(View.GONE);

                        Toast.makeText(ForgetPasswordActivity.this, forgetPasswordPkg.getData() + " check your email", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(ForgetPasswordActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ForgetPasswordPkg> call, Throwable t) {
            }
        });
    }

    private void emptyInputEditText() {

        et_newpassword.setText(null);
        et_newpasswordConfg.setText(null);
        et_newpassword.requestFocus();

    }

    private void createOTPDialog() {

        dialog = new Dialog(ForgetPasswordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.create_otp_dialog);

        AppCompatButton btnOtpVerify = dialog.findViewById(R.id.btnOtpVerify);

        final TextInputEditText etOtp = dialog.findViewById(R.id.et_otp);

        btnOtpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkAccess.isNetAvailable(ForgetPasswordActivity.this)) {

                    if (!TextUtils.isEmpty(etOtp.getText().toString())) {

                        progress_bar.setVisibility(View.VISIBLE);
                        catchOTPVerification(mEmail, etOtp.getText().toString(), dialog);

                    } else {

                        etOtp.requestFocus();
                        etOtp.setError("Can't empty");

                    }

                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Check Internet Connection", Toast.LENGTH_LONG).show();
                }

            }

        });

        dialog.show();

    }

    private void catchOTPVerification(String mEmail, String mOTP, final Dialog dialog) {

        textInputLayoutEmail.setVisibility(View.GONE);

        cvReset.setVisibility(View.GONE);

        urlsApi.FORGET_PASSWORD_OTP_VERIFY_PKG_CALL(mOTP, mEmail).enqueue(new Callback<ForgetPasswordOtpVerifyPkg>() {

            @Override
            public void onResponse(Call<ForgetPasswordOtpVerifyPkg> call, Response<ForgetPasswordOtpVerifyPkg> response) {

                try {

                    if (!response.isSuccessful()) {

                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(ForgetPasswordActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        return;

                    }

                    ForgetPasswordOtpVerifyPkg forgetPasswordOtpVerifyPkg = response.body();
                    assert forgetPasswordOtpVerifyPkg != null;

                    textInputLayoutNewPassword.setVisibility(View.VISIBLE);
                    cvNewPassword.setVisibility(View.VISIBLE);

                    textInputLayoutNewPasswordConfg.setVisibility(View.VISIBLE);

                    mAccessToken = forgetPasswordOtpVerifyPkg.getToken().toString();

                    dialog.dismiss();

                    try {

                        if (forgetPasswordOtpVerifyPkg.getErr().length() > 1) {

                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(ForgetPasswordActivity.this, forgetPasswordOtpVerifyPkg.getErr(), Toast.LENGTH_SHORT).show();

                        }

                    } catch (Exception e) {

                        emptyInputEditText();
                        progress_bar.setVisibility(View.GONE);

                    }

                } catch (Exception e) {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(ForgetPasswordActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ForgetPasswordOtpVerifyPkg> call, Throwable t) {

            }
        });
    }

    private void NewPasswordMethod() {

        Map<String, String> header = new HashMap<>();
        header.put("x-access-token", mAccessToken);

        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("email", mEmail);
        stringMap.put("newPassword", mPassword);

        urlsApi.NEW_PASSWORD_PKG_CALL(header, stringMap).enqueue(new Callback<NewPasswordPkg>() {

            @Override
            public void onResponse(Call<NewPasswordPkg> call, Response<NewPasswordPkg> response) {

                try {

                    if (!response.isSuccessful()) {

                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(ForgetPasswordActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        return;

                    }

                    NewPasswordPkg newPasswordPkg = response.body();
                    assert newPasswordPkg != null;

                    try {
                        if (newPasswordPkg.getErr().length() > 1) {

                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(ForgetPasswordActivity.this, newPasswordPkg.getErr(), Toast.LENGTH_SHORT).show();

                        }

                    } catch (Exception e) {

                        emptyInputEditText();

                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();

                        progress_bar.setVisibility(View.GONE);

                        Toast.makeText(ForgetPasswordActivity.this, newPasswordPkg.getData(), Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {

                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(ForgetPasswordActivity.this, "Error!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<NewPasswordPkg> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
        CustomIntent.customType(ForgetPasswordActivity.this, "fadein-to-fadeout");
        finish();

    }
}
