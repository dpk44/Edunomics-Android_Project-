package com.dpk.hammoq.Activities.SplashActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.dpk.hammoq.Activities.CameraActivity.CameraXActivity;
import com.dpk.hammoq.Activities.Dashbaord.DashBoardActivity;
import com.dpk.hammoq.Activities.LoginActivity.LoginActivity;
import com.dpk.hammoq.R;
import com.dpk.hammoq.Utils.Appcontants;

public class SplashActivity extends AppCompatActivity {

    String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        mToken = sharedPreferences.getString(Appcontants.LOGIN_USER_TOKEN, "");

        ImageView imageView = findViewById(R.id.ivLoginLogo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.in_from_bottom);
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mToken.equals("")) {
                    Intent intent = new Intent(SplashActivity.this, DashBoardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}
