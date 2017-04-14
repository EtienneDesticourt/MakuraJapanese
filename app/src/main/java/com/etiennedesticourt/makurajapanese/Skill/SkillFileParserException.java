package com.etiennedesticourt.makurajapanese.Skill;

@Deprecated
public class SkillFileParserException extends Exception{
    public final String message;

    public SkillFileParserException(String message) {
        this.message = message;
    }
}
