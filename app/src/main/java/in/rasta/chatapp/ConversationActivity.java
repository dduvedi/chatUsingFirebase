package in.rasta.chatapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Adapters.ChatAdapter;
import Interface.ImageCompressor;
import Interface.ImageCompressorImpl;
import Interface.Observer;
import Model.Chat;
import Model.UserProfileModel;
import Utility.Constants;
import Utility.PermissionUtil;
import Utility.Util;
import ViewModel.ConversationViewModel;
import in.rasta.chatapp.databinding.ActivityUserConversationBinding;

public class ConversationActivity extends AppCompatActivity implements Observer<ArrayList<Chat>> {
    private Uri imageUri = null;

    private ActivityUserConversationBinding binding;
    private ConversationViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_conversation);

        UserProfileModel loggedInUserDetail = getLoggedInUserDetail();

        viewModel = new ConversationViewModel("chats/", loggedInUserDetail.getUserName().toUpperCase(),
                getIntent().getExtras().getString("userName").toUpperCase());


        binding.setConversationViewModel(viewModel);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        binding.setConversationActivity(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel.addObserver(this);
        viewModel.setListener();

        setSupportActionBar((android.support.v7.widget.Toolbar) binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getExtras().getString("userName").toUpperCase());
    }

    private UserProfileModel getLoggedInUserDetail() {
        Gson gson = new Gson();
        String json = PreferenceManager.getDefaultSharedPreferences(ConversationActivity.this).getString("userDetails", "");
        UserProfileModel obj = gson.fromJson(json, UserProfileModel.class);
        return obj;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 0) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Util.showToast(ConversationActivity.this, "Camera permission denied.");
            }
        } else if (requestCode == 1) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Util.showToast(ConversationActivity.this, "Storage access permission denied.");
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
        imageUri = Util.getUri(ConversationActivity.this);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, 1);
    }

    public void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ConversationActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    if (PermissionUtil.checkCameraPermission(ConversationActivity.this)) {
                        startCamera();
                    }
                } else if (items[item].equals("Choose from Gallery")) {
                    if (PermissionUtil.checkStoragePermission(ConversationActivity.this)) {
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
                Util.showToast(ConversationActivity.this, "Error while getting image.");
                e.printStackTrace();
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
            bitmap = compressor.compressBitmap(ConversationActivity.this, imageUri);
            showEditPopup(bitmap);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            try {
                bitmap = Util.decodeBitmap(ConversationActivity.this, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
            bitmap = compressor.compressBitmap(ConversationActivity.this, uri);
            showEditPopup(bitmap);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(ConversationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ConversationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public void onObserve(int event, ArrayList<Chat> eventMessage) {
        ChatAdapter chatAdapter = new ChatAdapter(this, eventMessage,
                getLoggedInUserDetail().getUserName(),
                getIntent().getExtras().getString("userName"));
        binding.recyclerView.setAdapter(chatAdapter);
        binding.recyclerView.scrollToPosition(eventMessage.size() - 1);
    }

    public void onSendMessageClicked() {
        if (Util.isNetworkConnected(ConversationActivity.this)) {
            viewModel.sendMessage("chats/" + getLoggedInUserDetail().getUserName().toUpperCase() + "_" + getIntent().getExtras().getString("userName").toUpperCase(),
                    binding.message.getText().toString(),
                    getLoggedInUserDetail().getUserName(),
                    getIntent().getExtras().getString("userName"));
            binding.message.getText().clear();
        } else {
            Util.showToast(ConversationActivity.this, Constants.INTERNET_ERROR);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.removeObserver(this);
        viewModel.onDestroy();
    }

    private void showEditPopup(final Bitmap bitmap) {
        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ConversationActivity.this);
        final View layout = getLayoutInflater().inflate(R.layout.popup_image_preview, null);
        final android.app.AlertDialog alertDialog = alert.create();
        final ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);

        imageView.setImageBitmap(bitmap);

        TextView send = (TextView) layout.findViewById(R.id.send);
        TextView close = (TextView) layout.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showProgressDialog(ConversationActivity.this, "Uploading image...");
                viewModel.uploadImage(Util.bitmapToByteArray(bitmap));
                alertDialog.dismiss();
            }
        });
        alertDialog.setCancelable(false);

        alertDialog.setView(layout);
        alertDialog.show();
    }

}
