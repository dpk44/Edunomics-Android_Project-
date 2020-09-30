package com.dpk.hammoq.Fragments.cameraUtils;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;

import java.util.ArrayList;
import java.util.List;

public class CameraUtil {
    public static Camera getCamera(Activity activity, String id) {
        List<Camera> cameras = getCameraList(activity);
        for (Camera c: cameras) {
            if(id.equals(c.getId())){
                return c;
            }
        }
        return null;
    }
    public static Camera getCamera(Activity activity, CameraType type) {
        List<Camera> cameras = getCameraList(activity);
        for (Camera c: cameras) {
            switch (type){
                case FRONT:
                    if(c.isFront()){
                        return c;
                    }
                    break;
                case REAR:
                    if(c.isRear()){
                        return c;
                    }
                    break;
                case EXT:
                    if(c.isExternal()){
                        return c;
                    }
                    break;
                case OTHER:
                    break;
            }

        }
        return null;
    }
    public static Camera getFrontCamera(Activity activity) {
        List<Camera> cameras = getCameraList(activity);
        for (Camera c: cameras) {
            if(c.isFront()){
                return c;
            }
        }
        return null;
    }

    public static List<Camera> getCameraList(Activity activity) {
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        List<Camera> cameras = new ArrayList<Camera>();
        try {
            String[] ids = manager.getCameraIdList();
            int frontCameras = 0;
            int rearCameras = 0;
            int extCameras = 0;
            int otherCameras = 0;
            for (String id : ids) {
                Camera camera = new Camera();
                camera.setId(id);
                StringBuilder sb = new StringBuilder("Câmera ");
                int lensFacing = manager.getCameraCharacteristics(id).get(CameraCharacteristics.LENS_FACING);
                switch (lensFacing) {
                    case CameraCharacteristics.LENS_FACING_FRONT:
                        frontCameras = appendCameraDesc(frontCameras, sb, CameraType.FRONT);
                        camera.setFront(true);
                        break;
                    case CameraCharacteristics.LENS_FACING_BACK:
                        rearCameras = appendCameraDesc(rearCameras, sb, CameraType.REAR);
                        camera.setRear(true);
                        break;
                    case CameraCharacteristics.LENS_FACING_EXTERNAL:
                        extCameras = appendCameraDesc(extCameras, sb, CameraType.EXT);
                        camera.setExternal(true);
                        break;
                    default:
                        otherCameras = appendCameraDesc(otherCameras, sb, CameraType.OTHER);
                        break;
                }
                camera.setName(sb.toString());
                cameras.add(camera);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return cameras;
    }

    private static int appendCameraDesc(int cameraCount, StringBuilder sb, CameraType type) {
        cameraCount++;
        sb.append(type.getType());
        if (cameraCount > 1) {
            sb.append(" ").append(cameraCount);
        }
        return cameraCount;
    }
    public enum CameraType {
        FRONT("Frontal"),
        REAR("Traseira"),
        EXT("Externa"),
        OTHER("Outra");

        private String type;

        CameraType(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
