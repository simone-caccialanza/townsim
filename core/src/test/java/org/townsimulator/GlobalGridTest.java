package org.townsimulator;

import jecs.core.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.townsimulator.components.Position;
import org.townsimulator.components.SpriteASCII;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalGridTest {

    private World world;

    @BeforeEach
    void setUp() {
        TownSimWorld.resetForTests();
        world = TownSimWorld.create();
    }

    @Test
    void bindSpritesPlacesAsciiCharactersOnGrid() {
        world.createEntity(new Position.Component(2, 3), new SpriteASCII.Component('X'));

        GlobalGrid.getInstance().bindSprites(world);

        assertEquals('X', GlobalGrid.getInstance().cellAt(2, 3).spriteCharacter);
    }

    @Test
    void setBlockedMarksCellAsImpassable() {
        GlobalGrid grid = GlobalGrid.getInstance();

        grid.setBlocked(5, 5, true);

        assertTrue(grid.isBlocked(5, 5));
    }

    @Test
    void resetForTestsClearsSingleton() {
        GlobalGrid grid = GlobalGrid.getInstance();
        grid.setBlocked(1, 1, true);

        GlobalGrid.resetForTests();

        assertFalse(GlobalGrid.getInstance().isBlocked(1, 1));
    }
}
