function [Y, InitialMatrix] = parse_data_set_file(file_path)
  % path -> a relative path to the .txt file

  % Y -> the vector with all actual values
  % InitialMatrix -> the matrix that must be transformed
  
  % parse_data_set_file implementation
  fid = fopen(file_path, "r");
  
  m = fscanf(fid, '%d', 1);
  n = fscanf(fid, '%d', 1);
  
  InitialMatrix = cell(m, n);
  
  for i = 1 : m
    Y(i) = fscanf(fid, '%d', 1);
    for j = 1 : n
      InitialMatrix(i, j) = fscanf(fid, '%s', 1); % Folosim fscanf pentru stringuri
      if any(isstrprop(InitialMatrix{i, j}, 'digit'))
        InitialMatrix{i, j} = str2num(InitialMatrix{i, j});
      endif
    endfor
  endfor
  
  fclose(fid);
endfunction