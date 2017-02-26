from collections import namedtuple
from enum import Enum

class Direction(Enum):
    Right = 0
    Left  = 1
    def __str__(self):
        return self.name
    def __repr__(self):
        return self.__str__()

class Tape(namedtuple('Tape', 'left middle right blank')):
    __slots__ = ()

    def write(self, char):
        return Tape(self.left, char, self.right, self.blank)

    def move_head_left(self):
        if len(self.left) > 0:
            return Tape(self.left[:-1], self.left[-1], [self.middle] + self.right, self.blank)
        else:
            return Tape([], self.blank, [self.middle] + self.right, self.blank)

    def move_head_right(self):
        if len(self.right) > 0:
            return Tape(self.left + [self.middle], self.right[0], self.right[1:], self.blank)
        else:
            return Tape(self.left + [self.middle], self.blank, [], self.blank)

    def __str__(self):
        return '<Tape %s(%s)%s>' % (''.join(self.left), str(self.middle), ''.join(self.right))

    def __repr__(self):
        return self.__str__()

class TMConfiguration(namedtuple('TMConfiguration', 'state tape')):
    __slots__ = ()

class TMRule(namedtuple('TMRule', ['state', 'char', 'next_state', 'write_char', 'direction'])):
    __slots__ = ()

    def is_applied_to(self, config):
        return self.state == config.state and self.char == config.tape.middle

    def follow(self, config):
        return TMConfiguration(self.next_state, self.next_tape(config))

    def next_tape(self, config):
        tape = config.tape.write(self.write_char)
        if self.direction == Direction.Left:
            return tape.move_head_left()
        elif self.direction == Direction.Right:
            return tape.move_head_right()
        else:
            raise RuntimeError('unexpected direction: ' + str(self.direction))

class DTMRulebook:
    def __init__(self, rules):
        self.rules = rules

    def is_applied_to(self, config):
        return self.rule_for(config) is not None

    def next_config(self, config):
        applied_rule = self.rule_for(config)
        return applied_rule.follow(config)

    def rule_for(self, config):
        applied_rules = frozenset(filter(lambda rule: rule.is_applied_to(config), self.rules))
        if len(applied_rules) == 0:
            return None
        return list(applied_rules)[0]

class DTM:
    def __init__(self, current_config, accept_states, rulebook):
        self.current_config = current_config
        self.accept_states = accept_states
        self.rulebook = rulebook

    def does_accept(self):
        return self.current_config.state in self.accept_states

    def is_stuck(self):
        return not self.rulebook.is_applied_to(self.current_config)

    def step(self):
        self.current_config = rulebook.next_config(self.current_config)

    def run(self):
        while not self.does_accept():
            if not self.is_stuck():
                self.step()
            else:
                break

if __name__ == '__main__':
    print('--- Tape ---')

    tape = Tape(['1', '0', '1'], '1', [], '_')
    print(tape)
    print(tape.move_head_left())
    print(tape.move_head_right())
    print(tape.write('0'))
    print(tape.move_head_left().write('0'))
    print(tape.move_head_right().write('0'))

    print('--- TMConfiguration ---')

    rule = TMRule(1, '0', 2, '1', Direction.Right)
    print(rule)
    print(rule.is_applied_to(TMConfiguration(1, Tape([], '0', [], '_'))))
    print(rule.is_applied_to(TMConfiguration(1, Tape([], '1', [], '_'))))
    print(rule.is_applied_to(TMConfiguration(2, Tape([], '0', [], '_'))))

    print('--- Apply TMRules ---')

    print(rule.follow(TMConfiguration(1, Tape([], '0', [], '-'))))

    print('--- Simulate turing machines incrementing binary number ---')
    rulebook = DTMRulebook(frozenset([
        TMRule(1, '0', 2, '1', Direction.Right),
        TMRule(1, '1', 1, '0', Direction.Left),
        TMRule(1, '_', 2, '1', Direction.Right),
        TMRule(2, '0', 2, '0', Direction.Right),
        TMRule(2, '1', 2, '1', Direction.Right),
        TMRule(2, '_', 3, '_', Direction.Left),
        ]))

    config = TMConfiguration(1, tape)
    dtm = DTM(config, [3], rulebook)
    print(dtm.does_accept())
    print(dtm.current_config)
    dtm.run()
    print(dtm.does_accept())
    print(dtm.current_config)

    tape = Tape(['1', '2', '1'], '1', [], '_')
    config = TMConfiguration(1, tape)
    dtm = DTM(config, [3], rulebook)
    print(dtm.does_accept())
    print(dtm.current_config)
    dtm.run()
    print(dtm.does_accept())
    print(dtm.current_config)
    print(dtm.is_stuck())
