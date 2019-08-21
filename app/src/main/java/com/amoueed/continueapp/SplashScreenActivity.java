package com.amoueed.continueapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashScreenActivity extends AppCompatActivity {
    ImageView titleImage;
    private SplashHandler mSplashHandler = new SplashHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_splash_screen);

        titleImage = findViewById(R.id.imageViewTitle);
        Glide.with(this).load(R.drawable.title_logo).into(titleImage);

        mSplashHandler.sendEmptyMessageDelayed(SplashHandler.TIMER_EXPIRED, 2000);
    }

    class SplashHandler extends Handler {
        static final int TIMER_EXPIRED = 100;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER_EXPIRED:
                    Intent in = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(in);
                    finish();
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
