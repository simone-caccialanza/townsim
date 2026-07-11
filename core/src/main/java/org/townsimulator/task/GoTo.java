package org.townsimulator.task;

import jecs.core.World;
import org.townsimulator.components.Movement;

public final class GoTo extends TaskAction {

    private final Integer xDst;
    private final Integer yDst;

    public GoTo(Integer xDst, Integer yDst) {
        this.xDst = xDst;
        this.yDst = yDst;
    }

    @Override
    public void action(World world, int entityId) {
        var movementComponent = world.getComponent(entityId, Movement.Component.class);
        if (movementComponent == null) {
            return;
        }
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
}
