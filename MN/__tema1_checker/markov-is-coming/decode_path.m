function [decoded_path] = decode_path (path, lines, cols)
	% path -> vector containing the order of the states that arrived
	% 		 to a winning position
	% lines -> numeber of lines
	% cols -> number of columns
	
	% decoded_path -> vector of pairs (line_index, column_index)
  %                 corresponding to path states
  % hint: decoded_path does not contain indices for the WIN state

  % decode_path implementation
  
  path_size = size(path, 2) - 1
  
  decoded_path = zeros(path_size, 2) % Construim un vector cu 2 coloane
  
  for i = 1 : path_size
    line = ceil(path(i) / cols)
    % Linia la care se afla celula rezulta din intregul acestei impartiri
    coll = mod(path(i) - 1, cols) + 1
    % Restul reprezinta coloana unde se afla
    decoded_path(i, 1) = line
    decoded_path(i, 2) = coll
  endfor

endfunction