package org.townsimulator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.townsimulator.shapes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.System.nanoTime;
import static org.townsimulator.utils.Constants.PROFILE_FILE_LOGGER;

public class NotCleanTestMulti {
    static final int ARRAY_LENGTH = 100000;
    static final int FUNCTION_CALL_AMOUNT = 100000;
    static final int WARM_UP_FUNCTION_CALL_AMOUNT = 1000;

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
            for (int i = 0; i < WARM_UP_FUNCTION_CALL_AMOUNT; i++) {
                getArea1(s);
            }
            ShapeWithType[] shapeWithTypes2 = new ShapeWithType[ARRAY_LENGTH];
            for (int i = 0; i < ARRAY_LENGTH / 2; i++) {
                shapeWithTypes2[i] = new ShapeWithType(1f, 2f, ShapeType.TRIANGLE);
            }
            for (int i = ARRAY_LENGTH / 2; i < ARRAY_LENGTH; i++) {
                shapeWithTypes2[i] = new ShapeWithType(1f, 2f, ShapeType.RECTANGLE);
            }
            for (int i = 0; i < WARM_UP_FUNCTION_CALL_AMOUNT; i++) {
                getArea2(shapeWithTypes2);
            }
            ShapeWithType[] shapeWithTypes3 = new ShapeWithType[ARRAY_LENGTH];
            for (int i = 0; i < ARRAY_LENGTH / 2; i++) {
                shapeWithTypes3[i] = new ShapeWithType(1f, 2f, ShapeType.TRIANGLE);
            }
            for (int i = ARRAY_LENGTH / 2; i < ARRAY_LENGTH; i++) {
                shapeWithTypes3[i] = new ShapeWithType(1f, 2f, ShapeType.RECTANGLE);
            }

            for (int i = 0; i < WARM_UP_FUNCTION_CALL_AMOUNT; i++) {
                getArea3(shapeWithTypes3);
            }
            ShapeWithType[] shapeWithTypes4 = new ShapeWithType[ARRAY_LENGTH];
            for (int i = 0; i < ARRAY_LENGTH / 2; i++) {
                shapeWithTypes4[i] = new ShapeWithType(1f, 2f, ShapeType.TRIANGLE);
            }
            for (int i = ARRAY_LENGTH / 2; i < ARRAY_LENGTH; i++) {
                shapeWithTypes4[i] = new ShapeWithType(1f, 2f, ShapeType.RECTANGLE);
            }
            for (int i = 0; i < WARM_UP_FUNCTION_CALL_AMOUNT; i++) {
                getArea4(shapeWithTypes4);
            }

        }

        multipleCallProfiler.debug("-----------END WARMUP-----------");
        multipleCallProfiler.debug("-----------END WARMUP-----------");
        multipleCallProfiler.debug("-----------END WARMUP-----------");
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

        var start = nanoTime();
        for (int i = 0; i < FUNCTION_CALL_AMOUNT; i++) {
            getArea1(s);
        }
        multipleCallProfiler.debug("GET_AREA_1_AVERAGE: {}", ((nanoTime() - start) / ARRAY_LENGTH));
    }

    private static void getArea1(Shape[] shapes) {
        for (Shape s : shapes) {
            s.getArea();
        }
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

        var start = nanoTime();
        for (int i = 0; i < FUNCTION_CALL_AMOUNT; i++) {
            getArea2(s);
        }
        multipleCallProfiler.debug("GET_AREA_2_AVERAGE: {}", ((nanoTime() - start) / ARRAY_LENGTH));

    }

    private static void getArea2(ShapeWithType[] s) {
        ShapeWithType.getAreaSwitch(s);
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

        var start = nanoTime();
        for (int i = 0; i < FUNCTION_CALL_AMOUNT; i++) {
            getArea3(s);
        }
        multipleCallProfiler.debug("GET_AREA_3_AVERAGE: {}", ((nanoTime() - start) / ARRAY_LENGTH));

    }

    private static void getArea3(ShapeWithType[] s) {
        Map<ShapeType, Float> coeff = new HashMap<>();
        coeff.put(ShapeType.TRIANGLE, 0.5f);
        coeff.put(ShapeType.RECTANGLE, 1f);
        ShapeWithType.getAreaCoefficients(s, coeff);
    }

    @Test
    void test4() {
        ShapeWithType[] s = new ShapeWithType[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH / 2; i++) {
            s[i] = new ShapeWithType(1f, 2f, ShapeType.TRIANGLE);
        }
        for (int i = ARRAY_LENGTH / 2; i < ARRAY_LENGTH; i++) {
            s[i] = new ShapeWithType(1f, 2f, ShapeType.RECTANGLE);
        }

        var start = nanoTime();
        for (int i = 0; i < FUNCTION_CALL_AMOUNT; i++) {
            getArea4(s);
        }
        multipleCallProfiler.debug("GET_AREA_4_AVERAGE: {}", ((nanoTime() - start) / ARRAY_LENGTH));

    }

    private static void getArea4(ShapeWithType[] shapes) {
        Float[] coeff = {0.5f, 1.0f};
        var area = 0f;
        for (ShapeWithType shape : shapes) {
            area += coeff[shape.type.index] * shape.b * shape.h;
        }
    }

    private static void getArea5(Shape[] shapes) {
        for (Shape s : shapes) {
            s.getArea();
        }
    }

    @Test
    void test51() {
        Shape[] s = new Shape[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH / 2; i++) {
            s[i] = new Triangle(1f, 2f);
        }
        for (int i = ARRAY_LENGTH / 2; i < ARRAY_LENGTH; i++) {
            s[i] = new Rectangle(1f, 2f);
        }

        int numThreads = Runtime.getRuntime().availableProcessors(); // Use available cores
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        var start = nanoTime();

        // Submit multiple tasks to the thread pool
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < FUNCTION_CALL_AMOUNT; i++) {
            futures.add(executor.submit(() -> getArea5(s)));
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        multipleCallProfiler.debug("GET_AREA_51_AVERAGE: {}", ((nanoTime() - start) / FUNCTION_CALL_AMOUNT));
    }

    @Test
    void test52() {
        Shape[] s = new Shape[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH / 2; i++) {
            s[i] = new Triangle(1f, 2f);
        }
        for (int i = ARRAY_LENGTH / 2; i < ARRAY_LENGTH; i++) {
            s[i] = new Rectangle(1f, 2f);
        }

        int numThreads = Runtime.getRuntime().availableProcessors(); // Use available CPU cores
        Thread[] threads = new Thread[numThreads];

        var start = nanoTime();

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < FUNCTION_CALL_AMOUNT / numThreads; j++) {
                    getArea5(s);
                }
            });
            threads[i].start(); // Start the thread
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        multipleCallProfiler.debug("GET_AREA_52_AVERAGE: {}", ((nanoTime() - start) / FUNCTION_CALL_AMOUNT));
    }


}
