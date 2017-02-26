from functools import reduce
from stack import Stack
from pdaconfig import PDAConfiguration, PDARule

STUCK_STATE = -1

class NPDARulebook:
    def __init__(self, rules):
        self.rules = rules

    def next_configs(self, configs, char):
        new_configs = []
        for config in configs:
            new_configs += [ rule.follow(config) for rule in self.rules_for(config, char) ]
        return frozenset(new_configs)

    def rules_for(self, config, char):
        return frozenset([ rule for rule in self.rules if rule.is_applied_to(config, char) ])

    def free_move(self, configs):
        more_configs = self.next_configs(configs, None)
        if more_configs <= configs:
            return configs
        else:
            return self.free_move(configs | more_configs)

    def __str__(self):
        return '<Rulebook rules=%s >' % (str(self.rules))

class NPDA:
    def __init__(self, current_configs, accept_states, rulebook):
        self.current_configs = rulebook.free_move(current_configs)
        self.accept_states = accept_states
        self.rulebook = rulebook

    def does_accept(self):
        return any([ (config.state in self.accept_states) for config in self.current_configs ])

    def read_char(self, char):
        self.current_configs = self.rulebook.free_move(self.rulebook.next_configs(self.current_configs, char))

    def read_str(self, string):
        for c in string:
            self.read_char(c)

class NPDADesign:
    def __init__(self, start_state, bottom_char, accept_states, rulebook):
        self.start_state = start_state
        self.bottom_char = bottom_char
        self.accept_states = accept_states
        self.rulebook = rulebook

    def does_accept(self, string):
        npda = self.to_npda()
        npda.read_str(string)
        return npda.does_accept()

    def to_npda(self):
        config = PDAConfiguration(self.start_state, Stack([self.bottom_char]))
        return NPDA(frozenset([config]), self.accept_states, self.rulebook)

if __name__ == '__main__':
    rulebook = NPDARulebook([
        PDARule(1, 'a', 1, '$', ['a', '$']),
        PDARule(1, 'a', 1, 'a', ['a', 'a']),
        PDARule(1, 'a', 1, 'b', ['a', 'b']),
        PDARule(1, 'b', 1, '$', ['b', '$']),
        PDARule(1, 'b', 1, 'a', ['b', 'a']),
        PDARule(1, 'b', 1, 'b', ['b', 'b']),
        PDARule(1, None, 2, 'a', ['a']),
        PDARule(1, None, 2, 'b', ['b']),
        PDARule(1, None, 2, '$', ['$']),
        PDARule(2, 'a', 2, 'a', []),
        PDARule(2, 'b', 2, 'b', []),
        PDARule(2, None, 3, '$', ['$']),
    ])
    config = PDAConfiguration(1, Stack(['$']))
    npda = NPDA(frozenset([config]), [3], rulebook)
    print(npda.does_accept())
    print(npda.current_configs)
    npda.read_str('abb')
    print(npda.does_accept())
    print(npda.current_configs)
    npda.read_str('a')
    print(npda.does_accept())
    print(npda.current_configs)

    npda_design = NPDADesign(1, '$', [3], rulebook)
    print(npda_design.does_accept('abba'))
    print('parse a')
    print(npda_design.does_accept('a'))
    print(npda_design.does_accept('babbaabbab'))
    print(npda_design.does_accept('abb'))
    print(npda_design.does_accept('baabaa'))
    print(npda_design.does_accept(''))

'''
This implementation cannot parse palindromes with odd length (e.g. just 'a')?
It is necessary to consume stack without input.
'''
