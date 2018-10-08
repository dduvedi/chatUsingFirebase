package Utility;


import android.Manifest;

public class Constants {

    public static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static String[] PERMISSIONS_CAMERA = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static int THRESHOLD_TO_LOAD = 5;

    public static String INTERNET_ERROR = "No internet connectivity!";

}
