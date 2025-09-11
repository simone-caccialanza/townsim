package org.annotationlib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.annotationlib.ClassAnnotationScanner.ScanType.*;

public class ClassAnnotationScanner {

    private Set<Class<? extends Annotation>> annotations;
    private Set<String> packages;
    private ScanType scanType;
    private Map<Class<? extends Annotation>, Collection<Class<?>>> annotatedClasses;
    private Map<Class<? extends Annotation>, Collection<Method>> annotatedMethods;

    public ClassAnnotationScanner(Set<Class<? extends Annotation>> annotations, Set<String> packages) {
        this.annotations = annotations;
        this.packages = packages;
        this.scanType = LAZY;
    }

    public ClassAnnotationScanner(Set<Class<? extends Annotation>> annotations, Set<String> packages, ScanType scanType) {
        this.annotations = annotations;
        this.packages = packages;
        if (EAGER.equals(scanType)) {
            scan();
        }
    }

    public void manualScan() {
        if (MANUAL.equals(scanType)) {
            scan();
        }
    }

    private void scan() {
        if (Objects.isNull(annotatedClasses)) {
            annotatedClasses = AnnotationUtils.loadClassesFromPackagesWithAnnotations(packages, annotations);
        }
    }

    public Collection<Class<?>> getAnnotatedClasses(Class<? extends Annotation> clz) {
        if (LAZY.equals(scanType)) {
            scan();
        }
        return this.annotatedClasses.get(clz);
    }

    public Collection<Method> getAnnotatedMethod(Class<? extends Annotation> clz) {
        if (LAZY.equals(scanType)) {
            scan();
        }
        return this.annotatedMethods.get(clz);
    }


    public enum ScanType {
        EAGER, MANUAL, LAZY
    }

}
