package etiennedesticourt.makurajapanese.Skill;

import java.io.Serializable;

public class Question implements Serializable{
    private QuestionType type;
    private String sentence;
    private String answer;

    public Question(QuestionType type, String sentence, String answer) {
        this.type = type;
        this.sentence = sentence;
        this.answer = answer;
    }
    public String getExplanation() {
        return QuestionType.getExplanation(type);
    }

    public QuestionType getType() {
        return type;
    }

    public String getSentence() {
        return sentence;
    }

    public String getAnswer() {
        return answer;
    }
}
