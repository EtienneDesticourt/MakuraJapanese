package com.etiennedesticourt.makurajapanese;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

public class LessonEndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_end);


        NativeExpressAdView  mAdView = (NativeExpressAdView) findViewById(R.id.adView);
        AdRequest adRequest;


        if (BuildConfig.USE_TEST_ADS) {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                    .addTestDevice(getResources().getString(R.string.test_device_id))  // An example device ID
                    .build();
        }
        else {
            adRequest = new AdRequest.Builder().build();
        }
        mAdView.loadAd(adRequest);

    }

    public void goBackToMainActivity(View v) {
        finish();
        /*
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
    }
}
