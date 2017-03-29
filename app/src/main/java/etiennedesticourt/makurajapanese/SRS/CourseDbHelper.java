package etiennedesticourt.makurajapanese.SRS;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.common.util.concurrent.UncheckedExecutionException;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import etiennedesticourt.makurajapanese.R;

public class CourseDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "makura.db";
    private Resources res;
    private static final String encoding = "UTF-8";
    private boolean loaded = false;

    public CourseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        res = context.getResources();
        //context.deleteDatabase(DATABASE_NAME);
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
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        Log.d("DATABASE", "Reading tables.");
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Log.d("DATABASE", "Table Name=> "+c.getString(0));
                c.moveToNext();
            }
        }
        return db.query(CourseContract.SKILL_TABLE, null, "name=?", new String[]{name}, null, null, null, null);
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
            throw new UncheckedExecutionException("Unrecoverable error while creating database.", null);
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