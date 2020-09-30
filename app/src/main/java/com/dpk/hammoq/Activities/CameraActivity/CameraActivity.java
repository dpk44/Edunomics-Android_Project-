package com.dpk.hammoq.Activities.CameraActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dpk.hammoq.Activities.GalleryActivity.ImagesActivity;
import com.dpk.hammoq.Activities.RegisterActivity.RegisterActivity;
import com.dpk.hammoq.Adopter.ChooseImages;
import com.dpk.hammoq.Adopter.ChooseImagesSupplyAdapter;
import com.dpk.hammoq.GsonModule.ProductListingApiModule.ProductListApi;
import com.dpk.hammoq.R;
import com.dpk.hammoq.Utils.Appcontants;
import com.dpk.hammoq.utilsNetwork.RetrofitClient;
import com.dpk.hammoq.utilsNetwork.UrlsApi;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import maes.tech.intentanim.CustomIntent;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    ImageSwitcher ivImage;

    public int PIC_CODE = 0;
    public int PICKER_REQUEST_CODE = 100;
    int position = 0;
    ArrayList<Uri> uriArrayList = new ArrayList<>();

    private static final int CAMERA_REQUEST = 1888;
    private File actualImage;

    private File compressedImage;

    private RecyclerView image_choose_imageView;
    private List<ChooseImages> chooseImages = new ArrayList<>();
    private ChooseImagesSupplyAdapter supplyAdapter;

    String randomNumber, mToken;

    UrlsApi urlsApi;

    private static FragmentManager fragmentManager;
    private AnimatedBottomBar animatedBottomBar;

    CardView progress_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        @SuppressLint("WrongViewCast")
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        urlsApi = RetrofitClient.getClient().create(UrlsApi.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(Appcontants.LOGIN_USER_TOKEN, "");

        progress_bar = findViewById(R.id.progress_bar);

        Button btnClick = findViewById(R.id.btnClick);
        btnClick.setOnClickListener(this::onClick);

        Button btnGallery = findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(this::onClick);


        permissons();


        image_choose_imageView = findViewById(R.id.image_choose_imageView);

        int numberOfColumns = 2;
        image_choose_imageView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        supplyAdapter = new ChooseImagesSupplyAdapter(this, chooseImages, uriArrayList);
        image_choose_imageView.setAdapter(supplyAdapter);


        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{
                            Manifest.permission.CAMERA
                    },
                    CAMERA_REQUEST);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            //TODO... onCamera Picker Result
            case CAMERA_REQUEST:
                try {
                    if (resultCode == RESULT_OK) {

                        actualImage = new File(Environment.getExternalStorageDirectory(), randomNumber + ".jpg");

                        Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", actualImage);

                        uriArrayList.add(uri);

                        Log.e("Photo URI", "onActivityResult: " + actualImage);

                        try {
                            compressedImage = new Compressor(this)
                                    .setMaxWidth(640)
                                    .setMaxHeight(480)
                                    .setQuality(75)
                                    .setCompressFormat(android.graphics.Bitmap.CompressFormat.WEBP)
                                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                    .compressToFile(actualImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        chooseImages.add(new ChooseImages(actualImage.getAbsolutePath()));
                        supplyAdapter.notifyDataSetChanged();

                        clickImg();
                        Toast.makeText(this, "Image added successfully!", Toast.LENGTH_SHORT).show();

//                        ProductListingMethod();

                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void permissons() {

        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(CameraActivity.this, perms)) {

            Log.e("TAG", "permissons: granted");

        } else {

            Log.e("TAG", "permissons: denied");
            EasyPermissions.requestPermissions(CameraActivity.this, "We need permissions For Take Photo and Video",
                    123, perms);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClick:

                clickImg();
                CustomIntent.customType(CameraActivity.this, "fadein-to-fadeout");


                break;

            case R.id.btnGallery:
                startActivity(new Intent(CameraActivity.this, ImagesActivity.class));
                break;

        }
    }

    private void clickImg() {

        if (chooseImages.size() < 12) {
            randomNumber = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(Environment.getExternalStorageDirectory(), String.valueOf(randomNumber) + ".jpg");
            Uri uri = FileProvider.getUriForFile(CameraActivity.this, this.getApplicationContext().getPackageName() + ".provider", file);
            m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(m_intent, CAMERA_REQUEST);

        } else {
            Toast.makeText(this, "You can choose upto 12 images", Toast.LENGTH_SHORT).show();
        }
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri uri) {

        File file = actualImage;

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);


    }

   /* private void ProductListingMethod() {
        progress_bar.setVisibility(View.VISIBLE);

        List<MultipartBody.Part> billImages = new ArrayList<>();
        MultipartBody.Part mReqType = MultipartBody.Part.createFormData("condition_name", "new");

        for (int i = 0; i < chooseImages.size(); i++) {

            billImages.add(prepareFilePart("default_image", uriArrayList.get(i)));

            Log.e("File URI", "----File URI---: " + uriArrayList.get(i));

        }

        urlsApi.PRODUCT_LIST_API_CAL(billImages, mReqType, mToken).enqueue(new Callback<ProductListApi>() {

            @Override
            public void onResponse(Call<ProductListApi> call, Response<ProductListApi> response) {

                try {

                    if (!response.isSuccessful()) {
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(CameraActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ProductListApi productListApi = response.body();
                    assert productListApi != null;

                    try {
                        if (productListApi.getErr().length() > 1) {
                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(CameraActivity.this, productListApi.getErr(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(CameraActivity.this, productListApi.getId(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(CameraActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductListApi> call, Throwable t) {
            }
        });
    } */

}
