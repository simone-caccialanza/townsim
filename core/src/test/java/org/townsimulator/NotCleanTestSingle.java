package org.townsimulator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.townsimulator.shapes.*;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.nanoTime;
import static org.townsimulator.utils.Constants.PROFILE_FILE_LOGGER;

public class NotCleanTestSingle {
    static final int ARRAY_LENGTH = 1000;
    static final int FUNCTION_CALL_AMOUNT = 1000;

    private static Logger singleCallProfiler;
    private static Logger multipleCallProfiler;

    static void initLoggers() {
        singleCallProfiler = LoggerFactory.getLogger(PROFILE_FILE_LOGGER);
        multipleCallProfiler = LoggerFactory.getLogger(PROFILE_FILE_LOGGER);
    }

    @BeforeAll
    static void warmUp() {
        initLoggers();
        for (int j = 0; j < 100; j++) {
            // Call the method to warm up the JIT compiler.
            // Optionally, use the getArea1, getArea2, and getArea3 methods as needed.
            Shape[] s = new Shape[ARRAY_LENGTH];
            for (int i = 0; i < ARRAY_LENGTH / 2; i++) {
                s[i] = new Triangle(1f, 2f);
            }
            for (int i = ARRAY_LENGTH / 2; i < ARRAY_LENGTH; i++) {
                s[i] = new Rectangle(1f, 2f);
            }
            getArea1(s);
            ShapeWithType[] shapeWithTypes2 = new ShapeWithType[ARRAY_LENGTH];
            for (int i = 0; i < ARRAY_LENGTH / 2; i++) {
                shapeWithTypes2[i] = new ShapeWithType(1f, 2f, ShapeType.TRIANGLE);
            }
            for (int i = ARRAY_LENGTH / 2; i < ARRAY_LENGTH; i++) {
                shapeWithTypes2[i] = new ShapeWithType(1f, 2f, ShapeType.RECTANGLE);
            }
            getArea2(shapeWithTypes2);
            ShapeWithType[] shapeWithTypes3 = new ShapeWithType[ARRAY_LENGTH];
            for (int i = 0; i < ARRAY_LENGTH / 2; i++) {
                shapeWithTypes3[i] = new ShapeWithType(1f, 2f, ShapeType.TRIANGLE);
            }
            for (int i = ARRAY_LENGTH / 2; i < ARRAY_LENGTH; i++) {
                shapeWithTypes3[i] = new ShapeWithType(1f, 2f, ShapeType.RECTANGLE);
            }
            getArea3(shapeWithTypes3);
        }

        singleCallProfiler.debug("-----------END WARMUP-----------");
        singleCallProfiler.debug("-----------END WARMUP-----------");
        singleCallProfiler.debug("-----------END WARMUP-----------");
    }

    @Test
    void test1() {
        Shape[] s = new Shape[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH / 2; i++) {
            s[i] = new Triangle(1f, 2f);
        }
        for (int i = ARRAY_LENGTH / 2; i < ARRAY_LENGTH; i++) {
            s[i] = new Rectangle(1f, 2f);
        }
        getArea1(s);
    }

    private static void getArea1(Shape[] shapes) {
        var start = nanoTime();
        for (Shape s : shapes) {
            s.getArea();
        }
        var end = nanoTime();
        singleCallProfiler.debug("GET_AREA_1: {}", ((end - start) / ARRAY_LENGTH));
    }

    @Test
    void test2() {
        ShapeWithType[] s = new ShapeWithType[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH / 2; i++) {
            s[i] = new ShapeWithType(1f, 2f, ShapeType.TRIANGLE);
        }
        for (int i = ARRAY_LENGTH / 2; i < ARRAY_LENGTH; i++) {
            s[i] = new ShapeWithType(1f, 2f, ShapeType.RECTANGLE);
        }
        getArea2(s);

    }

    private static void getArea2(ShapeWithType[] s) {
        var start = nanoTime();
        ShapeWithType.getAreaSwitch(s);
        var end = nanoTime();
        singleCallProfiler.debug("GET_AREA_2: {}", ((end - start) / ARRAY_LENGTH));
    }

    @Test
    void test3() {
        ShapeWithType[] s = new ShapeWithType[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH / 2; i++) {
            s[i] = new ShapeWithType(1f, 2f, ShapeType.TRIANGLE);
        }
        for (int i = ARRAY_LENGTH / 2; i < ARRAY_LENGTH; i++) {
            s[i] = new ShapeWithType(1f, 2f, ShapeType.RECTANGLE);
        }
        getArea3(s);
    }

    private static void getArea3(ShapeWithType[] s) {
        Map<ShapeType, Float> coeff = new HashMap<>();
        coeff.put(ShapeType.TRIANGLE, 0.5f);
        coeff.put(ShapeType.RECTANGLE, 1f);
        var start = nanoTime();
        ShapeWithType.getAreaCoefficients(s, coeff);
        var end = nanoTime();
        singleCallProfiler.debug("GET_AREA_3: {}", ((end - start) / ARRAY_LENGTH));
    }


}
