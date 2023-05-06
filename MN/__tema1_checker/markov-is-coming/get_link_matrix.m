function [Link] = get_link_matrix (Labyrinth)
	% Labyrinth -> the matrix of all encodings
  
  % Link -> the link matrix associated to the given labyrinth
  
  [n, m] = size(Labyrinth);
  Link_size = n * m + 2;
  % Matricea este initiata cu zero, urmand sa o completam
  Link = zeros(Link_size);
  % O transformam in matrice rara
  Link = sparse(Link);
  Link(Link_size - 1, Link_size - 1) = 1;
  Link(Link_size, Link_size) = 1;
  
  %   Mai intai inspectam extremele labirintului, intrucat doar in aceste
  % celule putem gasi iesiri
  
  for j = 1 : m
    link_nr_up = dec2bin(Labyrinth(1, j));
    link_nr_down = dec2bin(Labyrinth(n, j));
    %  Luam 2 variabile care vor retine cate legaturi are celula
    % din limita superioara si cea inferioara
    
    link_nr_up = 4 - sum(str2num(link_nr_up'));
    link_nr_down = 4 - sum(str2num(link_nr_down'));
    % Folosind sum insumam toti bitii din cod si dupa ii scadem cu 4
    % Din acest calcul rezulta numarul de biti nuli
    
    if (bitand(Labyrinth(1, j), 8) == 0)
      Link(j, Link_size - 1) = 1 / link_nr_up;
      % Exista conexiune in nordul celulei catre WIN
    endif
    if (bitand(Labyrinth(n, j), 4) == 0)
      Link((n - 1) * m + j, Link_size - 1) = 1 / link_nr_down;
      % Exista conexiune in sudul celulei catre WIN
    endif
    
    if (bitand(Labyrinth(n, j), 8) == 0)
      Link((n - 1) * m + j, (n - 2) * m + j) = 1 / link_nr_down;
      % Exista conexiune in nordul celulei catre alta celula
    endif
    if (bitand(Labyrinth(1, j), 4) == 0)
      Link(j, m + j) = 1 / link_nr_up;
      % Exista conexiune in sudul celulei catre alta celula
    endif
    
    if ((bitand(Labyrinth(1, j), 2) == 0) && (j != m))
      Link(j, j + 1) = 1 / link_nr_up;
      % Exista conexiune in dreapta celulei catre alta celula
    endif
    if ((bitand(Labyrinth(n, j), 2) == 0) && (j != m))
      Link((n - 1) * m + j, (n - 1) * m + j + 1) = 1 / link_nr_down;
      % Exista conexiune in dreapta celulei catre alta celula
    endif
    
    if ((bitand(Labyrinth(1, j), 1) == 0) && (j != 1))
      Link(j, j - 1) = 1 / link_nr_up;
      % Exista conexiune in stanga celulei catre alta celula
    endif
    if ((bitand(Labyrinth(n, j), 1) == 0) && (j != 1))
      Link((n - 1) * m + j, (n - 1) * m + j - 1) = 1 / link_nr_down;
      % Exista conexiune in stanga celulei catre alta celula
    endif
    
  endfor
  
  for i = 1 : n
    link_nr_left = dec2bin(Labyrinth(i, 1));
    link_nr_right = dec2bin(Labyrinth(i,m));
    %  Luam 2 variabile care vor retine cate legaturi are celula
    % din limita estica si cea vestica
    
    link_nr_left = 4 - sum(str2num(link_nr_left'));
    link_nr_right = 4 - sum(str2num(link_nr_right'));
    % Folosind sum insumam toti bitii din cod si dupa ii scadem cu 4
    % Din acest calcul rezulta numarul de biti nuli
    
    if (bitand(Labyrinth(i, 1), 1) == 0)
      Link((i - 1) * m + 1, Link_size) = 1 / link_nr_left;
      % Exista conexiune in vestul celulei catre LOSE
    endif
    if (bitand(Labyrinth(i, m), 2) == 0)
      Link((i - 1) * m + m, Link_size) = 1 / link_nr_right;
      % Exista conexiune in estul celulei catre LOSE
    endif
    
    if (bitand(Labyrinth(i, 1), 2) == 0)
      Link((i - 1) * m + 1, (i - 1) * m + 2) = 1 / link_nr_left;
      % Exista conexiune in estul celulei catre alta celula
    endif
    if (bitand(Labyrinth(i, m), 1) == 0)
      Link((i - 1) * m + m, (i - 1) * m + m - 1) = 1 / link_nr_right;
      % Exista conexiune in vestul celulei catre alta celula
    endif
    
    if (bitand(Labyrinth(i, 1), 4) == 0 && i != n)
      Link((i - 1) * m + 1, i * m + 1) = 1 / link_nr_left;
      % Exista conexiune in sudul celulei catre alta celula
    endif
    if (bitand(Labyrinth(i, m), 4) == 0 && i != n)
      Link((i - 1) * m + m, i * m + m) = 1 / link_nr_right;
      % Exista conexiune in sudul celulei catre alta celula
    endif
    
    if (bitand(Labyrinth(i, 1), 8) == 0 && i != 1)
      Link((i - 1) * m + 1, (i - 2) * m + 1) = 1 / link_nr_left;
      % Exista conexiune in nordu; celulei catre alta celula
    endif
    if (bitand(Labyrinth(i, m), 8) == 0 && i != 1)
      Link((i - 1) * m + m, (i - 2) * m + m) = 1 / link_nr_right;
      % Exista conexiune in nordul celulei catre alta celula
    endif
    
  endfor
  
  for i = 2 : n - 1
    for j = 2 : m - 1
      
      link_nr_curr = dec2bin(Labyrinth(i, j));
      % Variabila ce va retine suma legaturilor celulei curente
      link_nr_curr = 4 - sum(str2num(link_nr_curr'));
      % Insumam toti bitii iar apoi ii scadem cu 4 pentru a rezulta nr de biti nuli
      
      if bitand(Labyrinth(i, j), 1) == 0
          % Exista conexiune in vestul celulei catre alta celula
          Link((i - 1) * m + j, (i - 1) * m + j - 1) = 1 / link_nr_curr;
      endif

      if bitand(Labyrinth(i, j), 2) == 0
          % Exista conexiune in estul celulei catre alta celula
          Link((i - 1) * m + j, (i - 1) * m + j + 1) = 1 / link_nr_curr;
      endif
      
      if bitand(Labyrinth(i, j), 4) == 0
          % Exista conexiune in sudul celulei catre alta celula
          Link((i - 1) * m + j, i * m + j) = 1 / link_nr_curr;
      endif
      
      if bitand(Labyrinth(i, j), 8) == 0
          % Exista conexiune in nordul celulei catre alta celula
          Link((i - 1) * m + j, (i - 2) * m + j) = 1 / link_nr_curr;
      endif
      
    endfor
  endfor

endfunction
