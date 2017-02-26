from functools import reduce
from dfa import DFA, DFADesign, DFARulebook

class FARule:
    def __init__(self, current_state, char, next_state):
        self.current_state = current_state
        self.char = char
        self.next_state = next_state

    def is_applied(self, state, char):
        if char is None:
            return self.current_state == state and self.char is None
        else:
            return self.current_state == state and self.char == char

    def follow(self):
        return self.next_state

    def __str__(self):
        return '<FARule %s --%s--> %s>' % (str(self.current_state), str(self.char), str(self.next_state))

    def __repr__(self):
        return '<FARule %s --%s--> %s>' % (repr(self.current_state), repr(self.char), repr(self.next_state))

    def __eq__(self, other):
        if self is None:
            return other is None
        if not isinstance(other, FARule):
            return False
        return self.current_state == other.current_state \
                and self.char == other.char \
                and self.next_state == other.next_state

    def __ne__(self, other):
        return not self.__eq__(other)

    def __hash__(self):
        return hash((self.current_state, self.char, self.next_state))

class NFARulebook:
    def __init__(self, rules):
        self.rules = rules

    def alphabet(self):
        return list(filter(lambda x: x is not None, \
                           map(lambda x: x.char, self.rules)))

    def next_states(self, states, char):
        rules = reduce(lambda x, y: x + y, [ self.rules_for(state, char) for state in states ], [])
        return frozenset(map(lambda x: x.follow(), rules))

    def rules_for(self, state, char):
        return [ rule for rule in self.rules if rule.is_applied(state, char) ]

    def free_move(self, states):
        '''
        Do epsilon transition
        '''
        return states | self.next_states(states, None)

class NFA:
    def __init__(self, current_states, accept_states, rulebook):
        self.current_states = rulebook.free_move(current_states)
        self.accept_states = accept_states
        self.rulebook = rulebook

    def is_state_accept(self):
        return any([ x in self.accept_states for x in self.current_states ])

    def read_char(self, char):
        self.current_states = self.rulebook.next_states(self.current_states, char)
        self.current_states = self.rulebook.free_move(self.current_states)

    def read_str(self, string):
        for c in string:
            self.read_char(c)

class NFADesign:
    def __init__(self, current_states, accept_states, rulebook):
        self.current_states = current_states
        self.accept_states = accept_states
        self.rulebook = rulebook

    def to_nfa(self, states=None):
        if states is None:
            states = self.current_states
        return NFA(states, self.accept_states, self.rulebook)

    def does_accept(self, string):
        nfa = self.to_nfa()
        nfa.read_str(string)
        return nfa.is_state_accept()

class NFASimulation:
    def __init__(self, nfa_design):
        self.nfa_design = nfa_design

    def next_state(self, state, char):
        nfa = self.nfa_design.to_nfa(state)
        nfa.read_char(char)
        return nfa.current_states

    def rules_for(self, states):
        return list(frozenset( \
                [ FARule(states, char, self.next_state(states, char)) for char in self.nfa_design.rulebook.alphabet() ]))

    def discover_states_and_rules(self, states):
        rules = list(frozenset(reduce(lambda x,y: x+y, map(lambda x: self.rules_for(x), states))))
        more_states = frozenset(list(frozenset(map(lambda x: x.follow(), rules))))
        if more_states <= states:
            return (states, rules)
        else:
            return self.discover_states_and_rules(states | more_states)

    def to_dfa_design(self):
        start_state = self.nfa_design.to_nfa().current_states
        states, rules = self.discover_states_and_rules(frozenset([start_state]))
        accept_states = frozenset(filter(lambda x: self.nfa_design.to_nfa(x).is_state_accept(), states))
        return DFADesign(start_state, accept_states, DFARulebook(rules))

if __name__ == '__main__':
    rulebook = NFARulebook([
        FARule(1, 'a', 1), FARule(1, 'b', 1), FARule(1, 'b', 2), \
        FARule(2, 'a', 3), FARule(2, 'b', 3), \
        FARule(3, 'a', 4), FARule(3, 'b', 4)
        ])

    nfa_design = NFADesign(frozenset([1]), [4], rulebook)
    print(nfa_design.does_accept('a') == False)
    print(nfa_design.does_accept('bbaabab') == True)
    print(nfa_design.does_accept('bbbbbbb') == True)

    # test for epsilon transition
    rulebook = NFARulebook([
        FARule(1, None, 2), FARule(1, None, 4),
        FARule(2, 'a', 3),
        FARule(3, 'a', 2),
        FARule(4, 'a', 5),
        FARule(5, 'a', 6),
        FARule(6, 'a', 4),
        ])
    nfa_design = NFADesign(frozenset([1]), [2,4], rulebook)
    print(nfa_design.does_accept('aa') == True)
    print(nfa_design.does_accept('aaaaaaaaa') == True)
    print(nfa_design.does_accept('aaaaa') == False)

    # test for conversion of nfa to dfa
    rulebook = NFARulebook([
        FARule(1, 'a', 1), FARule(1, 'a', 2), FARule(1, None, 2),
        FARule(2, 'b', 3),
        FARule(3, 'b', 1), FARule(3, None, 2)
        ])
    nfa_design = NFADesign(frozenset([1]), [3], rulebook)
    simulation = NFASimulation(nfa_design)
    print(simulation.rules_for(frozenset([1,2])))
    print(simulation.rules_for(frozenset([3,2])))
    print(simulation.discover_states_and_rules(frozenset([frozenset([1,2])])))
    # create dfa_design
    dfa_design = simulation.to_dfa_design()
    print(dfa_design.does_accept('aaa'))
    print(dfa_design.does_accept('aab'))
    print(dfa_design.does_accept('bbbabb'))
