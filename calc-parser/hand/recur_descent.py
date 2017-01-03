from opfunc import plus, minus, mul, div, neg, ab, pw, \
                   name_to_op, op_to_name
from expression import NumExpr, UnOpExpr, TwoOpExpr
from calctokenizer import CalcTokenizer

'''
Construct the abstract syntax tree

Grammers:
E  -> TE'
E' -> +TE' | -TE' | e
T  -> FT'
T' -> *FT' | /FT' | e
F  -> F' | F'**F
F' -> (E) | +N | -N | ab F' | N
N  -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
('e' means empty)
'''
class RecursiveDescentParser:
    def __init__(self, expression):
        self.tokenizer = CalcTokenizer(expression)

    def parse(self):
        '''
        Parse a given input to the abstract syntax tree.
        '''
        self.tokenizer.next_token()
        return self.expr1()

    def expr1(self):
        '''
        E -> TE'
        '''
        e = self.term1()
        return self.expr2(e)

    def expr2(self, e1):
        '''
        E' -> +TE' | -TE' | e
        '''
        if self.tokenizer.token == '+':
            self.tokenizer.next_token()
            e2 = self.term1()
            return self.expr2(TwoOpExpr(plus, e1, e2))
        elif self.tokenizer.token == '-':
            self.tokenizer.next_token()
            e2 = self.term1()
            return self.expr2(TwoOpExpr(minus, e1, e2))
        else:
            return e1

    def term1(self):
        '''
        T  -> FT'
        '''
        e = self.factor1()
        return self.term2(e)

    def term2(self, e1):
        '''
        T' -> *FT' | /FT' | e
        '''
        if self.tokenizer.token == '*':
            self.tokenizer.next_token()
            e2 = self.factor1()
            return self.term2(TwoOpExpr(mul, e1, e2))
        elif self.tokenizer.token == '/':
            self.tokenizer.next_token()
            e2 = self.factor1()
            return self.term2(TwoOpExpr(div, e1, e2))
        else:
            return e1

    def factor1(self):
        '''
        F  -> F' | F'**F
        '''
        e1 = self.factor2()
        if self.tokenizer.token == '**':
            self.tokenizer.next_token()
            e2 = self.factor1()
            return TwoOpExpr(pw, e1, e2)
        else:
            return e1

    def factor2(self):
        '''
        F' -> (E) | +N | -N | ab F' | N
        '''
        if self.tokenizer.is_digit(self.tokenizer.token[0]):
            return self.number()
        elif self.tokenizer.token == '+':
            # this should be like '+53'.
            # skip and parse positive number.
            self.tokenizer.next_token()
            return self.number()
        elif self.tokenizer.token == '-':
            self.tokenizer.next_token()
            return UnOpExpr(neg, self.number())
        elif self.tokenizer.token == 'ab':
            self.tokenizer.next_token()
            return UnOpExpr(ab, self.factor2())
        elif self.tokenizer.token == '(':
            self.tokenizer.next_token()
            e = self.expr1()
            self.tokenizer.next_token()
            return e

    def number(self):
        '''
        N  -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
        '''
        e = NumExpr(int(self.tokenizer.token))
        self.tokenizer.next_token()
        return e

'''
Main program.
Parse and calculate basic math expressions.
'''

def calc_helper(inp):
    tdp = RecursiveDescentParser(inp)
    ast = tdp.parse()
    print(ast)
    print(inp, '=', ast.evaluate())

if __name__ == '__main__':
    calc_helper('1') # 1
    calc_helper('1 + 2') # 3
    calc_helper('1 + 2 + 3') # 6
    calc_helper('1 * 2 + 3') # 5
    calc_helper('1 + 2 * 3') # 7
    calc_helper('(1 + 2) * 3') # 9
    calc_helper('(2 + 5 * 3) * (1 + 4 * 2) + 8') # 161
    calc_helper('(2 - 5 * 3) + (4 * 3 / 2 - 1) * 2') # -3
    calc_helper('15*(+3)-202+99/-11+0') # -166
    calc_helper('ab(5 - 3 * 4) + 6 * ab 3 + 4 / 2') # 27
    calc_helper('1 * 2 + 5 ** 3 ** 2 - 5') # = 1953122
