package com.dpk.hammoq.Activities.Dashbaord;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpk.hammoq.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class DashBoardActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;
    private AnimatedBottomBar animatedBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        replaceFragment();
        fragmentManager = getSupportFragmentManager();

        animatedBottomBar = findViewById(R.id.animated_bar);

        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NotNull AnimatedBottomBar.Tab newTab) {
                Fragment fragment = null;
                switch (newTab.getId()) {
                    case R.id.nav_users:
                        fragment = new HomeFragment();
                        break;

                    case R.id.nav_agents:
                        fragment = new AgentFragment();
                        break;

                    case R.id.nav_rates:
                        fragment = new SettingFragment();
                        break;

                    case R.id.nav_logout:
                        fragment = new LogoutFragment();
                        break;
                }

                if (fragment!=null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frameContainer, fragment)
                            .addToBackStack(null)
                            .commit();
                } else{
                    Log.e("TAG", "Error");
                }
            }
        });

    }

    public void replaceFragment() {
        Fragment HomeFragment = new com.dpk.hammoq.Activities.Dashbaord.HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer, HomeFragment)
                .commit();
    }

}
