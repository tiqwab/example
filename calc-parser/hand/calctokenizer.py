'''
Tokens are defined in grammers in `recur_descent.py`
'''
class CalcTokenizer:
    def __init__(self, source):
        '''
        source: string to be parsed
        '''
        assert isinstance(source, str)
        self.source = source
        self.src_ind = 0
        self.token = None

    def empty(self):
        '''
        Return true if there is no tokens to read.
        '''
        return len(self.source) - 1 < self.src_ind

    def next_char(self):
        '''
        Read a character from the source.
        Return None if the reader reach the end of input.
        '''
        if self.empty():
            return None
        ch = self.source[self.src_ind]
        self.src_ind += 1
        return ch

    def peek_char(self):
        '''
        Read a character from the source without consuming.
        Return None if the reader reach the end of input.
        '''
        if self.empty():
            return None
        return self.source[self.src_ind]

    def is_digit(self, v):
        '''
        Return true if the passed character is a digit.
        '''
        return v in ['0','1','2','3','4','5','6','7','8','9']

    def next_token(self):
        '''
        Read a token from the source.
        '''
        self.token = self.next_char()
        if self.token is None:
            return None
        while self.token == ' ':
            self.token = self.next_char()
        # for numbers
        if self.is_digit(self.token) and not self.empty():
            nt = self.next_char()
            while self.is_digit(nt) and not self.empty():
                self.token += nt
                nt = self.next_char()
            self.src_ind -= 1 # dirty way, but have to go back
        # for ab operator
        elif self.token == 'a':
            t = self.next_char()
            if not t == 'b':
                raise RuntimeError("illegal character: " + t)
            self.token += t
        # for pow
        elif self.token == '*':
            if self.peek_char() == '*':
                self.token += self.next_token()
        return self.token

    def peek_token(self):
        '''
        Get the next token without consuming.
        '''
        ind = self.src_ind
        token = self.next_token()
        if token is not None:
            self.src_ind = ind
        return token
