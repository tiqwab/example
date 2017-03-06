from collections import namedtuple

class SKISymbol(namedtuple('SKISymbol', ['name'])):
    __slots__ = ()

    def combinator(self):
        return self

    def arguments(self):
        return []

    def is_callable(self, arguments):
        return False

    def is_reducible(self):
        return False

    def __str__(self):
        return '%s' % (str(self.name))

class SKICall(namedtuple('SKICall', ['left', 'right'])):
    __slots__ = ()

    def combinator(self):
        return self.left.combinator()

    def arguments(self):
        return self.left.arguments() + [self.right]

    def is_reducible(self):
        return self.left.is_reducible() \
                or self.right.is_reducible() \
                or self.combinator().is_callable(self.arguments())

    def reduce(self):
        if self.left.is_reducible():
            return SKICall(self.left.reduce(), self.right)
        elif self.right.is_reducible():
            return SKICall(self.left, self.right.reduce())
        else:
            return self.combinator().call(*self.arguments())

    def __str__(self):
        return '%s[%s]' % (str(self.left), str(self.right))

class CombinatorS(SKISymbol):
    __slots__ = ()

    def call(self, a, b, c):
        return SKICall(SKICall(a, c), SKICall(b, c))

    def is_callable(self, arguments):
        return len(arguments) == 3

class CombinatorK(SKISymbol):
    __slots__ = ()

    def call(self, a, b):
        return a

    def is_callable(self, arguments):
        return len(arguments) == 2

class CombinatorI(SKISymbol):
    __slots__ = ()

    def call(self, a):
        return a

    def is_callable(self, arguments):
        return len(arguments) == 1

S = CombinatorS('S')
K = CombinatorK('K')
I = CombinatorI('I')

if __name__ == '__main__':
    x = SKISymbol('x')
    y = SKISymbol('y')
    z = SKISymbol('z')
    print(S.call(x, y, z))
    expression = SKICall(SKICall(SKICall(S, SKISymbol('x')), SKISymbol('y')), SKISymbol('z'))
    print(expression)
    combinator = expression.combinator()
    arguments = expression.arguments()
    print("%s is called with %s and %s" % (str(expression), str(combinator), str(arguments)))
    print(combinator.call(*arguments))
    print(combinator.is_callable(arguments))

    print('--- reduce ---')
    swap = SKICall(SKICall(S, SKICall(K, SKICall(S, I))), K)
    expression = SKICall(SKICall(swap, x), y)
    while expression.is_reducible():
        print(expression)
        expression = expression.reduce()
    print(expression)
