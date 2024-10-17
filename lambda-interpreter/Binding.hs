module Binding where

import Lambda

type Context = [(String, Lambda)]

data Line = Eval Lambda 
          | Binding String Lambda deriving (Eq)

instance Show Line where
    show (Eval l) = show l
    show (Binding s l) = s ++ " = " ++ show l

tryReplace :: Context -> Lambda -> Maybe Lambda
tryReplace ctx (Var var) = Just $ Var var
tryReplace ctx (Macro macro) = lookup macro ctx
tryReplace ctx (Abs var expr) = Abs var <$> tryReplace ctx expr
tryReplace ctx (App expr1 expr2) = do
                                    lambda1 <- tryReplace ctx expr1
                                    lambda2 <- tryReplace ctx expr2
                                    return $ App lambda1 lambda2

-- 3.1.
simplifyCtx :: Context -> (Lambda -> Lambda) -> Lambda -> Either String [Lambda]
simplifyCtx ctx red lambda = case tryReplace ctx lambda of
                                Just lambda -> Right $ simplify red lambda
                                Nothing -> Left ":Error: Invalid input!"

normalCtx :: Context -> Lambda -> Either String [Lambda]
normalCtx ctx = simplifyCtx ctx normalStep

applicativeCtx :: Context -> Lambda -> Either String [Lambda]
applicativeCtx ctx = simplifyCtx ctx applicativeStep
