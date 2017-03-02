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
E -> E + T
E -> E - T 
E -> T
T -> T * F
T -> T / F
T -> T ** F
T -> F
F -> N
F -> +N
F -> -N
F -> (E)
F -> ab F
"""

precedence = ( \
        # ('left', 'PLUS', 'MINUS'), \
        # ('left', 'MULT', 'DIV'), \
        # ('right', 'EXPONENT'), \
        ('right', 'UPLUS', 'UMINUS', 'AB'), \
        )

def p_statement(p):
    'statement : expr'
    p[0] = p[1]

def p_expr_plus(p):
    'expr : expr PLUS term'
    p[0] = p[1] + p[3]

def p_expr_minus(p):
    'expr : expr MINUS term'
    p[0] = p[1] - p[3]

def p_expr_single(p):
    'expr : term'
    p[0] = p[1]

def p_term_mult(p):
    'term : term MULT factor'
    p[0] = p[1] * p[3]

def p_term_div(p):
    'term : term DIV factor'
    p[0] = p[1] / p[3]

def p_term_exponent(p):
    'term : factor EXPONENT term'
    p[0] = p[1] ** p[3]

def p_term_single(p):
    'term : factor'
    p[0] = p[1]

def p_factor_number(p):
    'factor : NUMBER'
    p[0] = p[1]

def p_factor_number_plus(p):
    'factor : PLUS NUMBER %prec UPLUS'
    p[0] = p[2]

def p_factor_number_minus(p):
    'factor : MINUS NUMBER %prec UMINUS'
    p[0] = -p[2]

def p_factor_paren(p):
    'factor : LPAREN expr RPAREN'
    p[0] = p[2]

def p_factor_ab(p):
    'factor : AB factor'
    p[0] = abs(p[2])

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
    print(parse('ab (1 - 2 - 3)'))
    print(parse('ab 1 - ab 3'))
