function run_test()
    "task-1";
    fout = fopen('out', 'w+');
    Labyrinth = parse_labyrinth("../../input/task1/test1.in");
	Link = get_link_matrix(Labyrinth);
	[G, c] = get_Jacobi_parameters(Link);
	tol = 1e-10;
	max_iter = 10000;
	[probs, err, steps] = perform_iterative(G, c, zeros(size(c)), tol, max_iter);
	fdisp(fout, probs);
	fdisp(fout, err);
	fdisp(fout, steps);
	fclose(fout);
end