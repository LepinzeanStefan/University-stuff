function [Y, InitialMatrix] = parse_csv_file(file_path)
  % path -> a relative path to the .csv file
  
  % Y -> the vector with all actual values
  % InitialMatrix -> the matrix that must be transformed

  fid = fopen(file_path, "r");
  
  fskipl(fid); % luam prima linia din fisier, care e irelevanta
  
  iter = 1; % retine pozitia la care ne aflam
  
  while true
    string = fgetl(fid); % citim o linie din fisier
    if (isnumeric(string))
      return % daca str = -1 inseamna ca am terminat de citit
    endif
    str = strsplit(string, ","); % apoi o impartim in celule
    Y(iter) = str2num(str{1,1});
    for i = 1 : 12
      if any(isstrprop(str{1, i + 1}, "digit"))
        InitialMatrix{iter, i} = str2num(str{1, i + 1});
      else
        InitialMatrix(iter, i) = str(1, i + 1);
      endif
    endfor
    iter++;
  endwhile
endfunction