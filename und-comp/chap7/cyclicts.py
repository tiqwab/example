from collections import namedtuple
from tagsystem import TagSystem

class CyclicTagRule(namedtuple('CyclicTagRule', ['append_chars'])):
    __slots__ = ()

    def is_applied_to(self, string):
        return string[0] == '1'

    def follow(self, string):
        return string + self.append_chars

class CyclicTagRulebook:
    def __init__(self, rules):
        self.rules = rules
        self.index = 0

    def is_applied_to(self, string):
        return len(string) >= 1

    def next_string(self, string):
        return self.follow_next_rule(string)[1:]

    def follow_next_rule(self, string):
        rule = self.rules[self.index]
        self.index = (self.index + 1) % len(self.rules)
        if rule.is_applied_to(string):
            return rule.follow(string)
        else:
            return string

if __name__ == '__main__':
    rulebook = CyclicTagRulebook(( \
            CyclicTagRule('1'), CyclicTagRule('0010'), CyclicTagRule('10'), \
            ))
    system = TagSystem('11', rulebook)
    for i in range(0, 16):
        print(system.current_string)
        system.step()
    print(system.current_string)
