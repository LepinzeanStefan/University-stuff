function [X_train, y_train, X_test, y_test] = split_dataset(X, y, percent)
  % X -> the loaded dataset with all training examples
  % y -> the corresponding labels
  % percent -> fraction of training examples to be put in training dataset
  
  % X_[train|test] -> the datasets for training and test respectively
  % y_[train|test] -> the corresponding labels
  
  % Example: [X, y] has 1000 training examples with labels and percent = 0.85
  %           -> X_train will have 850 examples
  %           -> X_test will have the other 150 examples

  [n, m]= size(X);
  idx = randperm(n); % Folosim acest index pentru a amesteca exemplele
  X = X(idx, 1 : m);
  y = y(idx);
  train = n * percent; % Impartim pentru a afla partea care se duce in train
  X_train(1 : train, 1 : m) = X(1 : train, 1 : m);
  y_train(1 : train, 1) = y(1 : train, 1);
  X_test(1 : n - train, 1 : m) = X((train + 1) : n, 1 : m);
  y_test(1 : n - train, 1) = y((train + 1) : n, 1);
endfunction
