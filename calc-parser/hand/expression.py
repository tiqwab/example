from opfunc import op_to_name

'''
Base class to express the abstract syntax tree of basic math expression.
'''
class Expr:
    def __init__(self):
        pass

    def evaluate(self):
        raise NotImplementedError()

class NumExpr(Expr):
    def __init__(self, val):
        super().__init__()
        self.val = val

    def evaluate(self):
        return self.val

    def __str__(self):
        return "NumExpr {val=%s}" % (str(self.val))

    def __repr__(self):
        return "NumExpr {val=%s}" % (repr(self.val))

class UnOpExpr(Expr):
    def __init__(self, op, elem):
        super().__init__()
        self.op = op
        self.elem = elem

    def evaluate(self):
        return self.op(self.elem.evaluate())

    def __str__(self):
        return "UnOpExpr {op=%s, elem=%s}" % (str(self.op), str(self.elem))

    def __repr__(self):
        return "UnOpExpr {op=%s, elem=%s}" % (repr(self.op), repr(self.elem))

class TwoOpExpr(Expr):
    def __init__(self, op, left, right):
        super().__init__()
        self.op = op
        self.left = left
        self.right = right

    def evaluate(self):
        return self.op(self.left.evaluate(), self.right.evaluate())

    def __str__(self):
        return "TwoOpExpr {op=%s, left=%s, right=%s}" \
            % (str(op_to_name(self.op)), str(self.left), str(self.right))

    def __repr__(self):
        return "TwoOpExpr {op=%s, left=%s, right=%s}" \
            % (repr(op_to_name(self.op)), repr(self.left), repr(self.right))
