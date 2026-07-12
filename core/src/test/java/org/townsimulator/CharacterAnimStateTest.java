package org.townsimulator;

import org.junit.jupiter.api.Test;
import org.townsimulator.graphics.CharacterAnimState;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterAnimStateTest {

    @Test
    void idleUsesFirstTwoColumns() {
        assertEquals(2, CharacterAnimState.IDLE.frameCount());
        assertEquals(0, CharacterAnimState.IDLE.frameOffset());
    }

    @Test
    void walkUsesNextFourColumns() {
        assertEquals(4, CharacterAnimState.WALK.frameCount());
        assertEquals(CharacterAnimState.IDLE_FRAMES, CharacterAnimState.WALK.frameOffset());
    }
}
