package org.townsimulator;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class PerformanceTest {

    public static final int A_INDEX = 0;
    public static final int B_INDEX = 1;

    public static long startProfile() {
        return System.nanoTime();
    }

    public static void endProfile(long begin) {
        System.out.println(System.nanoTime() - begin);
    }

    public static void aaa() {
//        HungerSystem.
    }

    static void doNothingStatic() {
        return;
    }

    @Test
    void mapVSarray() {
        var map = new HashMap<Class<? extends Object>, Object>();
        var array = new Object[10];

        var a = new A();
        var b = new B();

        System.out.println("Map profiling");
        var time1 = startProfile();
//            System.out.println("Put...");
//            var time2 = startProfile();
        map.put(a.getClass(), a);
        map.put(b.getClass(), b);
//            endProfile(time2);
//            System.out.println("Get...");
//            var time3 = startProfile();
        var mapRetA = map.get(a.getClass());
        var mapRetB = map.get(b.getClass());
//            endProfile(time3);
        endProfile(time1);

        a = new A();
        b = new B();
        System.out.println("Array profiling");
        var time4 = startProfile();
//            System.out.println("Indexing...");
//            var time5 = startProfile();
        array[A_INDEX] = a;
        array[B_INDEX] = b;
//            endProfile(time5);
//            System.out.println("Get...");
//            var time6 = startProfile();
        var arrRetA = array[A_INDEX];
        var arrRetB = array[B_INDEX];
//            endProfile(time6);
        endProfile(time4);

    }

    @Test
    void banchmarkStuff() {
        callFunction();
        callFunctionStatic();

    }

    @Benchmark
    public void callFunction() {
        for (int i = 0; i < 10; i++) {
            doNothing();
        }
    }

    @Benchmark
    public void callFunctionStatic() {
        for (int i = 0; i < 10; i++) {
            doNothingStatic();
        }
    }

    void doNothing() {
        return;
    }

    public static class A {
    }

    public static class B {
    }

}
