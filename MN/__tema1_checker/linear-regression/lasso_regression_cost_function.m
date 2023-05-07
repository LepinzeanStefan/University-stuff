function [Error] = lasso_regression_cost_function(Theta, Y, FeatureMatrix, lambda)
  % Theta -> the vector of weights
  % Y -> the vector with all actual values
  % FeatureMatrix -> the matrix with all training examples
  % lambda -> regularization parameter that controls the amount of 
  %           shrinkage applied to the regression coefficients

  % Error -> the error of the regularized cost function

  [m, n] = size(FeatureMatrix);
  
  sum = 0; % Aceasta va fi suma termenilor pentru functia de cost
  dist = norm(Theta, 1); % Calculam aici norma pentru a nu lungii loopul
  for i = 1 : m
    htheta = FeatureMatrix(i, 1 : n) * Theta(2 : n + 1);
    htheta = Y(i) - htheta; % Calculam valoarea prezisa din care scadem y
    sum += (htheta * htheta) + lambda * dist; % Ridicam la patrat si adaugam in suma
  endfor
  Error = sum / m; % Impartim cu 2m si obtinem rezultatul functiei
endfunction