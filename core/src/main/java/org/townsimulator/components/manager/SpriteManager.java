package org.townsimulator.components.manager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import jecs.core.World;
import org.townsimulator.TownSimWorld;
import org.townsimulator.components.TSSprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpriteManager {
    private SpriteManager() {
    }

    public static Sprite[] createSprite(List<String> paths) {
        List<Texture> textures = new ArrayList<>();
        Sprite[] sprites = paths.stream()
            .flatMap(path -> {
                Texture texture = new Texture(path);
                textures.add(texture);
                TextureRegion[][] regions = TextureRegion.split(texture, 512, 512);
                return Arrays.stream(regions)
                    .flatMap(Arrays::stream)
                    .map(textureRegion -> {
                        Sprite sprite = new Sprite(textureRegion);
                        sprite.setSize(64, 64);
                        return sprite;
                    });
            })
            .toArray(Sprite[]::new);
        return sprites;
    }

    public static List<Sprite> getActiveSprites() {
        return getActiveSprites(TownSimWorld.get());
    }

    public static List<Sprite> getActiveSprites(World world) {
        List<Sprite> activeSprites = new ArrayList<>();
        for (int entityId : world.query(TSSprite.Component.class)) {
            TSSprite.Component component = world.getComponent(entityId, TSSprite.Component.class);
            if (component != null && component.activeSprite != null) {
                activeSprites.add(component.activeSprite);
            }
        }
        return activeSprites;
    }
}
