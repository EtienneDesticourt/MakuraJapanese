DEFINITION = "definition"
TRANSCRIPTION = "transcription"


class Question(object):
    number = 0

    def __init__(self, qtype, question, answer, options, lesson_id):
        self.type = qtype
        self.question = question
        self.answer = answer
        self.options = options
        self.lesson_id = lesson_id
        Question.number += 1

    def to_sql(self):
        values = ["NULL",
                  "'%s'" % self.type ,
                  "1",
                  "'2017-04-10'",
                  Question.number,
                  "'%s'" % self.question,
                  "'%s'" % self.answer,
                  self.lesson_id]
        if not self.options:
            values += ["''"] * 8
        else:
            values += ["'%s'" % word.japanese for word in self.options]
            values += ["'%s'" % word.romanji.replace(" ", "").lower() for word in self.options]
        return "(" + ",".join([str(v) for v in values]) + ")"
