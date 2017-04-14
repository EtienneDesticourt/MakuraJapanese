package com.etiennedesticourt.makurajapanese;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.etiennedesticourt.makurajapanese.Analytics.FirebaseLogger;
import com.etiennedesticourt.makurajapanese.Analytics.Logger;
import com.etiennedesticourt.makurajapanese.Skill.Lesson;
import com.etiennedesticourt.makurajapanese.Skill.Skill;

public class LessonPagerAdapter extends FragmentPagerAdapter {
    private Skill skill;
    private Logger logger = FirebaseLogger.INSTANCE;

    public LessonPagerAdapter(FragmentManager fm, Skill skill) {
        super(fm);
        this.skill = skill;
    }

    @Override
    public Fragment getItem(int position) {
        Lesson lesson = skill.getLesson(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(LessonPresentationFragment.INTENT_LESSON_TAG, lesson);
        Fragment fragment = new LessonPresentationFragment();
        fragment.setArguments(bundle);
        logger.logSkillLessonSwipedEvent(skill.getId(), lesson.getId());
        return fragment;
    }

    @Override
    public int getCount() {
        return skill.getNumLessons();
    }
}
