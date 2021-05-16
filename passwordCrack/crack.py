from passlib.hash import bcrypt

from libs import pingo

with open("pass.txt", "rb") as text_file:
    words = text_file.read().splitlines()

password = input('hash to crack: ')
length = len(words)

correct_word = ""
for (index, word) in enumerate(words):
    pingo(index, length, prefix='Wait:', suffix='Complete')
    correct = bcrypt.verify(word, password)
    if correct:
        correct_word = word
        print()
        break

print("Results:", correct_word)