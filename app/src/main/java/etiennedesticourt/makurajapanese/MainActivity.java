package etiennedesticourt.makurajapanese;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

// Add analytics disabling settings
// Add analytics
// Add ads at end of lesson

//FULL RELEASE:
// Limit interval increase to once a day
// Fix bug where listen button read next question after validation
// Implement Construction fragment
// Beautify 3 fragments


public class MainActivity extends AppCompatActivity {
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
