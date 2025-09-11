package org.annotationlib;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ClassAnnotationProcessor {

    private final Map<Class<? extends Annotation>, List<Consumer<Class<?>>>> actions;
    private final Collection<ClassAnnotationScanner> scanners;

    public ClassAnnotationProcessor() {
        this.actions = new HashMap<>();
        this.scanners = new ArrayList<>();
    }

    public void addAnnotationAndActions(Property... props) {
        for (Property prop : props) {
            actions.computeIfAbsent(prop.annotation, k -> new ArrayList<>()).add(prop.fn);
        }
        scanners.add(new ClassAnnotationScanner(
                Arrays.stream(props).map(p -> p.annotation).collect(Collectors.toSet()),
                Arrays.stream(props).flatMap(p -> p.paths.stream()).collect(Collectors.toSet())
        ));
    }

    public void addScanners(ClassAnnotationScanner... scanners) {
        this.scanners.addAll(List.of(scanners));
    }

    public void addScanners(Collection<ClassAnnotationScanner> scanners) {
        this.scanners.addAll(scanners);
    }

    public void process() {
        var startTime = System.nanoTime();
        System.out.println("AnnotationProcessor - process started");
        actions.forEach((key, value) ->
                scanners.stream()
                        .flatMap(scanner ->
                                scanner.getAnnotatedClasses(key).stream())
                        .collect(Collectors.toSet())
                        .forEach(clz ->
                                value.forEach(consumer -> consumer.accept(clz))));
        System.out.println("AnnotationProcessor - process finished in " + ((System.nanoTime() - startTime) / 1_000) + "ms");
    }


    public record Property(
            Class<? extends Annotation> annotation,
            Consumer<Class<?>> fn,
            Collection<String> paths
    ) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Property property)) return false;
            return annotation.equals(property.annotation);
        }

        @Override
        public int hashCode() {
            return annotation.hashCode();
        }
    }
}
