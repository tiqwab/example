from stack import Stack

STUCK_STATE = -1

class PDAConfiguration:
    def __init__(self, state, stack):
        self.state = state
        self.stack = stack

    def stuck(self):
        return PDAConfiguration(STUCK_STATE, self.stack)

    def is_stuck(self):
        return self.state == STUCK_STATE

    def __eq__(self, other):
        if type(self) == type(other):
            return self.__dict__ == other.__dict__
        return NotImplemented

    def __hash__(self):
        return hash(tuple(sorted(self.__dict__.items())))

    def __str__(self):
        return '<PDAConfiguration state=%s, stack=%s >' % (str(self.state), str(self.stack))

    def __repr__(self):
        return '<PDAConfiguration state=%s, stack=%s >' % (repr(self.state), repr(self.stack))

class PDARule:
    def __init__(self, state, char, next_state, pop_char, push_chars):
        self.state = state
        self.char = char
        self.next_state = next_state
        self.pop_char = pop_char
        self.push_chars = push_chars

    def is_applied_to(self, config, char):
        if self.state == config.state and self.pop_char == config.stack.top():
            if char is None:
                return self.char is None
            else:
                return self.char == char
        return False

    def follow(self, config):
        return PDAConfiguration(self.next_state, self.next_stack(config))

    def next_stack(self, config):
        stack = config.stack.pop()
        for elem in reversed(self.push_chars):
            stack = stack.push(elem)
        return stack

    def __str__(self):
        return '<PDARule %s--%s;%s/%s-->%s >' % \
                (str(self.state), str(self.char), str(self.pop_char), str(self.push_chars), str(self.next_state))

class DPDARulebook:
    def __init__(self, rules):
        self.rules = rules

    def is_applied_to(self, config, char):
        return self.rule_for(config, char) is not None

    def next_config(self, config, char):
        return self.rule_for(config, char).follow(config)

    def rule_for(self, config, char):
        possible_rules = [ rule for rule in self.rules if rule.is_applied_to(config, char) ]
        if len(possible_rules) == 0:
            return None
        return possible_rules[0]

    def free_move(self, config):
        if self.is_applied_to(config, None):
            return self.free_move(self.next_config(config, None))
        else:
            return config

    def __str__(self):
        return '<Rulebook rules=%s >' % (str(self.rules))

class DPDA:
    def __init__(self, current_config, accept_state, rulebook):
        self.current_config = rulebook.free_move(current_config)
        self.accept_state = accept_state
        self.rulebook = rulebook

    def does_accept(self):
        return self.current_config.state in self.accept_state

    def next_config(self, char):
        if self.rulebook.is_applied_to(self.current_config, char):
            return self.rulebook.free_move(self.rulebook.next_config(self.current_config, char))
        else:
            return self.current_config.stuck()

    def read_char(self, char):
        self.current_config = self.next_config(char)

    def read_str(self, string):
        for c in string:
            if not self.current_config.is_stuck():
                self.read_char(c)

class DPDADesign:
    def __init__(self, start_state, bottom_char, accept_states, rulebook):
        self.start_state = start_state
        self.bottom_char = bottom_char
        self.accept_states = accept_states
        self.rulebook = rulebook

    def does_accept(self, string):
        dpda = self.to_dpda()
        dpda.read_str(string)
        return dpda.does_accept()


    def to_dpda(self):
        stack = Stack([self.bottom_char])
        config = PDAConfiguration(self.start_state, stack)
        return DPDA(config, self.accept_states, self.rulebook)

    def __str__(self):
        return '<DPDADesign start_state=%s, bottom_char=%s, accept_states=%s, rulebook=%s >' % \
                (str(self.start_state), str(self.bottom_char), str(self.accept_states), str(self.rulebook))

if __name__ == '__main__':
    rule = PDARule(1, '(', 2, '$', ['b', '$'])
    print(rule)
    config = PDAConfiguration(1, Stack(['$']))
    print(rule.is_applied_to(config, '('))
    print(rule.is_applied_to(config, ')'))

    rulebook = DPDARulebook([
        PDARule(1, '(', 2, '$', ['b', '$']),
        PDARule(2, '(', 2, 'b', ['b', 'b']),
        PDARule(2, ')', 2, 'b', []),
        PDARule(2, None, 1, '$', ['$']),
        ])
    print(config)
    config = rulebook.next_config(config, '(')
    print(config)
    config = rulebook.next_config(config, '(')
    print(config)
    config = rulebook.next_config(config, ')')
    print(config)
    config = rulebook.next_config(config, ')')
    print(config)

    dpda = DPDA(PDAConfiguration(1, Stack(['$'])), [1], rulebook)
    print(dpda.does_accept())
    dpda.read_str('(())')
    print(dpda.does_accept())

    dpda_design = DPDADesign(1, '$', [1], rulebook)
    print(dpda_design)
    print('(())', dpda_design.does_accept('(())'))
    print('(()', dpda_design.does_accept('(()'))
    print('(()()(()))', dpda_design.does_accept('(()()(()))'))
    print('())', dpda_design.does_accept('())'))
