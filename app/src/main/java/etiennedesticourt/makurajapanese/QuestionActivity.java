package etiennedesticourt.makurajapanese;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import etiennedesticourt.makurajapanese.Skill.Lesson;
import etiennedesticourt.makurajapanese.Skill.Question;
import etiennedesticourt.makurajapanese.Skill.QuestionType;
import etiennedesticourt.makurajapanese.databinding.ActivityQuestionBinding;

public class QuestionActivity extends AppCompatActivity {
    public static final String INTENT_LESSON_TAG = "lesson";
    private final String NEXT_BUTTON_TEXT = "NEXT";
    private final String VALIDATE_BUTTON_TEXT = "VALIDATE";
    private final String ANSWER_STATUS_CORRECT = "Correct.";
    private final String ANSWER_STATUS_INCORRECT = "Incorrect.";
    private Lesson lesson;
    private int currentQuestionId = 0;
    private AnswerFragment answerFragment;
    private ActivityQuestionBinding binding;
    private boolean validated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        Intent intent = getIntent();
        lesson = (Lesson) intent.getSerializableExtra(INTENT_LESSON_TAG);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_question);
        TextView sentence = (TextView) binding.getRoot().findViewById(R.id.question);
        sentence.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        displayQuestion();
    }

    public void nextQuestion() {
        if (currentQuestionId + 1 >= lesson.getNumQuestions()) {
            startLessonEndActivity();
            return;
        }
        hideAnswer();
        currentQuestionId += 1;
        displayQuestion();
    }

    public void onClickFragment(View v) {
        answerFragment.onClick(v);
    }

    public void validateAnswer(View v) {
        Button button = (Button) v;
        if (!validated) {
            displayAnswer();
            button.setText(NEXT_BUTTON_TEXT);
            validated = true;
        }
        else {
            nextQuestion();
            button.setText(VALIDATE_BUTTON_TEXT);
            validated = false;
        }
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

    private void displayAnswer() {
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
        TextView answerStatus = (TextView) findViewById(R.id.answerStatus);
        answerStatus.setText(ANSWER_STATUS_CORRECT);
        View layout = findViewById(R.id.answerValidationLayout);
        layout.setVisibility(View.VISIBLE);
        layout.setBackgroundResource(R.color.colorQuestionCorrectAnswerBackground);
    }

    private void incorrectAnswer() {
        TextView answerStatus = (TextView) findViewById(R.id.answerStatus);
        answerStatus.setText(ANSWER_STATUS_INCORRECT);
        View layout = findViewById(R.id.answerValidationLayout);
        layout.setVisibility(View.VISIBLE);
        layout.setBackgroundResource(R.color.colorQuestionIncorrectAnswerBackground);
    }

    private void hideAnswer() {
        View layout = findViewById(R.id.answerValidationLayout);
        layout.setVisibility(View.GONE);
    }

    private void startLessonEndActivity() {
        Intent intent = new Intent(this, LessonEndActivity.class);
        //intent.putExtra(LessonEndActivity.INTENT_LESSON_TAG, lesson);
        startActivity(intent);
    }
}
