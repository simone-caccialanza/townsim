package org.townsimulator.systems;

import jecs.core.World;
import jecs.core.system.ECSSystem;
import org.townsimulator.components.Hunger;

public class TimeSystem extends ECSSystem {

    @Override
    public void run(World world, double deltaSeconds) {
        for (int entityId : world.query(Hunger.Component.class)) {
            Hunger.Component hungerComponent = world.getComponent(entityId, Hunger.Component.class);
            if (hungerComponent != null) {
                hungerComponent.hunger += hungerComponent.ratio * 0.01f;
            }
        }
    }
}
