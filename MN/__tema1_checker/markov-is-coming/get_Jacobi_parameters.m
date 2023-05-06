function [G, c] = get_Jacobi_parameters (Link)
	% Link -> the link matrix (edge with probabilities matrix)
	
  % G -> iteration matrix
	% c -> iteration vector
	
  link_size = size(Link, 1);
  
  c = sparse(zeros(link_size - 2, 1));
  % Initializam c drept matrice de zero pe care o transformam in matrice rara
  % Al doilea argument al functiei sparse este 1 pentru a creea un vector coloana
  
  for i = 1 : link_size - 2
    if Link(i, link_size - 1) != 0
      c(i) = Link(i, link_size - 1)
      % Cautam in coloana "WIN" daca exista legaturi din alte celule
    endif
  endfor
  
  G = resize(Link, [link_size - 2, link_size - 2]);
  % Pentru matrice tot ce trebuie sa facem este sa eliminam din matricea Link
  % coloanele si randurile pentru WIN si LOSE

endfunction
