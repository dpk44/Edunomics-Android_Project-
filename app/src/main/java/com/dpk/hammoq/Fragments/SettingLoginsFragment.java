package com.dpk.hammoq.Fragments;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dpk.hammoq.Adapter.SettingLoginClientPassSupplyAdapter;
import com.dpk.hammoq.GsonModule.SettingLoginClientPassModule.Password;
import com.dpk.hammoq.GsonModule.SettingLoginClientPassModule.SettingLoginClientPassPkg;
import com.dpk.hammoq.R;
import com.dpk.hammoq.Utils.Appcontants;
import com.dpk.hammoq.utilsNetwork.CheckNetworkAccess;
import com.dpk.hammoq.utilsNetwork.RetrofitClient;
import com.dpk.hammoq.utilsNetwork.UrlsApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingLoginsFragment extends Fragment {

    private Spinner spinner;
    private Button btnAdd;

    String listOfWebsite[] = {"Please select a site to list on", "Ebay", "Poshmark", "Mercari", "Others"};
    ArrayList arrayList = new ArrayList(Arrays.asList(listOfWebsite));

    private RecyclerView recyclerView;
    private UrlsApi urlsApi;

    private String mAccessToken , mDelete;

    private List<Password> passwordList = new ArrayList();
    private SettingLoginClientPassSupplyAdapter supplyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_logins, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mAccessToken = sharedPreferences.getString(Appcontants.LOGIN_USER_TOKEN, "");

        init(v);

        urlsApi = RetrofitClient.getClient().create(UrlsApi.class);

        if (CheckNetworkAccess.isNetAvailable(getActivity())) {
            getPassList();
        } else {
            Toast.makeText(getActivity(), "Check Network Connection", Toast.LENGTH_LONG).show();
        }

        spinner = v.findViewById(R.id.spinner);
        btnAdd = v.findViewById(R.id.btnAdd);


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item != "Please select a site to list on" && item != "Others") {
                            arrayList.remove(String.valueOf(item));
                            mDelete = String.valueOf(arrayList.remove(String.valueOf(item)));
                            spinnerArrayAdapter.notifyDataSetChanged();
                            spinner.setAdapter(spinnerArrayAdapter);
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    private void init(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.rvLoginsHistory);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        supplyAdapter = new SettingLoginClientPassSupplyAdapter(getActivity(), passwordList);
        recyclerView.setAdapter(supplyAdapter);
    }

    private void getPassList() {

        Map<String, String> header = new HashMap<>();
        header.put("x-access-token", mAccessToken);

        Log.e("Token", "Token----> " + mAccessToken);

        urlsApi.SETTING_LOGIN_CLIENT_PASS_PKG_CALL(header).enqueue(new Callback<SettingLoginClientPassPkg>() {

            @Override
            public void onResponse(Call<SettingLoginClientPassPkg> call, Response<SettingLoginClientPassPkg> response) {

                try {

                    if (!response.isSuccessful()) {
                        Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), String.valueOf(response.message()), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SettingLoginClientPassPkg settingLoginClientPassPkg = response.body();
                    assert settingLoginClientPassPkg != null;

                    try {
                        if (settingLoginClientPassPkg.getErr().length() > 1) {
                            Toast.makeText(getActivity(), settingLoginClientPassPkg.getErr(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        passwordList.addAll(settingLoginClientPassPkg.getPasswords());
                        supplyAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SettingLoginClientPassPkg> call, Throwable t) {
            }
        });
    }

}
