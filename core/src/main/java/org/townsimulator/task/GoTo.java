package org.townsimulator.task;

import jecs.core.Archetype;
import jecs.core.EntityManager;
import org.townsimulator.components.Movement;

public final class GoTo extends TaskAction {

    private final Integer xDst;
    private final Integer yDst;

    public GoTo(Integer xDst, Integer yDst) {
        this.xDst = xDst;
        this.yDst = yDst;
    }

    @Override
    public void action(int entityId) {
        var movementComponent = EntityManager.getComponent(entityId, Movement.Component.class);
        if (movementComponent.xVel <= 0.0f) {
            movementComponent.xVel = 1.0f;
        }
        if (movementComponent.yVel <= 0.0f) {
            movementComponent.yVel = 1.0f;
        }
        movementComponent.xDst = xDst;
        movementComponent.yDst = yDst;
        movementComponent.wantsToMove = true;
    }

    public static class Properties {
        Archetype archetype;
        Integer xDst;
        Integer yDst;
    }

}
