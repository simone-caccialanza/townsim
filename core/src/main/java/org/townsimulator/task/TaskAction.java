package org.townsimulator.task;

import jecs.core.World;

public abstract class TaskAction {

    private final Integer priority = 0;

    public abstract void action(World world, int entityId);

    public Integer getPriority() {
        return this.priority;
    }
}
