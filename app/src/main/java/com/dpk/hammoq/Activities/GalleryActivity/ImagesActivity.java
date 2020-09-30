package com.dpk.hammoq.Activities.GalleryActivity;


import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dpk.hammoq.Adapter.ImageAdapter;
import com.dpk.hammoq.Adapter.ImageModel;
import com.dpk.hammoq.Adapter.SelectedImageAdapter;
import com.dpk.hammoq.GsonModule.ProductListingApiModule.ProductListApi;
import com.dpk.hammoq.R;
import com.dpk.hammoq.Utils.Appcontants;
import com.dpk.hammoq.utilsNetwork.RetrofitClient;
import com.dpk.hammoq.utilsNetwork.UrlsApi;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesActivity extends Activity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGES = 2;
    public static final int STORAGE_PERMISSION = 100;

    ArrayList<ImageModel> imageList;
    private List<Uri> chooseImagesURI = new ArrayList<>();
    ArrayList<String> selectedImageList;
    RecyclerView imageRecyclerView, selectedImageRecyclerView;
    int[] resImg = {R.drawable.ic_camera_white_30dp, R.drawable.ic_folder_white_30dp};
    String[] title = {"Camera", "Folder"};
    String mCurrentPhotoPath, absolutePathOfImage, mToken;
    SelectedImageAdapter selectedImageAdapter;
    ImageAdapter imageAdapter;
    String[] projection = {MediaStore.MediaColumns.DATA};
    File actualimage;
    Button done;
    Snackbar snackbar;
    UrlsApi urlsApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        if (isStoragePermissionGranted()) {
            init();
            getAllImages();
            setImageList();
            setSelectedImageList();
        }

        urlsApi = RetrofitClient.getClient().create(UrlsApi.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(Appcontants.LOGIN_USER_TOKEN, "");
    }

    public void init() {
        imageRecyclerView = findViewById(R.id.recycler_view);
        selectedImageRecyclerView = findViewById(R.id.selected_recycler_view);
        done = findViewById(R.id.done);
        selectedImageList = new ArrayList<>();
        imageList = new ArrayList<>();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actualimage = new File(String.valueOf(selectedImageList)).getAbsoluteFile();

                if (selectedImageList.size() < 2) {
                    ProductListingMethod();

                    for (int i = 0; i < selectedImageList.size(); i++) {
                        Toast.makeText(getApplicationContext(), selectedImageList.get(i), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(v,"This is Simple Snackbar",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    Toast.makeText(ImagesActivity.this, "You can choose 12 images only", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setImageList() {
        imageRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        imageAdapter = new ImageAdapter(getApplicationContext(), imageList);
        imageRecyclerView.setAdapter(imageAdapter);

        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (position == 0) {
                    takePicture();
                } else if (position == 1) {
                    getPickImageIntent();
                } else {
                    try {
                        if (!imageList.get(position).isSelected()) {
                            selectImage(position);

                        } else {
                            unSelectImage(position);
                        }
                    } catch (ArrayIndexOutOfBoundsException ed) {
                        ed.printStackTrace();
                    }
                }
            }
        });
        setImagePickerList();
    }

    public void setSelectedImageList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        selectedImageRecyclerView.setLayoutManager(layoutManager);
        selectedImageAdapter = new SelectedImageAdapter(this, selectedImageList);
        selectedImageRecyclerView.setAdapter(selectedImageAdapter);
    }

    // Add Camera and Folder in ArrayList
    public void setImagePickerList() {
        for (int i = 0; i < resImg.length; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setResImg(resImg[i]);
            imageModel.setTitle(title[i]);
            imageList.add(i, imageModel);
        }
        imageAdapter.notifyDataSetChanged();
    }

    // get all images from external storage
    public void getAllImages() {
        imageList.clear();
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        while (cursor.moveToNext()) {
            String absolutePathOfImage = "file:" + cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            ImageModel ImageModel = new ImageModel();
            ImageModel.setImage(absolutePathOfImage);
            imageList.add(ImageModel);
        }
        cursor.close();
    }

    // start the image capture Intent
    public void takePicture() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Continue only if the File was successfully created;
        File photoFile = createImageFile();
        if (photoFile != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void getPickImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGES);
    }

    // Add image in SelectedArrayList
    public void selectImage(int position) {
        // Check before add new item in ArrayList;
        if (!selectedImageList.contains(imageList.get(position).getImage())) {
            imageList.get(position).setSelected(true);
            selectedImageList.add(0, imageList.get(position).getImage());
            selectedImageAdapter.notifyDataSetChanged();
            imageAdapter.notifyDataSetChanged();
        }
    }

    // Remove image from selectedImageList
    public void unSelectImage(int position) {
        for (int i = 0; i < selectedImageList.size(); i++) {
            if (imageList.get(position).getImage() != null) {
                if (selectedImageList.get(i).equals(imageList.get(position).getImage())) {
                    imageList.get(position).setSelected(false);
                    selectedImageList.remove(i);
                    selectedImageAdapter.notifyDataSetChanged();
                    imageAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public File createImageFile() {
        // Create an image file name
        String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + dateTime + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            actualimage = File.createTempFile(imageFileName, ".jpg", storageDir);
            Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", actualimage);
            chooseImagesURI.add(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + actualimage.getAbsolutePath();

        return actualimage;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (mCurrentPhotoPath != null) {
                    addImage(mCurrentPhotoPath);
                    takePicture();

                    Log.e("TAG", "ActualImage:---> " + String.valueOf(mCurrentPhotoPath));
                }
            } else if (requestCode == PICK_IMAGES) {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        getImageFilePath(uri);
                        chooseImagesURI.add(uri);

                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    getImageFilePath(uri);
                    chooseImagesURI.add(uri);
                    Log.e("TAG", "ElseActualImage:---> " + String.valueOf(uri));
                }
            }
        }
    }

    // Get image file path
    public void getImageFilePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                if (absolutePathOfImage != null) {
                    checkImage(absolutePathOfImage);
                } else {
                    checkImage(String.valueOf(uri));
                }
            }
        }
    }

    public void checkImage(String filePath) {
        if (!selectedImageList.contains(filePath)) {
            for (int pos = 0; pos < imageList.size(); pos++) {
                if (imageList.get(pos).getImage() != null) {
                    if (imageList.get(pos).getImage().equalsIgnoreCase(filePath)) {
                        imageList.remove(pos);
                    }
                }
            }
            addImage(filePath);

        }
    }

    // add image in selectedImageList and imageList
    public void addImage(String filePath) {
        ImageModel imageModel = new ImageModel();
        imageModel.setImage(filePath);
        imageModel.setSelected(true);
        imageList.add(2, imageModel);
        selectedImageList.add(0, filePath);
        selectedImageAdapter.notifyDataSetChanged();
        imageAdapter.notifyDataSetChanged();
    }

    public boolean isStoragePermissionGranted() {
        int ACCESS_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if ((ACCESS_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
            getAllImages();
            setImageList();
            setSelectedImageList();
        }
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        actualimage = new File(String.valueOf(selectedImageList));

        RequestBody requestFile = RequestBody.create(
                        MediaType.parse("image/*"),
                        actualimage);

        return MultipartBody.Part.createFormData(partName, actualimage.getName(), requestFile);


    }

    private void ProductListingMethod() {

        List<MultipartBody.Part> billImages = new ArrayList<>();
        MultipartBody.Part mReqType = MultipartBody.Part.createFormData("condition_name", "new");

        for (int i = 0; i < imageList.size(); i++) {


            billImages.add(prepareFilePart("default_image", chooseImagesURI.get(i)));

            Log.e("File URI", "----File URI---: " + chooseImagesURI.get(i));


        }

//        urlsApi.PRODUCT_LIST_API_CAL(billImages, mReqType, mToken).enqueue(new Callback<ProductListApi>() {
//
//            @Override
//            public void onResponse(Call<ProductListApi> call, Response<ProductListApi> response) {
//
//                try {
//
//                    if (!response.isSuccessful()) {
//                        Toast.makeText(ImagesActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    ProductListApi productListApi = response.body();
//                    assert productListApi != null;
//
//                    try {
//                        if (productListApi.getErr().length() > 1) {
//                            Toast.makeText(ImagesActivity.this, productListApi.getErr(), Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                        Toast.makeText(ImagesActivity.this, productListApi.getId(), Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception e) {
//                    Toast.makeText(ImagesActivity.this, "Error!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProductListApi> call, Throwable t) {
//            }
//        });
    }

}
