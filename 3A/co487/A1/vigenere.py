# Does actual decryption

f = open("cipher.txt")
cipher = f.readline()[:-1]

# Return the key that would be used to convert a to b
# Also decrypt charb with key chara
def foo(chara, charb):
  diff = ord(charb) - ord(chara)
  if diff < 0:
    diff += 26
  return chr(65 + diff)

# Encrypt chara with charb key
def bar(chara, charb):
  diff = ord(chara) + ord(charb) - 130
  if diff > 25:
    diff -= 26
  return 

def decrypt(cipher, key):
  i = 0
  plain = ""
  for char in cipher:
    plain += foo(key[i%8], char)
    i += 1
  return plain

f = open("dict.txt")
words = [line[:-1] for line in f.readlines()]

#Print out the real plaintext
print decrypt(cipher, "HAZELNUT")

#Print out all plaintexts
#for word in words:
  #print decrypt(cipher, word)
  #print '$$$'
