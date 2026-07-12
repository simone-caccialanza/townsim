package org.townsimulator;

import org.junit.jupiter.api.Test;
import org.townsimulator.graphics.Direction4;
import org.townsimulator.input.PlayerMovementInput;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Direction4Test {

    @Test
    void fromDeltaMapsCardinalDirections() {
        assertEquals(Direction4.N, Direction4.fromDelta(0, 1));
        assertEquals(Direction4.S, Direction4.fromDelta(0, -1));
        assertEquals(Direction4.E, Direction4.fromDelta(1, 0));
        assertEquals(Direction4.W, Direction4.fromDelta(-1, 0));
    }

    @Test
    void fromDeltaCollapsesDiagonalsToDominantAxis() {
        assertEquals(Direction4.E, Direction4.fromDelta(2, 1));
        assertEquals(Direction4.W, Direction4.fromDelta(-2, 1));
        assertEquals(Direction4.N, Direction4.fromDelta(1, 2));
        assertEquals(Direction4.S, Direction4.fromDelta(1, -2));
    }

    @Test
    void fromKeyboardMapsWASD() {
        assertEquals(Direction4.N, Direction4.fromKeyboard(PlayerMovementInput.Direction.UP));
        assertEquals(Direction4.S, Direction4.fromKeyboard(PlayerMovementInput.Direction.DOWN));
        assertEquals(Direction4.W, Direction4.fromKeyboard(PlayerMovementInput.Direction.LEFT));
        assertEquals(Direction4.E, Direction4.fromKeyboard(PlayerMovementInput.Direction.RIGHT));
    }
}
