function run_test()
    "task-3";
    fout = fopen("out", "w+");
    [X, y] = load_dataset("../../input/task3/ex1.mat");
    fdisp(fout, [X, y]);
    fclose(fout);
endfunction