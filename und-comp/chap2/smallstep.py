class Number:
    def __init__(self, num):
        self.val = num

    def can_reduce(self):
        return False

    def reduce(self, environment):
        raise RuntimeError('can not be reduced: ', str(self))

    def __repr__(self):
        return 'Number {val=%s}' % (repr(self.val))

    def __str__(self):
        return str(self.val)

class Bool:
    def __init__(self, val):
        self.val = val

    def can_reduce(self):
        return False

    def reduce(self, environment):
        raise RuntimeError('can not be reduced: ', str(self))

    def __repr__(self):
        return 'Bool {val=%s}' % (repr(self.val))

    def __str__(self):
        return str(self.val)

class Add:
    def __init__(self, left, right):
        self.left = left
        self.right = right

    def can_reduce(self):
        return True

    def reduce(self, environment):
        if self.left.can_reduce():
            return (Add(self.left.reduce(environment)[0], self.right), environment)
        elif self.right.can_reduce():
            return (Add(self.left, self.right.reduce(environment)[0]), environment)
        else:
            return (Number(self.left.val + self.right.val),  environment)

    def __repr__(self):
        return 'Add {left=%s, right=%s}' % (repr(self.left), repr(self.right))

    def __str__(self):
        return '%s + %s' % (str(self.left), str(self.right))

class Mult:
    def __init__(self, left, right):
        self.left = left
        self.right = right

    def can_reduce(self):
        return True

    def reduce(self, environment):
        if self.left.can_reduce():
            return (Mult(self.left.reduce(environment)[0], self.right), environment)
        elif self.right.can_reduce():
            return (Mult(self.left, self.right.reduce(environment)[0]), environment)
        else:
            return (Number(self.left.val * self.right.val), environment)

    def __repr__(self):
        return 'Mult {left=%s, right=%s}' % (repr(self.left), repr(self.right))

    def __str__(self):
        return '%s * %s' % (str(self.left), str(self.right))

class LessThan:
    def __init__(self, left, right):
        self.left = left
        self.right = right

    def can_reduce(self):
        return True

    def reduce(self, environment):
        if self.left.can_reduce():
            return (LessThan(self.left.reduce(environment)[0], self.right), environment)
        elif self.right.can_reduce():
            return (LessThan(self.left, self.right.reduce(environment)[0]), environment)
        else:
            return (Bool(self.left.val < self.right.val), environment)

    def __repr__(self):
        return 'LessThan {left=%s, right=%s}' % (repr(self.left), repr(self.right))

    def __str__(self):
        return '%s < %s' % (str(self.left), str(self.right))


class Var:
    def __init__(self, name):
        self.name = name

    def can_reduce(self):
        return True

    def reduce(self, environment):
        return (environment[self.name], environment)

    def __repr__(self):
        return 'Var {name=%s}' % (repr(self.name))

    def __str__(self):
        return str(self.name)

"""
Statements
"""

class DoNothing:
    def __init__(self):
        pass

    def can_reduce(self):
        return False

    def reduce(self):
        raise RuntimeError('DoNothing can not be reduced')

    def __repr__(self):
        return 'DoNothing'

    def __str__(self):
        return 'DoNothing'

class Assign:
    def __init__(self, name, exp):
        self.name = name
        self.exp = exp

    def can_reduce(self):
        return True

    def reduce(self, environment):
        if self.exp.can_reduce():
            return (Assign(self.name, self.exp.reduce(environment)[0]), environment)
        else:
            new_env = dict(environment)
            new_env[self.name] = self.exp
            return (DoNothing(), new_env)

    def __repr__(self):
        return 'Assign {name=%s, exp=%s}' % (repr(self.name), repr(self.exp))

    def __str__(self):
        return '%s = %s' % (str(self.name), str(self.exp))

class If:
    def __init__(self, condition, consequence, alternative):
        self.condition = condition
        self.consequence = consequence
        self.alternative = alternative

    def can_reduce(self):
        return True

    def reduce(self, environment):
        if self.condition.can_reduce():
            return (If(self.condition.reduce(environment)[0], self.consequence, self.alternative), environment)
        if self.condition.val:
            return (self.consequence, environment)
        else:
            return (self.alternative, environment)

    def __repr__(self):
        return 'If {condition=%s, consequence=%s, alternative=%s}' % \
                (repr(self.condition), repr(self.consequence), repr(self.alternative))

    def __str__(self):
        return 'if (%s) { %s } else { %s }' % (str(self.condition), str(self.consequence), str(self.alternative))

class Sequence:
    def __init__(self, first, second):
        self.first = first
        self.second = second

    def can_reduce(self):
        return True

    def reduce(self, environment):
        if self.first.can_reduce():
            new_elem, new_env = self.first.reduce(environment)
            return (Sequence(new_elem, self.second), new_env)
        return (self.second, environment)

    def __repr__(self):
        return 'Sequence {first=%s, second=%s}' % (repr(self.first), repr(self.second))

    def __str__(self):
        return '%s; %s' % (str(self.first), str(self.second))

class While:
    def __init__(self, condition, body):
        self.condition = condition
        self.body = body

    def can_reduce(self):
        return True

    def reduce(self, environment):
        return (If(self.condition, Sequence(self.body, self), DoNothing()), environment)

    def __repr__(self):
        return 'While {condition=%s, body=%s}' % (repr(self.condition), repr(self.body))

    def __str__(self):
        return 'while (%s) { %s }' % (str(self.condition), str(self.body))

class Machine:
    def __init__(self, stmt, environment):
        self.stmt = stmt
        self.environment = environment

    def step(self):
        return self.stmt.reduce(self.environment)

    def eval(self):
        print(self.stmt, self.environment)
        while self.stmt.can_reduce():
            self.stmt, self.environment = self.step()
            print(self.stmt, self.environment)

if __name__ == '__main__':
    environment = {'x': Number(10), 'y': Number(20), 'z': Bool(True)}

    print('exp1: Math expression')
    exp1 = Add(Mult(Number(1), Number(2)), Add(Var('x'), Var('y')))
    machine1 = Machine(exp1, environment)
    machine1.eval()

    print('exp2: Assign')
    exp2 = Assign('x', Add(Var('x'), Number(1)))
    machine2 = Machine(exp2, environment)
    machine2.eval()

    print('exp3: If else')
    exp3 = If(Var('z'), Assign('x', Number(1)), Add(Number(2), Number(5)))
    machine3 = Machine(exp3, environment)
    machine3.eval()

    print('exp4: If')
    exp4 = If(LessThan(Add(Var('x'), Number(11)), Var('y')), Assign('x', Number(1)), DoNothing())
    machine4 = Machine(exp4, environment)
    machine4.eval()

    print('exp5: Sequence')
    exp5 = Sequence(Assign('x', Add(Number(1), Number(1))), Assign('y', Add(Var('x'), Number(3))))
    machine5 = Machine(exp5, environment)
    machine5.eval()

    print('exp6: While')
    exp6 = While(Var('z'), Sequence(Assign('x', Add(Var('x'), Number(1))), Assign('z', Bool(False))))
    machine6 = Machine(exp6, environment)
    machine6.eval()

