from collections import namedtuple
import lesson_generator
from word import Word

WORD_DATA = """飼う,かう,kau,To keep an animal"""

words = []
for word_string in WORD_DATA.split("\n"):
    word = Word.from_string(word_string)
    words.append(word)

Sentence = namedtuple('Sentence', ['japanese', 'english'])
SENTENCE_DATA = """誰の犬ですか。;Whose dog is it?"""
sentences = [line.split(";") for line in SENTENCE_DATA.split("\n")]
sentences = [Sentence(japanese=japanese, english=english) for japanese, english in sentences]


gen = lesson_generator.LessonGenerator(7, words, sentences)

with open("output.txt", "w", encoding="utf-8") as f:
    f.write(gen.to_sql())
