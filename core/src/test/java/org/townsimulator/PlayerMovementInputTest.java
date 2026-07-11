package org.townsimulator;

import jecs.core.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.townsimulator.components.Position;
import org.townsimulator.components.SpriteASCII;
import org.townsimulator.input.PlayerMovementInput;
import org.townsimulator.input.PlayerMovementInput.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerMovementInputTest {

    private World world;
    private int playerEntityId;

    @BeforeEach
    void setUp() {
        TownSimWorld.resetForTests();
        world = TownSimWorld.create();
        var player = world.createEntity(
                new Position.Component(10, 10, true),
                new SpriteASCII.Component('P')
        );
        playerEntityId = player.id();
        GlobalGrid.getInstance().bindSprites(world);
    }

    @Test
    void applyMovesPlayerPositionInEcs() {
        assertTrue(PlayerMovementInput.apply(world, playerEntityId, Direction.RIGHT));

        var position = world.getComponent(playerEntityId, Position.Component.class);
        assertEquals(10f + PlayerMovementInput.X_STEP, position.xPos);
    }

    @Test
    void applyUpdatesGlobalGridCell() {
        PlayerMovementInput.apply(world, playerEntityId, Direction.UP);

        assertEquals('P', GlobalGrid.getInstance().cellAt(10, (int) (10 + PlayerMovementInput.Y_STEP)).spriteCharacter);
    }

    @Test
    void applyReturnsFalseForInvalidEntity() {
        assertFalse(PlayerMovementInput.apply(world, -1, Direction.UP));
    }
}
