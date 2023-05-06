function [Adj] = get_Adjacency_matrix (Labyrinth)
	% Labyrinth -> the matrix of all encodings
  
  % Adj -> the Adjacency matrix associated to the given labyrinth
  
  [n, m] = size(Labyrinth);
  Adj_size = (n * m) + 2;
  % Matricea este initiata cu zero, urmand sa o completam
  Adj = zeros(Adj_size);
  Adj(Adj_size - 1, Adj_size - 1) = 1;
  Adj(Adj_size, Adj_size) = 1;
  
  %   Mai intai inspectam extremele labirintului, intrucat doar in aceste
  % celule putem gasi iesiri
  
  for j = 1 : m
    if (bitand(Labyrinth(1, j), 8) == 0)
      Adj(j, Adj_size - 1) = 1;
      % Exista conexiune in nordul celulei catre WIN
    endif
    if (bitand(Labyrinth(n, j), 4) == 0)
      Adj((n - 1) * m + j, Adj_size - 1) = 1;
      % Exista conexiune in sudul celulei catre WIN
    endif
    
    if (bitand(Labyrinth(n, j), 8) == 0)
      Adj((n - 1) * m + j, (n - 2) * m + j) = 1;
      % Exista conexiune in nordul celulei catre alta celula
    endif
    if (bitand(Labyrinth(1, j), 4) == 0)
      Adj(j, m + j) = 1;
      % Exista conexiune in sudul celulei catre alta celula
    endif
    
    if ((bitand(Labyrinth(1, j), 2) == 0) && (j != m))
      Adj(j, j + 1) = 1;
      % Exista conexiune in dreapta celulei catre alta celula
    endif
    if ((bitand(Labyrinth(n, j), 2) == 0) && (j != m))
      Adj((n - 1) * m + j, (n - 1) * m + j + 1) = 1;
      % Exista conexiune in dreapta celulei catre alta celula
    endif
    
    if ((bitand(Labyrinth(1, j), 1) == 0) && (j != 1))
      Adj(j, j - 1) = 1;
      % Exista conexiune in stanga celulei catre alta celula
    endif
    if ((bitand(Labyrinth(n, j), 1) == 0) && (j != 1))
      Adj((n - 1) * m + j, (n - 1) * m + j - 1) = 1;
      % Exista conexiune in stanga celulei catre alta celula
    endif
    
  endfor
  
  for i = 1 : n
    
    if (bitand(Labyrinth(i, 1), 1) == 0)
      Adj((i - 1) * m + 1, Adj_size) = 1;
      % Exista conexiune in vestul celulei catre LOSE
    endif
    if (bitand(Labyrinth(i, m), 2) == 0)
      Adj((i - 1) * m + m, Adj_size) = 1;
      % Exista conexiune in estul celulei catre LOSE
    endif
    
    if (bitand(Labyrinth(i, 1), 2) == 0)
      Adj((i - 1) * m + 1, (i - 1) * m + 2) = 1;
      % Exista conexiune in estul celulei catre alta celula
    endif
    if (bitand(Labyrinth(i, m), 1) == 0)
      Adj((i - 1) * m + m, (i - 1) * m + m - 1) = 1;
      % Exista conexiune in vestul celulei catre alta celula
    endif
    
    if (bitand(Labyrinth(i, 1), 4) == 0 && i != n)
      Adj((i - 1) * m + 1, i * m + 1) = 1;
      % Exista conexiune in sudul celulei catre alta celula
    endif
    if (bitand(Labyrinth(i, m), 4) == 0 && i != n)
      Adj((i - 1) * m + m, i * m + m) = 1;
      % Exista conexiune in sudul celulei catre alta celula
    endif
    
    if (bitand(Labyrinth(i, 1), 8) == 0 && i != 1)
      Adj((i - 1) * m + 1, (i - 2) * m + 1) = 1;
      % Exista conexiune in nordu; celulei catre alta celula
    endif
    if (bitand(Labyrinth(i, m), 8) == 0 && i != 1)
      Adj((i - 1) * m + m, (i - 2) * m + m) = 1;
      % Exista conexiune in nordul celulei catre alta celula
    endif
    
  endfor
  
  for i = 2 : n - 1
    for j = 2 : m - 1
      if bitand(Labyrinth(i, j), 1) == 0
          % Exista conexiune in vestul celulei catre alta celula
          Adj((i - 1) * m + j, (i - 1) * m + j - 1) = 1;
      endif

      if bitand(Labyrinth(i, j), 2) == 0
          % Exista conexiune in estul celulei catre alta celula
          Adj((i - 1) * m + j, (i - 1) * m + j + 1) = 1;
      endif
      
      if bitand(Labyrinth(i, j), 4) == 0
          % Exista conexiune in sudul celulei catre alta celula
          Adj((i - 1) * m + j, i * m + j) = 1;
      endif
      
      if bitand(Labyrinth(i, j), 8) == 0
          % Exista conexiune in nordul celulei catre alta celula
          Adj((i - 1) * m + j, (i - 2) * m + j) = 1;
      endif
      
    endfor
  endfor
  % O transformam in matrice rara
  Adj = sparse(Adj);

endfunction
