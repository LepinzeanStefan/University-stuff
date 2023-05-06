function run_test()
    "task-1";
    fout = fopen('out', 'w+');
    Labyrinth = parse_labyrinth("../../input/task1/test1.in");
	Link = get_link_matrix(Labyrinth);
    [lines, cols, elem] = find(Link);
	fdisp(fout, [lines, cols, elem]);
	fclose(fout);
end