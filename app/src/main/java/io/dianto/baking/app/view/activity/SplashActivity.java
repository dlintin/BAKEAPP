package io.dianto.baking.app.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.GregorianCalendar;

import io.dianto.baking.app.R;

import static io.dianto.baking.app.util.Constant.Data.REMAINING_TIME_KEY;
import static io.dianto.baking.app.util.Constant.Function.nextActivity;

public class SplashActivity extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;
    private long SPLASH_TIME = 2000;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (savedInstanceState != null) {
            SPLASH_TIME = savedInstanceState.getLong(REMAINING_TIME_KEY);
        }

        doingSplash();
    }

    private void doingSplash() {
        startTime = GregorianCalendar.getInstance().getTimeInMillis();
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                goToMain();
            }
        };

        mHandler.postDelayed(mRunnable, SPLASH_TIME);
    }

    private void goToMain() {
        nextActivity(this, MainActivity.class, true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mHandler.removeCallbacks(mRunnable);
        outState.putLong(REMAINING_TIME_KEY, getRemainingTime());
    }

    private long getRemainingTime() {
        long endTime = GregorianCalendar.getInstance().getTimeInMillis();
        return SPLASH_TIME - (endTime - startTime);
    }
}