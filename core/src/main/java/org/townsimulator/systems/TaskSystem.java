package org.townsimulator.systems;

import jecs.core.World;
import jecs.core.system.ECSSystem;
import org.townsimulator.components.Task;

import static org.townsimulator.components.Task.Status.*;

public class TaskSystem extends ECSSystem {

    @Override
    public void run(World world, double deltaSeconds) {
        for (int entityId : world.query(Task.Component.class)) {
            var task = world.getComponent(entityId, Task.Component.class);
            if (task == null) {
                continue;
            }
            if (task.status.equals(RUNNING) || task.actionQueue.isEmpty()) {
                continue;
            }
            if (task.status.equals(WAITING)) {
                if (task.actionQueue.peek() != null) {
                    task.actionQueue.peek().action(world, entityId);
                    task.status = RUNNING;
                }
                continue;
            }
            if (task.status.equals(FINISHED)) {
                task.actionQueue.poll();
                task.status = WAITING;
            }
        }
    }
}
