function run_test()
    "task-2";
    "task-3";
    fout = fopen("out", "w+");
    load("../../input/task2/normal_eq3");

    tol = 1e-16;
    max_iter = 750;

    [Theta] = normal_equation(X, y, tol, max_iter);
    
    for i = 1 : length(Theta)
        fprintf(fout, "%e\n", Theta(i));
    endfor

    fclose(fout);
endfunction