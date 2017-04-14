package com.etiennedesticourt.makurajapanese;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.etiennedesticourt.makurajapanese.Analytics.FirebaseLogger;
import com.etiennedesticourt.makurajapanese.Analytics.Logger;
import com.etiennedesticourt.makurajapanese.Analytics.UserProperty;
import com.etiennedesticourt.makurajapanese.SRS.CourseDbHelper;
import com.google.android.gms.ads.MobileAds;

//TODO:
//FULL RELEASE:
// Add some more red to question activity, change native ad color to match color theme
// Limit interval increase to once a day
// Fix bug where listen button read next question after validation
// Implement Construction fragment
// Beautify 3 fragments
// Handle touch event + scrolling issue
// Clean last minute demo shit (put all analytics widget shit in analytics package)
// Fix finished lesson quick validation bug (it brings back question activity to the front despite the finish)
// Fix memory issue in setOptionImage (temporary fix for demo, solve long term).


public class MainActivity extends AppCompatActivity {
    private final String PREFS_NAME = "MyPrefsFile";
    private final String PREFS_FIRST_OPEN = "first_open";
    private final String PREFS_ANALYTICS_ENABLED = "analytics_enabled";
    private FragmentPagerAdapter adapter;
    private ViewPager pager;
    public static String PACKAGE_NAME;
    private Logger logger = FirebaseLogger.INSTANCE;
    private boolean disableAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getPackageName();

        pager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new SkillPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        //pager.setCurrentItem(1);


        // INITIALIZE ADMOB
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.admob_app_id));

        // INITIALIZE ANALYTICS
        FirebaseLogger.INSTANCE.setContext(this.getApplicationContext());
        //getSharedPreferences(PREFS_NAME, 0).edit().clear().commit();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean(PREFS_FIRST_OPEN, true)) {
            showAnalyticsInfo();
            FirebaseLogger.INSTANCE.setAnalyticsCollectionEnabled(true);
            settings.edit().putBoolean(PREFS_ANALYTICS_ENABLED, true).commit();
            settings.edit().putBoolean(PREFS_FIRST_OPEN, false).commit();
        }
        if (!settings.getBoolean(PREFS_ANALYTICS_ENABLED, false)) {
            FirebaseLogger.INSTANCE.setAnalyticsCollectionEnabled(false);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        FirebaseLogger.INSTANCE.setUserProperty(UserProperty.DATABASE_VERSION,
                String.valueOf(CourseDbHelper.DATABASE_VERSION));
        FirebaseLogger.INSTANCE.setUserProperty(UserProperty.SCREEN_RESOLUTION_X,
                String.valueOf(width));
        FirebaseLogger.INSTANCE.setUserProperty(UserProperty.SCREEN_RESOLUTION_Y,
                String.valueOf(height));

    }

    public void showSettings(View v) {
        CharSequence[] items = {"Disable analytics."};
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Settings")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        disableAnalytics = !disableAnalytics;
                        ((AlertDialog)dialog).getListView().setItemChecked(0, disableAnalytics);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (disableAnalytics) {
                            disableAnalytics();
                        }
                        else {
                            enableAnalytics();
                        }
                        disableAnalytics = false;
                    }
                })
                .show();
    }

    public void disableAnalytics() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean(PREFS_ANALYTICS_ENABLED, false)) {
            logger.logAnalyticsDisabled();
            settings.edit().putBoolean(PREFS_ANALYTICS_ENABLED, false).commit();
            FirebaseLogger.INSTANCE.setAnalyticsCollectionEnabled(false);
        }
    }

    public void enableAnalytics() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (!settings.getBoolean(PREFS_ANALYTICS_ENABLED, false)) {
            settings.edit().putBoolean(PREFS_ANALYTICS_ENABLED, true).commit();
            FirebaseLogger.INSTANCE.setAnalyticsCollectionEnabled(true);
            logger.logAnalyticsReenabled();
        }
    }

    public void showAnalyticsInfo() {
        new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("App Info")
            .setMessage("This app uses analytics to improve itself by logging events such as how long a question or lesson takes to be completed.\n No sensitive information is logged.\n All information is anonymous.\n Analytics can be disabled in the settings.")
            .setPositiveButton("OK", null)
            .show();
    }

    public void onSkillClick(View v) {
        String skillFile = (String) v.getTag();
        if (skillFile != null) {
            startSkillActivity(skillFile);
        }
    }

    private void startSkillActivity(String skillFile) {
        Intent intent = new Intent(this, SkillActivity.class);
        intent.putExtra(SkillActivity.INTENT_SKILL_FILE_TAG, skillFile);
        startActivity(intent);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == event.ACTION_DOWN) {
            boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
            logger.logScreenTouchedEvent("MainActivity", (int) event.getX(), (int) event.getY(), portrait);
        }
        return super.dispatchTouchEvent(event);
    }


    public void startLessonEndActivity(View v) {
        Intent intent = new Intent(this, LessonEndActivity.class);
        //intent.putExtra(LessonEndActivity.INTENT_LESSON_TAG, lesson);
        startActivity(intent);
    }
}
