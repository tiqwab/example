class Number:
    def __init__(self, val):
        self.val = val

    def evaluate(self, environment):
        return self

    def __str__(self):
        return str(self.val)

    def __repr__(self):
        return repr(self.val)

class Bool:
    def __init__(self, val):
        self.val = val

    def evaluate(self, environment):
        return self

    def __str__(self):
        return str(self.val)

    def __repr__(self):
        return repr(self.val)

class Var:
    def __init__(self, name):
        self.name = name

    def evaluate(self, environment):
        return environment[self.name]

    def __str__(self):
        return str(self.name)

class Add:
    def __init__(self, left, right):
        self.left = left
        self.right = right

    def evaluate(self, environment):
        return Number(self.left.evaluate(environment).val + self.right.evaluate(environment).val)

    def __str__(self):
        return '%s + %s' % (str(self.left), str(self.right))

class Mult:
    def __init__(self, left, right):
        self.left = left
        self.right = right

    def evaluate(self, environment):
        return Number(self.left.evaluate(environment).val * self.right.evaluate(environment).val)

    def __str__(self):
        return '%s * %s' % (str(self.left), str(self.right))

class LessThan:
    def __init__(self, left, right):
        self.left = left
        self.right = right

    def evaluate(self, environment):
        return Bool(self.left.evaluate(environment).val < self.right.evaluate(environment).val)

    def __str__(self):
        return '%s < %s' % (str(self.left), str(self.right))

class Assign:
    def __init__(self, name, exp):
        self.name = name
        self.exp = exp

    def evaluate(self, environment):
        new_env = dict(environment)
        new_env[self.name] = self.exp.evaluate(environment)
        return new_env

    def __str__(self):
        return '%s = %s' % (str(self.name), str(self.exp))

class If:
    def __init__(self, condition, consequence, alternative):
        self.condition = condition
        self.consequence = consequence
        self.alternative = alternative

    def evaluate(self, environment):
        if self.condition.evaluate(environment).val:
            return self.consequence.evaluate(environment)
        else:
            return self.alternative.evaluate(environment)

    def __str__(self):
        return 'if (%s) { %s } else { %s }' % (str(self.condition), str(self.consequence), str(self.alternative))

class Sequence:
    def __init__(self, first, second):
        self.first = first
        self.second=  second

    def evaluate(self, environment):
        return self.second.evaluate(self.first.evaluate(environment))

    def __str__(self):
        return '%s; %s' % (str(self.first), str(self.second))

class While:
    def __init__(self, condition, body):
        self.condition = condition
        self.body = body

    def evaluate(self, environment):
        if self.condition.evaluate(environment).val:
            return self.evaluate(self.body.evaluate(environment))
        else:
            return environment

    def __str__(self):
        return 'while (%s) { %s }' % (str(self.condition), str(self.body))

if __name__ == '__main__':
    environment = {'x': Number(5), 'y': Bool(True)}

    print('math expression: ')
    exp1 = Mult(Add(Number(1), Var('x')), Add(Number(3), Number(4)))
    print(exp1)
    print(exp1.evaluate(environment))

    print('assign: ')
    exp2 = Assign('x', Add(Number(1), Var('x')))
    print(exp2)
    print(exp2.evaluate(environment))

    print('if: ')
    exp3 = If(LessThan(Var('x'), Number(6)), Assign('y', Bool(True)), Assign('y', Bool(False)))
    print(exp3)
    print(exp3.evaluate(environment))

    print('sequence: ')
    exp4 = Sequence(If(Bool(True), Assign('x', Number(10)), Assign('x', Number(0))), Mult(Var('x'), Number(2)))
    print(exp4)
    print(exp4.evaluate(environment))

    print('while: ')
    exp5 = While(LessThan(Var('x'), Number(10)), Assign('x', Add(Var('x'), Number(2))))
    print(exp5)
    print(exp5.evaluate(environment))
