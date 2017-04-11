

class Word(object):

    def __init__(self, japanese, english, hiragana, romanji):
        self.japanese = japanese
        self.english = english
        self.hiragana = hiragana
        self.romanji = romanji

    @staticmethod
    def from_string(string):
        jp, hi, ro, en = string.split(",")
        return Word(jp, en, hi, ro)

EMPTY = Word('', '', '', '')
