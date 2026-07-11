package org.townsimulator.systems;

import jecs.core.World;
import jecs.core.system.ECSSystem;
import org.townsimulator.components.FoodProvider;
import org.townsimulator.components.Hunger;
import org.townsimulator.components.Position;
import org.townsimulator.components.SeekingFood;

public class FoodProvidingSystem extends ECSSystem {

    @Override
    public void run(World world, double deltaSeconds) {
        for (int providerEntityId : world.query(FoodProvider.Component.class, Position.Component.class)) {
            var foodProvider = world.getComponent(providerEntityId, FoodProvider.Component.class);
            var foodProviderPosition = world.getComponent(providerEntityId, Position.Component.class);
            if (foodProvider == null || foodProviderPosition == null) {
                continue;
            }

            for (int entityId : world.getEntitiesWith(Hunger.Component.class, Position.Component.class)) {
                var hungerComponent = world.getComponent(entityId, Hunger.Component.class);
                var positionComponent = world.getComponent(entityId, Position.Component.class);
                if (hungerComponent == null || positionComponent == null) {
                    continue;
                }
                if (hungerComponent.isHungry
                        && foodProviderPosition.xPos == positionComponent.xPos
                        && foodProviderPosition.yPos == positionComponent.yPos) {
                    decreaseHunger(world, entityId, hungerComponent, foodProvider);
                }
            }
        }
    }

    private static void decreaseHunger(World world, int entityId, Hunger.Component hungerComponent, FoodProvider.Component foodProvider) {
        if (hungerComponent.hunger < foodProvider.foodSupply) {
            hungerComponent.hunger = 0;
            hungerComponent.isHungry = false;
            world.removeComponent(entityId, SeekingFood.Component.class);
        } else {
            hungerComponent.hunger -= foodProvider.foodSupply;
            if (hungerComponent.hunger <= 1) {
                hungerComponent.isHungry = false;
                world.removeComponent(entityId, SeekingFood.Component.class);
            }
        }
    }
}
