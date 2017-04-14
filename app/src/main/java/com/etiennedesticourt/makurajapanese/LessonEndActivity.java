package com.etiennedesticourt.makurajapanese;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LessonEndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_end);
    }

    public void goBackToMainActivity(View v) {
        finish();
        /*
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
    }
}
