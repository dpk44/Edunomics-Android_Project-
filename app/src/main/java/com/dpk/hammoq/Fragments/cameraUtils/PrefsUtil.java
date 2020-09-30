package com.dpk.hammoq.Fragments.cameraUtils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsUtil {
    private static String PREFERENCE_FILE_KEY = "poc_camera_user_prefs";
    private static String SELECTED_CAMERA_ID = "selected_camera_id";
    private static String SELECTED_CAMERA_NAME = "selected_camera_name";
    private static String SELECTED_CAMERA_NAME_ISFRONT = "selected_camera_isfront";

    public static Camera selectedCamera = null;


    public static void savePrefs(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SELECTED_CAMERA_ID, selectedCamera.getId());
        editor.putString(SELECTED_CAMERA_NAME, selectedCamera.getName());
        editor.putBoolean(SELECTED_CAMERA_NAME_ISFRONT, selectedCamera.isFront());
        editor.commit();
    }

    public static void saveSelectedCamera(Camera camera, Context context) {
        selectedCamera = camera;
        savePrefs(context);
    }

    public static Camera getSelectedCamera(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String cameraId = sharedPref.getString(SELECTED_CAMERA_ID, null);
        String cameraName = sharedPref.getString(SELECTED_CAMERA_NAME, null);
        boolean isFront = sharedPref.getBoolean(SELECTED_CAMERA_NAME_ISFRONT, false);
        if (cameraId != null && !cameraId.isEmpty()
                && cameraName != null && !cameraName.isEmpty()) {
            selectedCamera = new Camera();
            selectedCamera.setId(cameraId);
            selectedCamera.setName(cameraName);
            selectedCamera.setFront(isFront);
        }else{
            selectedCamera = null;
        }
        return selectedCamera;
    }
}
