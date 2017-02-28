from ply import yacc
from calclexer import tokens, lexer

"""
Grammers:
E  -> TE'
E' -> +TE' | -TE' | e
T  -> FT'
T' -> *FT' | /FT' | e
F  -> F' | F'**F
F' -> (E) | +N | -N | ab F' | N
N  -> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
('e' means empty)
"""

"""
Grammars:
S -> E
E -> E + E
E -> E - E
E -> E * E
E -> E / E
E -> E ** E
E -> N
E -> +N
E -> -N
E -> (E)
"""

"""
Grammars:
S -> E
E -> T + T
E -> T - T
T -> F * F
T -> F / F
T -> F ** F
F -> N
F -> +N
F -> -N
F -> (E)
F -> ab F
"""

precedence = ( \
        ('left', 'PLUS', 'MINUS'), \
        ('left', 'MULT', 'DIV'), \
        ('right', 'EXPONENT'), \
        ('right', 'UPLUS', 'UMINUS'), \
        )

def p_statement(p):
    'statement : expr'
    p[0] = p[1]

def p_expr_paren(p):
    'expr : LPAREN expr RPAREN'
    p[0] = p[2]

def p_expr_plus(p):
    'expr : expr PLUS expr'
    p[0] = p[1] + p[3]

def p_expr_minus(p):
    'expr : expr MINUS expr'
    p[0] = p[1] - p[3]

def p_expr_mult(p):
    'expr : expr MULT expr'
    p[0] = p[1] * p[3]

def p_expr_div(p):
    'expr : expr DIV expr'
    p[0] = p[1] / p[3]

def p_expr_exponent(p):
    'expr : expr EXPONENT expr'
    p[0] = p[1] ** p[3]

def p_expr_num(p):
    'expr : NUMBER'
    p[0] = p[1]

def p_expr_up_num(p):
    'expr : PLUS NUMBER %prec UPLUS'
    p[0] = p[2]

def p_expr_um_num(p):
    'expr : MINUS NUMBER %prec UMINUS'
    p[0] = -p[2]

def p_error(t):
    print("syntax error at '%s'" % (t.value))

parser = yacc.yacc()

def parse(input_string):
    lexer.input(input_string)
    parse_tree = parser.parse(input_string, lexer=lexer)
    return parse_tree

if __name__ == '__main__':
    print(parse('1 + 2 + 3'))
    print(parse('1 + 2 * 3 * 4'))
    print(parse('3 * 4 - 10 / 2 + 5'))
    print(parse('-3 * (+4 - 10) / -2 + 5'))
    print(parse('1 + 2 ** 3 ** 2'))
