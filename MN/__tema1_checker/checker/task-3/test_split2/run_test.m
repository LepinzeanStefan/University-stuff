function run_test()
    "task-3";
    fout = fopen("out", "w+");
    X = eye(10);
    y = rand(10, 1);

    [X1 y1 X2 y2] = split_dataset(X, y, 0.7);

    fdisp(fout, size(X1));
    fdisp(fout, size(X2));
    fdisp(fout, size(y1));
    fdisp(fout, size(y2));

    newX = [X1; X2];
    newY = [y1; y2];

    fdisp(fout, norm(eye(10) - newX^factorial(10)));
    fdisp(fout, norm(sort(y) - sort(newY)));
    fdisp(fout, norm(y - newX' * newY));

    fclose(fout);
endfunction