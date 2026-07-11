package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.World;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;
import org.townsimulator.TownSimWorld;

import static org.townsimulator.utils.Constants.MAP_WIDTH;

public final class Grid {

    private Grid() {
    }

    @JecsComponent
    public static final class Component extends ComponentBase {

        @LogField
        public Cell[] grid;

        public Component(int width, int length) {
            this.grid = new Cell[width * length];
            init(TownSimWorld.get());
        }

        public Component(int width, int length, World world) {
            this.grid = new Cell[width * length];
            init(world);
        }

        @Override
        protected void reset() {
            this.grid = new Cell[grid.length];
        }

        public void init(World world) {
            for (int i = 0; i < grid.length; i++) {
                grid[i] = new Cell();
            }
            for (int entityId : world.query(Position.Component.class, SpriteASCII.Component.class)) {
                var pos = world.getComponent(entityId, Position.Component.class);
                var sprite = world.getComponent(entityId, SpriteASCII.Component.class);
                if (pos == null || sprite == null) {
                    continue;
                }
                grid[((int) pos.yPos * MAP_WIDTH + (int) pos.xPos)].spriteCharacter = sprite.spriteCharacter;
            }
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
