package com.etiennedesticourt.makurajapanese;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class TranslationFragment extends AnswerFragment {
    @Override
    public View bind(LayoutInflater inflater, ViewGroup container) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_question_translation, container, false);
        return rootView;
    }

    @Override
    public String getAnswer() {
        EditText view = (EditText) getView().findViewById(R.id.answerField);
        return view.getText().toString();
    }

    @Override
    public void onClick(View v) {

    }
}
