function [path] = heuristic_greedy (start_position, probabilities, Adj)
	% start_position -> the starting point in the labyrinth
	% probabilities -> vector of associated probabilities: including WIN and LOSE
	% Adj -> adjacency matrix
	
	% path -> the states chosen by the algorithm
  
  index = 0; % indica pozitia celei mai noi celule din path
  path(++index) = start_position;
  % Initializam path cu pozitia de inceput
  cell_num = size(Adj, 1);
  visited = zeros(1, cell_num);
  visited(start_position) = 1;
  while index > 0 
    position = path(index);
    if probabilities(position) == 1
      return;
    endif
    max = -1; % Probabilitate maxima a vecinilor
    neigh = 0;
    for i = 1 : cell_num
      if (Adj(position, i) == 1 && visited(i) == 0 && max < probabilities(i))
        neigh = i;
        max = probabilities(i);
      endif
    endfor
    if neigh == 0;
      index--;
      path = resize(path, 1, index)
    else
      path(++index) = neigh;
      visited(neigh) = 1;
    endif
  endwhile
endfunction
