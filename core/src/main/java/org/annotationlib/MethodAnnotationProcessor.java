package org.annotationlib;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MethodAnnotationProcessor {

    private final Collection<Class<? extends Annotation>> annotations;
    private final Collection<MethodAnnotationScanner> scanners;

    public MethodAnnotationProcessor() {
        this.annotations = new HashSet<>();
        this.scanners = new ArrayList<>();
    }

    public void addAnnotations(Class<? extends Annotation>... annotations) {
        Collections.addAll(this.annotations, annotations);
    }

    public void addScanners(MethodAnnotationScanner... scanners) {
        this.scanners.addAll(List.of(scanners));
    }

    public void addScanners(Collection<MethodAnnotationScanner> scanners) {
        this.scanners.addAll(scanners);
    }

    public void process() {
        var startTime = System.nanoTime();
        System.out.println("AnnotationProcessor - process started");
        annotations.forEach((a) ->
                scanners.stream()
                        .flatMap(scanner ->
                                scanner.getAnnotatedMethods(a).stream())
                        .collect(Collectors.toSet())
                        .forEach(method -> {
                            if (method.getParameterCount() != 0) {
                                throw new RuntimeException("Method " + method.getName() + " has unexpected parameters.");
                            }
                            try {
                                method.invoke(null);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }

                        }));
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
