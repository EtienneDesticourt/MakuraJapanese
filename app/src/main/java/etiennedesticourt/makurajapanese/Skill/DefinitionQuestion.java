package etiennedesticourt.makurajapanese.Skill;

import android.databinding.BindingAdapter;

import java.util.ArrayList;

public class DefinitionQuestion extends Question{
    private ArrayList<String> imageNames;
    private ArrayList<String> options;

    public DefinitionQuestion(ArrayList<String> options, ArrayList<String> imageNames, String sentence, String answer) {
        super(QuestionType.WORD_DEFINITION, sentence, answer);
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
