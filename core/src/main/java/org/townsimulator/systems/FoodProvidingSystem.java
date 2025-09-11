package org.townsimulator.systems;

import jecs.core.ArchetypeManager;
import jecs.core.EntityManager;
import jecs.core.system.ECSSystem;
import org.townsimulator.components.FoodProvider;
import org.townsimulator.components.Hunger;
import org.townsimulator.components.Position;
import org.townsimulator.components.SeekingFood;

import java.util.List;

public class FoodProvidingSystem extends ECSSystem<FoodProvidingSystem> {

    private FoodProvidingSystem() {
        super(FoodProvidingSystem.class);
    }

    public static FoodProvidingSystem getInstance() {
        return getInstanceOf(FoodProvidingSystem.class);
    }

    private static void decreaseHunger(Integer entityId, Hunger.Component hungerComponent, List<FoodProvider.Component> foodProvider, int i) {
        if (hungerComponent.hunger < foodProvider.get(i).foodSupply) {
            hungerComponent.hunger = 0;
            hungerComponent.isHungry = false;
            EntityManager.removeComponent(entityId, SeekingFood.Component.class);
        } else {
            hungerComponent.hunger -= foodProvider.get(i).foodSupply;
            if (hungerComponent.hunger <= 1) {
                hungerComponent.isHungry = false;
                EntityManager.removeComponent(entityId, SeekingFood.Component.class);
            }
        }
    }

    @Override
    public void run() {
        var archetypesWithFoodProvider = ArchetypeManager.allArchetypesWithType(FoodProvider.Component.class, Position.Component.class);
        archetypesWithFoodProvider.forEach(archetype -> {
            var foodProvider = archetype.getComponentsOfType(FoodProvider.Component.class);
            if (foodProvider.isEmpty()) return;
            var foodProviderPosition = archetype.getComponentsOfType(Position.Component.class);
            if (foodProvider.isEmpty() || foodProviderPosition.isEmpty()) {
                return;
            }
            var entities = EntityManager.getEntitiesWith(Hunger.Component.class, Position.Component.class);
            if (foodProvider.isEmpty() || foodProviderPosition.isEmpty()) {
                return;
            }

            for (int i = 0; i < foodProvider.size(); i++) {
                for (Integer entityId : entities) {
                    var hungerComponent = EntityManager.getComponent(entityId, Hunger.Component.class);
                    var positionComponent = EntityManager.getComponent(entityId, Position.Component.class);
                    if (hungerComponent == null || positionComponent == null) continue;
                    if (hungerComponent.isHungry && foodProviderPosition.get(i).xPos == positionComponent.xPos && foodProviderPosition.get(i).yPos == positionComponent.yPos) {
                        decreaseHunger(entityId, hungerComponent, foodProvider, i);
                    }
                    if (!hungerComponent.isHungry) {

                    }
                }
            }
        });
    }
}
