package com.etiennedesticourt.makurajapanese.Skill;

import java.util.ArrayList;
import java.util.Date;

public class DefinitionQuestion extends Question{
    private ArrayList<String> imageNames;
    private ArrayList<String> options;

    public DefinitionQuestion(ArrayList<String> options, ArrayList<String> imageNames, int id, String sentence, String answer, int interval, Date date) {
        super(QuestionType.WORD_DEFINITION, id, sentence, answer, interval, date);
        this.options = options;
        this.imageNames = imageNames;
    }

    public String getImageResource(int index) {
        return imageNames.get(index);
    }

    public String getOption(int index) {
        return options.get(index);
    }
}
