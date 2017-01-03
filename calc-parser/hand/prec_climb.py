from opfunc import plus, minus, mul, div, pw, \
                   name_to_op, op_to_name, \
                   Assoc, assess_op
from expression import NumExpr, UnOpExpr, TwoOpExpr
from calctokenizer import CalcTokenizer

class PrecedenceClimbingParser:
    def __init__(self, source):
        self.tokenizer = CalcTokenizer(source)

    def parse(self):
        return self.parse_prec(0)

    def parse_prec(self, prev_prec):
        left = self.parse_factor()
        cur_prec, cur_assoc = assess_op(self.tokenizer.peek_token())
        while not self.tokenizer.empty() \
                and cur_prec is not None \
                and cur_prec >= prev_prec:
            op_token = self.tokenizer.next_token()
            if cur_assoc == Assoc.Left:
                right = self.parse_prec(cur_prec+1)
            else:
                right = self.parse_prec(cur_prec)
            left = self.parse_op(op_token, left, right)
            cur_prec, cur_assoc = assess_op(self.tokenizer.peek_token())
        return left

    def parse_factor(self):
        token = self.tokenizer.next_token()
        if self.tokenizer.is_digit(token):
            return NumExpr(int(token))
        elif token == '(':
            factor = self.parse_prec(0)
            self.tokenizer.next_token()
            return factor
        raise RuntimeError('illegal token:' + token)

    def parse_op(self, op, left, right):
        return TwoOpExpr(name_to_op(op), left, right)

def calc_helper(inp):
    tdp =PrecedenceClimbingParser(inp)
    ast = tdp.parse()
    print(ast)
    print(inp, '=', ast.evaluate())

if __name__ == '__main__':
    calc_helper('1')
    calc_helper('1 + 2')
    calc_helper('1 + 2 + 3')
    calc_helper('1 * 2 + 3')
    calc_helper('1 + 2 * 3')
    calc_helper('1 + 2 + 3 * 4 * 5 + 6')
    calc_helper('(2 + 3) * 4')
    calc_helper('2 * (3 + 4)')
    calc_helper('(1 + 2 * (3 + 4)) * ((5 + 6) * 7)') # 1155
    calc_helper('1 + 2 * 3 - 4 + 6 / 2')
    calc_helper('5 + 2 * 4 ** 2 ** 3 + 1') # 131078
