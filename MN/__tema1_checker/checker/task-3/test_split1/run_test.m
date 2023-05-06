function run_test()
    "task-3";
    fout = fopen("out", "w+");
    X = eye(2);
    y = [1; 1];

    [X1 y1 X2 y2] = split_dataset(X, y, 0.5);

    fdisp(fout, size(X1));
    fdisp(fout, size(X2));
    fdisp(fout, size(y1));
    fdisp(fout, size(y2));

    newX = [X1; X2];
    newY = [y1; y2];

    fdisp(fout, norm(newX ^ 2 - eye(2)));
    fdisp(fout, norm(newY - y));
    fclose(fout);
endfunction