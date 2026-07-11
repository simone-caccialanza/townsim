package org.annotationlib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationUtilsClasspathTest {

    @Test
    void classpathFilterTargetsTownsimProject() {
        assertFalse(AnnotationUtils.FILTERED_CLASS_PATH_ENTRIES.isEmpty());
        assertTrue(AnnotationUtils.FILTERED_CLASS_PATH_ENTRIES.stream()
                .anyMatch(entry -> entry.contains("townsim")));
    }
}
