package etiennedesticourt.makurajapanese;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import etiennedesticourt.makurajapanese.Skill.Lesson;
import etiennedesticourt.makurajapanese.Skill.Question;
import etiennedesticourt.makurajapanese.Skill.QuestionType;
import etiennedesticourt.makurajapanese.databinding.ActivityQuestionBinding;

public class QuestionActivity extends AppCompatActivity {
    public static final String INTENT_LESSON_TAG = "lesson";
    private Lesson lesson;
    private int currentQuestionId = 0;
    private AnswerFragment answerFragment;
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

    public void nextQuestion(View v) {
        if (currentQuestionId + 1 >= lesson.getNumQuestions()) {
            //TODO: Switch to next activity;
            return;
        }
        hideAnswer();
        currentQuestionId += 1;
        displayQuestion();
    }

    private void displayQuestion() {
        Question question = lesson.getQuestion(currentQuestionId);
        binding.setQuestion(question);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (answerFragment != null) {
            ft.remove(answerFragment);
        }
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
        ft.add(R.id.fragmentLayout, answerFragment).commit();
    }

    public void onClickFragment(View v) {
        answerFragment.onClick(v);
    }

    public void validateAnswer(View v) {
        String given = answerFragment.getAnswer();
        String actual = lesson.getQuestion(currentQuestionId).getAnswer();
        if (given.equals(actual)) {
            correctAnswer();
        }
        else {
            incorrectAnswer();
        }
    }

    private void correctAnswer(){
        View layout = findViewById(R.id.answerValidationLayout);
        layout.setVisibility(View.VISIBLE);
        layout.setBackgroundResource(R.color.colorQuestionCorrectAnswerBackground);
    }

    private void incorrectAnswer() {
        View layout = findViewById(R.id.answerValidationLayout);
        layout.setVisibility(View.VISIBLE);
        layout.setBackgroundResource(R.color.colorQuestionIncorrectAnswerBackground);
    }

    private void hideAnswer() {
        View layout = findViewById(R.id.answerValidationLayout);
        layout.setVisibility(View.GONE);
    }

    // 5 parts:
    // Instruction
    // Question
    // Answer
    // Validate
    // progress bar

}
