package com.etiennedesticourt.makurajapanese;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etiennedesticourt.makurajapanese.SRS.CourseDbHelper;
import com.etiennedesticourt.makurajapanese.Skill.CourseFactory;
import com.etiennedesticourt.makurajapanese.Skill.Lesson;
import com.etiennedesticourt.makurajapanese.databinding.FragmentLessonPresentationBinding;

public class LessonPresentationFragment extends Fragment {
    public static final String INTENT_LESSON_TAG = "lesson_instance";
    private FragmentLessonPresentationBinding binding;
    private Lesson lesson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        lesson = (Lesson) bundle.getSerializable(INTENT_LESSON_TAG);
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_lesson_presentation, container, false);
        binding.setLesson(lesson);
        return binding.getRoot();
    }

    public void onResume() {
        super.onResume();
        CourseDbHelper dbHelper = CourseDbHelper.getInstance(getContext().getApplicationContext());
        Cursor cursor = dbHelper.getLesson(lesson.getId());
        cursor.moveToFirst();
        CourseFactory factory = new CourseFactory(dbHelper);
        lesson = factory.buildLesson(cursor);
        binding.setLesson(lesson);
    }
}
