from ply import lex

tokens = ( 'PLUS', 'MINUS', 'MULT', 'DIV', 'EXPONENT', \
        'LPAREN', 'RPAREN', 'NUMBER', \
        )

t_ignore = " \t"

def t_PLUS(t):
    r'\+'
    return t

def t_MINUS(t):
    r'\-'
    return t

def t_EXPONENT(t):
    r'\*\*'
    return t

def t_MULT(t):
    r'\*'
    return t

def t_DIV(t):
    r'/'
    return t

def t_LPAREN(t):
    r'\('
    return t

def t_RPAREN(t):
    r'\)'
    return t

def t_NUMBER(t):
    r'[0-9]+'
    t.value = int(t.value)
    return t

def t_newline(t):
    r'\n+'
    t.lexer.lineno += t.value.count("\n")

def t_error(t):
    print("illegal character '%s'" % (t.value[0]))
    t.lexer.skip(1)

lexer = lex.lex()

def test_lexer(input_string):
    lexer.input(input_string)
    result = []
    while True:
        tok = lexer.token()
        if not tok:
            break
        result = result + [(tok.type, tok.value)]
    return result

if __name__ == '__main__':
    print(test_lexer('1 + 2'))
    print(test_lexer('1 + 20 * 3 - 10 / -2 * (1 + 3)'))
    print(test_lexer('1 ** 2'))
