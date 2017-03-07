from collections import namedtuple
from enum import Enum
import re

class Operator(namedtuple('Operator', ['symbol', 'func', 'precedence', 'associativity'])):
    __slots__ = ()

class Assoc(Enum):
    Left = 0
    Right = 1

operators = (
        Operator('+', lambda x, y: x + y, 1, Assoc.Left),
        Operator('-', lambda x, y: x - y, 1, Assoc.Left),
        Operator('*', lambda x, y: x * y, 2, Assoc.Left),
        Operator('/', lambda x, y: x / y, 2, Assoc.Left),
        Operator('**', lambda x, y: x ** y, 3, Assoc.Right),
        )

def find_op(sym):
    ops = list(filter(lambda op: op.symbol == sym, operators))
    if len(ops) == 0:
        raise RuntimeError('illegal operator: ' + str(sym))
    return ops[0]

def can_apply_prev_op(prev_op, current_op):
    if current_op.associativity == Assoc.Left:
        return prev_op.precedence >= current_op.precedence
    else:
        return prev_op.precedence > current_op.precedence

class Tokenizer:
    def __init__(self, input):
        self.tokens = input.split()
        self.index = 0

    def next_token(self):
        if self.index >= len(self.tokens):
            return None
        token = self.tokens[self.index]
        if re.match(r'^\d+$', token):
            token = int(token)
        self.index += 1
        return token

class ShuntingYardParser:
    def __init__(self, tokenizer):
        self.tokenizer = tokenizer

    def parse(self):
        output = []
        opstack = []
        while True:
            token = self.tokenizer.next_token()
            if token is None:
                break
            if isinstance(token, int):
                output.append(token)
                continue
            current_op = find_op(token)
            while True:
                if len(opstack) == 0:
                    break
                prev_op = opstack.pop()
                if not can_apply_prev_op(prev_op, current_op):
                    opstack.append(prev_op)
                    break
                rhs = output.pop()
                lhs = output.pop()
                output.append(prev_op.func(lhs, rhs))
            opstack.append(current_op)
        while len(opstack) > 0:
            prev_op = opstack.pop()
            rhs = output.pop()
            lhs = output.pop()
            output.append(prev_op.func(lhs, rhs))
        return output[0]


def calculate(input):
    tokenizer = Tokenizer(input)
    parser = ShuntingYardParser(tokenizer)
    return parser.parse()

if __name__ == '__main__':
    assert calculate('1 + 2 + 3') == 6
    assert calculate('1 * 2 + 3') == 5
    assert calculate('1 + 2 * 3') == 7
    assert calculate('1 - 6 * 3 / 2 + 4') == -4
    assert calculate('1 + 3 * 2 ** 3 ** 2 - 4') == 1 + 3 * 512 - 4
