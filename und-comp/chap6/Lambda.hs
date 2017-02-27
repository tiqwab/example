{-
Church number

zero = \s.\z.z
one = \s.\z.sz
two = \s.\z.s(sz)
-}

zero = \f x -> x
one  = \f x -> f x
two  = \f x -> f . f $ x

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
-}

increment = \g f x -> f (g f x)

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
