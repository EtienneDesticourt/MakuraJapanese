package etiennedesticourt.makurajapanese;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import etiennedesticourt.makurajapanese.Skill.Lesson;
import etiennedesticourt.makurajapanese.Skill.Question;
import etiennedesticourt.makurajapanese.Skill.QuestionType;
import etiennedesticourt.makurajapanese.databinding.ActivityQuestionBinding;

public class QuestionActivity extends AppCompatActivity {
    public static final String INTENT_LESSON_TAG = "lesson";
    private Lesson lesson;
    private int currentQuestionId = 0;
    ActivityQuestionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        Intent intent = getIntent();
        lesson = (Lesson) intent.getSerializableExtra(INTENT_LESSON_TAG);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_question);
        displayQuestion();
    }

    private void displayQuestion() {
        Question question = lesson.getQuestion(currentQuestionId);
        binding.setQuestion(question);

        Fragment answerFragment;
        if (question.getType() == QuestionType.WORD_DEFINITION) {
            answerFragment = new DefinitionFragment();
        }
        else if (question.getType() == QuestionType.SENTENCE_CONSTRUCTION) {
            answerFragment = new ConstructionFragment();
        }
        else { // if (question.getType() == QuestionType.WRITTEN_TRANSLATION) {
            answerFragment = new TranslationFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(AnswerFragment.INTENT_QUESTION_TAG, question);
        answerFragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragmentLayout, answerFragment).commit();
    }

    public void validateAnswer(View v) {
    }

    // 5 parts:
    // Instruction
    // Question
    // Answer
    // Validate
    // progress bar

}
