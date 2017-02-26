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
