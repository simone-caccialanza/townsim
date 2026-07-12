package org.townsimulator;

import org.junit.jupiter.api.Test;
import org.townsimulator.components.manager.PlayerSpriteAtlas;
import org.townsimulator.graphics.CharacterAnimState;
import org.townsimulator.graphics.Direction4;

import javax.imageio.ImageIO;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlayerSpriteAtlasTest {

    @Test
    void atlasLayoutMatchesSpritesheet() throws Exception {
        assertEquals(6, PlayerSpriteAtlas.COLUMNS);
        assertEquals(3, PlayerSpriteAtlas.ROWS);
        assertEquals(32, PlayerSpriteAtlas.FRAME_SIZE);
        assertEquals(0, CharacterAnimState.IDLE.frameOffset());
        assertEquals(CharacterAnimState.IDLE_FRAMES, CharacterAnimState.WALK.frameOffset());
        assertEquals(Direction4.W.sheetRow(), Direction4.E.sheetRow());
    }

    @Test
    void spritesheetMatchesAtlasLayout() throws Exception {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("characters/player/player.png")) {
            assertNotNull(input);
            var image = ImageIO.read(input);
            assertEquals(PlayerSpriteAtlas.COLUMNS * PlayerSpriteAtlas.FRAME_SIZE, image.getWidth());
            assertEquals(PlayerSpriteAtlas.ROWS * PlayerSpriteAtlas.FRAME_SIZE, image.getHeight());
        }
    }
}
