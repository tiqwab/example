# Lambda calculus simulation implemented by ruby
# Reference for Church encoding: https://en.wikipedia.org/wiki/Church_encoding

(1..100).map do |n|
    if (n % 15).zero?
        'FizzBuzz'
    elsif (n % 3).zero?
        'Fizz'
    elsif (n % 5).zero?
        'Buzz'
    else
        n.to_s
    end
end

# --- church encoding of number ---
# zero = \s.\z.z
# one  = \s.\z.sz
# two  = \s.\z.s(sz)

ZERO  = -> p { -> x { x } }
ONE   = -> p { -> x { p[x] } }
TWO   = -> p { -> x { p[p[x]] } }
THREE = -> p { -> x { p[p[p[x]]] } }

def to_integer(proc)
    proc[-> n { n + 1 }][0]
end

# --- church encoding of bool ---
# true  = \t.\f.t
# false = \t.\f.f
# if    = \p.\t.\f.ptf

TRUE  = -> t { -> f { t } }
FALSE = -> t { -> f { f } }
IF = -> p { -> x { -> y { p[x][y] } } }

def to_bool(proc)
    proc[true][false]
end

# is_zero = \p.(p(\s.false))(true)
IS_ZERO = -> p { p[-> s { FALSE }][TRUE] }

# --- calcultion with church numbers
# pair      = \x.\y.\f.fxy
# first     = \f.f(\x.\y.x)
# second    = \f.f(\x.\y.y)
# increment = \n.\s.\z.s(nsz)
PAIR      = -> x { -> y { -> f { f[x][y] } } }
LEFT      = -> p { p[-> x { -> y { x } }] }
RIGHT     = -> p { p[-> x { -> y { y } }] }
INCREMENT = -> n { -> p { -> x { p[n[p][x]] } } } 
SLIDE     = -> p { PAIR[RIGHT[p]][INCREMENT[RIGHT[p]]] }
DECREMENT = -> n { LEFT[n[SLIDE][PAIR[ZERO][ZERO]]] }

ADD      = -> m { -> n { n[INCREMENT][m] } }
SUBTRACT = -> m { -> n { n[DECREMENT][m] } }
MULTIPLY = -> m { -> n { n[ADD[m]][ZERO] } }
POWER    = -> m { -> n { n[MULTIPLY[m]][ONE] } }

# --- main ---
puts to_integer(ZERO)
puts to_integer(THREE)

puts to_bool(TRUE)
puts to_bool(FALSE)
puts to_integer(IF[TRUE][ZERO][ONE])
puts to_integer(IF[FALSE][ZERO][ONE])
puts to_bool(IS_ZERO[ZERO])
puts to_bool(IS_ZERO[ONE])

puts to_integer(LEFT[PAIR[ONE][TWO]])
puts to_integer(RIGHT[PAIR[ONE][TWO]])

puts to_integer(INCREMENT[ZERO])

puts to_integer(DECREMENT[TWO])
puts to_integer(DECREMENT[ONE])
puts to_integer(DECREMENT[ZERO])

puts to_integer(ADD[ONE][TWO])
puts to_integer(SUBTRACT[TWO][ONE])
puts to_integer(SUBTRACT[ONE][TWO])
puts to_integer(SUBTRACT[TWO][TWO])
puts to_integer(MULTIPLY[TWO][THREE])
puts to_integer(POWER[TWO][THREE])
