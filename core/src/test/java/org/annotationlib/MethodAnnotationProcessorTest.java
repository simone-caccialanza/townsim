package org.annotationlib;

import org.annotationlib.annotations.PostInit;
import org.junit.jupiter.api.Test;

import java.util.Set;

class MethodAnnotationProcessorTest {

    @Test
    void callPostInit() {
        MethodAnnotationProcessor processor = new MethodAnnotationProcessor();
        processor.addAnnotations(PostInit.class);
        processor.addScanners(
                new MethodAnnotationScanner(Set.of(PostInit.class), Set.of("org.townsimulator"), MethodAnnotationScanner.ScanType.EAGER)
        );
        processor.process();
    }

}