function [x, err, steps] = perform_iterative (G, c, x0, tol, max_steps)
	% G -> iteration matrix
	% c -> iteration vector
	% x0 -> initial solution
	% tol -> accepted tolerance (norm of the difference between two solutions)
	% max_steps -> the maximum number of iterations
	
	% x -> final solution
	% err -> last calculated difference (as an euclidean norm)
	% steps -> actual number of steps performed

  len = length(c);
  % lungimea vectorului c adica numarul de celule
  
  x = x0;
  
  for steps = 1 : max_steps + 1
    x_nou = G * x + c;
    % Noua iteratie a solutiei, dupa metoda Jacobi
    err = norm(x_nou - x);
    if err < tol
      return
    end
    x = x_nou;
  endfor
   
endfunction
