package com.dpk.hammoq.Fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dpk.hammoq.Activities.ForgetPasswordActivity.ForgetPasswordActivity;
import com.dpk.hammoq.Adapter.PaymentDetailsSupplyAdapter;
import com.dpk.hammoq.GsonModule.ForgetPasswordModule.ForgetPasswordPkg;
import com.dpk.hammoq.GsonModule.PaymentDetailsModule.PaymentDetailsPkg;
import com.dpk.hammoq.GsonModule.PaymentDetailsModule.Result;
import com.dpk.hammoq.R;
import com.dpk.hammoq.Utils.Appcontants;
import com.dpk.hammoq.utilsNetwork.RetrofitClient;
import com.dpk.hammoq.utilsNetwork.UrlsApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailsFragment extends Fragment {

    private UrlsApi urlsApi;
    private String mAccessToken;
    private RecyclerView rvPayment;
    private CardView progress_bar;
    private List<Result> paymentList = new ArrayList();
    private PaymentDetailsSupplyAdapter supplyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment_details, container, false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mAccessToken = sharedPreferences.getString(Appcontants.LOGIN_USER_TOKEN, "");

        urlsApi = RetrofitClient.getClient().create(UrlsApi.class);

        PayDetailsMethod();

        init(v);

        return v;
    }

    private void init(View v) {
        rvPayment = v.findViewById(R.id.rvPayment);
        progress_bar = v.findViewById(R.id.progress_bar);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvPayment.setLayoutManager(layoutManager);
        supplyAdapter = new PaymentDetailsSupplyAdapter(getActivity(), paymentList);
        rvPayment.setAdapter(supplyAdapter);

    }

    private void PayDetailsMethod() {

        Map<String, String> header = new HashMap<>();
        header.put("x-access-token", mAccessToken);

        Log.e("Token" , "Token----> " + mAccessToken);

        urlsApi.PAYMENT_DETAILS_PKG_CALL(header).enqueue(new Callback<PaymentDetailsPkg>() {

            @Override
            public void onResponse(Call<PaymentDetailsPkg> call, Response<PaymentDetailsPkg> response) {

                try {

                    if (!response.isSuccessful()) {
                        Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), String.valueOf(response.message()), Toast.LENGTH_SHORT).show();
                        progress_bar.setVisibility(View.GONE);
                        return;
                    }

                    PaymentDetailsPkg paymentDetailsPkg = response.body();
                    assert paymentDetailsPkg != null;

                    try {
                        if (paymentDetailsPkg.getErr().length() > 1) {
                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), paymentDetailsPkg.getErr(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        paymentList.addAll(paymentDetailsPkg.getResult());
                        supplyAdapter.notifyDataSetChanged();
                        progress_bar.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentDetailsPkg> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
            }
        });
    }
}
