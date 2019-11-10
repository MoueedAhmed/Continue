package com.amoueed.continueapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.amoueed.continueapp.main.MainActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {
    ImageView titleImage;
    private SplashHandler mSplashHandler = new SplashHandler();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_splash_screen);

        titleImage = findViewById(R.id.imageViewTitle);
        Glide.with(this).load(R.drawable.title_logo).into(titleImage);
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        titleImage.startAnimation(startAnimation);


        mSplashHandler.sendEmptyMessageDelayed(SplashHandler.TIMER_EXPIRED, 1500);
    }

    class SplashHandler extends Handler {
        static final int TIMER_EXPIRED = 100;
        @Override
        public void handleMessage(Message msg) {
            titleImage.setAnimation(null);
            switch (msg.what) {
                case TIMER_EXPIRED:
                    if(mAuth.getCurrentUser()!=null){
                        Intent in = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(in);
                        finish();
                    }else{
                        Intent in = new Intent(SplashScreenActivity.this, PolicyActivity.class);
                        startActivity(in);
                        finish();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        mSplashHandler = null;
        super.onDestroy();
    }
}
