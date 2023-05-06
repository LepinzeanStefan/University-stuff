function run_test()
    "task-2";
    fout = fopen("out", "w+");
    [Y InitialMatrix] = parse_data_set_file("../../input/task2/data_input1");
    [FeatureMatrix] = prepare_for_regression(InitialMatrix);
    [m n] = size(FeatureMatrix);
    for i = 1 : m
        for j = 1 : n
            if j != n
                    if mod(FeatureMatrix(i,j), 1) == 0
                    fprintf(fout, '%d ', FeatureMatrix(i, j));
                    else
                    fprintf(fout, '%.2f ', FeatureMatrix(i, j));
                    endif
            else
                if mod(FeatureMatrix(i,j), 1) == 0
                    fprintf(fout, '%d', FeatureMatrix(i, j));
                    else
                    fprintf(fout, '%.2f', FeatureMatrix(i, j));
                    endif
            endif
        endfor

        if i != m
        fprintf(fout, '\n');
        endif
    endfor
    fclose(fout);
endfunction