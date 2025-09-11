package org.townsimulator.systems;

import jecs.core.Archetype;
import jecs.core.ArchetypeManager;
import jecs.core.EntityManager;
import jecs.core.system.ECSSystem;
import org.townsimulator.components.*;
import org.townsimulator.task.GoTo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HungerSystem extends ECSSystem<HungerSystem> {

    private HungerSystem() {
        super(HungerSystem.class);
    }

    public static HungerSystem getInstance() {
        return getInstanceOf(HungerSystem.class);
    }

    @Override
    public void run() {

        // Step 1: Accumula gli entityId che devono ricevere un task (non modificare ancora niente)
        List<Integer> entitiesToAssignTasks = new ArrayList<>();

        Set<Archetype> allArchetypesWithHunger = ArchetypeManager.allArchetypesWithType(Hunger.Component.class);

        for (Archetype archetype : allArchetypesWithHunger) {
            List<Hunger.Component> hungerComponents = archetype.getComponentsOfType(Hunger.Component.class);
            for (int i = 0; i < hungerComponents.size(); i++) {
                if (hungerComponents.get(i) == null) continue;
                var hungerComponent = hungerComponents.get(i);

                hungerComponent.hunger += hungerComponent.ratio * 0.01f;
                System.out.println("Component " + hungerComponent + "has hunger: " + hungerComponent.hunger);
                if (hungerComponent.hunger > 10) {
                    hungerComponent.isHungry = true;
                    var entityId = archetype.getEntityIdFromIndex(i);
                    if (EntityManager.getComponent(entityId, SeekingFood.Component.class) != null) {
                        // Se ha già un task, non fare nulla
                        System.out.println("Entity " + entityId + " is already seeking food");
                        continue;
                    }
                    // Non bisogna assegnare task a chi ne ha già uno
                    System.out.println("Assign task to component" + hungerComponent);
                    entitiesToAssignTasks.add(archetype.getEntityIdFromIndex(i));
                }
            }

        }

        // Step 2: Per ciascun entityId che ha fame, assegna il task "GoTo" verso il food provider più vicino
        Set<Archetype> allArchsWithPositionAndFoodProvider = ArchetypeManager.allArchetypesWithType(
                Position.Component.class, FoodProvider.Component.class
        );
        List<Position.Component> allFoodProvidersPositions = allArchsWithPositionAndFoodProvider.stream()
                .flatMap(arch -> arch.getComponentsOfType(Position.Component.class).stream())
                .toList();

        for (int entityId : entitiesToAssignTasks) {
            Position.Component entityPosition = EntityManager.getComponent(entityId, Position.Component.class);
            if (entityPosition == null) continue;

            PairFloat nearest = findNearestPosition(entityPosition, allFoodProvidersPositions);
            if (nearest == null) continue;

            Task.Component taskComponent = EntityManager.getComponent(entityId, Task.Component.class);
            if (taskComponent == null) {
                taskComponent = new Task.Component();
                EntityManager.addComponent(entityId, taskComponent);
            }
            taskComponent.actionQueue.add(new GoTo((int) nearest.x, (int) nearest.y));
            EntityManager.addComponent(entityId, new SeekingFood.Component());
        }
    }

    private PairFloat findNearestPosition(Position.Component start, List<Position.Component> candidates) {
        double minDistance = Double.MAX_VALUE;
        Position.Component nearestCandidate = null;

        // Loop through all candidates
        for (Position.Component candidate : candidates) {
            // Calculate the Euclidean distance
            double distance = Math.sqrt(Math.pow(candidate.xPos - start.xPos, 2) + Math.pow(candidate.yPos - start.yPos, 2));

            // If this candidate is closer, update the minimum distance and nearest candidate
            if (distance < minDistance) {
                minDistance = distance;
                nearestCandidate = candidate;
            }
        }

        // Return the nearest candidate's position as PairInt (x, y)
        if (nearestCandidate != null) {
            return new PairFloat(nearestCandidate.xPos, nearestCandidate.yPos);
        }

        // If no candidates are provided, return null or handle the case
        return null;
    }

    private record PairFloat(float x, float y) {
    }
}
