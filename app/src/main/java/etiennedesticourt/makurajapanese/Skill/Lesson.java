package etiennedesticourt.makurajapanese.Skill;

import java.io.Serializable;
import java.util.ArrayList;

public class Lesson implements Serializable{
    private int number;
    private String[] words;
    private String wordsSentence;
    private boolean completed;
    private ArrayList<Question> questions;

    public Lesson(int number, String words, boolean completed, ArrayList<Question> questions) {
        this.number = number;
        this.words = words.split(" ");
        this.wordsSentence = words;
        this.completed = completed;
        this.questions = questions;
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
