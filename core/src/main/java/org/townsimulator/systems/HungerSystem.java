package org.townsimulator.systems;

import jecs.core.World;
import jecs.core.system.ECSSystem;
import org.townsimulator.components.FoodProvider;
import org.townsimulator.components.Hunger;
import org.townsimulator.components.Position;
import org.townsimulator.components.SeekingFood;
import org.townsimulator.components.Task;
import org.townsimulator.task.GoTo;

import java.util.ArrayList;
import java.util.List;

public class HungerSystem extends ECSSystem {

    @Override
    public void run(World world, double deltaSeconds) {
        List<Integer> entitiesToAssignTasks = new ArrayList<>();

        for (int entityId : world.query(Hunger.Component.class)) {
            Hunger.Component hungerComponent = world.getComponent(entityId, Hunger.Component.class);
            if (hungerComponent == null) {
                continue;
            }

            hungerComponent.hunger += hungerComponent.ratio * 0.01f;
            if (hungerComponent.hunger > 10) {
                hungerComponent.isHungry = true;
                if (world.hasComponent(entityId, SeekingFood.Component.class)) {
                    continue;
                }
                entitiesToAssignTasks.add(entityId);
            }
        }

        List<Position.Component> allFoodProvidersPositions = new ArrayList<>();
        for (int entityId : world.query(Position.Component.class, FoodProvider.Component.class)) {
            Position.Component position = world.getComponent(entityId, Position.Component.class);
            if (position != null) {
                allFoodProvidersPositions.add(position);
            }
        }

        for (int entityId : entitiesToAssignTasks) {
            Position.Component entityPosition = world.getComponent(entityId, Position.Component.class);
            if (entityPosition == null) {
                continue;
            }

            PairFloat nearest = findNearestPosition(entityPosition, allFoodProvidersPositions);
            if (nearest == null) {
                continue;
            }

            Task.Component taskComponent = world.getComponent(entityId, Task.Component.class);
            if (taskComponent == null) {
                taskComponent = new Task.Component();
                world.addComponent(entityId, taskComponent);
            }
            taskComponent.addAction(new GoTo((int) nearest.x, (int) nearest.y));
            world.addComponent(entityId, new SeekingFood.Component());
        }
    }

    private PairFloat findNearestPosition(Position.Component start, List<Position.Component> candidates) {
        double minDistance = Double.MAX_VALUE;
        Position.Component nearestCandidate = null;

        for (Position.Component candidate : candidates) {
            double distance = Math.sqrt(Math.pow(candidate.xPos - start.xPos, 2) + Math.pow(candidate.yPos - start.yPos, 2));
            if (distance < minDistance) {
                minDistance = distance;
                nearestCandidate = candidate;
            }
        }

        if (nearestCandidate != null) {
            return new PairFloat(nearestCandidate.xPos, nearestCandidate.yPos);
        }
        return null;
    }

    private record PairFloat(float x, float y) {
    }
}
