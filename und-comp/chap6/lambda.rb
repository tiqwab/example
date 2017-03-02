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
FOUR  = -> p { -> x { p[p[p[p[x]]]] } }
FIVE  = -> p { -> x { p[p[p[p[p[x]]]]] } }

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

# add  = \m.\n.\s.\z.ms(nsz)
# mult = \m.\n.\s.\z.n(\z'.msz')z
# pow  = \m.\n.\s.\z.(nm)sz
ADD      = -> m { -> n { n[INCREMENT][m] } }
SUBTRACT = -> m { -> n { n[DECREMENT][m] } }
MULTIPLY = -> m { -> n { n[ADD[m]][ZERO] } }
POWER    = -> m { -> n { n[MULTIPLY[m]][ONE] } }

# --- mod ---
# leq = \m.\n.\s.\z.(is_zero (subtract m n)) s z
IS_LESS_OR_EQUAL = -> m { -> n { IS_ZERO[SUBTRACT[m][n]] } }
# MOD = -> m { -> n {
#     IF[IS_LESS_OR_EQUAL[m][n]][m][
#         -> x { MOD[SUBTRACT[m][n]][n][x] }
#     ]
# } }
Y = -> f { -> x { f[x[x]] }[-> x { f[x[x]] }] }
Z = -> f { -> x { f[-> y { x[x][y] }] }[-> x { f[-> y { x[x][y] }] }] }
MOD = Z[-> f { -> m { -> n {
    IF[IS_LESS_OR_EQUAL[n][m]][
        -> x {
            f[SUBTRACT[m][n]][n][x] 
        }
    ][
        m
    ]
} } }]

# --- list ---
EMPTY    = PAIR[TRUE][TRUE]
UNSHIFT  = -> l { -> x {
    PAIR[FALSE][PAIR[x][l]]
} }
IS_EMPTY = LEFT
FIRST    = -> l { LEFT[RIGHT[l]] }
REST     = -> l { RIGHT[RIGHT[l]] }
RANGE    = Z[
    -> f {
        -> m { -> n {
            IF[IS_LESS_OR_EQUAL[m][n]][
                -> x { UNSHIFT[f[INCREMENT[m]][n]][m][x] }
            ][
                EMPTY
            ]
        } }
    }
]

FOLD = Z[
    -> f {
        -> l { -> x { -> g {
            IF[IS_EMPTY[l]][
                x
            ][
                -> y {
                    g[f[REST[l]][x][g]][FIRST[l]][y]
                }
            ]
        } } }
    }
]

MAP = -> k { -> f {
    FOLD[k][EMPTY][-> l { -> x { UNSHIFT[l][f[x]] } }]
} }

def to_array(proc)
    array = []
    until to_bool(IS_EMPTY[proc])
        array.push(FIRST[proc])
        proc = REST[proc]
    end
    array
end

# --- string ---

TEN = MULTIPLY[TWO][FIVE]
B   = TEN
F   = INCREMENT[B]
I   = INCREMENT[F]
U   = INCREMENT[I]
ZED = INCREMENT[U]

FIZZ     = UNSHIFT[UNSHIFT[UNSHIFT[UNSHIFT[EMPTY][ZED]][ZED]][I]][F]
BUZZ     = UNSHIFT[UNSHIFT[UNSHIFT[UNSHIFT[EMPTY][ZED]][ZED]][U]][B]
FIZZBUZZ = UNSHIFT[UNSHIFT[UNSHIFT[UNSHIFT[BUZZ][ZED]][ZED]][I]][F]

def to_char(c)
    '0123456789BFiuz'.slice(to_integer(c))
end

def to_string(s)
    to_array(s).map { |c| to_char(c) }.join
end

# --- to_digits
DIV = Z[
    -> f {
        -> m { -> n {
            IF[IS_LESS_OR_EQUAL[n][m]][
                -> x {
                    INCREMENT[f[SUBTRACT[m][n]][n]][x]
                }
            ][
                ZERO
            ]
        } }
    }
]

PUSH = -> l { -> x {
    FOLD[l][UNSHIFT[EMPTY][x]][UNSHIFT]
} }

TO_DIGITS = Z[
    -> f {
        -> n { PUSH[
            IF[IS_LESS_OR_EQUAL[n][DECREMENT[TEN]]][
                EMPTY
            ][
                -> x {
                    f[DIV[n][TEN]][x]
                }
            ]
        ][MOD[n][TEN]] }
    }
]

