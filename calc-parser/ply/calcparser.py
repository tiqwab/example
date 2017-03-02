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
E -> ab E
E -> (E)
"""

precedence = ( \
        ('left', 'PLUS', 'MINUS'), \
        ('left', 'MULT', 'DIV'), \
        ('right', 'EXPONENT'), \
        ('right', 'UPLUS', 'UMINUS', 'AB'), \
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

def p_expr_ab(p):
    'expr : AB expr'
    p[0] = abs(p[2])

def p_error(t):
    print("syntax error at '%s'" % (t.value))

parser = yacc.yacc()

def parse(input_string):
    lexer.input(input_string)
    parse_tree = parser.parse(input_string, lexer=lexer)
    return parse_tree

if __name__ == '__main__':
    assert parse('1 + 2 + 3') == 6
    assert parse('1 + 2 * 3 * 4') == 25
    assert parse('3 * 4 - 10 / 2 + 5') == 12
    assert parse('-3 * (+4 - 10) / -2 + 5') == -4
    assert parse('1 + 2 ** 3 ** 2') == 513
    assert parse('ab (1 - 2  -3)') == 4
    assert parse('ab 2 * 5 - ab (-2)') == 8
