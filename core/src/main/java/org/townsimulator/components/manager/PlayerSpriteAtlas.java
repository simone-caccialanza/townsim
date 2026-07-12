package org.townsimulator.components.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.townsimulator.graphics.CharacterAnimState;
import org.townsimulator.graphics.Direction4;

public final class PlayerSpriteAtlas {

    public static final int FRAME_SIZE = 32;
    public static final int COLUMNS = CharacterAnimState.IDLE_FRAMES + CharacterAnimState.WALK_FRAMES;
    public static final int ROWS = 3;

    private static PlayerSpriteAtlas instance;

    private final Texture texture;
    private final TextureRegion[][] regions = new TextureRegion[ROWS][COLUMNS];

    private PlayerSpriteAtlas() {
        texture = new Texture(Gdx.files.internal("characters/player/player.png"));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        for (Direction4 direction : new Direction4[]{Direction4.N, Direction4.S, Direction4.W}) {
            int row = direction.sheetRow();
            for (int col = 0; col < COLUMNS; col++) {
                int x = col * FRAME_SIZE;
                int y = (ROWS - 1 - row) * FRAME_SIZE;
                regions[row][col] = new TextureRegion(texture, x, y, FRAME_SIZE, FRAME_SIZE);
            }
        }
    }

    public static PlayerSpriteAtlas get() {
        if (instance == null) {
            instance = new PlayerSpriteAtlas();
        }
        return instance;
    }

    public static void dispose() {
        if (instance != null) {
            instance.texture.dispose();
            instance = null;
        }
    }

    public static void resetForTests() {
        dispose();
    }

    public TextureRegion getRegion(Direction4 direction, CharacterAnimState state, int frameIndex) {
        int row = direction.sheetRow();
        int col = state.frameOffset() + Math.floorMod(frameIndex, state.frameCount());
        return regions[row][col];
    }
}
