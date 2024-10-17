{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}
{-# HLINT ignore "Use lambda-case" #-}
{-# HLINT ignore "Use <$>" #-}
{-# OPTIONS_GHC -Wno-noncanonical-monad-instances #-}
module Parser (parseLambda, parseLine) where

import Control.Monad
import Control.Applicative

import Lambda
import Binding
import Data.Char
import Distribution.Utils.Generic (isAsciiAlphaNum)

newtype Parser a = Parser { parse :: String -> Maybe (a, String) }

failParser :: Parser a
failParser = Parser $ const Nothing

charParser :: Char -> Parser Char
charParser c = Parser $
  \s -> case s of
           [] -> Nothing
           (x:xs) -> if x == c then Just (c,xs) else Nothing

predicateParser :: (Char -> Bool) -> Parser Char
predicateParser p = Parser $
  \s -> case s of
           [] -> Nothing
           (x:xs) -> if p x then Just (x,xs) else Nothing

instance Monad Parser where
  -- (>>=) :: Parser a -> (a -> Parser b) -> Parser b
  mp >>= f = Parser $
    \s -> case parse mp s of
            Nothing -> Nothing
            Just(val,rest) -> parse (f val) rest

  return x = Parser $ \s -> Just (x,s)

instance Applicative Parser where
  af <*> mp =
    do
      f <- af
      f <$> mp
  pure = return

instance Functor Parser where 
  fmap f mp =
    do
      x <- mp
      return $ f x

instance Alternative Parser where
    empty = failParser
    p1 <|> p2 = Parser $ \s -> case parse p1 s of
                                Nothing -> parse p2 s
                                x -> x

plusParser :: Parser a -> Parser [a]
plusParser p = do
                x <- p
                xs <- starParser p
                return (x:xs)

starParser :: Parser a -> Parser [a]
starParser p = plusParser p <|> return []

varParser :: Parser String
varParser = plusParser $ predicateParser isAsciiLower

whitespaceParser :: Parser String
whitespaceParser = starParser (charParser ' ')

lambdaVarParser :: Parser Lambda
lambdaVarParser = do
                    whitespaceParser
                    var <- varParser
                    whitespaceParser
                    return $ Var var

lambdaAbsParser :: Parser Lambda
-- For me it makes sense to be able to have whitespaces between the '.' delimiter, for readability
lambdaAbsParser = do
                    whitespaceParser
                    charParser '\\'
                    var <- varParser
                    whitespaceParser
                    charParser '.'
                    whitespaceParser
                    lambda <- lambdaExprParser
                    whitespaceParser
                    return $ Abs var lambda

lambdaAppParser :: Parser Lambda
lambdaAppParser = do
                    whitespaceParser
                    charParser '('
                    whitespaceParser
                    lambda1 <- lambdaExprParser
                    whitespaceParser
                    lambda2 <- lambdaExprParser
                    whitespaceParser
                    charParser ')'
                    whitespaceParser
                    return $ App lambda1 lambda2

lambdaMacroParser :: Parser Lambda
lambdaMacroParser = do
                    whitespaceParser
                    macro <- plusParser $ predicateParser isAsciiUpper <|> predicateParser isDigit
                    whitespaceParser
                    return $ Macro macro

lambdaExprParser :: Parser Lambda
lambdaExprParser = lambdaVarParser <|> lambdaAbsParser <|> lambdaAppParser <|> lambdaMacroParser

-- 2.1. / 3.2.
parseLambda :: String -> Lambda
parseLambda expr = case parse lambdaExprParser expr of
                Just(lambda, "") -> lambda
                -- Used for error forwarding
                _ -> Macro ":Error: Invalid input!"

bindingParser :: Parser Line
bindingParser = do
                    whitespaceParser
                    macro <- plusParser $ predicateParser isAsciiUpper <|> predicateParser isDigit
                    whitespaceParser
                    charParser '='
                    whitespaceParser
                    lambda <- lambdaExprParser
                    return $ Binding macro lambda

parseBind :: String -> Either String Line
parseBind binding = case parse bindingParser binding of
            Just(line, "") -> Right line
            -- Used for error forwarding
            _ -> Left ":Error: Invalid input!"

-- 3.3.
parseLine :: String -> Either String Line
parseLine line = case parseLambda line of
                Macro ":Error: Invalid input!" -> parseBind line
                lambda -> Right $ Eval lambda

