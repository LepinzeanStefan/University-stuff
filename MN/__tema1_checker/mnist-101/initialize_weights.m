function [matrix] = initialize_weights(L_prev, L_next)
  % L_prev -> the number of units in the previous layer
  % L_next -> the number of units in the next layer

  % matrix -> the matrix with random values
  
  e = sqrt(6) / sqrt(L_prev + L_next); % Calculam e conform cerintei
  matrix = -e + (e + e) * rand(L_next, (L_prev + 1)); % Initializam matricea
  % Initializarea se face cu numere random din intervalul (-e, e)
  % Pentru a reusi acest lucru, dar a pastra totusi intervalul in float,
  % folosim aceasta formula aritmetica "a + (b - a) * rand"
  % De notat ca numarul de coloane este incrementat cu 1 pentru a lua
  % in calcul si inputul initial
endfunction
