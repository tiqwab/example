from collections import namedtuple
import math

class Point(namedtuple('Point', ['x', 'y'])):
    '''
    Sample class to implement immutable classes
    '''
    __slots__ = ()

    def distance(self, other):
        return math.sqrt((self.x - other.x) ** 2 + (self.y - other.y) ** 2)

if __name__ == '__main__':
    p1 = Point(1, 2)
    p2 = Point(4, 6)
    print(p1.x)             # 1
    print(p1.y)             # 2
    print(p1)               # Point(x=1, y=2)
    print(p1._replace(y=1)) # Point(x=1, y=1)
    print(p1.distance(p2))  # 5.0

    # check immutability
    try:
        p1.x = 3
    except AttributeError as e:
        print(e) # can't set attribute
    try:
        p1.z = 3
    except AttributeError as e:
        print(e) # 'Point' object has no attribute 'z'

    # check equality
    print(p1 == p1) # True
    print(p1 == p2) # False
    print(p2 == p1) # False
    print(p1 == Point(1, 2)) # True
    print(Point(1, 2) == p1) # True
