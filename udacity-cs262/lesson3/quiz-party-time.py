def party(n):
    if n == 0:
        yield []
    else:
        for x in party(n-1):
            yield [n] + x
            yield x

print([x for x in party(5)])
print(len([x for x in party(5)]))
