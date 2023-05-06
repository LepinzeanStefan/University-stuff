function run_test()
    "task-3";
    fout = fopen("out", "w+");
    L_prev = 10;
    L_next = 20;
    A = initialize_weights(L_prev, L_next);
    for i=1:size(A,1)
      fprintf(fout, "%f ", A(i, :));
      fprintf(fout, "\n");
    endfor

    fclose(fout);
endfunction