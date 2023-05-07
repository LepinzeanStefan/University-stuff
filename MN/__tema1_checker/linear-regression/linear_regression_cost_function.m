function [Error] = linear_regression_cost_function(Theta, Y, FeatureMatrix)
  % Theta -> the vector of weights
  % Y -> the vector with all actual values
  % FeatureMatrix -> the matrix with all training examples

  % the error of the regularized cost function

  [m, n] = size(FeatureMatrix);
  
  sum = 0; % Aceasta va fi suma termenilor pentru functia de cost
  for i = 1 : m
    htheta = FeatureMatrix(i, 1 : n) * Theta(2 : n + 1);
    htheta = htheta - Y(i); % Calculam valoarea prezisa din care scadem y
    sum += (htheta * htheta); % Ridicam la patrat si adaugam in suma
  endfor
  Error = sum / (2 * m); % Impartim cu 2m si obtinem rezultatul functiei
endfunction