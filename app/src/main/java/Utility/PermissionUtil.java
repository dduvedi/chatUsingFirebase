package Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import static Utility.Constants.PERMISSIONS_CAMERA;
import static Utility.Constants.PERMISSIONS_STORAGE;

public class PermissionUtil {

    public static final int PERMISSION_CAMERA_REQUEST = 100;
    public static final int PERMISSION_GALLERY_REQUEST = 101;

    private static String[] PERMISSIONS_CAMERA = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public static boolean checkCameraPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)
                        || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_CAMERA, PERMISSION_CAMERA_REQUEST);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_CAMERA, PERMISSION_CAMERA_REQUEST);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkStoragePermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_STORAGE, PERMISSION_GALLERY_REQUEST);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_STORAGE, PERMISSION_GALLERY_REQUEST);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
