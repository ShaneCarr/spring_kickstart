package com.example.demo.graphql.model.options; // Note: This is in a 'options' sub-package

import java.util.Optional;

public record HydrantOptions(
        Optional<String> greaterThan,
        Optional<String> lessThan,
        Optional<Integer> threadsCount
) {
    public static HydrantOptions defaultOptions() {
        return new HydrantOptions(Optional.empty(), Optional.empty(), Optional.empty());
    }
}