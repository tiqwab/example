from enum import Enum

'''
Functions for basic calculation
'''

plus  = lambda x, y: x + y
minus = lambda x, y: x - y
mul   = lambda x, y: x * y
div   = lambda x, y: x / y
neg   = lambda x: x * (-1)
ab    = lambda x: abs(x)
pw    = lambda x, y: pow(x,y)

def name_to_op(op):
    '''
    Generate lambda judged by a `op` symbol
    '''
    if op == '+':
        return plus
    elif op == '-':
        return minus
    elif op == '*':
        return mul
    elif op == '/':
        return div
    elif op == '**':
        return pw
    else:
        raise RuntimeError('illegal operator: ' + op)

def op_to_name(func):
    if func == plus:
        return '+'
    elif func == minus:
        return '-'
    elif func == mul:
        return '*'
    elif func == div:
        return '/'
    elif func == pw:
        return '**'
    else:
        raise RuntimeError("illegal function")

class Assoc(Enum):
    Left = 1,
    Right = 2

def assess_op(op):
    if op == '+':
        return (2, Assoc.Left)
    elif op == '-':
        return (2, Assoc.Left)
    elif op == '*':
        return (4, Assoc.Left)
    elif op == '/':
        return (4, Assoc.Left)
    elif op == '**':
        return (6, Assoc.Right)
    elif op is None:
        return (0, Assoc.Left)
    else:
        return (None, None)
