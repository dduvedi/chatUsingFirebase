package in.rasta.chatapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;

import Model.UserProfileModel;
import Utility.Constants;
import Utility.Util;
import ViewModel.UserLoginViewModel;
import in.rasta.chatapp.databinding.ActivityUserLoginBinding;

public class UserLogin extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserLoginViewModel viewModel = ViewModelProviders.of(this).get(UserLoginViewModel.class);

        ActivityUserLoginBinding activityUserLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_login);
        activityUserLoginBinding.setLoginViewModel(viewModel);
        activityUserLoginBinding.executePendingBindings();
        activityUserLoginBinding.setLifecycleOwner(this);


        setSupportActionBar((Toolbar) activityUserLoginBinding.toolbar);
        Util.ifKeyboardOpen(activityUserLoginBinding.containerLayout, activityUserLoginBinding.actionContainer);

        registerForLiveData(viewModel);
    }

    private void registerForLiveData(UserLoginViewModel viewModel) {
        viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String message) {
                Util.showToast(UserLogin.this, message);
            }
        });

        viewModel.getSignInResponse().observe(this, new Observer<UserProfileModel>() {
            @Override
            public void onChanged(@Nullable UserProfileModel data) {
                if (data != null) {
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(UserLogin.this).edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(data);
                    edit.putString("userDetails", json);
                    edit.putBoolean("isLoggedIn", true);
                    edit.commit();
                    Intent intent = new Intent(UserLogin.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    Util.showToast(UserLogin.this, "No user found against given username!");
                }
            }
        });

        viewModel.getActionClicked().observe(this, new Observer<UserLoginViewModel.Action>() {
            @Override
            public void onChanged(@Nullable UserLoginViewModel.Action action) {
                if (action.equals(UserLoginViewModel.Action.LOGIN_CLICKED)) {
                    Intent intent = new Intent(UserLogin.this, UserRegister.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ActivityCompat.finishAffinity(UserLogin.this);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        });

        viewModel.showProgressDialog().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (Util.isNetworkConnected(UserLogin.this)) {
                    if (aBoolean) Util.showProgressDialog(UserLogin.this, "Wait...");
                    else Util.removeProgressDialog();
                } else {
                    Util.showToast(UserLogin.this, Constants.INTERNET_ERROR);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

}
