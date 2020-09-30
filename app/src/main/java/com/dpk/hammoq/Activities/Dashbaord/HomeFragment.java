package com.dpk.hammoq.Activities.Dashbaord;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dpk.hammoq.Activities.CameraActivity.CameraXActivity;
import com.dpk.hammoq.Adopter.CameraChooseImagesSupplyAdapter;
import com.dpk.hammoq.Adopter.ChooseImages;
import com.dpk.hammoq.Adopter.ChooseImagesSupplyAdapter;
import com.dpk.hammoq.R;
import com.dpk.hammoq.Utils.Appcontants;
import com.dpk.hammoq.utilsNetwork.RetrofitClient;
import com.dpk.hammoq.utilsNetwork.UrlsApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    private File compressedImage;
    private List<ChooseImages> chooseImages = new ArrayList<>();
    private RecyclerView image_choose_imageView, image_choose_imageView1;
    private CameraChooseImagesSupplyAdapter cameraSupplyAdapter;

    private Spinner spinnerGender, spinnerCat;
    private List<String> category;
    private List<String> category_id;

    private List<String> categoryCat;
    private List<String> category_idCat;

    private Button btnGallery, btnClick;
    private static final int CAMERA_REQUEST = 1888;
    private File actualImage;
    private ChooseImagesSupplyAdapter supplyAdapter;
    private String randomNumber, mToken;
    private UrlsApi urlsApi;
    private CardView progress_bar;
    private RelativeLayout Layout2;
    private static FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        urlsApi = RetrofitClient.getClient().create(UrlsApi.class);
        fragmentManager = getFragmentManager();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mToken = sharedPreferences.getString(Appcontants.LOGIN_USER_TOKEN, "");

        init(view);

        return view;

    }

    private void init(View view) {
        progress_bar = view.findViewById(R.id.progress_bar);

        btnClick = view.findViewById(R.id.btnClickCamera);
        btnClick.setOnClickListener(this::onClick);

        btnGallery = view.findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(this::onClick);

        image_choose_imageView = view.findViewById(R.id.image_choose_imageView);

        int numberOfColumns = 2;
        image_choose_imageView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        supplyAdapter = new ChooseImagesSupplyAdapter(getActivity(), chooseImages, uriArrayList);
        image_choose_imageView.setAdapter(supplyAdapter);

        spinnerCat = view.findViewById(R.id.spinnerCat);
        category = new ArrayList<>();
        category_id = new ArrayList<>();

        category.add("Gender");
        category_id.add("0");

        category.add("Male");
        category_id.add("1");

        category.add("Female");
        category_id.add("2");

        category.add("Others");
        category_id.add("3");

        spinnerGender = view.findViewById(R.id.spinnerGender);
        categoryCat = new ArrayList<>();
        category_idCat = new ArrayList<>();

        categoryCat.add("Condition");
        category_idCat.add("0");

        categoryCat.add("New with tags");
        category_idCat.add("1");

        categoryCat.add("New without tags");
        category_idCat.add("2");

        categoryCat.add("Used");
        category_idCat.add("3");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryCat);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<String> spinnerGenderArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, category);
        spinnerGenderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(spinnerGenderArrayAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClickCamera:
                startActivity(new Intent(getActivity(), CameraXActivity.class));
                break;

            case R.id.btnGallery:
//                startActivity(new Intent(getActivity(), CameraXActivity.class));
                break;

        }
    }
}
