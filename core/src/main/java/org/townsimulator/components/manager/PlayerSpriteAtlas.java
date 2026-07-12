package org.townsimulator.components.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.townsimulator.graphics.Direction4;

public final class PlayerSpriteAtlas {

    public static final int FRAME_SIZE = 32;
    public static final int COLUMNS = 3;

    private static PlayerSpriteAtlas instance;

    private final Texture texture;
    private final TextureRegion[] regions = new TextureRegion[COLUMNS];

    private PlayerSpriteAtlas() {
        texture = new Texture(Gdx.files.internal("characters/player/player.png"));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        for (int column = 0; column < COLUMNS; column++) {
            int x = column * FRAME_SIZE;
            regions[column] = new TextureRegion(texture, x, 0, FRAME_SIZE, FRAME_SIZE);
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

    public TextureRegion getRegion(Direction4 direction) {
        return regions[direction.sheetColumn()];
    }
}
