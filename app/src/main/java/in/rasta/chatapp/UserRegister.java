package in.rasta.chatapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import Interface.ImageCompressor;
import Interface.ImageCompressorImpl;
import Model.UserRegisterModel;
import Utility.Constants;
import Utility.PermissionUtil;
import Utility.Util;
import ViewModel.UserRegisterViewModel;
import in.rasta.chatapp.databinding.ActivityUserSignupBinding;

public class UserRegister extends AppCompatActivity {

    private Uri imageUri = null;
    private ActivityUserSignupBinding activityUserSignupBinding;
    private UserRegisterViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityUserSignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_signup);

        viewModel = ViewModelProviders.of(this).get(UserRegisterViewModel.class);
        activityUserSignupBinding.setSignupViewModel(viewModel);
        activityUserSignupBinding.executePendingBindings();
        activityUserSignupBinding.setLifecycleOwner(this);

        setSupportActionBar((Toolbar) activityUserSignupBinding.toolbar);

        Util.ifKeyboardOpen(activityUserSignupBinding.containerLayout, activityUserSignupBinding.actionContainer);

        registerForLiveData(viewModel);
    }

    private void registerForLiveData(UserRegisterViewModel viewModel) {

        viewModel.getSignUpResponse().observe(this, new Observer<UserRegisterModel>() {
            @Override
            public void onChanged(@Nullable UserRegisterModel userRegisterModel) {
                if (userRegisterModel != null) {
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(UserRegister.this).edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(userRegisterModel);
                    edit.putString("userDetails", json);
                    edit.putBoolean("isLoggedIn", true);
                    edit.commit();

                    Intent intent = new Intent(UserRegister.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ActivityCompat.finishAffinity(UserRegister.this);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    Util.showToast(UserRegister.this, "Error while registering user!");

                }
            }
        });

        viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String message) {
                Util.showToast(UserRegister.this, "Error: " + message);
            }
        });


        viewModel.getActionClicked().observe(this, new Observer<UserRegisterViewModel.Action>() {
            @Override
            public void onChanged(@Nullable UserRegisterViewModel.Action action) {
                if (action.equals(UserRegisterViewModel.Action.LOGIN_CLICKED)) {
                    Intent intent = new Intent(UserRegister.this, UserLogin.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else if (action.equals(UserRegisterViewModel.Action.IMAGE_CLICK)) {
                    selectImage();
                }
            }
        });

        viewModel.showProgressDialog().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (Util.isNetworkConnected(UserRegister.this)) {
                    if (aBoolean) Util.showProgressDialog(UserRegister.this, "Wait...");
                    else Util.removeProgressDialog();
                } else {
                    Util.showToast(UserRegister.this, Constants.INTERNET_ERROR);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PermissionUtil.PERMISSION_CAMERA_REQUEST) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Util.showToast(UserRegister.this, "Camera permission denied.");
            }
        } else if (requestCode == PermissionUtil.PERMISSION_GALLERY_REQUEST) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Util.showToast(UserRegister.this, "Storage access permission denied.");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 2);
    }

    private void startCamera() {
        imageUri = null;
        imageUri = Util.getUri(UserRegister.this);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, 1);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserRegister.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    if (PermissionUtil.checkCameraPermission(UserRegister.this)) {
                        startCamera();
                    }
                } else if (items[item].equals("Choose from Gallery")) {
                    if (PermissionUtil.checkStoragePermission(UserRegister.this)) {
                        openGallery();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;
        ImageCompressor compressor = new ImageCompressorImpl();
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            } catch (FileNotFoundException e) {
                Util.showToast(UserRegister.this, "Error while getting image.");
                e.printStackTrace();
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
            activityUserSignupBinding.userImage.setImageDrawable(new BitmapDrawable(bitmap));
            bitmap = compressor.compressBitmap(UserRegister.this, imageUri);
            viewModel.userImage.setValue(new BitmapDrawable(bitmap));
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            try {
                bitmap = Util.decodeBitmap(UserRegister.this, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
            activityUserSignupBinding.userImage.setImageDrawable(new BitmapDrawable(bitmap));
            bitmap = compressor.compressBitmap(UserRegister.this, uri);
            viewModel.userImage.setValue(new BitmapDrawable(bitmap));
        }
    }


}
