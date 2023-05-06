function run_test()
    "task-2";
    fout = fopen("out", "w+");
    [Y InitialMatrix] = parse_csv_file("../../input/task2/Housing.csv");
    [FeatureMatrix] = prepare_for_regression(InitialMatrix);
    alpha = (1e-4)^4;
    [m n] = size(FeatureMatrix);
    iter = 125;

    [Theta] = gradient_descent(FeatureMatrix, Y, n, m, alpha, iter);
    
    for i = 1 : length(Theta)
        fprintf(fout, "%e\n", Theta(i));
    endfor

    fclose(fout);
endfunction