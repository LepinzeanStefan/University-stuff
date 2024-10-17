module Lambda where

import Data.List (nub, (\\), sort)

data Lambda = Var String
            | App Lambda Lambda
            | Abs String Lambda
            | Macro String

instance Show Lambda where
    show (Var x) = x
    show (App e1 e2) = "(" ++ show e1 ++ " " ++ show e2 ++ ")"
    show (Abs x e) = "λ" ++ x ++ "." ++ show e
    show (Macro x) = x

instance Eq Lambda where
    e1 == e2 = eq e1 e2 ([],[],[])
      where
        eq (Var x) (Var y) (env,xb,yb) = elem (x,y) env || (not $ elem x xb || elem y yb)
        eq (App e1 e2) (App f1 f2) env = eq e1 f1 env && eq e2 f2 env
        eq (Abs x e) (Abs y f) (env,xb,yb) = eq e f ((x,y):env,x:xb,y:yb)
        eq (Macro x) (Macro y) _ = x == y
        eq _ _ _ = False

-- 1.1.
vars :: Lambda -> [String]
vars (Var var) = [var]
vars (App lambda1 lambda2) = nub $ vars lambda1 ++ vars lambda2
vars (Abs var lambda) = nub $ var : vars lambda
-- 1.2.
freeVars :: Lambda -> [String]
freeVars (Var var) = [var]
freeVars (App lambda1 lambda2) = nub $ freeVars lambda1 ++ freeVars lambda2
freeVars (Abs var lambda) = freeVars lambda \\ [var]

-- 1.3.
alphabet = ['a'..'z']
-- Generates a list of all lexicographically ordered strings of a given length
genLenStrings :: Int -> [String]
genLenStrings 0 = [[]]
genLenStrings n = [c : s | c <- alphabet, s <- genLenStrings (n - 1)]
-- Generates all strings in a lexicographical order starting from a speciefied length
-- Warning! The function is infinitely recursive!
genAllStrings :: Int -> [String]
genAllStrings n = genLenStrings n ++ genAllStrings (n + 1)
newVar :: [String] -> String
newVar inputStr = head newStrList
    where newStrList = genAllStrings 1 \\ inputStr

-- 1.4.
isNormalForm :: Lambda -> Bool
isNormalForm (Var var) = True
isNormalForm (App (Abs _ _) _) = False
isNormalForm (App lambda1 lambda2) = isNormalForm lambda1 && isNormalForm lambda2
isNormalForm (Abs var lambda) = isNormalForm lambda

-- 1.5.
-- Changes all the tgt(target) variables to a new value
changeVar :: String -> String -> Lambda -> Lambda
changeVar tgt var1 (Var var2) = if var2 == tgt then Var var1 else Var var2
changeVar tgt var1 (App lambda1 lambda2) = App (changeVar tgt var1 lambda1) (changeVar tgt var1 lambda2)
changeVar tgt var1 (Abs var2 lambda) = if var2 == tgt then Abs var2 lambda
    else Abs var2 $ changeVar tgt var1 lambda

-- Finds a new variable that isn't considered free in either e1 or e2
newStr :: Lambda -> Lambda -> String
newStr e1 e2 = newVar $ nub $ freeVars e1 ++ freeVars e2

-- Tries to change all the free tgt(target) variables in a lambda expression to the second expression
reduce :: String -> Lambda -> Lambda -> Lambda
reduce tgt (Var var) e2 = if var == tgt then e2 else Var var
reduce tgt (App lambda1 lambda2) e2 = App (reduce tgt lambda1 e2) (reduce tgt lambda2 e2)
reduce tgt (Abs var lambda) e2 = if var == tgt then Abs var lambda else evalRed tgt var lambda e2
    where
    evalRed tgt var e1 e2 = if elem var $ freeVars e2
        then Abs (newStr e1 e2) (reduce tgt (changeVar var (newStr e1 e2) lambda) e2)
        else Abs var (reduce tgt e1 e2)

-- 1.6.
normalStep :: Lambda -> Lambda
normalStep lambda = fst $ tryReduce (lambda, False)
    where
    tryReduce (lambda, True) = (lambda, True)
    tryReduce (App (Abs tgt exp1) exp2, False) = (reduce tgt exp1 exp2, True)
    tryReduce (App exp1 exp2, False) =
        let (lambda1, stop1) = tryReduce (exp1, False)
            (lambda2, stop2) = tryReduce (exp2, stop1)
        in (App lambda1 lambda2 , stop2)
    tryReduce (Abs var exp, False) = 
        let (lambda, stop) = tryReduce (exp, False)
        in (Abs var lambda, stop)
    tryReduce (Var var, False) = (Var var, False)


-- 1.7.
applicativeStep :: Lambda -> Lambda
applicativeStep lambda = fst $ tryReduce (lambda, False)
    where
    tryReduce (lambda, True) = (lambda, True)
    tryReduce (App (Abs tgt exp1) exp2, False) =
        let (lambda, stop) = tryReduce (exp2, False)
        in if stop then (App (Abs tgt exp1) lambda, True) else (reduce tgt exp1 exp2, True)
    tryReduce (App exp1 exp2, False) =
        let (lambda1, stop1) = tryReduce (exp1, False)
            (lambda2, stop2) = tryReduce (exp2, stop1)
        in (App lambda1 lambda2, stop2)
    tryReduce (Abs var exp, False) = 
        let (lambda, stop) = tryReduce (exp, False)
        in (Abs var lambda, stop)
    tryReduce (Var var, False) = (Var var, False)

-- 1.8.
simplify :: (Lambda -> Lambda) -> Lambda -> [Lambda]
simplify reduceStep expr = if isNormalForm expr
    then [expr]
    else expr : simplify reduceStep reducedExpr
    where reducedExpr = reduceStep expr

normal :: Lambda -> [Lambda]
normal = simplify normalStep

applicative :: Lambda -> [Lambda]
applicative = simplify applicativeStep
