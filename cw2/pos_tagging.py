# File: pos_tagging.py
# Template file for Informatics 2A Assignment 2:
# 'A Natural Language Query System in Python/NLTK'

# John Longley, November 2012
# Revised November 2013 and November 2014 with help from Nikolay Bogoychev
# Revised November 2015 by Toms Bergmanis


# PART B: POS tagging


from statements import *

# The tagset we shall use is:
# P  A  Ns  Np  Is  Ip  Ts  Tp  BEs  BEp  DOs  DOp  AR  AND  WHO  WHICH  ?

# Tags for words playing a special role in the grammar:

function_words_tags = [('a','AR'), ('an','AR'), ('and','AND'),
     ('is','BEs'), ('are','BEp'), ('does','DOs'), ('do','DOp'),
     ('who','WHO'), ('which','WHICH'), ('Who','WHO'), ('Which','WHICH'), ('?','?')]
     # upper or lowercase tolerated at start of question.

function_words = [p[0] for p in function_words_tags]


def unchanging_plurals():
	NN = set()
	NNS = set()
	with open("sentences.txt", "r") as f:
		for line in f:
			for word, pos in [x.split('|') for x in line.split(' ')]:
				if pos == 'NN':
					NN.add(word)
				elif pos == 'NNS':
					NNS.add(word)
	return list(NN.intersection(NNS))


unchanging_plurals_list = unchanging_plurals()


def match(str,s):
    return re.match(str + '$', s) != None


def noun_stem (s):
	#"""extracts the stem from a plural noun, or returns empty string"""

    str = ''

    if s in unchanging_plurals_list:
        str = s
    if match('.*men',s):
        if (s[0:len(s)-3] + 'man' , 'NN') in brown.tagged_words():
            str = s[0:len(s)-3] + 'man'
    if (match('.*[aeiou]ys',s)):
        str = s[0:len(s)-1]
    if (match('.*.[^aeiou]ies',s)):
        str = s[0:len(s)-3] + 'y'
    if (match('[^aeiou]ies',s)):
        str = s[0:len(s)-1]
    if (match('.*(o|x|ch|sh|ss|zz)es',s)):
        str = s[0:len(s)-2]
    if (match('.*([^s]se|[^z]ze)s',s)):
        str = s[0:len(s)-1]
    if (match('.*([^iosxz]|(ch)|(sh))es',s)):
        str = s[0:len(s)-1]
    if (match('.*([^aeiousxyz]|ch|sh)s',s)):
        str = s[0:len(s)-1]
    return str


def tag_word (lx,wd):
	#"""returns a list of all possible tags for wd relative to lx"""
    taggedset = set()
    for (word,tag) in function_words_tags:
        if (wd == word):
            taggedset.add(tag)

    if wd in lx.getAll('P'):
      taggedset.add('P')
    if wd in lx.getAll('A'):
      taggedset.add('A')

    if wd in lx.getAll('N'):
      taggedset.add('Ns')
    if noun_stem(wd) in lx.getAll('N'):
      taggedset.add('Np')

    if wd in lx.getAll('I'):
      taggedset.add('Ip')
    if verb_stem(wd) in lx.getAll('I'):
      taggedset.add('Is')

    if wd in lx.getAll('T'):
      taggedset.add('Tp')
    if verb_stem(wd) in lx.getAll('T'):
      taggedset.add('Ts')

    return list(taggedset)

def tag_words(lx, wds):
	"""returns a list of all possible taggings for a list of words"""
	if (wds == []):
		return [[]]
	else:
		tag_first = tag_word(lx, wds[0])
		tag_rest = tag_words(lx, wds[1:])
		return [[fst] + rst for fst in tag_first for rst in tag_rest]

# End of PART B.
