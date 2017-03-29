package etiennedesticourt.makurajapanese.Skill;

import java.io.Serializable;
import java.util.ArrayList;

public class Lesson implements Serializable{
    private int id;
    private int number;
    private String[] words;
    private String wordsSentence;
    private boolean completed;
    private int masteryLevel;
    private ArrayList<Question> questions;

    public Lesson(int id, int number, String words, boolean completed, ArrayList<Question> questions) {
        this.id = id;
        this.number = number;
        this.words = words.split(" ");
        this.wordsSentence = words;
        this.completed = completed;
        this.questions = questions;
        masteryLevel = calculateMasteryLevel();
    }

    public int calculateMasteryLevel() {
        if (questions.size() == 0) {
            return 5;
        }
        int numOverdue = 0;
        for (int i=0; i<questions.size(); i++) {
            Question question = questions.get(i);
            if (question.isOverdue()) {
                numOverdue += 1;
            }
        }
        int percentOverdue = numOverdue / questions.size() * 100;
        int masteryLevel = 5 - percentOverdue / 20;
        if (masteryLevel == 5 && percentOverdue > 0) {
            masteryLevel = 4;
        }
        return  masteryLevel;
    }

    public int getId() {
        return id;
    }

    public int getMasteryLevel() {
        return masteryLevel;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getNumber() {
        return number;
    }

    public String[] getWords(){
        return words;
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public ArrayList<Question> getQuestions(){
        return questions;
    }

    public String getWordsSentence() {
        return wordsSentence;
    }

    public String getName() {
        return "Lesson " + String.valueOf(number);
    }

    public int getNumQuestions() {
        return questions.size();
    }
}
