function run_test()
    "task-2";
    "task-3";
    fout = fopen("out", "w+");
    load("../../input/task2/linear_cost_3");

    lambda = 15;

    [Error] = lasso_regression_cost_function(weights, y, X, lambda);
    
    fprintf(fout, "%f", Error)

    fclose(fout);
endfunction