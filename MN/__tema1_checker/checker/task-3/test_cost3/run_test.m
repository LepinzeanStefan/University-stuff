function run_test()
    "task-3";
    fout = fopen("out", "w+");
    load("../../input/task3/cost1.mat");

    old_precision = output_precision;

    input_layer_size = 400;
    hidden_layer_size = 25;
    output_layer_size = 10;

    lambda = 10;
    [J, grad] = cost_function(weights, X, y, lambda, input_layer_size, ...
                            hidden_layer_size, output_layer_size);
    

    output_precision(10);

    fdisp(fout, [J; grad]);
    fclose(fout);
endfunction