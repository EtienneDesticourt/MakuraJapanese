package etiennedesticourt.makurajapanese;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import etiennedesticourt.makurajapanese.Skill.Question;

public abstract class AnswerFragment extends Fragment {
    public static final String INTENT_QUESTION_TAG = "question_instance";
    private Question question;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        question = (Question) bundle.getSerializable(INTENT_QUESTION_TAG);
        return bind(inflater, container);
    }

    public abstract View bind(LayoutInflater inflater, ViewGroup container);

    public abstract String getAnswer();

    public abstract void onClick(View v);

    public Question getQuestion() {
        return question;
    }
}
