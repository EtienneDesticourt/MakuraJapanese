package etiennedesticourt.makurajapanese.Analytics;

public interface Logger {
    Logger INSTANCE = null;

    void logSkillOpenedEvent(int id);

    void logSkillLessonSwipedEvent(int skillId, int lessonId);

    void logSkillClosedEvent(int skillId, int duration);

    void logSkillCompletedEvent(int skillId);

    void logLessonStartedEvent(int lessonId);

    void logLessonCompletedEvent(int lessonId, int duration,
                                        int numFailures,
                                        int numAttempts);

    void logLessonAbortedEvent(int lessonId, int questionId,
                                      int duration,
                                      int numFailures,
                                      int numAttempts);

    void logLessonClosedEvent(int lessonId, int duration);

    void logQuestionOpenedEvent(int lessonId, int questionId);

    void logQuestionOptionSelectedEvent(int questionId, int optionId, String optionName);

    void logQuestionSoundPlayedEvent(int lessonId, int questionId);

    void logQuestionValidatedEvent(int lessonId, int questionId,
                                          int duration,
                                          int attempts,
                                          boolean success,
                                          String answer);

    void logHiraganaShownEvent(int lessonId, int questionId);

    void logScreenTouchedEvent(String activityName, int x, int y);

    void logScreenRotatedEvent(String activityName, String orientation);
}
