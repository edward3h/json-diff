package com.deblock;

import com.deblock.matcher.*;
import com.deblock.viewer.OnlyErrorDiffViewer;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Test {
    public static void main(String[] args) throws JsonProcessingException {
        final var expectedJson = "{\"foo\": \"bar\", \"bar\": \"bar\", \"numberMatch\": 10.0, \"numberUnmatched\": 10.01}";
        final var receivedJson = "{\"foo\": \"foo\", \"bar\": \"bar\", \"numberMatch\": 10, \"numberUnmatched\": 10.02}";

        // define your matcher
        // CompositeJsonMatcher use other matcher to perform matching on objects, list or primitive
        final var jsonMatcher = new CompositeJsonMatcher(
                new LenientJsonArrayPartialMatcher(), // comparing array using lenient mode (ignore array order and extra items)
                new LenientJsonObjectPartialMatcher(), // comparing object using lenient mode (ignoring extra properties)
                new PrimitivePartialMatcher()
        );

        // generate a diff
        final var jsondiff = DiffGenerator.diff(expectedJson, receivedJson, jsonMatcher);

        // use the viewer to collect diff data
        final var viewer = new OnlyErrorDiffViewer();
        jsondiff.display(viewer);

        // print the diff result
        System.out.println(viewer);
        // print a similarity ratio between expected and received json (0 <= ratio <= 100)
        System.out.println(jsondiff.similarityRate());
    }
}
