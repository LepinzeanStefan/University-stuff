function run_test()
    "task-1";
    fout = fopen('out', 'w+');
    Labyrinth = parse_labyrinth("../../input/task1/test1.in");
	Adj = get_adjacency_matrix(Labyrinth);
	Link = get_link_matrix(Labyrinth);
	[G, c] = get_Jacobi_parameters(Link);
	tol = 1e-10;
	max_iter = 200;
	[probs, err, steps] = perform_iterative(G, c, zeros(size(c)), tol, max_iter);
	probs = [probs; 1; 0];
	[m, n] = size(Labyrinth);

	path = heuristic_greedy(2, probs, Adj);
	
	decoded_path = decode_path(path, m, n);
	fdisp(fout, decoded_path);
	
	fclose(fout);

end