package com.etiennedesticourt.makurajapanese;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import com.etiennedesticourt.makurajapanese.Analytics.FirebaseLogger;
import com.etiennedesticourt.makurajapanese.Analytics.Logger;
import com.etiennedesticourt.makurajapanese.SRS.CourseDbHelper;
import com.etiennedesticourt.makurajapanese.Skill.CourseFactory;
import com.etiennedesticourt.makurajapanese.Skill.Lesson;
import com.etiennedesticourt.makurajapanese.Skill.Skill;
import com.etiennedesticourt.makurajapanese.Utils.SimpleTimer;

public class SkillActivity extends AppCompatActivity {
    public static final String INTENT_SKILL_FILE_TAG = "skill_path";
    private static final String SKILL_FILE_FOLDER = "raw";
    private static final String SKILL_BACKGROUND_FOLDER = "drawable";
    private FragmentPagerAdapter adapter;
    private ViewPager pager;
    private Skill skill;
    private boolean wasCompleted;
    private Logger logger = FirebaseLogger.INSTANCE;
    private SimpleTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        // Load skill
        Intent intent = getIntent();
        String skillName = intent.getStringExtra(INTENT_SKILL_FILE_TAG);
        CourseDbHelper dbHelper = CourseDbHelper.getInstance(getApplicationContext());
        CourseFactory factory = new CourseFactory(dbHelper);
        skill = factory.buildSkill(skillName); //TODO: Add error handling
        wasCompleted = skill.isCompleted();


        // Get corresponding picture
        int backgroundId = getResources().getIdentifier(
                skill.getTitle().toLowerCase(),
                SKILL_BACKGROUND_FOLDER,
                getPackageName());
        ImageView skillPic = (ImageView) findViewById(R.id.skillImage);
        skillPic.setImageResource(backgroundId);
        //skillPic.setBackgroundResource(backgroundId);

        TextView skillTitle = (TextView) findViewById(R.id.skillTitle);
        skillTitle.setText(skill.getTitle());


        pager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new LessonPagerAdapter(getSupportFragmentManager(), skill);
        pager.setAdapter(adapter);
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.titles);
        indicator.setViewPager(pager);
    }

    public void onResume() {
        super.onResume();
        /*
        if (!wasCompleted && skill.isCompleted()) {
            logger.logSkillCompletedEvent(skill.getId());
            wasCompleted = true;
        }
        */
        logger.logSkillOpenedEvent(skill.getId()); //TODO: Fix issue here, appears when question activity crashes
        timer = SimpleTimer.start();
    }

    public void onPause() {
        super.onPause();
        int timeSpent = timer.getDuration();
        logger.logSkillClosedEvent(skill.getId(), timeSpent);
    }

    public void startLesson(View v) {
        startQuestionActivity();
    }

    private void startQuestionActivity() {
        int lessonIndex = pager.getCurrentItem();
        Lesson lesson = skill.getLesson(lessonIndex);
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra(QuestionActivity.INTENT_LESSON_TAG, lesson);
        intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(intent);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == event.ACTION_DOWN) {
            boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
            logger.logScreenTouchedEvent("SkillActivity", (int) event.getX(), (int) event.getY(), portrait);
        }
        return super.dispatchTouchEvent(event);
    }
}
