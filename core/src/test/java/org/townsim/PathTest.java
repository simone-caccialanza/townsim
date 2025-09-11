package org.townsim;

import org.junit.jupiter.api.Test;

class PathTest {
    @Test
    void pathTest() {
        Class<?> clazz = jecs.core.Game.class;
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println("Libreria caricata da: " + path);
    }
}
