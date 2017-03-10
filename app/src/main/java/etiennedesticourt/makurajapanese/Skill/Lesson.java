package etiennedesticourt.makurajapanese.Skill;

import java.io.Serializable;
import java.util.ArrayList;

public class Lesson implements Serializable{
    private int id;
    private String[] words;
    private String wordsSentence;
    private ArrayList<Question> questions;

    public Lesson(int id, String words, ArrayList<Question> questions) {
        this.id = id;
        this.words = words.split(" ");
        this.wordsSentence = words;
        this.questions = questions;
    }

    public int getId() {
        return id;
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
        return "Lesson " + String.valueOf(id);
    }
}
