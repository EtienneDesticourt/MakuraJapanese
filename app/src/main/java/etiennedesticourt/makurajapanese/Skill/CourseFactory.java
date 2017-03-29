package etiennedesticourt.makurajapanese.Skill;

import android.database.Cursor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import etiennedesticourt.makurajapanese.SRS.CourseDbHelper;

//TODO: Add error handling everywhere there are cursors
//TODO: Add interval and shit to questions
public class CourseFactory {
    private CourseDbHelper dbHelper;

    public CourseFactory(CourseDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public Skill buildSkill(String skillName) {
        Cursor cursor;

        cursor = dbHelper.getSkillByName(skillName);
        cursor.moveToFirst();
        int skillId = cursor.getInt(0);

        ArrayList<Lesson> lessons = new ArrayList<>();
        cursor = dbHelper.getLessonsForSkill(skillId);
        while (cursor.moveToNext()) {
            Lesson lesson = buildLesson(cursor);
            lessons.add(lesson);
        }

        return new Skill(skillName, lessons);
    }

    private Lesson buildLesson(Cursor cursor) {
        int lessonId = cursor.getInt(0);
        int number = dbHelper.getInt(cursor, "number");
        String words = dbHelper.getString(cursor, "words");
        boolean completed = dbHelper.getBoolean(cursor, "completed");

        ArrayList<Question> questions = new ArrayList<>();
        cursor = dbHelper.getQuestionsForLesson(lessonId);
        while (cursor.moveToNext()) {
            Question question = buildQuestion(cursor);
            questions.add(question);
        }

        return new Lesson(lessonId, number, words, completed, questions);
    }

    private Question buildQuestion(Cursor cursor) {
        int questionId = cursor.getInt(0);
        String type = dbHelper.getString(cursor, "type");
        int interval = dbHelper.getInt(cursor, "interval");
        String nextReviewString = dbHelper.getString(cursor, "next_review");
        Date nextReview = null;
        try {
            nextReview = Question.DATE_FORMAT.parse(nextReviewString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int number = dbHelper.getInt(cursor, "number");
        String sentence = dbHelper.getString(cursor, "sentence");
        String answer = dbHelper.getString(cursor, "answer");
        String option1 = dbHelper.getString(cursor, "option1");
        String option2 = dbHelper.getString(cursor, "option2");
        String option3 = dbHelper.getString(cursor, "option3");
        String option4 = dbHelper.getString(cursor, "option4");
        String image1 = dbHelper.getString(cursor, "image1");
        String image2 = dbHelper.getString(cursor, "image2");
        String image3 = dbHelper.getString(cursor, "image3");
        String image4 = dbHelper.getString(cursor, "image4");

        if (QuestionType.fromString(type) == QuestionType.WORD_DEFINITION) {
            ArrayList<String> options = new ArrayList<>();
            options.add(option1);
            options.add(option2);
            options.add(option3);
            options.add(option4);
            ArrayList<String> images = new ArrayList<>();
            images.add(image1);
            images.add(image2);
            images.add(image3);
            images.add(image4);
            return new DefinitionQuestion(options, images, questionId, sentence, answer, interval, nextReview);
        }

        return new Question(QuestionType.fromString(type), questionId, sentence, answer, interval, nextReview);
    }
}
