package etiennedesticourt.makurajapanese.SRS;

import android.provider.BaseColumns;

public class CourseContract {
    private CourseContract() {}

    public static final String SKILL_TABLE = "skill";
    public static final String LESSON_TABLE = "lesson";
    public static final String QUESTION_TABLE = "question";

    public static class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "course";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public static class SkillEntry implements BaseColumns {
        public static final String TABLE_NAME = "skill";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }

    public static class LessonEntry implements BaseColumns {
        public static final String TABLE_NAME = "lesson";
        public static final String COLUMN_NAME_SKILL_ID = "skill_id";
        public static final String COLUMN_NAME_WORDS = "words";
        public static final String COLUMN_NAME_COMPLETED = "completed";
        public static final String COLUMN_NAME_NUMBER = "number";
    }

    public static class QuestionEntry implements BaseColumns {
        public static final String TABLE_NAME = "question";
        public static final String COLUMN_NAME_INTERVAL = "interval";
        public static final String COLUMN_NAME_NEXT_REVIEW = "next_review";
        public static final String COLUMN_NAME_LESSON_ID = "lesson_id";
        public static final String COLUMN_NAME_NUMBER = "number";
        public static final String COLUMN_NAME_SENTENCE = "sentence";
        public static final String COLUMN_NAME_ANSWER = "answer";
        public static final String COLUMN_NAME_OPTION1 = "option1";
        public static final String COLUMN_NAME_IMAGE1 = "image1";
        public static final String COLUMN_NAME_OPTION2 = "option2";
        public static final String COLUMN_NAME_IMAGE2 = "image2";
        public static final String COLUMN_NAME_OPTION3 = "option3";
        public static final String COLUMN_NAME_IMAGE3 = "image3";
        public static final String COLUMN_NAME_OPTION4 = "option4";
        public static final String COLUMN_NAME_IMAGE4 = "image4";
    }
}
