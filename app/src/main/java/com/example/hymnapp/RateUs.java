package com.example.hymnapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class RateUs extends Dialog {

    private float userRate = 0;

    public RateUs(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_us);

        final AppCompatButton rateNowBtn = findViewById(R.id.rateNowBtn);
        final AppCompatButton laterBtn = findViewById(R.id.laterBtn);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final ImageView ratingImage = findViewById(R.id.ratingImage);
        final TextView dialog = findViewById(R.id.dialog);


//        rateNowBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //code goes here
//                builder.setMessage("").setTitle("");
//                builder.setMessage("Do you want to exit this Application?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", (dialog, id) -> {
//                            finish();
//                            Toast.makeText(getApplicationContext(),"You choose Yes action AlertBox", Toast.LENGTH_LONG).show();
//                        })
//                        .setNegativeButton("No", (dialog, id) ->{
//                            dialog.cancel();
//
//                            Toast.makeText(getApplicationContext(),"You choose No action AlertBox", Toast.LENGTH_LONG).show();
//                        });
//                AlertDialog alert = builder.create();
//                alert.setTitle("SoftwareAlertMessage");
//                alert.show();
//            }
//
//        });

        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide the Rating bar dialog
                dismiss();
            }
        });


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating <= 1){
                    ratingImage.setImageResource(R.drawable.one);//will change the 5 star to ne star
                }
                else if (rating <= 2){
                    ratingImage.setImageResource(R.drawable.two);//will change the 2 star to ne star
                }
                else if (rating <= 3){
                    ratingImage.setImageResource(R.drawable.three);//will change the 3 star to ne star
                }
                else if (rating <= 4){
                    ratingImage.setImageResource(R.drawable.four);//will change the 4 star to ne star
                }
                else if (rating <= 5){
                    ratingImage.setImageResource(R.drawable.five);
                }
                //animate Emoji image
                animateImage(ratingImage);

                //selected rating by user
                userRate = rating;
            }
        });
    }

    private  void animateImage(ImageView ratingImage){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1f,0,1f,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);
    }
}