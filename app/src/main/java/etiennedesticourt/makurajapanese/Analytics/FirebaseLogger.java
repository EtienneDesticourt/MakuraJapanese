package etiennedesticourt.makurajapanese.Analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public enum FirebaseLogger implements Logger{
    INSTANCE;

    private FirebaseAnalytics firebaseAnalytics;

    public void setContext(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void logSkillOpenedEvent(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.SKILL_ID, id);
        firebaseAnalytics.logEvent(Event.SKILL_OPENED, bundle);
    }

    public void logSkillLessonSwipedEvent(int skillId, int lessonId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.SKILL_ID, skillId);
        bundle.putInt(Param.LESSON_ID, lessonId);
        firebaseAnalytics.logEvent(Event.SKILL_LESSON_SWIPED, bundle);
    }

    public void logSkillClosedEvent(int skillId, int duration) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.SKILL_ID, skillId);
        bundle.putInt(Param.DURATION, duration);
        firebaseAnalytics.logEvent(Event.SKILL_CLOSED, bundle);
    }

    public void logSkillCompletedEvent(int skillId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.SKILL_ID, skillId);
        firebaseAnalytics.logEvent(Event.SKILL_COMPLETED, bundle);
    }

    public void logLessonStartedEvent(int lessonId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.LESSON_ID, lessonId);
        firebaseAnalytics.logEvent(Event.LESSON_STARTED, bundle);
    }

    public void logLessonCompletedEvent(int lessonId, int duration,
                                        int numFailures,
                                        int numAttempts) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.LESSON_ID, lessonId);
        bundle.putInt(Param.DURATION, duration);
        bundle.putInt(Param.QUESTION_FAILURES, numFailures);
        bundle.putInt(Param.QUESTION_ATTEMPTS, numAttempts);
        firebaseAnalytics.logEvent(Event.LESSON_COMPLETED, bundle);
    }

    public void logLessonAbortedEvent(int lessonId, int questionId,
                                      int duration,
                                      int numFailures,
                                      int numAttempts) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.LESSON_ID, lessonId);
        bundle.putInt(Param.QUESTION_ID, questionId);
        bundle.putInt(Param.DURATION, duration);
        bundle.putInt(Param.QUESTION_FAILURES, numFailures);
        bundle.putInt(Param.QUESTION_ATTEMPTS, numAttempts);
        firebaseAnalytics.logEvent(Event.LESSON_ABORTED, bundle);
    }

    public void logLessonClosedEvent(int lessonId, int duration) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.LESSON_ID, lessonId);
        bundle.putInt(Param.DURATION, duration);
        firebaseAnalytics.logEvent(Event.LESSON_CLOSED, bundle);
    }

    public void logQuestionOpenedEvent(int lessonId, int questionId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.LESSON_ID, lessonId);
        bundle.putInt(Param.QUESTION_ID, questionId);
        firebaseAnalytics.logEvent(Event.QUESTION_OPENED, bundle);
    }

    public void logQuestionOptionSelectedEvent(int questionId, int optionId, String optionName) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.QUESTION_ID, questionId);
        bundle.putInt(Param.OPTION_ID, optionId);
        bundle.putString(Param.OPTION_NAME, optionName);
        firebaseAnalytics.logEvent(Event.QUESTION_OPTION_SELECTED, bundle);
    }

    public void logQuestionSoundPlayedEvent(int lessonId, int questionId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.LESSON_ID, lessonId);
        bundle.putInt(Param.QUESTION_ID, questionId);
        firebaseAnalytics.logEvent(Event.QUESTION_SOUND_PLAYED, bundle);
    }

    public void logQuestionValidatedEvent(int lessonId, int questionId,
                                          int duration,
                                          int attempts,
                                          boolean success,
                                          String answer) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.LESSON_ID, lessonId);
        bundle.putInt(Param.QUESTION_ID, questionId);
        bundle.putInt(Param.DURATION, duration);
        bundle.putInt(Param.QUESTION_ATTEMPTS, attempts);
        bundle.putString(Param.QUESTION_ANSWER, answer);
        bundle.putBoolean(Param.SUCCESS, success);
        firebaseAnalytics.logEvent(Event.QUESTION_VALIDATED, bundle);
    }

    public void logHiraganaShownEvent(int lessonId, int questionId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.LESSON_ID, lessonId);
        bundle.putInt(Param.QUESTION_ID, questionId);
        firebaseAnalytics.logEvent(Event.HIRAGANA_SHOWN, bundle);
    }

    public void logScreenTouchedEvent(String activityName, int x, int y, boolean portrait_orientation) {
        Bundle bundle = new Bundle();
        bundle.putString(Param.ACTIVITY_NAME, activityName);
        bundle.putInt(Param.LOCATION_X, x);
        bundle.putInt(Param.LOCATION_Y, y);
        bundle.putBoolean(Param.ORIENTATION, portrait_orientation);
        firebaseAnalytics.logEvent(Event.SCREEN_TOUCHED, bundle);
    }

    public void logScreenRotatedEvent(String activityName, String orientation) {
        Bundle bundle = new Bundle();
        bundle.putString(Param.ACTIVITY_NAME, activityName);
        bundle.putString(Param.ORIENTATION, orientation);
        firebaseAnalytics.logEvent(Event.SCREEN_ROTATED, bundle);
    }
}