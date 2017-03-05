from collections import namedtuple

class LCVariable(namedtuple('LCVariable', ['name'])):
    __slots__ = ()

    def replace(self, name, replacement):
        if self.name == name:
            return LCVariable(replacement)
        else:
            return self

    def is_callable(self):
        return False

    def is_reducible(self):
        return False

    def __str__(self):
        return str(self.name)

class LCFunction(namedtuple('LCFunction', ['parameter', 'body'])):
    __slots__ = ()

    def replace(self, name, replacement):
        if self.parameter == name:
            return self
        else:
            return LCFunction(self.parameter, self.body.replace(name, replacement))

    def call(self, argument):
        return self.body.replace(self.parameter, argument)

    def is_callable(self):
        return True

    def is_reducible(self):
        return False

    def __str__(self):
        return '-> %s { %s }' % (str(self.parameter), str(self.body))

class LCCall(namedtuple('LCCall', ['left', 'right'])):
    __slots__ = ()

    def replace(self, name, replacement):
        return LCCall(self.left.replace(name, replacement), self.right.replace(name, replacement))

    def is_callable(self):
        return False

    def is_reducible(self):
        return self.left.is_reducible() or self.right.is_reducible() or self.left.is_callable()

    def reduce(self):
        if self.left.is_reducible():
            return LCCall(self.left.reduce(), self.right)
        elif self.right.is_reducible():
            return LCCall(self.left, self.right.reduce())
        else:
            return self.left.call(self.right)

    def __str__(self):
        return '%s[%s]' % (str(self.left), str(self.right))

if __name__ == '__main__':
    # one = \p.\x.px
    one = LCFunction('p', \
            LCFunction('x', \
              LCCall(LCVariable('p'), LCVariable('x')) \
            ) \
          )
    print(one)

    # increment = \n.\p.\x.p(npx)
    increment = LCFunction('n', \
                  LCFunction('p', \
                    LCFunction('x', \
                      LCCall(LCVariable('p'), \
                             LCCall(LCCall(LCVariable('n'), LCVariable('p')), LCVariable('x')) \
                      ) \
                    ) \
                  ) \
                )
    print(increment)

    # add = \m.\n.\p.\x.mp(npx)
    add = LCFunction('m', \
            LCFunction('n', \
              LCFunction('p', \
                LCFunction('x', \
                  LCCall( \
                    LCCall(LCVariable('m'), LCVariable('p')), \
                    LCCall(LCCall(LCVariable('n'), LCVariable('p')), LCVariable('x')) \
                  ) \
                ) \
              ) \
            ) \
          )
    print(add)

    # replace
    print('--- replace ---')
    expression = LCVariable('x')
    print(expression)
    print(expression.replace('x', LCFunction('y', LCVariable('y'))))
    print(expression.replace('z', LCFunction('y', LCVariable('y'))))

    expression = \
            LCCall( \
              LCCall( \
                LCCall(LCVariable('a'), LCVariable('b')), \
                LCVariable('c'), \
              ), \
              LCVariable('b') \
            )
    print(expression)
    print(expression.replace('a', LCVariable('x')))
    print(expression.replace('b', LCFunction('x', LCVariable('x'))))

    expression = \
            LCFunction('y', \
              LCCall(LCVariable('x'), LCVariable('y')) \
            )
    print(expression)
    print(expression.replace('x', LCVariable('z')))
    print(expression.replace('y', LCVariable('z')))

    # call function
    print('--- call function ---')
    function = \
            LCFunction('x', \
                       LCFunction('y', \
                                  LCCall(LCVariable('x'), LCVariable('y')) \
                       ) \
            )
    print(function)
    argument = LCFunction('z', LCVariable('z'))
    print(function.call(argument))

    # reduce
    print('--- reduce ---')
    expression = LCCall(LCCall(add, one), one)
    while expression.is_reducible():
        print(expression)
        expression = expression.reduce()
    print(expression)

    inc = LCVariable('inc')
    zero = LCVariable('zero')
    expression = LCCall(LCCall(expression, inc), zero)
    while expression.is_reducible():
        print(expression)
        expression = expression.reduce()
    print(expression)
