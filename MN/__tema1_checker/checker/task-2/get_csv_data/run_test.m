function run_test()
    "task-2";
    fout = fopen("out", "w+");
    [Y InitialMatrix] = parse_csv_file("../../input/task2/Housing.csv");
    [m n] = size(InitialMatrix);
    for i = 1 : m
        fprintf(fout , '%ld ', Y(i));

        for j = 1 : n
            if j != n
                if isnumeric(InitialMatrix{i,j})
                    if mod(InitialMatrix{i,j}, 1) == 0
                    fprintf(fout, '%d ', InitialMatrix{i, j});
                    else
                    fprintf(fout, '%.2f ', InitialMatrix{i, j});
                    endif
                else
                fprintf(fout, '%s ', InitialMatrix{i, j});
                endif
            else
                if isnumeric(InitialMatrix{i,j})
                if mod(InitialMatrix{i,j}, 1) == 0
                    fprintf(fout, '%d', InitialMatrix{i, j});
                    else
                    fprintf(fout, '%.2f', InitialMatrix{i, j});
                    endif
                else
                fprintf(fout, '%s', InitialMatrix{i, j});
                endif
            endif
        endfor

        if i != m
        fprintf(fout, '\n');
        endif
    endfor
    fclose(fout);
endfunction