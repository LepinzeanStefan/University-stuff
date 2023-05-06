function run_test()
    "task-3";
    fout = fopen("out", "w+");
    [X, y] = load_dataset("../../input/task3/ex2.mat");
    for i=1:length(X)
      fprintf(fout, "%f ", X(i,:));
      fprintf(fout, "\n");
    endfor
    fprintf(fout, "%f ", y);

    fclose(fout);
endfunction