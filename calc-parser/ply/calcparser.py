from ply import yacc
from calclexer import tokens, lexer

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

# (optional) Define precedence and associativity of operators.
# The format is ( ('left' or 'right', <token name>, ...), (...) ).
# <token name> is expected to be defined in the lexer definition.
# The latter has the the higher precedence (e.g. 'MULT' and 'DIV' have the higher precedence than 'PLUS' and 'MINUS').
# 'UPLUS' and 'UMINUS' are defined as aliases to override precedence (see `p_expr_um_num`)
precedence = ( \
        ('left', 'PLUS', 'MINUS'), \
        ('left', 'MULT', 'DIV'), \
        ('right', 'EXPONENT'), \
        ('right', 'UPLUS', 'UMINUS', 'AB'), \
        )

# Parsing rules
# Functions should be start with `p_`.
# The first rule will be the starting rule of parsing (?).

# S -> E
def p_statement(p):
    'statement : expr'
    p[0] = p[1]

# E -> E + E
def p_expr_plus(p):
    'expr : expr PLUS expr'
    p[0] = p[1] + p[3]

# E -> E - E
def p_expr_minus(p):
    'expr : expr MINUS expr'
    p[0] = p[1] - p[3]

# E -> E * E
def p_expr_mult(p):
    'expr : expr MULT expr'
    p[0] = p[1] * p[3]

# E -> E / E
def p_expr_div(p):
    'expr : expr DIV expr'
    p[0] = p[1] / p[3]

# E -> E ** E
def p_expr_exponent(p):
    'expr : expr EXPONENT expr'
    p[0] = p[1] ** p[3]

# E -> N
def p_expr_num(p):
    'expr : NUMBER'
    p[0] = p[1]

# E -> +N
def p_expr_up_num(p):
    'expr : PLUS NUMBER %prec UPLUS' # override precedence of PLUS by `%prec UPLUS`
    p[0] = p[2]

# E -> -N
def p_expr_um_num(p):
    'expr : MINUS NUMBER %prec UMINUS' # override precedence of MINUS by `%prec UMINUS`
    p[0] = -p[2]

# E -> ab E
def p_expr_ab(p):
    'expr : AB expr'
    p[0] = abs(p[2])

# E -> ( E )
def p_expr_paren(p):
    'expr : LPAREN expr RPAREN'
    p[0] = p[2]

# Rule for error handling
def p_error(t):
    print("syntax error at '%s'" % (t.value))

# Generate a LALR parser
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
