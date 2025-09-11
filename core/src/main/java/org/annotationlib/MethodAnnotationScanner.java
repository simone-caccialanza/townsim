package org.annotationlib;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.annotationlib.MethodAnnotationScanner.ScanType.*;

public class MethodAnnotationScanner {

    private Set<Class<? extends Annotation>> annotations;
    private Set<String> packages;
    private ScanType scanType;
    private Map<Class<? extends Annotation>, Collection<Method>> annotatedMethods;

    public MethodAnnotationScanner(Set<Class<? extends Annotation>> annotations, Set<String> packages) {
        this.annotations = annotations;
        this.packages = packages;
        this.scanType = ScanType.LAZY;
    }

    public MethodAnnotationScanner(Set<Class<? extends Annotation>> annotations, Set<String> packages, ScanType scanType) {
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
        if (Objects.isNull(annotatedMethods)) {
            annotatedMethods = AnnotationUtils.loadMethodsFromPackagesWithAnnotations(packages, annotations);
        }
    }

    public Collection<Method> getAnnotatedMethods(Class<? extends Annotation> clz) {
        if (LAZY.equals(scanType)) {
            scan();
        }
        return this.annotatedMethods.get(clz);
    }

    public enum ScanType {
        EAGER, MANUAL, LAZY
    }

}
