package org.townsimulator;

import jecs.core.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.townsimulator.components.Movement;
import org.townsimulator.components.Position;
import org.townsimulator.components.SpriteASCII;
import org.townsimulator.components.Task;
import org.townsimulator.systems.MovementSystem;
import org.townsimulator.systems.TaskSystem;
import org.townsimulator.task.GoTo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.townsimulator.components.Task.Status.RUNNING;
import static org.townsimulator.components.Task.Status.WAITING;

class TaskLifecycleTest {

    private World world;

    @BeforeEach
    void setUp() {
        TownSimWorld.resetForTests();
        world = TownSimWorld.create();
    }

    @Test
    void movementCompletionAdvancesTaskQueue() {
        var entity = world.createEntity(
                new Movement.Component(1, 1, 3, 3),
                new Position.Component(3, 3),
                new SpriteASCII.Component('E'),
                new Task.Component()
        );
        var task = world.getComponent(entity.id(), Task.Component.class);
        task.addAction(new GoTo(3, 3));
        task.addAction(new GoTo(5, 5));

        new TaskSystem().run(world, 0.5);
        assertEquals(RUNNING, task.status);

        new MovementSystem().run(world, 0.5);
        new TaskSystem().run(world, 0.5);

        assertEquals(WAITING, task.status);
        assertEquals(1, task.actionQueue.size());
        assertTrue(world.getComponent(entity.id(), Movement.Component.class).wantsToMove);
    }
}
