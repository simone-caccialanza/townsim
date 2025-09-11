package org.townsimulator.task;

public abstract class TaskAction {

    private final Integer priority = 0;

    public abstract void action(int entityId);

    public Integer getPriority() {
        return this.priority;
    }
}

