package Utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.rasta.chatapp.BuildConfig;

public class Util {

    private static ProgressDialog progressDialog;

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public static void ifKeyboardOpen(final RelativeLayout view, final RelativeLayout toHide) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if ((view.getRootView().getHeight() - view.getHeight()) >
                        view.getRootView().getHeight() / 3) {
                    toHide.setVisibility(View.GONE);
                } else {
                    toHide.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public static void showProgressDialog(Context context, String msg) {
        if (progressDialog != null) {
            progressDialog = null;
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context) {
                @Override
                public void onBackPressed() {

                }
            };
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }


    public static void removeProgressDialog() {
        try {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public static Uri getUri(Context context) {
        try {
            return FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    createImageFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static Bitmap decodeBitmap(Context context, Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        int scale = 4;

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
    }


    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] b = baos.toByteArray();
        return b;
    }

    public static Bitmap encodedBase64ToBitmap(String encodedBase64) {
        byte[] decodedString = Base64.decode(encodedBase64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }


}

