package com.dpk.hammoq.Activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class Main2Activity extends AppCompatActivity {

    ImageButton button;
    TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();


    static {
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }



    private String cameraId, mToken;
    CameraDevice cameraDevice;
    CameraCaptureSession cameraCaptureSession;
    CaptureRequest captureRequest;
    CaptureRequest.Builder captureRequestBuilder;

    private Size imageDimensions;
    private ImageReader imageReader;
    private File file;
    private Uri ImgUri;
    private File actualImage;
    private UrlsApi urlsApi;
    Handler mBackgroundHandler;
    HandlerThread mBackgroundThread;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        urlsApi = RetrofitClient.getClient().create(UrlsApi.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Main2Activity.this);
        mToken = sharedPreferences.getString(Appcontants.LOGIN_USER_TOKEN, "");

        textureView = (TextureView)findViewById(R.id.texture);
        button = (ImageButton) findViewById(R.id.button_capture);
        textureView.setSurfaceTextureListener(textureListener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    takePicture();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                Toast.makeText(getApplicationContext(),"Sorry, camera permission is necessary",Toast.LENGTH_LONG).show();
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
        texture.setDefaultBufferSize(imageDimensions.getWidth(),imageDimensions.getHeight());
        Surface surface = new Surface(texture);

        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        captureRequestBuilder.addTarget(surface);

        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                if(cameraDevice==null)
                {
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
            public void onConfigureFailed( CameraCaptureSession session) {
                Toast.makeText(getApplicationContext(),"Configuration Changed",Toast.LENGTH_LONG).show();
            }
        },null);
    }

    private void updatePreview() throws CameraAccessException {
        if(cameraDevice == null)
        {
            return;
        }

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(),null,mBackgroundHandler);

    }


    private void openCamera() throws CameraAccessException {
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);

        cameraId = manager.getCameraIdList()[0];

        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        imageDimensions = map.getOutputSizes(SurfaceTexture.class)[0];

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Main2Activity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
            return;
        }

        manager.openCamera(cameraId,stateCallback,null);

    }

    private void takePicture() throws CameraAccessException {
        if(cameraDevice == null)
        {
            return;

        }

        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);


        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
        Size[] jpegSizes = null;

        jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);

        int width = 640;
        int height = 480;

        if(jpegSizes!=null && jpegSizes.length>0)
        {
            width = jpegSizes[0].getWidth();
            height = jpegSizes[0].getHeight();
        }

        ImageReader reader = ImageReader.newInstance(width,height,ImageFormat.JPEG,1);
        List<Surface> outputSurfaces = new ArrayList<>(2);
        outputSurfaces.add(reader.getSurface());

        outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

        final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        captureBuilder.addTarget(reader.getSurface());
        captureBuilder.set(CaptureRequest.CONTROL_MODE,CameraMetadata.CONTROL_MODE_AUTO);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,ORIENTATIONS.get(rotation));


        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        actualImage = new File(Environment.getExternalStorageDirectory(), ts + ".jpg");
        ImgUri = FileProvider.getUriForFile(Main2Activity.this, this.getApplicationContext().getPackageName() + ".provider", actualImage);

        Log.e("Photo URI", "onActivityResult: " + actualImage);

        ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = null;

                image  = reader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
                try {
                    save(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(image!=null)
                    {
                        image.close();
                    }
                }

            }
        };

        reader.setOnImageAvailableListener(readerListener,mBackgroundHandler);


        final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted( CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();
                ProductListingMethod();
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
                    session.capture(captureBuilder.build(),captureListener,mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onConfigureFailed( CameraCaptureSession session) {

            }
        },mBackgroundHandler);


    }

    private void save(byte[] bytes) throws IOException {
        OutputStream outputStream = null;

        outputStream = new FileOutputStream(actualImage);

        outputStream.write(bytes);

        outputStream.close();

    }

    @Override
    protected void onResume() {
        super.onResume();

        startBackgroundThread();

        if(textureView.isAvailable())
        {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        else
        {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    private void startBackgroundThread()
    {
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

        File ac = actualImage;

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        ac);

        return MultipartBody.Part.createFormData(partName, actualImage.getName(), requestFile);


    }

    private void ProductListingMethod() {

        List<MultipartBody.Part> billImages = new ArrayList<>();
        MultipartBody.Part mReqType = MultipartBody.Part.createFormData("condition_name", "new");

       /* for (int i = 0; i < chooseImages.size(); i++) {

            billImages.add(prepareFilePart("default_image", uriArrayList.get(i)));

            Log.e("File URI", "----File URI---: " + uriArrayList.get(i));

        } */


        billImages.add(prepareFilePart("default_image", ImgUri));

//        urlsApi.PRODUCT_LIST_API_CAL(billImages, mReqType, mToken).enqueue(new Callback<ProductListApi>() {
//
//            @Override
//            public void onResponse(Call<ProductListApi> call, Response<ProductListApi> response) {
//
//                try {
//
//                    if (!response.isSuccessful()) {
//                        Toast.makeText(Main2Activity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    ProductListApi productListApi = response.body();
//                    assert productListApi != null;
//
//                    try {
//                        if (productListApi.getErr().length() > 1) {
//                            Toast.makeText(Main2Activity.this, productListApi.getErr(), Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception e) {
//                        Toast.makeText(Main2Activity.this, productListApi.getId(), Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception e) {
//                    Toast.makeText(Main2Activity.this, "Error!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProductListApi> call, Throwable t) {
//            }
//        });
    }


}
