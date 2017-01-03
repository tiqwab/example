from enum import Enum, unique

class Assoc(Enum):
    Left  = 1,
    Right = 2

@unique
class CalcOp(Enum):
    plus  = ('+', lambda x,y: x+y, 1, Assoc.Left)
    minus = ('-', lambda x,y: x-y, 1, Assoc.Left)
    mult  = ('*', lambda x,y: x*y, 2, Assoc.Left)
    div   = ('/', lambda x,y: x/y, 2, Assoc.Left)
    pw    = ('^', lambda x,y: pow(x,y), 3, Assoc.Right)

    def __init__(self, symbol, func, precedence, assoc):
        self.symbol = symbol
        self.func = func
        self.precedence = precedence
        self.assoc = assoc

    @classmethod
    def token_to_op(self, token):
        for op in CalcOp:
            if op.symbol == token:
                return op
        return None

class Tokenizer:
    def __init__(self, src):
        self.src = src
        self.src_ind = 0

    def next_token(self):
        if self.src_ind >= len(self.src):
            return None
        token = self.src[self.src_ind]
        self.src_ind += 1
        return token

def parse_op(op_token, outputs):
    op_info = CalcOp.token_to_op(op_token)
    e2 = outputs.pop()
    e1 = outputs.pop()
    outputs.append(op_info.func(int(e1), int(e2)))

def compare_prec(cur_op, prev_op):
    cur_op_info = CalcOp.token_to_op(cur_op)
    prev_op_info = CalcOp.token_to_op(prev_op)
    if cur_op_info is None or prev_op_info is None:
        return False
    if prev_op_info.assoc == Assoc.Left:
        return prev_op_info.precedence >= cur_op_info.precedence
    else:
        return prev_op_info.precedence > cur_op_info.precedence

def parse_to_rpn(src):
    tokenizer = Tokenizer(src)
    outputs = []
    ops = []

    while True:
        token = tokenizer.next_token()
        # after reading all input
        if token is None:
            while len(ops) > 0:
                parse_op(ops.pop(), outputs)
            break
        op_info = CalcOp.token_to_op(token)
        # process lparen
        if token == '(':
            ops.append(token)
        # process rparen
        elif token == ')':
            while True:
                op = ops.pop()
                if op == '(':
                    break
                else:
                    parse_op(op, outputs)
        # Process operator
        elif op_info is not None:
            while len(ops) > 0:
                if compare_prec(token, ops[len(ops)-1]):
                    parse_op(ops.pop(), outputs)
                else:
                    break
            ops.append(token)
        # Process operand
        else:
            outputs.append(token)
    return outputs

if __name__ == '__main__':
    print(parse_to_rpn('5*(4+3*2)+1'))
    print(parse_to_rpn('5*(4-3*2)+6/2'))
    print(parse_to_rpn('1+5*4^3^2')) # 1310721
