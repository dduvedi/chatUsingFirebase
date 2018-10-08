package in.rasta.chatapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import Model.UserProfileModel;
import in.rasta.chatapp.databinding.ActivityUserProfileBinding;

public class UserProfile extends AppCompatActivity {

    private ActivityUserProfileBinding activityUserProfileBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityUserProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

        setSupportActionBar((Toolbar) activityUserProfileBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getUserDetails();
        showInformativeDialog();
    }

    private void getUserDetails() {
        Gson gson = new Gson();
        String json = PreferenceManager.getDefaultSharedPreferences(UserProfile.this).getString("userDetails", "");
        UserProfileModel obj = gson.fromJson(json, UserProfileModel.class);

        activityUserProfileBinding.userName.setText(obj.getUserName());
        activityUserProfileBinding.firstName.setText(obj.getFirstName());
        activityUserProfileBinding.lastName.setText(obj.getLastName());
        if (obj.getUserImage() != null && !obj.getUserImage().isEmpty())
            Picasso.with(UserProfile.this).load(obj.getUserImage()).into(activityUserProfileBinding.userImage);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(UserProfile.this, MainActivity.class);
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
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    private void showInformativeDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setMessage("This screen is view only, no updates are allowed.");


        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
    }


}
