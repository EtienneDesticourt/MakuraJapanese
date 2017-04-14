package com.etiennedesticourt.makurajapanese;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.etiennedesticourt.makurajapanese.SRS.CourseDbHelper;
import com.etiennedesticourt.makurajapanese.Skill.CourseFactory;
import com.etiennedesticourt.makurajapanese.Skill.Skill;

public class SkillsFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_skills, container, false);
        return rootView;
    }

    public void onResume() {
        super.onResume();
        updateMasteryDisplay(getView());
    }

    private void setMasteryDrawable(Skill skill, ImageView view) {
        int mastery = skill.isCompleted() ? skill.getMasteryLevel() : 0;
        view.setVisibility(View.VISIBLE);
        switch (mastery) {
            case 1: view.setImageResource(R.drawable.mastery1); break;
            case 2: view.setImageResource(R.drawable.mastery2); break;
            case 3: view.setImageResource(R.drawable.mastery3); break;
            case 4: view.setImageResource(R.drawable.mastery4); break;
            case 5: view.setImageResource(R.drawable.mastery5); break;
            default: view.setVisibility(View.INVISIBLE); break;
        }
    }

    public void updateMasteryDisplay(View rootView) {
        CourseDbHelper dbHelper = CourseDbHelper.getInstance(getContext().getApplicationContext());
        CourseFactory factory = new CourseFactory(dbHelper);

        Skill greetings = factory.buildSkill("Greetings");
        Skill subject = factory.buildSkill("Subject");
        Skill pointing = factory.buildSkill("Pointing");
        Skill questions = factory.buildSkill("Questions");
        Skill introduction = factory.buildSkill("Animals");

        ImageView imageView;
        View mainLayout = rootView.findViewById(R.id.skillScroller).findViewById(R.id.mainSkillLayout);
        imageView = (ImageView) mainLayout.findViewById(R.id.greetingsMastery);
        setMasteryDrawable(greetings, imageView);
        imageView = (ImageView) mainLayout.findViewById(R.id.desuMastery);
        setMasteryDrawable(subject, imageView);
        imageView = (ImageView) mainLayout.findViewById(R.id.pointingQuestionLayout).findViewById(R.id.pointingMastery);
        setMasteryDrawable(pointing, imageView);
        imageView = (ImageView) mainLayout.findViewById(R.id.pointingQuestionLayout).findViewById(R.id.questionsMastery);
        setMasteryDrawable(questions, imageView);
        imageView = (ImageView) mainLayout.findViewById(R.id.animalsMastery);
        setMasteryDrawable(introduction, imageView);
    }
}