# --- fizzbuzz ---
FIFTEEN = MULTIPLY[FIVE][THREE]
HUNDRED = POWER[MULTIPLY[TWO][FIVE]][TWO]
solution =
    MAP[RANGE[ONE][HUNDRED]][ -> n {
        IF[IS_ZERO[MOD[n][FIFTEEN]]][
            FIZZBUZZ
        ][IF[IS_ZERO[MOD[n][THREE]]][
            FIZZ
        ][IF[IS_ZERO[MOD[n][FIVE]]][
            BUZZ
        ][
            TO_DIGITS[n]
        ]]]
}]





def show(msg, result)
    "#{msg}: #{result}"
end

# --- main ---
puts show('ZERO', to_integer(ZERO))
puts show('THREE', to_integer(THREE))

puts show('TRUE', to_bool(TRUE))
puts show('FALSE', to_bool(FALSE))
puts show('IF[TRUE][ZERO][ONE]', to_integer(IF[TRUE][ZERO][ONE]))
puts show('IF[FALSE][ZERO][ONE]', to_integer(IF[FALSE][ZERO][ONE]))
puts show('IS_ZERO[ZERO]', to_bool(IS_ZERO[ZERO]))
puts show('IS_ZERO[ONE]', to_bool(IS_ZERO[ONE]))

puts show('LEFT[PAIR[ONE][TWO]]', to_integer(LEFT[PAIR[ONE][TWO]]))
puts show('RIGHT[PAIR[ONE][TWO]]', to_integer(RIGHT[PAIR[ONE][TWO]]))

puts show('INCREMENT[ZERO]', to_integer(INCREMENT[ZERO]))

puts show('DECREMENT[TWO]', to_integer(DECREMENT[TWO]))
puts show('DECREMENT[ONE]', to_integer(DECREMENT[ONE]))
puts show('DECREMENT[ZERO]', to_integer(DECREMENT[ZERO]))

puts show('ADD[ONE][TWO]', to_integer(ADD[ONE][TWO]))
puts show('SUBTRACT[TWO][ONE]', to_integer(SUBTRACT[TWO][ONE]))
puts show('SUBTRACT[ONE][TWO]', to_integer(SUBTRACT[ONE][TWO]))
puts show('SUBTRACT[TWO][TWO]', to_integer(SUBTRACT[TWO][TWO]))
puts show('MULTIPLY[TWO][THREE]', to_integer(MULTIPLY[TWO][THREE]))
puts show('POWER[TWO][THREE]', to_integer(POWER[TWO][THREE]))

puts show('IS_LESS_OR_EQUAL[ONE][TWO]', to_bool(IS_LESS_OR_EQUAL[ONE][TWO]))
puts show('IS_LESS_OR_EQUAL[ONE][ONE]', to_bool(IS_LESS_OR_EQUAL[ONE][ONE]))
puts show('IS_LESS_OR_EQUAL[TWO][ONE]', to_bool(IS_LESS_OR_EQUAL[TWO][ONE]))

puts show('MOD[THREE][TWO]', to_integer(MOD[THREE][TWO]))
puts show('MOD[POWER[THREE][THREE]][ADD[THREE][ONE]]', to_integer(MOD[POWER[THREE][THREE]][ADD[THREE][ONE]])) # 3 ** 3 `mod` (3+1)
puts show('MOD[THREE][THREE]', to_integer(MOD[THREE][THREE]))

my_list =
    UNSHIFT[
        UNSHIFT[
            UNSHIFT[EMPTY][THREE]
        ][TWO]
    ][ONE]
puts to_array(my_list).map { |p| to_integer(p) }
puts to_array(RANGE[ONE][POWER[TWO][THREE]]).map { |p| to_integer(p) }

puts show('FOLD[RANGE[ONE][FIVE]][ZERO][ADD]', to_integer(FOLD[RANGE[ONE][FIVE]][ZERO][ADD]))
puts show('FOLD[RANGE[ONE][FIVE]][ONE][MULTIPLY]', to_integer(FOLD[RANGE[ONE][FIVE]][ONE][MULTIPLY]))
puts show('MAP[RANGE[ONE][FIVE]][INCREMENT]', to_array(MAP[RANGE[ONE][FIVE]][INCREMENT]).map { |p| to_integer(p) })
puts show('TO_DIGITS[POWER[FIVE][THREE]]', to_string(TO_DIGITS[POWER[FIVE][THREE]]))

puts '--- FIZZBUZZ ---'
puts to_array(solution).map { |p| to_string(p) }
