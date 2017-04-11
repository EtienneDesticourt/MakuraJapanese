import lesson_generator
from word import Word

WORD_DATA = """飼う,かう,kau,To keep an animal
犬,いぬ,inu,Dog
猫,ねこ,neko,Cat
動物,どうぶつ,doubutsu,Animal
豚,ぶた,buta,Pig"""
words = []
for word_string in WORD_DATA.split("\n"):
    word = Word.from_string(word_string)
    words.append(word)

gen = lesson_generator.LessonGenerator(7, words)

with open("output.txt", "w", encoding="utf-8") as f:
    f.write(gen.to_sql())
