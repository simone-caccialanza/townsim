package org.townsimulator.systems;

import jecs.core.Archetype;
import jecs.core.ArchetypeManager;
import jecs.core.system.ECSSystem;
import org.townsimulator.components.Hunger;

import java.util.Set;
import java.util.Vector;

public class TimeSystem extends ECSSystem<TimeSystem> {

    private TimeSystem() {
        super(TimeSystem.class);
    }

    public static TimeSystem getInstance() {
        return getInstanceOf(TimeSystem.class);
    }

    @Override
    public void run() {
        //TODO parametrize this, hardcoded for demo
        Set<Archetype> allArchetypesWithHunger = ArchetypeManager.allArchetypesWithType(Hunger.Component.class);
        Vector<Hunger.Component> allHungerComponents = new Vector<>(allArchetypesWithHunger.stream()
                .flatMap(archetype ->
                        archetype.getComponentsOfType(Hunger.Component.class).stream())
                .toList()
        );

        allHungerComponents.forEach(hungerComponent -> {
            hungerComponent.hunger += hungerComponent.ratio * 0.01f;
        });
    }
}
