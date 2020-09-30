package com.dpk.hammoq.Activities.CameraActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dpk.hammoq.Adopter.CameraChooseImagesSupplyAdapter;
import com.dpk.hammoq.Adopter.ChooseImages;
import com.dpk.hammoq.Adopter.ChooseImagesSupplyAdapter;
import com.dpk.hammoq.GsonModule.ProductListingApiModule.ProductListApi;
import com.dpk.hammoq.R;
import com.dpk.hammoq.Utils.Appcontants;
import com.dpk.hammoq.utilsNetwork.RetrofitClient;
import com.dpk.hammoq.utilsNetwork.UrlsApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraXActivity extends AppCompatActivity {


    ImageView btnTakePicture;
    Button btnUpload;
    Button btnDone;
    TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();


    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    private String cameraId;
    CameraDevice cameraDevice;
    CameraCaptureSession cameraCaptureSession;
    CaptureRequest captureRequest;
    CaptureRequest.Builder captureRequestBuilder;

    private Size imageDimensions;
    private ImageReader imageReader;
    private File file;
    Handler mBackgroundHandler;
    HandlerThread mBackgroundThread;


    private ArrayList<Uri> chooseImagesURI = new ArrayList<>();
    private List<ChooseImages> chooseImages = new ArrayList<>();
    private RecyclerView image_choose_imageView1;

    private RecyclerView image_choose_imageView;
    private CameraChooseImagesSupplyAdapter cameraSupplyAdapter;
    private ChooseImagesSupplyAdapter supplyAdapter;
    private TextView tvCount;
    UrlsApi urlsApi;
    private RelativeLayout Layout1, Layout2;
    private String mToken;
    private CardView progress_bar;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_x);

        Window window = CameraXActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        urlsApi = RetrofitClient.getClient().create(UrlsApi.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(Appcontants.LOGIN_USER_TOKEN, "");

        progress_bar = findViewById(R.id.progress_bar);


        textureView = (TextureView) findViewById(R.id.texture);
        btnTakePicture = (ImageView) findViewById(R.id.btnTakePicture);
        textureView.setSurfaceTextureListener(textureListener);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (chooseImages.size() < 12) {
                        takePicture();
                    } else {
                        Toast.makeText(CameraXActivity.this, "You can choose upto 12 images", Toast.LENGTH_SHORT).show();
                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            }
        });

        btnDone = findViewById(R.id.btnDone);
        btnUpload = findViewById(R.id.btnUpload);
        tvCount = findViewById(R.id.tvCount);
        Layout1 = findViewById(R.id.Layout1);
        Layout2 = findViewById(R.id.Layout2);
        btnDone.setEnabled(false);
        btnDone.setTextColor(Color.parseColor("#333333"));
        image_choose_imageView1 = findViewById(R.id.image_choose_imageView1);
        image_choose_imageView = findViewById(R.id.image_choose_imageView);

        int i = 3;
        image_choose_imageView.setLayoutManager(new GridLayoutManager(this, i));
        supplyAdapter = new ChooseImagesSupplyAdapter(this, chooseImages, chooseImagesURI);
        image_choose_imageView.setAdapter(supplyAdapter);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Layout1.setVisibility(View.GONE);
                Layout2.setVisibility(View.VISIBLE);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CameraXActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();
                ProductListingMethod();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getApplicationContext(), "Sorry, camera permission is necessary", Toast.LENGTH_LONG).show();
                //   finish();

            }
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            try {
                createCameraPreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    private void createCameraPreview() throws CameraAccessException {
        SurfaceTexture texture = textureView.getSurfaceTexture();
        texture.setDefaultBufferSize(imageDimensions.getWidth(), imageDimensions.getHeight());
        Surface surface = new Surface(texture);

        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        captureRequestBuilder.addTarget(surface);

        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                if (cameraDevice == null) {
                    return;
                }

                cameraCaptureSession = session;
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {
                Toast.makeText(getApplicationContext(), "Configuration Changed", Toast.LENGTH_LONG).show();
            }
        }, null);
    }

    private void updatePreview() throws CameraAccessException {
        if (cameraDevice == null) {
            return;
        }

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);

    }


    private void openCamera() throws CameraAccessException {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        cameraId = manager.getCameraIdList()[0];

        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        imageDimensions = map.getOutputSizes(SurfaceTexture.class)[0];

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraXActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            return;
        }

        manager.openCamera(cameraId, stateCallback, null);

    }

    private void takePicture() throws CameraAccessException {
        if (cameraDevice == null) {
            return;

        }

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);


        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
        Size[] jpegSizes = null;

        jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);

        int width = 640;
        int height = 480;

        if (jpegSizes != null && jpegSizes.length > 0) {
            width = jpegSizes[0].getWidth();
            height = jpegSizes[0].getHeight();
        }

        ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
        List<Surface> outputSurfaces = new ArrayList<>(2);
        outputSurfaces.add(reader.getSurface());

        outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

        final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        captureBuilder.addTarget(reader.getSurface());
        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));


        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        file = new File(Environment.getExternalStorageDirectory() + "/" + ts + ".jpg");

        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + ts + ".jpg"));
        chooseImagesURI.add(uri);
        Log.e("TAG", "IMG_URI---->" + uri);

        chooseImages.add(new ChooseImages(file.getAbsolutePath()));
        supplyAdapter.notifyDataSetChanged();

        int mNumbItems = chooseImages.size();
        tvCount.setText(mNumbItems + "/" + "12");

        if (chooseImages.size() > 0) {
            btnDone.setEnabled(true);
            btnDone.setTextColor(Color.parseColor("#FFFFFF"));
        }

        MediaActionSound sound = new MediaActionSound();
        sound.play(MediaActionSound.SHUTTER_CLICK);

        ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = null;

                image = reader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
                try {
                    save(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (image != null) {
                        image.close();
                    }
                }

            }
        };

        reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);


        final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                try {
                    createCameraPreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            }
        };

        cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                try {
                    session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {

            }
        }, mBackgroundHandler);


    }

    private void save(byte[] bytes) throws IOException {
        OutputStream outputStream = null;

        outputStream = new FileOutputStream(file);

        outputStream.write(bytes);

        outputStream.close();

    }

    @Override
    protected void onResume() {
        super.onResume();

        startBackgroundThread();

        if (textureView.isAvailable()) {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());

    }


    @Override
    protected void onPause() {
        try {
            stopBackgroundThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    protected void stopBackgroundThread() throws InterruptedException {
        mBackgroundThread.quitSafely();

        mBackgroundThread.join();
        mBackgroundThread = null;
        mBackgroundHandler = null;

    }


    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri uri) {

        File actualFile = file;

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        actualFile);

        return MultipartBody.Part.createFormData(partName, actualFile.getName(), requestFile);


    }

    private void ProductListingMethod() {
        progress_bar.setVisibility(View.VISIBLE);
        MultipartBody.Part default_image = null;
        MultipartBody.Part brand_image = null;
        MultipartBody.Part model_image = null;
        MultipartBody.Part side1_image = null;
        MultipartBody.Part side2_image = null;
        MultipartBody.Part front_image = null;
        MultipartBody.Part back_image = null;
        MultipartBody.Part condition1_image = null;
        MultipartBody.Part condition2_image = null;
        MultipartBody.Part condition3_image = null;
        MultipartBody.Part condition4_image = null;
        MultipartBody.Part condition5_image = null;
        for (int i = 0; i < chooseImages.size(); i++) {

            RequestBody requestFileOne = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(chooseImagesURI));
            default_image = MultipartBody.Part.createFormData("default_image", String.valueOf(chooseImagesURI.get(0)), requestFileOne);
            try {
                brand_image = MultipartBody.Part.createFormData("brand_image", String.valueOf(chooseImagesURI.get(1)), requestFileOne);
            } catch (Exception e) {
                brand_image = MultipartBody.Part.createFormData("brand_image", "null");
            }
            try {
                model_image = MultipartBody.Part.createFormData("model_image", String.valueOf(chooseImagesURI.get(2)), requestFileOne);
            } catch (Exception e) {
                model_image = MultipartBody.Part.createFormData("model_image", "null");
            }
            try {
                side1_image = MultipartBody.Part.createFormData("side1_image", String.valueOf(chooseImagesURI.get(3)), requestFileOne);
            } catch (Exception e) {
                side1_image = MultipartBody.Part.createFormData("side1_image", "null");
            }
            try {
                side2_image = MultipartBody.Part.createFormData("side2_image", String.valueOf(chooseImagesURI.get(4)), requestFileOne);
            } catch (Exception e) {
                side2_image = MultipartBody.Part.createFormData("side2_image", "null");
            }
            try {
                front_image = MultipartBody.Part.createFormData("front_image", String.valueOf(chooseImagesURI.get(5)), requestFileOne);
            } catch (Exception e) {
                front_image = MultipartBody.Part.createFormData("front_image", "null");
            }
            try {
                back_image = MultipartBody.Part.createFormData("back_image", String.valueOf(chooseImagesURI.get(6)), requestFileOne);
            } catch (Exception e) {
                back_image = MultipartBody.Part.createFormData("back_image", "null");
            }
            try {
                condition1_image = MultipartBody.Part.createFormData("condition1_image", String.valueOf(chooseImagesURI.get(7)), requestFileOne);
            } catch (Exception e) {
                condition1_image = MultipartBody.Part.createFormData("condition1_image", "null");
            }
            try {
                condition2_image = MultipartBody.Part.createFormData("condition2_image", String.valueOf(chooseImagesURI.get(8)), requestFileOne);
            } catch (Exception e) {
                condition2_image = MultipartBody.Part.createFormData("condition2_image", "null");
            }
            try {
                condition3_image = MultipartBody.Part.createFormData("condition3_image", String.valueOf(chooseImagesURI.get(9)), requestFileOne);
            } catch (Exception e) {
                condition3_image = MultipartBody.Part.createFormData("condition3_image", "null");
            }
            try {
                condition4_image = MultipartBody.Part.createFormData("condition4_image", String.valueOf(chooseImagesURI.get(10)), requestFileOne);
            } catch (Exception e) {
                condition4_image = MultipartBody.Part.createFormData("condition4_image", "null");
            }
            try {
                condition5_image = MultipartBody.Part.createFormData("condition5_image", String.valueOf(chooseImagesURI.get(11)), requestFileOne);
            } catch (Exception e) {
                condition5_image = MultipartBody.Part.createFormData("condition5_image", "null");
            }

        }
        MultipartBody.Part mReqType = MultipartBody.Part.createFormData("condition_name", "new");

        urlsApi.PRODUCT_LIST_API_CAL(
                default_image,
                brand_image,
                model_image,
                side1_image,
                side2_image,
                front_image,
                back_image,
                condition1_image,
                condition2_image,
                condition3_image,
                condition4_image,
                condition5_image,
                mReqType, mToken).enqueue(new Callback<ProductListApi>() {

            @Override
            public void onResponse(Call<ProductListApi> call, Response<ProductListApi> response) {

                try {

                    if (!response.isSuccessful()) {
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(CameraXActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ProductListApi productListApi = response.body();
                    assert productListApi != null;

                    try {
                        if (productListApi.getErr().length() > 1) {
                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(CameraXActivity.this, productListApi.getErr(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(CameraXActivity.this, productListApi.getId(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(CameraXActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductListApi> call, Throwable t) {
            }
        });
    }
}
