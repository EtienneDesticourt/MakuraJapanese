package etiennedesticourt.makurajapanese.Skill;

import java.util.ArrayList;

public class Skill {
    private String title;
    private ArrayList<Lesson> lessons;

    public Skill(String title, ArrayList<Lesson> lessons) {
        this.title = title;
        this.lessons = lessons;
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
