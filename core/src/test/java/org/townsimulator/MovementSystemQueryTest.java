package org.townsimulator;

import jecs.core.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.townsimulator.components.Movement;
import org.townsimulator.components.Position;
import org.townsimulator.components.SpriteASCII;
import org.townsimulator.systems.MovementSystem;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MovementSystemQueryTest {

    private World world;

    @BeforeEach
    void setUp() {
        TownSimWorld.resetForTests();
        world = TownSimWorld.create();
    }

    @Test
    void movesEntitiesWithoutTextureSprite() {
        var entity = world.createEntity(
                new Movement.Component(1, 1, 2, 0),
                new Position.Component(0, 0),
                new SpriteASCII.Component('E')
        );
        var movement = world.getComponent(entity.id(), Movement.Component.class);
        movement.wantsToMove = true;
        movement.xDst = 2;
        movement.yDst = 0;

        GlobalGrid.getInstance().bindSprites(world);

        new MovementSystem().run(world, 0.5);

        var position = world.getComponent(entity.id(), Position.Component.class);
        assertNotEquals(0f, position.xPos);
    }
}
