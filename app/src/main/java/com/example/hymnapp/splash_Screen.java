package com.example.hymnapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash_Screen extends AppCompatActivity {

    private TextView name, slogan;
    private ImageView logo;
    private View topView1,topView2,topView3;
    private View buttomView1,buttomView2,buttomView3;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //HIDE navigation bar to make activity appear in full screen
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_splash_screen);

        name = findViewById(R.id.name);
        slogan = findViewById(R.id.slogan);
        logo = findViewById(R.id.logo);
        topView1 = findViewById(R.id.topview1);
        topView2 = findViewById(R.id.topView2);
        topView3 = findViewById(R.id.topView3);
        buttomView1 = findViewById(R.id.buttonView1);
        buttomView2 = findViewById(R.id.buttonView2);
        buttomView3 = findViewById(R.id.buttonView3);

        Animation logoAnimation = AnimationUtils.loadAnimation(splash_Screen.this, R.anim.zoom_animation);
        Animation nameAnimation = AnimationUtils.loadAnimation(splash_Screen.this, R.anim.zoom_animation);

        Animation topView1Animation = AnimationUtils.loadAnimation(splash_Screen.this, R.anim.top_views_animation);
        Animation topView2Animation = AnimationUtils.loadAnimation(splash_Screen.this, R.anim.top_views_animation);
        Animation topView3Animation = AnimationUtils.loadAnimation(splash_Screen.this, R.anim.top_views_animation);

        Animation buttomView1Animation = AnimationUtils.loadAnimation(splash_Screen.this, R.anim.buttom_views_animation);
        Animation buttomView2Animation = AnimationUtils.loadAnimation(splash_Screen.this, R.anim.buttom_views_animation);
        Animation buttomView3Animation = AnimationUtils.loadAnimation(splash_Screen.this, R.anim.buttom_views_animation);

        topView1.startAnimation(topView1Animation);
        buttomView1.startAnimation(buttomView1Animation);

        topView1Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                topView2.setVisibility(View.VISIBLE);
                buttomView2.setVisibility(View.VISIBLE);

                topView2.startAnimation(topView2Animation);
                buttomView2.startAnimation(buttomView2Animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        topView2Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                topView3.setVisibility(View.VISIBLE);
                buttomView3.setVisibility(View.VISIBLE);

                topView3.startAnimation(topView3Animation);
                buttomView3.startAnimation(buttomView3Animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        topView3Animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.setVisibility(View.VISIBLE);
                logo.startAnimation(logoAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        logoAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                name.setVisibility(View.VISIBLE);
                name.startAnimation(nameAnimation);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        nameAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                slogan.setVisibility(View.VISIBLE);
                final String animateTxt = slogan.getText().toString();
                slogan.setText("");
                count = 0;

                new CountDownTimer(animateTxt.length() * 100, 100){

                    @Override
                    public void onTick(long millisUntilFinished) {
                        slogan.setText(slogan.getText().toString()+animateTxt.charAt(count));
                        count++;
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
                slogan.startAnimation(buttomView2Animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        new Handler().postDelayed(() -> {
            startActivity(new Intent(splash_Screen.this,MainActivity.class));
            finish();
        },7000);
    }
}