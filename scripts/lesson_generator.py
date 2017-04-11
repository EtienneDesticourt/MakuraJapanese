import random
import question
import word


class LessonGenerator(object):
    "Tool to generate full-fledged lessons from a list of words."

    def __init__(self, lid, lesson_words, known_words=[]):
        self.words = lesson_words
        self.id = lid

    def pick_definition_options(self, word):
        if len(self.words) < 4:
            options = self.words[:]
            options += [word.EMPTY] * (4 - len(options))
        else:
            options = self.words[:4]

        if word not in options:
            i = random.randrange(len(options))
            options[i] = word

        return options

    def generate_definition_question(self, word):
        qtype = question.DEFINITION
        question_text = word.english
        answer = word.japanese
        options = self.pick_definition_options(word)
        return question.Question(qtype, question_text, answer, options, self.id)

    def generate_transcription_question(self, word):
        qtype = question.TRANSCRIPTION
        question_text = word.japanese
        answer = word.hiragana
        options = None
        return question.Question(qtype, question_text, answer, options, self.id)

    def to_sql(self):
        sql_values = []
        for word in self.words:
            definition = self.generate_definition_question(word)
            qsql = definition.to_sql()
            sql_values.append(qsql)

            transcription = self.generate_transcription_question(word)
            qsql = transcription.to_sql()
            sql_values.append(qsql)

        return ",\n".join(sql_values)
