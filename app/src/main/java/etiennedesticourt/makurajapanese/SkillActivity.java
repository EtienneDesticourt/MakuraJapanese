package etiennedesticourt.makurajapanese;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.io.InputStream;

import etiennedesticourt.makurajapanese.Skill.Lesson;
import etiennedesticourt.makurajapanese.Skill.Skill;
import etiennedesticourt.makurajapanese.Skill.SkillFileParser;
import etiennedesticourt.makurajapanese.Skill.SkillFileParserException;

public class SkillActivity extends AppCompatActivity {
    public static final String INTENT_SKILL_FILE_TAG = "skill_path";
    private static final String SKILL_FILE_FOLDER = "raw";
    private static final String SKILL_BACKGROUND_FOLDER = "drawable";
    private FragmentPagerAdapter adapter;
    private ViewPager pager;
    private Skill skill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        // Load skill
        Intent intent = getIntent();
        String skillFile = intent.getStringExtra(INTENT_SKILL_FILE_TAG);
        int resourceId = getResources().getIdentifier(skillFile, SKILL_FILE_FOLDER, getPackageName());
        InputStream skillStream = getResources().openRawResource(resourceId);

        try {
            skill = SkillFileParser.parseSkillFile(skillStream);
        }
        catch (SkillFileParserException e) {
            Log.d("FUCK", e.message);
            return;
            // Handle.
        }

        // Get corresponding picture
        int backgroundId = getResources().getIdentifier(
                skill.getTitle().toLowerCase(),
                SKILL_BACKGROUND_FOLDER,
                getPackageName());
        ImageView skillPic = (ImageView) findViewById(R.id.skillImage);
        skillPic.setBackgroundResource(backgroundId);

        TextView skillTitle = (TextView) findViewById(R.id.skillTitle);
        skillTitle.setText(skill.getTitle());


        pager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new LessonPagerAdapter(getSupportFragmentManager(), skill);
        pager.setAdapter(adapter);
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.titles);
        indicator.setViewPager(pager);
    }

    public void startLesson(View v) {
        startQuestionActivity();
    }

    private void startQuestionActivity() {
        int lessonIndex = pager.getCurrentItem();
        Lesson lesson = skill.getLesson(lessonIndex);
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra(QuestionActivity.INTENT_LESSON_TAG, lesson);
        startActivity(intent);
    }
}