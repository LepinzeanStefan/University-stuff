function run_test()
    "task-3";
    fout = fopen("out", "w+");

    [X, y] = load_dataset("../../input/task3/dataset.mat");

    input_layer_size = 400;
    hidden_layer_size = 25;
    output_layer_size = 10;

    m = size(X, 1);

    percent = 0.85;
    [X_train, y_train, X_test, y_test] = split_dataset(X, y, percent);

    initial_theta1 = initialize_weights(input_layer_size, hidden_layer_size);
    initial_theta2 = initialize_weights(hidden_layer_size, output_layer_size);

    initial_theta = [initial_theta1(:); initial_theta2(:)];

    lambda = 1;

    max_steps = 100;

    options = optimset('MaxIter', 100);

    curry_cost_function = @(params) cost_function(params, X_train, y_train, lambda, ...
                                        input_layer_size, ...
                                        hidden_layer_size, ...
                                        output_layer_size);
                                        
    [optimal_theta, cost] = fmincg(curry_cost_function, initial_theta, options);

    predictions = predict_classes(X_test, optimal_theta, ...
                            input_layer_size, ...
                            hidden_layer_size, ...
                            output_layer_size);

    accuracy = sum(predictions == y_test) / length(y_test);
    fdisp(fout, accuracy);

    fclose(fout);
endfunction