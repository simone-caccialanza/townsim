package org.townsimulator;

import jecs.core.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.townsimulator.components.Age;
import org.townsimulator.components.Grid;
import org.townsimulator.components.Hunger;
import org.townsimulator.components.Movement;
import org.townsimulator.components.Name;
import org.townsimulator.components.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EcsWorldTest {

    private World world;

    @BeforeEach
    void setUp() {
        TownSimWorld.resetForTests();
        world = TownSimWorld.create();
    }

    @Test
    void createEntityStoresComponents() {
        var entity = world.createEntity(
                new Movement.Component(1, 2, 7, 8),
                new Position.Component(3, 4)
        );

        assertTrue(world.isEntityAlive(entity.id()));
        assertEquals(3f, world.getComponent(entity.id(), Position.Component.class).xPos);
        assertEquals(4f, world.getComponent(entity.id(), Position.Component.class).yPos);
        assertEquals(7f, world.getComponent(entity.id(), Movement.Component.class).xDst);
    }

    @Test
    void queryFindsMatchingArchetypes() {
        world.createEntity(new Age.Component(40), new Name.Component("Simone", "sss"), new Hunger.Component());
        world.createEntity(new Age.Component(30), new Name.Component("Albano", "aaa"));
        world.createEntity(new Age.Component(20), new Hunger.Component());
        world.createEntity(new Grid.Component(100, 100, world));

        assertEquals(2, world.query(Age.Component.class, Name.Component.class).entityIds().size());
        assertEquals(3, world.query(Age.Component.class).entityIds().size());
    }

    @Test
    void addComponentExtendsEntitySignature() {
        var entity = world.createEntity(new Position.Component(1, 2));
        assertNotNull(world.getComponent(entity.id(), Position.Component.class));

        world.addComponent(entity.id(), new Hunger.Component());
        assertTrue(world.hasComponent(entity.id(), Hunger.Component.class));
        assertTrue(world.hasComponent(entity.id(), Position.Component.class));
    }
}
