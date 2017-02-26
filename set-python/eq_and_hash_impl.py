class Foo:
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def __eq__(self, other):
        if isinstance(other, Foo):
            return self.__dict__ == other.__dict__
        return False

    def __hash__(self):
        return hash(tuple(sorted(self.__dict__.items())))

class BarParentEq(Foo):
    def __init__(self, name, age):
        super().__init__(name, age)

class BarSelfEq(Foo):
    def __init__(self, name, age):
        super().__init__(name, age)

    def __eq__(self, other):
        if isinstance(other, BarSelfEq):
            return self.__dict__ == other.__dict__
        return False

if __name__ == '__main__':
    # check equality
    assert Foo('Alice', 10) == Foo('Alice', 10)
    assert not Foo('Alice', 10) == Foo('Bob', 10)
    assert not Foo('Bob', 10) == Foo('Alice', 10)
    assert not Foo('Alice', 10) == Foo('Alice', 15)
    assert not Foo('Alice', 15) == Foo('Alice', 10)

    # `__ne__` seems to be defined as the inverse of `__eq__`
    assert not Foo('Alice', 10) != Foo('Alice', 10)
    assert Foo('Bob', 10) != Foo('Alice', 10)
    assert Foo('Alice', 10) != Foo('Alice', 15)
    assert Foo('Alice', 15) != Foo('Alice', 10)

    # check equality of extended classes using the parent's eq
    assert BarParentEq('Alice', 10) == BarParentEq('Alice', 10)
    assert Foo('Alice', 10) == BarParentEq('Alice', 10)
    assert BarParentEq('Alice', 10) == Foo('Alice', 10)

    # check equality of extended classes using their own eq.
    assert BarSelfEq('Alice', 10) == BarSelfEq('Alice', 10)
    assert Foo('Alice', 10) != BarSelfEq('Alice', 10)
    assert BarSelfEq('Alice', 10) != Foo('Alice', 10)

    # check hash
    assert hash(Foo('Alice', 10)) == hash(Foo('Alice', 10))
    # two hash values below can be same, but usually different.
    assert hash(Foo('Alice', 10)) != hash(Foo('Bob', 10))
    assert hash(Foo('Bob', 10)) != hash(Foo('Alice', 10))
    assert hash(Foo('Alice', 10)) != hash(Foo('Alice', 15))
    assert hash(Foo('Alice', 15)) != hash(Foo('Alice', 10))
