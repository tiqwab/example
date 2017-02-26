class Stack:
    def __init__(self, contents):
        self.contents = contents

    def push(self, char):
        return Stack([char] + self.contents)

    def pop(self):
        return Stack(self.contents[1:])

    def top(self):
        return self.contents[0]

    def __eq__(self, other):
        if type(self) == type(other):
            return self.__dict__ == other.__dict__
        return NotImplemented

    def __hash__(self):
        return hash(tuple(self.contents))

    def __str__(self):
        return '<Stack (%s)%s>' % (str(self.contents[0]), str(self.contents[1:]))

    def __repr__(self):
        return '<Stack (%s)%s>' % (repr(self.contents[0]), repr(self.contents[1:]))

if __name__ == '__main__':
    stack = Stack([2, 1, 0])
    print(stack)
    print(stack.push(3))
    print(stack.top())
    print(stack.pop())
