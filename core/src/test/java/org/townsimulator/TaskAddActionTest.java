package org.townsimulator;

import org.junit.jupiter.api.Test;
import org.townsimulator.components.Task;
import org.townsimulator.task.GoTo;
import org.townsimulator.task.TaskAction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskAddActionTest {

    @Test
    void addActionAcceptsFirstActionWithPriority() {
        var task = new Task.Component();

        assertTrue(task.addAction(new GoTo(1, 2)));
        assertEquals(1, task.actionQueue.size());
    }

    @Test
    void addActionRejectsDuplicatePriority() {
        var task = new Task.Component();
        task.addAction(new GoTo(1, 2));

        assertFalse(task.addAction(new GoTo(3, 4)));
        assertEquals(1, task.actionQueue.size());
    }

    @Test
    void addActionAcceptsDifferentPriorities() {
        var task = new Task.Component();

        assertTrue(task.addAction(new GoTo(1, 2)));
        assertTrue(task.addAction(new HighPriorityGoTo(3, 4)));
        assertEquals(2, task.actionQueue.size());
    }

    private static final class HighPriorityGoTo extends TaskAction {
        private final int x;
        private final int y;

        private HighPriorityGoTo(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void action(jecs.core.World world, int entityId) {
        }

        @Override
        public Integer getPriority() {
            return 1;
        }
    }
}
