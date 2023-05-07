function [Theta] = normal_equation(FeatureMatrix, Y, tol, iter)
  % FeatureMatrix -> the matrix with all training examples
  % Y -> the vector with all actual values
  % tol -> the learning rate
  % iter -> the number of iterations
  % tol -> the tolerance level for convergence of the conjugate gradient method

  % Theta -> the vector of weights
  
  if (size(Y, 1) ==  1)
    Y = Y';
  endif
  Y = FeatureMatrix' * Y;
  FeatureMatrix = FeatureMatrix' * FeatureMatrix;
  [m, n] = size(FeatureMatrix);
  [m, n]
  Theta = zeros(n, 1); % Initializam Theta drept vector coloana de 0
  
  % In primul rand, verificam daca matricea este pozitiv definita
  % O metoda foarte eficienta este functia chol care poate returna tipul matricii
  [~, p] = chol(FeatureMatrix); % Daca p este mai mare de zero, matricea nu este buna
  if p > 0
    Theta = zeros(n + 1, 1); % Adaugam elementul Theta0
    return;
  endif
  
  % Aplicam pseudocodul pentru algoritmul metodei gradientului conjugate
  
  r = Y - (FeatureMatrix * Theta); % Calculam vectorul inital r
  v = r;
  tol *= tol; % Vom avea nevoie de patratul tolerantei
  k = 1;
  
  while (k <= iter && (r' * r) > tol)
    tk = (r' * r) / (v' * FeatureMatrix * v);
    Theta = Theta + (tk * v); % Actualizam Theta cu noile aproximari
    r_new = r - (tk * FeatureMatrix * v);
    sk = (r_new' * r_new) / (r' * r);
    r = r_new; % Actualizam vectorul r
    v = r + (sk * v); % Actualizam v
    k++;
  endwhile
  Theta = resize(Theta, n + 1, 1); % Extindem Theta pentru a retine si Theta 0
  Theta(2 : n + 1) = Theta(1 : n); % Shiftam vectorul in jos
  Theta(1) = 0; % Theta0 = 0
endfunction