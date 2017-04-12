package etiennedesticourt.makurajapanese.Skill;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;

public class Question implements Serializable{
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private QuestionType type;
    private int id;
    private String sentence;
    private String answer;
    private int interval;
    private Date nextReview;

    public Question(QuestionType type, int id, String sentence, String answer, int interval, Date nextReview) {
        this.id = id;
        this.type = type;
        this.sentence = sentence;
        this.answer = answer;
        this.interval = interval;
        this.nextReview = nextReview;
    }

    public boolean answerIsCorrect(String givenAnswer) {
        double similarity = StringUtils.getJaroWinklerDistance(answer, givenAnswer);
        return similarity >= 0.9;
    }

    public void resetInterval() {
        interval = 1;
        nextReview = calcNextReview();
    }

    public void increaseInterval() {
        interval += Math.ceil( interval / 2 );
        nextReview = calcNextReview();
    }

    public Date calcNextReview() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, interval);
        return cal.getTime();
    }

    public int getDaysUntilNextReview() {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        long diff = today.getTime() - nextReview.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public int getId() {
        return id;
    }

    public String getNextReviewFormatted() {
        return Question.DATE_FORMAT.format(nextReview);
    }

    public Date getNextReview() {
        return nextReview;
    }

    public int getInterval() {
        return interval;
    }

    public boolean isOverdue() {
        return getDaysUntilNextReview() <= 0;
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
