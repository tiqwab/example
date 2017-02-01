# Infinite Mind Reading
#
# Just as a context-free grammar may be 'empty', it may also have an
# infinite language. We say that the language for a grammar is infinite if
# the grammar accepts an infinite number of different strings (each of
# which is of finite length). Most interesting (and creative!) languages
# are infinite.
# 
# For example, the language of this grammar is infinite:
#
# grammar1 = [ 
#       ("S", [ "S", "a" ] ),        # S -> S a
#       ("S", [ "b", ]) ,            # S -> b 
#       ] 
#
# Because it accepts the strings b, ba, baa, baaa, baaaa, etc. 
#
# However, this similar grammar does _not_ have an infinite language: 
#
# grammar2 = [ 
#       ("S", [ "S", ]),             # S -> S 
#       ("S", [ "b", ]) ,            # S -> b 
#       ] 
#
# Because it only accepts one string: b. 
#
# For this problem you will write a procedure cfginfinite(grammar)
# that returns True (the value True, not the string "True") if the grammar
# accepts an infinite number of strings (starting from any symbol). Your
# procedure should return False otherwise. 
#
# Consider this example: 
# 
# grammar3 = [ 
#       ("S", [ "Q", ] ),        # S -> Q
#       ("Q", [ "b", ]) ,        # Q -> b
#       ("Q", [ "R", "a" ]),     # Q -> R a 
#       ("R", [ "Q"]),           # R -> Q
#       ] 
#
# The language of this grammar is infinite (b, ba, baa, etc.) because it is
# possible to "loop" or "travel" from Q back to Q, picking up an "a" each
# time. Since we can travel around the loop as often as we like, we can
# generate infinite strings. By contrast, in grammar2 it is possible to
# travel from S to S, but we do not pick up any symbols by doing so. 
#
# Important Assumption: For this problem, you may assume that for every
# non-terminal in the grammar, that non-terminal derives at least one
# non-empty finite string.  (You could just call cfgempty() from before to
# determine this, so we'll assume it.)  
#
# Hint 1: Determine if "Q" can be re-written to "x Q y", where either x
# or y is non-empty. 
#
# Hint 2: The "Important Assumption" above is more important than it looks:
# it means that any rewrite rule "bigger" than ("P", ["Q"]) adds at least
# one token. 
#
# Hint 3: While cfginfinite(grammar) is not recursive, you may want to
# write a helper procedure (that determines if Q can be re-written to "x Q
# y" with |x+y| > 0 ) that _is_ recursive. Watch out for infinite loops:
# keep track of what you have already visited. 

def cfginfinite(grammar): 
    return cfgcheck(grammar, "S", [], [])

'''
def cfgcheck(grammar, symbol, visited, tss):
    if symbol in visited:
        return len(tss) > 0

    visited.append(symbol)
    current_grammars = list(filter(lambda x: x[0] == symbol, grammar))
    for gr in current_grammars:
        new_tss = list(filter(lambda x: is_ts(x, grammar), gr[1]))
        for nts in [x for x in filter(lambda x: not is_ts(x, grammar), gr[1])]:
            if cfgcheck(grammar, nts, visited, tss + new_tss):
                return True
    return False
'''

def cfgcheck(grammar, symbol, visited, tss):
    if symbol in list(map(lambda x: x[0], visited)):
        prev_tss = list(filter(lambda x: x[0] == symbol, visited))[0][1]
        return len(tss) - len(prev_tss) > 0

    visited.append((symbol, tss))
    current_grammars = list(filter(lambda x: x[0] == symbol, grammar))
    for gr in current_grammars:
        new_tss = list(filter(lambda x: is_ts(x, grammar), gr[1]))
        for nts in [x for x in filter(lambda x: not is_ts(x, grammar), gr[1])]:
            if cfgcheck(grammar, nts, visited, tss + new_tss):
                return True
    return False

def is_ts(s, grammar):
    for x in grammar:
        if x[0] == s:
            return False
    return True

# We have provided a few test cases. You will likely want to write your own
# as well. 

grammar1 = [ 
      ("S", [ "S", "a" ]), # S -> S a
      ("S", [ "b", ]) , # S -> b 
      ] 
print(cfginfinite(grammar1) == True)

grammar2 = [ 
      ("S", [ "S", ]), # S -> S 
      ("S", [ "b", ]) , # S -> b 
      ] 

print(cfginfinite(grammar2) == False)

grammar3 = [ 
      ("S", [ "Q", ]), # S -> Q
      ("Q", [ "b", ]) , # Q -> b
      ("Q", [ "R", "a" ]), # Q -> R a 
      ("R", [ "Q"]), # R -> Q
      ] 

print(cfginfinite(grammar3) == True)

grammar4 = [  # Nobel Peace Prizes, 1990-1993
      ("S", [ "Q", ]),
      ("Q", [ "Mikhail Gorbachev", ]) ,
      ("Q", [ "P", "Aung San Suu Kyi" ]),
      ("R", [ "Q"]),
      ("R", [ "Rigoberta Tum"]),
      ("P", [ "Mandela and de Klerk"]),
      ] 

print(cfginfinite(grammar4) == False)

