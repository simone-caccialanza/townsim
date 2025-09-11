package org.townsimulator.components;


import jecs.core.ArchetypeManager;
import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import static jecs.core.utils.GlobalConstants.MAP_WIDTH;
import static org.townsimulator.utils.Constants.GRID_COMPONENT_TABLE_INDEX;

public final class Grid {

    private Grid() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {

        @LogField
        public Cell[] grid;

        public Component(int width, int length) {
            this.grid = new Cell[width * length];
            init();
        }

        public static int tableIndex() {
            return GRID_COMPONENT_TABLE_INDEX;
        }

        @Override
        public void reset() {
            this.grid = new Cell[grid.length];
        }

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public void init() {
            for (int i = 0; i < grid.length; i++) {
                grid[i] = new Cell();
            }
            var all = ArchetypeManager.allArchetypesWithType(Position.Component.class, SpriteASCII.Component.class);
            all.forEach(archetype -> {
                var positionComponents = archetype.getComponentsOfType(Position.Component.class);
                var spriteComponents = archetype.getComponentsOfType(SpriteASCII.Component.class);
                for (int i = 0; i < positionComponents.size(); i++) {
                    var pos = positionComponents.get(i);
                    var sprite = spriteComponents.get(i);
                    grid[((int) pos.yPos * MAP_WIDTH + (int) pos.xPos)].spriteCharacter = sprite.spriteCharacter;
                }
            });
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }

    public static class Cell {
        public Character spriteCharacter;
        short movementWeight;

        public Cell() {
            this.movementWeight = 1;
        }
    }
}
