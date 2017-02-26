class FARule:
    def __init__(self, current_state, char, next_state):
        self.current_state = current_state
        self.char = char
        self.next_state = next_state

    def is_applied(self, state, char):
        return self.current_state == state and self.char == char

    def follow(self):
        return self.next_state

    def __str__(self):
        return '<FARule %s --%s-- %s>' % (str(self.current_state), str(self.char), str(self.next_state))

class DFARulebook:
    def __init__(self, rules):
        self.rules = rules

    def next_state(self, state, char):
        rule = self.rule_for(state, char)
        if rule is None:
            raise RuntimeError('there is no rule for (state=%s, char=%s)' % (str(state), str(char)))
        return rule.follow()

    def rule_for(self, state, char):
        for rule in self.rules:
            if rule.is_applied(state, char):
                return rule
        return None

class DFA:
    def __init__(self, current_state, accept_states, rulebook):
        self.current_state = current_state
        self.accept_states = accept_states
        self.rulebook = rulebook

    def is_state_accept(self):
        return self.current_state in self.accept_states

    def read_char(self, char):
        self.current_state = self.rulebook.next_state(self.current_state, char)

    def read_str(self, string):
        for c in string:
            self.read_char(c)

class DFADesign:
    def __init__(self, current_state, accept_states, rulebook):
        self.current_state = current_state
        self.accept_states = accept_states
        self.rulebook = rulebook

    def to_dfa(self):
        return DFA(self.current_state, self.accept_states, self.rulebook)

    def does_accept(self, string):
        dfa = self.to_dfa()
        dfa.read_str(string)
        return dfa.is_state_accept()

if __name__ == '__main__':
    rulebook = DFARulebook([
        FARule(1, 'a', 2), FARule(1, 'b', 1), FARule(2, 'a', 2), FARule(2, 'b', 3), FARule(3, 'a', 3), FARule(3, 'b', 3)
        ])
    print(rulebook.next_state(1, 'a'))
    print(rulebook.next_state(1, 'b'))
    print(rulebook.next_state(2, 'b'))

    dfa_design = DFADesign(1, [3], rulebook)
    print(dfa_design.does_accept('a'))
    print(dfa_design.does_accept('bbaabab'))
