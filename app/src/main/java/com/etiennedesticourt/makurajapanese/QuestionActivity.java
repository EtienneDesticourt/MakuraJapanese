package com.etiennedesticourt.makurajapanese;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import com.etiennedesticourt.makurajapanese.Analytics.FirebaseLogger;
import com.etiennedesticourt.makurajapanese.Analytics.Logger;
import com.etiennedesticourt.makurajapanese.SRS.CourseDbHelper;
import com.etiennedesticourt.makurajapanese.Skill.Lesson;
import com.etiennedesticourt.makurajapanese.Skill.Question;
import com.etiennedesticourt.makurajapanese.Skill.QuestionType;
import com.etiennedesticourt.makurajapanese.Utils.SimpleTimer;
import com.etiennedesticourt.makurajapanese.databinding.ActivityQuestionBinding;

public class QuestionActivity extends AppCompatActivity {
    public static final String INTENT_LESSON_TAG = "lesson";
    private final String NEXT_BUTTON_TEXT = "NEXT";
    private final String VALIDATE_BUTTON_TEXT = "VALIDATE";
    private final String ANSWER_STATUS_CORRECT = "Correct.";
    private final String ANSWER_STATUS_INCORRECT = "Incorrect.";
    private final String SOUND_FOLDER = "raw";
    private Lesson lesson;
    private ArrayList<Question> questions;
    private AnswerFragment answerFragment;
    private ActivityQuestionBinding binding;
    private boolean validated = false;
    private boolean chartVisible = false;
    private int failures = 0;
    private int attempts = 1;
    private Logger logger = FirebaseLogger.INSTANCE;
    private SimpleTimer lessonTimer;
    private SimpleTimer questionTimer;
    private boolean doneWithReview = false;

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

        lessonTimer = SimpleTimer.start();
        logger.logLessonStartedEvent(lesson.getId());
    }

    public void onPause() {
        super.onPause();
        logger.logLessonClosedEvent(lesson.getId(), lessonTimer.getDuration());
    }

    public void toggleChart(View v) {
        chartVisible = !chartVisible;
        if (chartVisible) {
            findViewById(R.id.hiraganaChart).setVisibility(View.VISIBLE);
            findViewById(R.id.hiraganaChart).bringToFront();
            findViewById(R.id.hiraganaButton).bringToFront();
            logger.logHiraganaShownEvent(lesson.getId(), getCurrentQuestion().getId());
        }
        else {
            findViewById(R.id.hiraganaChart).setVisibility(View.INVISIBLE);
        }

    }

    public void playSound(View v) {
        Question question = getCurrentQuestion();
        int questionId = question.getId();
        String soundFile = "q" + String.valueOf(questionId);
        int resourceId = getResources().getIdentifier(soundFile, SOUND_FOLDER, MainActivity.PACKAGE_NAME);
        if (resourceId != 0) {
            MediaPlayer sound = MediaPlayer.create(this, resourceId);
            sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            sound.start();
        }
        logger.logQuestionSoundPlayedEvent(lesson.getId(), questionId);
    }

    public Question getCurrentQuestion() {
        return questions.get(0);
    }

    public int getNumRemainingQuestions() {
        return questions.size();
    }

    public void nextQuestion() {
        if (getNumRemainingQuestions() == 0) {
            doneWithReview = true;
            lesson.setCompleted(true);
            saveLesson();
            startLessonEndActivity();
            logger.logLessonCompletedEvent(lesson.getId(), lessonTimer.getDuration(),
                    failures, attempts);
            return;
        }
        attempts += 1;
        hideAnswer();
        displayQuestion();
    }

    public void onClickFragment(View v) {
        answerFragment.onClick(v);
    }

    public void validateAnswer(View v) {
        if (doneWithReview) { //Handles spamming the next button at the end of the lesson
            return;
        }
        Button button = (Button) v;
        if (!validated) {
            Question question = getCurrentQuestion();
            String given = answerFragment.getAnswer();
            questions.remove(question);
            boolean answerIsCorrect = question.answerIsCorrect(given);
            if (answerIsCorrect) {
                displayCorrectAnswer();
                question.increaseInterval();
            }
            else {
                questions.add(question);
                displayIncorrectAnswer();
                question.resetInterval();
                failures += 1;
            }
            button.setText(NEXT_BUTTON_TEXT);
            validated = true;
            logger.logQuestionValidatedEvent(lesson.getId(), question.getId(),
                    questionTimer.getDuration(),
                    question.getAttempts(),
                    answerIsCorrect,
                    given);
        }
        else {
            nextQuestion();
            button.setText(VALIDATE_BUTTON_TEXT);
            validated = false;
        }
    }

    private void displayQuestion() {
        questionTimer = SimpleTimer.start();
        Question question = getCurrentQuestion();
        question.increaseAttempts();
        binding.setQuestion(question);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (answerFragment != null) {
            ft.remove(answerFragment).commit();
            ft = getFragmentManager().beginTransaction();
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

        logger.logQuestionOpenedEvent(lesson.getId(), question.getId());
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
        CourseDbHelper dbHelper = CourseDbHelper.getInstance(getApplicationContext());
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
                        Question q = getCurrentQuestion();
                        logger.logLessonAbortedEvent(lesson.getId(), q.getId(),
                                lessonTimer.getDuration(),
                                failures,
                                attempts);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == event.ACTION_DOWN) {
            boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
            logger.logScreenTouchedEvent("QuestionActivity", (int) event.getX(), (int) event.getY(), portrait);
        }
        return super.dispatchTouchEvent(event);
    }
}
