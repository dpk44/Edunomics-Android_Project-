package com.dpk.hammoq.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dpk.hammoq.GsonModule.SettingClientLoginEditPassModule.SettingLoginClientEditPassPkg;
import com.dpk.hammoq.R;
import com.dpk.hammoq.Utils.Appcontants;
import com.dpk.hammoq.utilsNetwork.RetrofitClient;
import com.dpk.hammoq.utilsNetwork.UrlsApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginEditFragment extends Fragment implements View.OnClickListener {

    private UrlsApi urlsApi;

    private String mAccessToken, mWebType, mUser, mPass;

    private TextView tvSite, tvUsername, tvPassword;

    private Button btnEdit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_login_edit, container, false);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mAccessToken = sharedPreferences.getString(Appcontants.LOGIN_USER_TOKEN, "");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mWebType = String.valueOf(bundle.get("website"));
            mUser = String.valueOf(bundle.get("username"));
            mPass = String.valueOf(bundle.get("password"));
        }

        init(v);

        urlsApi = RetrofitClient.getClient().create(UrlsApi.class);

        return v;
    }

    private void init(View v) {
        btnEdit = v.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);
        tvSite = v.findViewById(R.id.tvSite);
        tvUsername = v.findViewById(R.id.tvUsername);
        tvPassword = v.findViewById(R.id.tvPassword);
        tvSite.setText(mWebType);
        tvUsername.setHint(mUser);
        tvPassword.setHint(mPass);

    }


    private boolean checkValidateInputs() {

        if (TextUtils.isEmpty(tvUsername.getText().toString().trim())) {
            tvUsername.setError("Enter Username");
            tvUsername.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(tvPassword.getText().toString().trim())) {
            tvPassword.setError("Enter Password");
            tvPassword.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEdit:
                boolean inputFlag = checkValidateInputs();
                if (inputFlag == true) {
                    String mUsername = tvUsername.getText().toString().trim();
                    String mPassword = tvPassword.getText().toString().trim();
                    getEditList(mUsername, mPassword);
                }
                break;
        }
    }

    private void getEditList(String mUsername, String mPassword) {

        Map<String, String> header = new HashMap<>();
        header.put("x-access-token", mAccessToken);

        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("website", mWebType);
        stringMap.put("username", mUsername);
        stringMap.put("password", mPassword);

        Log.e("Token", "Token----> " + mAccessToken);
        Log.e("Token", "mWebType----> " + mWebType);
        Log.e("Token", "mUsername----> " + mUsername);
        Log.e("Token", "mPassword----> " + mPassword);

        urlsApi.SETTING_LOGIN_CLIENT_EDIT_PASS_PKG_CALL(header, stringMap).enqueue(new Callback<SettingLoginClientEditPassPkg>() {

            @Override
            public void onResponse(Call<SettingLoginClientEditPassPkg> call, Response<SettingLoginClientEditPassPkg> response) {

                try {

                    if (!response.isSuccessful()) {
                        Toast.makeText(getActivity(), String.valueOf(response.code()+response.message()), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SettingLoginClientEditPassPkg settingLoginClientPassPkg = response.body();
                    assert settingLoginClientPassPkg != null;

                    try {
                        if (settingLoginClientPassPkg.getErr().length() > 1) {
                            Toast.makeText(getActivity(), settingLoginClientPassPkg.getErr(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), settingLoginClientPassPkg.getId(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SettingLoginClientEditPassPkg> call, Throwable t) {
            }
        });
    }
}
