package etiennedesticourt.makurajapanese;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

import etiennedesticourt.makurajapanese.Analytics.FirebaseLogger;
import etiennedesticourt.makurajapanese.Analytics.Logger;

//TODO:
//DEMO :
//Finish lessons

// Add validation sound

// Fix question validation bug
// Fix memory issue in setOptionImage
// Fix finished lesson quick validation bug

// Add user properties:
    // Database version
    // Screen resolution
// Add analytics disabling settings
// Add ads at end of lesson

//FULL RELEASE:
// Limit interval increase to once a day
// Fix bug where listen button read next question after validation
// Implement Construction fragment
// Beautify 3 fragments
// Handle touch event + scrolling issue


public class MainActivity extends AppCompatActivity {
    private final String PREFS_NAME = "MyPrefsFile";
    private final String PREFS_FIRST_OPEN = "first_open";
    private FragmentPagerAdapter adapter;
    private ViewPager pager;
    public static String PACKAGE_NAME;
    private Logger logger = FirebaseLogger.INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getPackageName();

        pager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new SkillPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        //pager.setCurrentItem(1);

        FirebaseLogger.INSTANCE.setContext(this);

        //getSharedPreferences(PREFS_NAME, 0).edit().clear().commit();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean(PREFS_FIRST_OPEN, true)) {
            showAnalyticsInfo();
            settings.edit().putBoolean(PREFS_FIRST_OPEN, false).commit();
        }
    }

    public void showAnalyticsInfo() {new AlertDialog.Builder(this)
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
}
