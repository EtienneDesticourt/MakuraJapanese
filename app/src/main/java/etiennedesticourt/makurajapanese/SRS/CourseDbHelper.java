package etiennedesticourt.makurajapanese.SRS;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import etiennedesticourt.makurajapanese.R;
import etiennedesticourt.makurajapanese.Skill.Lesson;
import etiennedesticourt.makurajapanese.Skill.Question;

public class CourseDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "makura.db";
    private static CourseDbHelper instance;
    private Resources res;
    private static final String encoding = "UTF-8";
    private boolean loaded = false;

    public static CourseDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CourseDbHelper(context);
        }
        return instance;
    }

    private CourseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        res = context.getResources();
        context.deleteDatabase(DATABASE_NAME);
    }

    public void saveLesson(Lesson lesson) {
        SQLiteDatabase db = getWritableDatabase();

        // Set lesson as completed
        ContentValues insertValues = new ContentValues();
        insertValues.put(CourseContract.LessonEntry.COLUMN_NAME_COMPLETED, true);
        String id = String.valueOf(lesson.getId());
        db.update(CourseContract.LESSON_TABLE, insertValues, "id=?", new String[]{id});
        db.close();

        // Save all questions
        for (int i=0; i<lesson.getNumQuestions(); i++) {
            Question question = lesson.getQuestion(i);
            saveQuestion(question);
        }
    }

    public void saveQuestion(Question question) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CourseContract.QuestionEntry.COLUMN_NAME_INTERVAL, question.getInterval());
        values.put(CourseContract.QuestionEntry.COLUMN_NAME_NEXT_REVIEW, question.getNextReviewFormatted());
        String id = String.valueOf(question.getId());

        db.update(CourseContract.QUESTION_TABLE, values, "id=?", new String[]{id});
        db.close();
    }

    public String getString(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        return cursor.getString(index);
    }

    public int getInt(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        return cursor.getInt(index);
    }

    public boolean getBoolean(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        return cursor.getInt(index) == 1;
    }

    public Cursor getSkillByName(String name) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(CourseContract.SKILL_TABLE, null, "name=?", new String[]{name}, null, null, null, null);
    }

    public Cursor getLesson(int lessonId) {
        String id = String.valueOf(lessonId);
        SQLiteDatabase db = getReadableDatabase();
        return db.query(CourseContract.LESSON_TABLE, null, "id=?", new String[]{id}, null, null, null, null);
    }

    public Cursor getLessonsForSkill(int skillId) {
        String id = String.valueOf(skillId);
        SQLiteDatabase db = getReadableDatabase();
        return db.query(CourseContract.LESSON_TABLE, null, "skill_id=?", new String[]{id}, null, null, null, null);
    }

    public Cursor getQuestionsForLesson(int lessonId) {
        String id = String.valueOf(lessonId);
        SQLiteDatabase db = getReadableDatabase();
        return db.query(CourseContract.QUESTION_TABLE, null, "lesson_id=?", new String[]{id}, null, null, null, null);
    }

    public void onCreate(SQLiteDatabase db) {
        InputStream stream = res.openRawResource(R.raw.makura);
        String sql;
        try {
            sql = IOUtils.toString(stream, encoding);
        } catch (IOException e) {
            throw new RuntimeException("Unrecoverable error while creating database.");
        }
        executeBatchSql(db, sql);
        loaded = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void executeBatchSql(SQLiteDatabase db, String sql){
        String[] statements = sql.split(";");
        for (int i=0; i<statements.length-1; i++) {
            String statement = statements[i];
            db.execSQL(statement);
        }
    }
}