package etiennedesticourt.makurajapanese.Skill;

public enum QuestionType {
    WORD_DEFINITION,
    SENTENCE_CONSTRUCTION,
    WRITTEN_TRANSLATION;

    public static String getExplanation(QuestionType type) {
        if (type == WORD_DEFINITION) {
            return "Choose the correct translation.";
        }
        if (type == SENTENCE_CONSTRUCTION) {
            return "Pick words to build a translation.";
        }
        if (type == WRITTEN_TRANSLATION) {
            return "Translate the following sentence.";
        }
        return "No recorded explanation for this question type.";
    }

    public static QuestionType fromString(String type) {
        if ("definition".equals(type)) {
            return WORD_DEFINITION;
        }
        if ("construction".equals(type)) {
            return SENTENCE_CONSTRUCTION;
        }
        if ("translation".equals(type)) {
            return WRITTEN_TRANSLATION;
        }
        return null;
    }
}
