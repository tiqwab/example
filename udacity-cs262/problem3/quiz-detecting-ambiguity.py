# Detecting Ambiguity 
#
# A grammar is ambiguous if there exists a string in the language of that
# grammar that has two (or more) parse trees. Equivalently, a grammar is
# ambiguous if there are two (or more) different sequences of rewrite rules
# that arrive at the same final string.
#
# Ambiguity is a critical concept in natural languages and in programming
# languages. If we are not careful, our formal grammars for languages like
# JavaScript will have ambiguity. 
#
# In this problem you will write a procedure isambig(grammar,start,tokens)
# that takes as input a grammar with a finite number of possible
# derivations and a string and returns True (the value True, not the string
# "True") if those tokens demonstrate that the grammar is ambiguous
# starting from that start symbol (i.e., because two different sequences of
# rewrite rules can arrive at those tokens). 
#
# For example: 
#
# grammar1 = [                  # Rule Number
#       ("S", [ "P", ] ),       # 0
#       ("S", [ "a", "Q", ]) ,  # 1
#       ("P", [ "a", "T"]),     # 2
#       ("P", [ "c" ]),         # 3
#       ("Q", [ "b" ]),         # 4
#       ("T", [ "b" ]),         # 5
#       ] 
#
# In this grammar, the tokens ["a", "b"] do demonstrate that the
# grammar is ambiguous because there are two difference sequences of
# rewrite rules to obtain them:  
#
#       S  --0->  P  --2->  a T  --5->  a b 
#
#       S  --1->  a Q  --4->  a b 
#
# (I have written the number of the rule used inside the arrow for
# clarity.) The two sequences are [0,2,5] and [1,4]. 
#
# However, the tokens ["c"] do _not_ demonstrate that the grammar is
# ambiguous, because there is only one derivation for it:
#
#       S  --0->  P  --3->  c
#
# So even though the grammar is ambiguous, the tokens ["c"] do not
# demonstrate that: there is only one sequence [0,3]. 
#
# Important Assumption: In this problem the grammar given to you will
# always have a finite number of possible derivations. So only a
# finite set of strings will be in the language of the grammar. (You could
# test this with something like cfginfinite, so we'll just assume it.) 
#
# Hint 1: Consider something like "expand" from the end of the Unit, but
# instead of just enumerating utterances, enumerate (utterance,derivation)
# pairs. For a derivation, you might use a list of the rule indexes as we
# did in the example above. 
#
# Hint 2: Because the grammar has only a finite number of derivations, you
# can just keep enumerating new (utterance,derivation) pairs until you 
# cannot find any that are not already enumerated. 

def isambig(grammar, start, utterance): 
    stock = []
    list_sentence(grammar, [start], stock)
    return len(list(filter(lambda x: utterance == x, stock))) > 1 

def list_sentence(grammar, parsing, stock):
    if all(list(map(lambda x: is_ts(grammar, x), parsing))):
        stock.append(parsing)
        return
    for i in range(0, len(parsing)):
        p = parsing[i]
        next_grs = list(filter(lambda x: x[0] == p, grammar))
        for gr in next_grs:
            list_sentence(grammar, (parsing[:i] + gr[1] + parsing[(i+1):]), stock)

def is_ts(grammar, symbol):
    for s in [x[0] for x in grammar]:
        if s == symbol:
            return False
    return True

'''
def list_sentence(grammar, start):
    if is_ts(grammar, start):
        print('sentences: ', [[start]])
        return [[start]]

    sentences = []
    current_grs = list(filter(lambda gr: gr[0] == start, grammar))
    print('current_grs: ', current_grs)
    for gr in current_grs:
        print('grammar: ', gr)

        produced = list(map(lambda x: list_sentence(grammar, x), gr[1]))
        print('produced:', produced)
        sentence = []
        reduced = []
        if len(produced) == 1:
            reduced = produced[0]
        else:
            for i in range(0, len(produced)-1):
                reduced = reduce_list(produced[i], produced[i+1])
        print('reduced: ', reduced)
        sentences += [reduced]
    print('sentences: ', sentences)
    return sentences

def reduce_list(xs, ys):
    result = []
    for x in xs:
        for y in ys:
            result += x + y
    return result
'''

'''
def isambig(grammar, start, utterance): 
    count = []
    accept_utterance(grammar, start, utterance, count)
    return len(count) > 1

def accept_utterance(grammar, start, utterance, count):
    if len(utterance) == 0:
        return True

    symbol = utterance[0]
    remain_ut = utterance[1:]
    current_gr = list(filter(lambda gr: gr[1][0] == symbol or gr[0] == start, grammar))
    for gr in current_gr:
        result = list(map(lambda x: accept_utterance(grammar, x, remain_ut, count), gr[1]))
        if all(result):
            count += [1]
    return False
'''

# We have provided a few test cases. You will likely want to add your own.

grammar1 = [ 
       ("S", [ "P", ]),
       ("S", [ "a", "Q", ]) ,
       ("P", [ "a", "T"]),
       ("P", [ "c" ]),
       ("Q", [ "b" ]),
       ("T", [ "b" ]),
       ] 
print(isambig(grammar1, "S", ["a", "b"]) == True)
print(isambig(grammar1, "S", ["c"]) == False)

grammar2 = [ 
       ("A", [ "B", ]),
       ("B", [ "C", ]),
       ("C", [ "D", ]),
       ("D", [ "E", ]),
       ("E", [ "F", ]),
       ("E", [ "G", ]),
       ("E", [ "x", "H", ]),
       ("F", [ "x", "H"]),
       ("G", [ "x", "H"]),
       ("H", [ "y", ]),
       ] 
print(isambig(grammar2, "A", ["x", "y"]) == True)
print(isambig(grammar2, "E", ["y"]) == False)

grammar3 = [ # Rivers in Kenya
       ("A", [ "B", "C"]),
       ("A", [ "D", ]),
       ("B", [ "Dawa", ]),
       ("C", [ "Gucha", ]),
       ("D", [ "B", "Gucha"]),
       ("A", [ "E", "Mbagathi"]),
       ("A", [ "F", "Nairobi"]),
       ("E", [ "Tsavo" ]),
       ("F", [ "Dawa", "Gucha" ])
       ] 
print(isambig(grammar3, "A", ["Dawa", "Gucha"]) == True)
print(isambig(grammar3, "A", ["Dawa", "Gucha", "Nairobi"]) == False)
print(isambig(grammar3, "A", ["Tsavo"]) == False)

