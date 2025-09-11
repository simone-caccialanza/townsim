package org.townsimulator;

import jecs.core.ArchetypeManager;
import org.annotationlib.annotations.PostInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.townsimulator.components.Position;
import org.townsimulator.components.SpriteASCII;

import static java.lang.System.nanoTime;
import static jecs.core.utils.GlobalConstants.MAP_LENGTH;
import static jecs.core.utils.GlobalConstants.MAP_WIDTH;
import static org.townsimulator.utils.Constants.PROFILE_FILE_LOGGER;

public class GlobalGrid {
    private static final Logger log = LoggerFactory.getLogger(GlobalGrid.class);
    private static final Logger profiler = LoggerFactory.getLogger(PROFILE_FILE_LOGGER);

    public static final short DEFAULT_MOVEMENT_WEIGHT = 100;
    public static final short MAX_MOVEMENT_WEIGHT = Short.MAX_VALUE;

    private static Cell[] grid;
    private static GlobalGrid instance;

    private static void init() {
        log.debug("Start GlobalGrid initialization...");
        long t0 = nanoTime();
        grid = new Cell[MAP_WIDTH * MAP_LENGTH];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = new Cell();
        }
        long deltaT = nanoTime() - t0;
        log.debug("Initialization finished in {}s", (deltaT / 1_000_000d));
        profiler.info("GlobalGrid static initialization: {}s", (deltaT / 1_000_000d));
    }

    private GlobalGrid() {
    }

    public static GlobalGrid getInstance() {
        if (instance == null) {
            instance = new GlobalGrid();
            init();
        }
        return instance;
    }

    @PostInit
    public void bindSprites() {
        profiler.info("");
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

    public boolean isBlocked(int x, int y) {
        if (x < 0 || y < 0 || x >= MAP_WIDTH || y >= MAP_LENGTH) return true;
        if (grid[y * MAP_WIDTH + x].isBlockedByEntity) return true;
        Cell cell = cellAt(x, y);
        return cell == null || cell.movementWeight >= MAX_MOVEMENT_WEIGHT;
    }

    public void setBlocked(int x, int y, boolean blocked) {
        if (x < 0 || y < 0 || x >= MAP_WIDTH || y >= MAP_LENGTH) return;
        grid[y * MAP_WIDTH + x].isBlockedByEntity = blocked;
    }

    public Cell cellAt(int x, int y) {
        if (x < 0 || y < 0 || x >= MAP_WIDTH || y >= MAP_LENGTH) return null;
        return grid[y * MAP_WIDTH + x];
    }

    public static class Cell {
        public Character spriteCharacter;
        public short movementWeight;
        public boolean isBlockedByEntity;

        public Cell() {
            this.movementWeight = DEFAULT_MOVEMENT_WEIGHT;
            this.isBlockedByEntity = false;
        }
    }
}
