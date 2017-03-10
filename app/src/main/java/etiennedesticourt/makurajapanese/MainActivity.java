package etiennedesticourt.makurajapanese;

import android.content.Intent;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private FragmentPagerAdapter adapter;
    private ViewPager pager;
    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getPackageName();

        pager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new SkillPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
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
}
