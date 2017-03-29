package etiennedesticourt.makurajapanese.Skill;

import java.util.ArrayList;

public class Skill {
    private String title;
    private ArrayList<Lesson> lessons;

    public Skill(String title) {
        this.title = title;
    }

    public Skill(String title, ArrayList<Lesson> lessons) {
        this.title = title;
        this.lessons = lessons;
    }

    public int getMasteryLevel() {
        if (lessons.size() == 0) {
            return 5;
        }
        int total = 0;
        for (int i=0; i<lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            total += lesson.getMasteryLevel();
        }
        return total / lessons.size();
    }

    public boolean isCompleted() {
        for (int i=0; i<lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            if (!lesson.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    public String getTitle(){
        return title;
    }

    public ArrayList<Lesson> getLessons(){
        return lessons;
    }

    public Lesson getLesson(int index) {
        return lessons.get(index);
    }

    public int getNumLessons() {
        return lessons.size();
    }
}
