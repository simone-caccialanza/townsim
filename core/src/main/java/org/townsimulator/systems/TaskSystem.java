package org.townsimulator.systems;

import jecs.core.ArchetypeManager;
import jecs.core.system.ECSSystem;
import org.townsimulator.components.Task;

import static org.townsimulator.components.Task.Status.*;

public class TaskSystem extends ECSSystem<TaskSystem> {

    private TaskSystem() {
        super(TaskSystem.class);
    }

    public static TaskSystem getInstance() {
        return getInstanceOf(TaskSystem.class);
    }

    @Override
    public void run() {
        var archetypesWithTask = ArchetypeManager.allArchetypesWithType(Task.Component.class);
        archetypesWithTask.forEach(archetype -> {
            var tasks = archetype.getComponentsOfType(Task.Component.class);
            for (int i = 0; i < tasks.size(); i++) {
                var task = tasks.get(i);
                if (task == null) continue;
                if (task.status.equals(RUNNING) || task.actionQueue.isEmpty()) continue;
                if (task.status.equals(WAITING)) {
                    if (task.actionQueue.peek() != null) {
                        task.actionQueue.peek().action(archetype.getEntityIdFromIndex(i));
                        task.status = RUNNING;
                    }
                    continue;
                }
                if (task.status.equals(FINISHED)) {
                    task.actionQueue.poll();
                    task.status = WAITING;
                }
            }
        });
    }
}
