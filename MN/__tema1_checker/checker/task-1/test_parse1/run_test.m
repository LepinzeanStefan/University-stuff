function run_test()
    "task-1";
    fout = fopen('out', 'w+');
    Labyrinth = parse_labyrinth("../../input/task1/test1.in");
    for i=1:size(Labyrinth, 1)
        fprintf(fout, '%d ', Labyrinth(i,:));
        fprintf(fout, '\n');
    endfor
    fclose(fout);
end