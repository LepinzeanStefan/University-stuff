function run_test()
    "task-1";
    fout = fopen('out', 'w+');
    Labyrinth = parse_labyrinth("../../input/task1/test2.in");
	Adj = get_adjacency_matrix(Labyrinth);
	[lines, cols, elem] = find(Adj);
	fdisp(fout, [lines, cols, elem]);
	fclose(fout);
end