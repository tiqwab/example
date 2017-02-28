{-
Church number

zero = \s.\z.z
one = \s.\z.sz
two = \s.\z.s(sz)
-}

zero  = \f x -> x
one   = \f x -> f x
two   = \f x -> f (f x)
three = \f x -> f (f (f x))

toInt f = f (+1) 0

{-
true = \t.\f.t
false = \t.\f.f
-}

true  = \t f -> t
false = \t f -> f

toBool = \f -> f True False

ifelse = \e t f -> e t f

isZero = \f -> f (const false) true

{-
Pair is a data structure consisting of two values.
-}

pair x y = \f -> f x y
left = \f -> f (\x y -> x)
right = \f -> f (\x y -> y)

{-
Calculation
m + n = \s.\z.ns(msz)
m - n = ?
m * n = \s.\z.n(\z'.(msz'))z
m ** n = ?
-}

increment = \g f x -> f (g f x)

slide = \p -> pair (right p) (increment (right p))
decrement = \n -> left (n slide (pair zero zero))
-- decrement = \n f x -> n (\g h -> h (g f)) (\u -> x) (\u -> u)

add = \m n s z -> n s (m s z)
-- add = \m n -> n (increment m)
-- minus = \m n -> (n decrement (m increment zero))
minus = \m n -> n (decrement m)
mult = \m n s z -> n (\z' -> m s z') z
pow = \m n s z -> (n m) s z

isLessOrEqual = \m n -> isZero (minus m n)

main :: IO ()
main = do
    putStrLn "-- Int --"
    print $ toInt zero
    print $ toInt one
    print $ toInt two
    putStrLn "-- Bool --"
    print $ toBool true
    print $ toBool false
    print $ ifelse true (toInt zero) (toInt one)
    print $ ifelse false (toInt zero) (toInt one)
    print $ toBool $ isZero zero
    print $ toBool $ isZero one
    print $ toBool $ isZero two
    putStrLn "-- Pair --"
    print $ toInt $ left $ pair one two
    print $ toInt $ right $ pair one two
    putStrLn "-- Calculation --"
    print $ toInt $ increment zero
    print $ toInt $ increment two
    print $ toInt $ decrement zero
    print $ toInt $ decrement one
    print $ toInt $ decrement two
    print $ toInt $ decrement three
    putStrLn "2 + 1 = "
    print $ toInt $ add two one
    putStrLn "2 - 1 = "
    print $ toInt $ minus two one
    print $ toInt $ minus one two
    print $ toInt $ minus three three
    putStrLn "2 * 3 = "
    print $ toInt $ mult two three
    print $ toInt $ mult three two
    print $ toInt $ mult three zero
    print $ toInt $ pow three two
    print $ toInt $ pow two three
    putStrLn "1 <= 2"
    print $ toBool $ isLessOrEqual one two
    print $ toBool $ isLessOrEqual two two
    print $ toBool $ isLessOrEqual three two
