package etiennedesticourt.makurajapanese;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import etiennedesticourt.makurajapanese.SRS.CourseDbHelper;
import etiennedesticourt.makurajapanese.Skill.CourseFactory;
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
    private ArrayList<Question> questions;
    private AnswerFragment answerFragment;
    private ActivityQuestionBinding binding;
    private boolean validated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        Intent intent = getIntent();
        lesson = (Lesson) intent.getSerializableExtra(INTENT_LESSON_TAG);
        questions = new ArrayList<>(lesson.getQuestions());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_question);
        TextView sentence = (TextView) binding.getRoot().findViewById(R.id.question);
        sentence.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        displayQuestion();
    }

    public Question getCurrentQuestion() {
        return questions.get(0);
    }

    public int getNumRemainingQuestions() {
        return questions.size();
    }

    public void nextQuestion() {
        if (getNumRemainingQuestions() == 0) {
            lesson.setCompleted(true);
            saveLesson();
            startLessonEndActivity();
            return;
        }
        hideAnswer();
        displayQuestion();
    }

    public void onClickFragment(View v) {
        answerFragment.onClick(v);
    }

    public void validateAnswer(View v) {
        Button button = (Button) v;
        if (!validated) {
            Question question = getCurrentQuestion();
            String given = answerFragment.getAnswer();
            String actual = question.getAnswer();
            questions.remove(question);
            if (given.equals(actual)) {
                displayCorrectAnswer();
                question.increaseInterval();
            }
            else {
                questions.add(question);
                displayIncorrectAnswer();
                question.resetInterval();
            }
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
        Question question = getCurrentQuestion();
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

    private void displayCorrectAnswer(){
        TextView answerStatus = (TextView) findViewById(R.id.answerStatus);
        answerStatus.setText(ANSWER_STATUS_CORRECT);
        View layout = findViewById(R.id.answerValidationLayout);
        layout.setVisibility(View.VISIBLE);
        layout.setBackgroundResource(R.color.colorQuestionCorrectAnswerBackground);
    }

    private void displayIncorrectAnswer() {
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

    private void saveLesson() {
        CourseDbHelper dbHelper = CourseDbHelper.getInstance(this);
        dbHelper.saveLesson(lesson);
    }

    private void startLessonEndActivity() {
        Intent intent = new Intent(this, LessonEndActivity.class);
        //intent.putExtra(LessonEndActivity.INTENT_LESSON_TAG, lesson);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to leave? You will lose your progress on this lesson.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
