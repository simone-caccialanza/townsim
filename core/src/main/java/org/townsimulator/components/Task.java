package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.townsimulator.task.TaskAction;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.townsimulator.components.Task.Status.WAITING;

public final class Task {

    private Task() {
    }

    public enum Status {
        WAITING, RUNNING, FINISHED
    }

    public enum Type {
        GO_TO
    }

    @JecsComponent
    public static final class Component extends ComponentBase {

        public Queue<TaskAction> actionQueue;

        public Status status;

        public Component() {
            this.actionQueue = new PriorityQueue<>(10, Comparator.comparing(TaskAction::getPriority));
            this.status = WAITING;
        }

        @Override
        protected void reset() {
            this.actionQueue.clear();
        }

        public boolean addAction(TaskAction action) {
            if (actionQueue.parallelStream().filter(a -> a.getPriority().equals(action.getPriority())).toList().isEmpty()) {
                return false;
            }
            return actionQueue.add(action);
        }
    }
}
