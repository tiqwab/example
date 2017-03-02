from ply import lex

# Define `tokens`, a list of token names.
tokens = ( 'PLUS', 'MINUS', 'MULT', 'DIV', 'EXPONENT', \
        'LPAREN', 'RPAREN', 'AB', 'NUMBER', \
        )

# Define `t_ignore` to ignore unnecessary characters between tokens, such as whitespaces.
t_ignore = " \t"

# Define functions representing regular expression rules for each token.
# The name of functions must be like `t_<token_name>`.
# Functions accept one argument, which is a parsed token.
#    t.type  : name of token
#    t.value : string of parsed token 
#    t.lineno: line number of token
#    t.lexpos: position of token from the beginning of input string

def t_PLUS(t):
    r'\+' # regular expression for the token
    return t

def t_MINUS(t):
    r'\-'
    return t

# The order of declaration is also the order of rules the lexer uses.
# That is why `t_EXPONENT` must be before `t_MULT`.
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

def t_AB(t):
    r'ab'
    return t

def t_NUMBER(t):
    r'[0-9]+'
    t.value = int(t.value)
    return t

# To count correct line number
def t_newline(t):
    r'\n+'
    t.lexer.lineno += t.value.count("\n")
    # return None, so this newlines will not be in the parsed token list.

# Special function for error handling
def t_error(t):
    print("illegal character '%s'" % (t.value[0]))
    t.lexer.skip(1)

# Generate a lexer by `lex.lex()`
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
    print(test_lexer('ab 5 + ab -2 * ab (1 - 2)'))
