function [Theta] = gradient_descent(FeatureMatrix, Y, n, m, alpha, iter)
  % FeatureMatrix -> the matrix with all training examples
  % Y -> the vector with all actual values
  % n -> the number of predictors
  % m -> the number of trainings
  % alpha -> the learning rate
  % iter -> the number of iterations

  % Theta -> the vector of weights

  Theta = zeros(n + 1, 1); % Initializam pe Theta drept un vector de zeros
  % Theta are n + 1 elemente deoarece retinem si theta0 = 0
  for k = 1 : iter
    for j = 1 : n
      sum = 0; % Initializam suma pentru derivata
      for i = 1 : m
        htheta = FeatureMatrix(i, 1 : n) * Theta(2 : n + 1);
        % Calculam valoarea prezisa, nu luam in considerare Theta0
        sum += (htheta - Y(i))* FeatureMatrix(i, j); % Adaugam in suma conform formulei
      endfor
      sum = sum / m; % Dupa ce impartim cu m, suma devine valoarea derivatei
      Theta(j + 1) = Theta(j + 1) - (alpha * sum);
      % Nu uitam sa luam in considerare ca iteratia lui Theta incep de la 0
    endfor
  endfor
endfunction