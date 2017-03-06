from collections import namedtuple

class TagRule(namedtuple('TagRule', ['first_char', 'append_chars'])):
    __slots__ = ()

    def is_applied_to(self, string):
        return string[0] == self.first_char

    def follow(self, string):
        return string + self.append_chars

class TagRulebook(namedtuple('TagRulebook', ['deletion_number', 'rules'])):
    __slots__ = ()

    def is_applied_to(self, string):
        rule = self.rule_for(string)
        return rule is not None and len(string) >= self.deletion_number

    def next_string(self, string):
        return self.rule_for(string).follow(string)[self.deletion_number:]

    def rule_for(self, string):
        applicable_rules = list(filter(lambda r: r.is_applied_to(string), self.rules))
        if len(applicable_rules) == 0:
            return None
        return applicable_rules[0]

class TagSystem:
    def __init__(self, current_string, rulebook):
        self.current_string = current_string
        self.rulebook = rulebook

    def step(self):
        self.current_string = self.rulebook.next_string(self.current_string)

    def run(self):
        while self.rulebook.is_applied_to(self.current_string):
            print(self.current_string)
            self.step()
        print(self.current_string)

if __name__ == '__main__':
    print('--- tag system for x * 2 + 1 ---')
    rulebook = TagRulebook(2, frozenset({ \
        TagRule('a', 'cc'), TagRule('b', 'dddd'), \
        TagRule('c', 'eeff'), TagRule('d', 'ff'), \
        }) \
    ) 
    system = TagSystem('aabbbb', rulebook)
    system.run()

    print('--- tag system to check if the value is even ---')
    rulebook = TagRulebook(2, frozenset({
        TagRule('a', 'cc'), TagRule('b', 'd'), \
        TagRule('c', 'eo'), TagRule('d', ''), \
        TagRule('e', 'e') \
    }))
    system = TagSystem('aabbbbbbbb', rulebook)
    system.run()
    system = TagSystem('aabbbbbbb', rulebook)
    system.run()
