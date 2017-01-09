{-# LANGUAGE DefaultSignatures   #-}
{-# LANGUAGE DeriveAnyClass      #-}
{-# LANGUAGE DeriveGeneric       #-}
{-# LANGUAGE FlexibleContexts    #-}
{-# LANGUAGE ScopedTypeVariables #-}
{-# LANGUAGE TypeOperators       #-}

module Tiqwab.Generics.Serializable
where

import           Control.Monad
import           GHC.Generics

{-
Definition of Serializable type class
-}

data Bit = I | O
    deriving (Show, Eq)

-- Enable `DefaultSignatures`
class Serializable a where
    serialize :: a -> [Bit]
    default serialize :: (Generic a, Serializable' (Rep a)) => a -> [Bit]
    serialize x = serialize' (from x)
    deserialize :: [Bit] -> Maybe (a, [Bit]) -- (deserialized object, remain bits)
    default deserialize :: (Generic a, Serializable' (Rep a)) => [Bit] -> Maybe (a, [Bit])
    deserialize bits = do (repx, remain) <- deserialize' bits
                          return (to repx, remain)

----------

{-
Fruit and Serializable without GHC.Generics
-}

data Fruit = Apple | Banana | Grape | Orange
    deriving (Show, Eq)

instance Serializable Fruit where
    serialize Apple  = [I]
    serialize Banana = [O,I]
    serialize Grape  = [O,O,I]
    serialize Orange = [O,O,O,I]
    deserialize (I:xs)       = Just (Apple, xs)
    deserialize (O:I:xs)     = Just (Banana, xs)
    deserialize (O:O:I:xs)   = Just (Grape, xs)
    deserialize (O:O:O:I:xs) = Just (Orange, xs)
    deserialize _            = Nothing

{-
Tree and Serializable without GHC.Generics
-}

data Tree a = Node (Tree a) (Tree a) | Leaf a
    deriving (Show, Eq, Generic)

instance (Serializable a) => Serializable (Tree a) where
    serialize (Node left right) = I : serialize left ++ serialize right
    serialize (Leaf a1)         = O : serialize a1
    deserialize (I:xs) = do (l, remain1) <- deserialize xs
                            (r, remain2) <- deserialize remain1
                            return (Node l r, remain2)
    deserialize (O:xs) = do (x, remain) <- deserialize xs :: Maybe (a, [Bit])
                            return (Leaf x, remain)

----------

{-
Definition of Serializable' using GHC.Generics

# data types defined in GHC.Generics to represent any algebraic data types:
data    V1        p                       -- lifted version of Empty
data    U1        p = U1                  -- lifted version of ()
data    (:+:) f g p = L1 (f p) | R1 (g p) -- lifted version of Either
data    (:*:) f g p = (f p) :*: (g p)     -- lifted version of (,)
newtype K1    i c p = K1 { unK1 :: c }    -- a container for a c
newtype M1  i t f p = M1 { unM1 :: f p }  -- a wrapper
-}

class Serializable' f where
    serialize' :: f a -> [Bit]
    deserialize' :: [Bit] -> Maybe (f a, [Bit])

instance Serializable' V1 where
    serialize' x = undefined
    deserialize' _ = undefined

instance Serializable' U1 where
    serialize' U1 = []
    deserialize' xs = return (U1, xs)

-- Use `TypeOperators`
instance (Serializable' f, Serializable' g) => Serializable' (f :+: g) where
    serialize' (L1 x) = I : serialize' x
    serialize' (R1 x) = O : serialize' x
    deserialize' (I:xs) = do (l, remain) <- deserialize' xs
                             return (L1 l, remain)
    deserialize' (O:xs) = do (r, remain) <- deserialize' xs
                             return (R1 r, remain)

-- Use `TypeOperators`
instance (Serializable' f, Serializable' g) => Serializable' (f :*: g) where
    serialize' (f :*: g) = serialize' f ++ serialize' g
    deserialize' xs = do (l, remain1) <- deserialize' xs
                         (r, remain2) <- deserialize' remain1
                         return (l :*: r, remain2)

instance (Serializable c) => Serializable' (K1 i c) where
    serialize' (K1 x) = serialize x
    deserialize' xs = do (k, remain) <- deserialize xs
                         return (K1 k, remain)

instance (Serializable' f) => Serializable' (M1 i t f) where
    serialize' (M1 x) = serialize' x
    deserialize' xs = do (m, remain) <- deserialize' xs
                         return (M1 m, remain)

----------

{-
Fruit' and Tree' Deriving Serializable using GHC.Generics
-}

data Fruit' = Apple' | Banana' | Grape' | Orange'
    deriving (Show, Eq, Generic, Serializable)

data Tree' a = Node' (Tree' a) (Tree' a) | Leaf' a
    deriving (Show, Eq, Generic, Serializable)
