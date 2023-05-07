function [FeatureMatrix] = prepare_for_regression(InitialMatrix)
  % InitialMatrix -> the matrix that must be transformed

  % FeatureMatrix -> the matrix with all training examples
  [m, n] = size(InitialMatrix)
  
  for i = 1 : m
    iter = 1; % Vom folosi acest iter pentru a inregistra datele in matrice
    for j = 1 : n
      if ischar(InitialMatrix{i, j})
        if strcmp(InitialMatrix{i, j}, "yes") == 1
          FeatureMatrix(i, iter++) = 1;
        elseif strcmp(InitialMatrix{i, j}, "no") == 1
          FeatureMatrix(i, iter++) = 0;
        elseif strcmp(InitialMatrix{i, j}, "semi-furnished") == 1
          FeatureMatrix(i, iter++) = 1;
          FeatureMatrix(i, iter++) = 0;
        elseif strcmp(InitialMatrix{i, j}, "unfurnished") == 1
          FeatureMatrix(i, iter++) = 0;
          FeatureMatrix(i, iter++) = 1;
        else
          FeatureMatrix(i, iter++) = 0;
          FeatureMatrix(i, iter++) = 0;
        endif
      else
        FeatureMatrix(i, iter++) = InitialMatrix{i, j};
      endif
    endfor
  endfor
endfunction