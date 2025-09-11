package org.townsimulator.utils;

import org.townsimulator.GlobalGrid;

import static jecs.core.utils.GlobalConstants.MAP_LENGTH;
import static jecs.core.utils.GlobalConstants.MAP_WIDTH;

public class PrintUtils {

    private PrintUtils() {
    }

    public static void printGridASCII() {
        for (int y = 0; y < MAP_LENGTH; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                System.out.print("[");
                try {
                    if (GlobalGrid.getInstance().cellAt(x, y).spriteCharacter != null) {
                        System.out.print(GlobalGrid.getInstance().cellAt(x, y).spriteCharacter);
                    } else {
                        System.out.print(" ");
                    }
                } catch (Exception e) {
                    System.out.println("FAILED TO PRINT CELL AT (" + x + ", " + y + ")");
                    throw new RuntimeException(e);
                }
                System.out.print("]");
            }
            System.out.println();
        }
    }
}
