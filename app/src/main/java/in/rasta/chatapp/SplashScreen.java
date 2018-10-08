package in.rasta.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import Utility.Constants;
import Utility.Util;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Util.isNetworkConnected(SplashScreen.this)) {
                    if (PreferenceManager.getDefaultSharedPreferences(SplashScreen.this).getBoolean("isLoggedIn", false)) {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    } else {
                        Intent intent = new Intent(SplashScreen.this, UserLogin.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                } else {
                    Util.showToast(SplashScreen.this, Constants.INTERNET_ERROR);
                    finish();
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                }
            }
        }, 3000);
    }

}
