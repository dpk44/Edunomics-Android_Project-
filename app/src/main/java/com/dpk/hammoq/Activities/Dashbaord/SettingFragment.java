package com.dpk.hammoq.Activities.Dashbaord;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dpk.hammoq.Activities.LoginActivity.LoginActivity;
import com.dpk.hammoq.Fragments.PaymentDetailsFragment;
import com.dpk.hammoq.Fragments.SettingLoginsFragment;
import com.dpk.hammoq.GsonModule.ChangePasswordModule.ChangePasswordPkg;
import com.dpk.hammoq.R;
import com.dpk.hammoq.Utils.Appcontants;
import com.dpk.hammoq.utilsNetwork.CheckNetworkAccess;
import com.dpk.hammoq.utilsNetwork.RetrofitClient;
import com.dpk.hammoq.utilsNetwork.UrlsApi;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment implements View.OnClickListener {

    private UrlsApi urlsApi;
    private CardView cvPayDetails, cvChangePay, cvChangePass, cvLogin, cvConfig, cvLogout;
    private View top_view;
    private Dialog dialog;
    private Button Pay;
    private String mOldPass, mNewPass, mAccessToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mAccessToken = sharedPreferences.getString(Appcontants.LOGIN_USER_TOKEN, "");

        urlsApi = RetrofitClient.getClient().create(UrlsApi.class);
        init(v);
        return v;
    }

    private void init(View v) {
        Pay = v.findViewById(R.id.Pay);
        top_view = v.findViewById(R.id.top_view);
        cvPayDetails = v.findViewById(R.id.cvPayDetails);
        cvChangePay = v.findViewById(R.id.cvChangePay);
        cvChangePass = v.findViewById(R.id.cvChangePass);
        cvLogin = v.findViewById(R.id.cvLogin);
        cvConfig = v.findViewById(R.id.cvConfig);
        cvLogout = v.findViewById(R.id.cvLogout);

        cvLogout.setOnClickListener(this);
        cvConfig.setOnClickListener(this);
        cvLogin.setOnClickListener(this);
        cvChangePass.setOnClickListener(this);
        cvChangePay.setOnClickListener(this);
        cvPayDetails.setOnClickListener(this);
        Pay.setOnClickListener(this);
    }


    public void onEnterAnimationComplete(View v) {
        super.getActivity().onEnterAnimationComplete();
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        top_view.setVisibility(View.VISIBLE);
        top_view.startAnimation(anim);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_to_up);
        top_view.startAnimation(animation);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvPayDetails:
                final FragmentTransaction settingTransaction = getFragmentManager().beginTransaction();
                settingTransaction.replace(R.id.frameContainer, new PaymentDetailsFragment(), "SettingFrag");
                settingTransaction.addToBackStack("SettingFrag");
                settingTransaction.commit();
                break;

            case R.id.cvChangePay:
                onEnterAnimationComplete(v);
                break;

            case R.id.cvChangePass:
                createDialogChangePass();
                break;

            case R.id.cvLogin:
                final FragmentTransaction LoginTransaction = getFragmentManager().beginTransaction();
                LoginTransaction.replace(R.id.frameContainer, new SettingLoginsFragment(), "LoginFrag");
                LoginTransaction.addToBackStack("LoginFrag");
                LoginTransaction.commit();
                break;

            case R.id.cvConfig:
                createDialogConfig();
                break;

            case R.id.Pay:
                top_view.setVisibility(View.GONE);
                break;

            case R.id.cvLogout:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }

    private void createDialogChangePass() {
        top_view.setVisibility(View.GONE);
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.change_password_layout);

        Button btnOtpVerify = dialog.findViewById(R.id.btnOtpVerify);

        final TextInputEditText et_oldPass = dialog.findViewById(R.id.et_oldPass);
        mOldPass = et_oldPass.getText().toString();

        final TextInputEditText et_newPassword = dialog.findViewById(R.id.et_newPassword);
        mNewPass = et_newPassword.getText().toString();

        final TextInputEditText et_newpasswordConfg = dialog.findViewById(R.id.et_newpasswordConfg);

        btnOtpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkAccess.isNetAvailable(getActivity())) {

                    if (!TextUtils.isEmpty(et_oldPass.getText().toString()) || !TextUtils.isEmpty(et_newPassword.getText().toString())
                            || !TextUtils.isEmpty(et_newpasswordConfg.getText().toString())) {

                        ChangePasswordMethod();

                    } else {

                        et_oldPass.requestFocus();
                        et_oldPass.setError("Can't empty");

                        et_newPassword.requestFocus();
                        et_newPassword.setError("Can't empty");

                        et_newpasswordConfg.requestFocus();
                        et_newpasswordConfg.setError("Can't empty");

                    }

                } else {
                    Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }

            }

        });

        dialog.show();

    }

    private void createDialogConfig() {
        top_view.setVisibility(View.GONE);
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.config_layout);

     /*   AppCompatButton btnOtpVerify = dialog.findViewById(R.id.btnOtpVerify);

        final TextInputEditText etOtp = dialog.findViewById(R.id.et_otp);

        btnOtpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkAccess.isNetAvailable(getActivity())) {

                    if (!TextUtils.isEmpty(etOtp.getText().toString())) {

                    } else {

                        etOtp.requestFocus();
                        etOtp.setError("Can't empty");

                    }

                } else {
                    Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_LONG).show();
                }

            }

        }); */

        dialog.show();

    }

    private void ChangePasswordMethod() {

        Map<String, String> header = new HashMap<>();
        header.put("x-access-token", mAccessToken);

        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("oldPassword", mOldPass);
        stringMap.put("newPassword", mNewPass);

        urlsApi.CHANGE_PASSWORD_PKG_CALL(header, stringMap).enqueue(new Callback<ChangePasswordPkg>() {

            @Override
            public void onResponse(Call<ChangePasswordPkg> call, Response<ChangePasswordPkg> response) {

                try {

                    if (!response.isSuccessful()) {
                        Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ChangePasswordPkg changePasswordPkg = response.body();
                    assert changePasswordPkg != null;

                    try {
                        if (changePasswordPkg.getErr().length() > 1) {

                            Toast.makeText(getActivity(), changePasswordPkg.getErr(), Toast.LENGTH_SHORT).show();

                        }

                    } catch (Exception e) {

                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ChangePasswordPkg> call, Throwable t) {
            }
        });
    }


}
