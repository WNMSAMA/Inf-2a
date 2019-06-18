# File: statements.py
# Template file for Informatics 2A Assignment 2:
# 'A Natural Language Query System in Python/NLTK'

# John Longley, November 2012
# Revised November 2013 and November 2014 with help from Nikolay Bogoychev
# Revised November 2015 by Toms Bergmanis
# Revised October 2017 by Chunchuan Lyu


# PART A: Processing statements

def add(lst,item):
    if (item not in lst):
        lst.insert(len(lst),item)

class Lexicon:
    """stores known word stems of various part-of-speech categories"""
    def __init__(self):
        self.dict = {}
    def add(self,stem,cat):
        self.dict.setdefault(cat,set()).add(stem)
    def getAll(self,cat):
        return list(self.dict.get(cat,[]))


class FactBase:
    """stores unary and binary relational facts"""
    def __init__(self):
        self.unary = set()
        self.binary = set()
    def addUnary(self,pred,e1):
        self.unary.add((pred,e1))
    def queryUnary(self,pred,e1):
        return (pred,e1) in self.unary
    def addBinary(self,pred,e1,e2):
        self.binary.add((pred,e1,e2))
    def queryBinary(self,pred,e1,e2):
        return (pred,e1,e2) in self.binary


import re
from nltk.corpus import brown

verbs = []
for a in brown.tagged_words():
    if a[1] == 'VB' or a[1] == 'VBZ':
        verbs.append((a[0],a[1]))

def rematch(str,s):
    return re.match(str + '$', s) != None

def verb_stem(s):
    str = ''
    if s != 'has' and s != 'does':
        if (rematch('.*[aeiou]ys',s)):
            str = s[0:len(s)-1]
        if (rematch('.*.[^aeiou]ies',s)):
            str = s[0:len(s)-3] + 'y'
        if (rematch('[^aeiou]ies',s)):
            str = s[0:len(s)-1]
        if (rematch('.*(o|x|ch|sh|ss|zz)es',s)):
            str = s[0:len(s)-2]
        if (rematch('.*([^s]se|[^z]ze)s',s)):
            str = s[0:len(s)-1]
        if (rematch('.*([^iosxz]|(ch)|(sh))es',s)):
            str = s[0:len(s)-1]
        if (rematch('.*([^aeiousxyz]|ch|sh)s',s)):
            str = s[0:len(s)-1]
        if ((s,'VBZ') not in verbs and (str,'VB') not in verbs):
            str = ''
    else:
        if s == 'has':
            return 'have'

    return str


def add_proper_name (w,lx):
    """adds a name to a lexicon, checking if first letter is uppercase"""
    if ('A' <= w[0] and w[0] <= 'Z'):
        lx.add(w,'P')
        return ''
    else:
        return (w + " isn't a proper name")

def process_statement (lx,wlist,fb):
    """analyses a statement and updates lexicon and fact base accordingly;
       returns '' if successful, or error message if not."""
    # Grammar for the statement language is:
    #   S  -> P is AR Ns | P is A | P Is | P Ts P
    #   AR -> a | an
    # We parse this in an ad hoc way.
    msg = add_proper_name (wlist[0],lx)
    if (msg == ''):
        if (wlist[1] == 'is'):
            if (wlist[2] in ['a','an']):
                lx.add (wlist[3],'N')
                fb.addUnary ('N_'+wlist[3],wlist[0])
            else:
                lx.add (wlist[2],'A')
                fb.addUnary ('A_'+wlist[2],wlist[0])
        else:
            stem = verb_stem(wlist[1])
            if (len(wlist) == 2):
                lx.add (stem,'I')
                fb.addUnary ('I_'+stem,wlist[0])
            else:
                msg = add_proper_name (wlist[2],lx)
                if (msg == ''):
                    lx.add (stem,'T')
                    fb.addBinary ('T_'+stem,wlist[0],wlist[2])
    return msg

# End of PART A.
