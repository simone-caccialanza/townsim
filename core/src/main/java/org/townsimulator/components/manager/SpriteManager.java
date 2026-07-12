package org.townsimulator.components.manager;

import com.badlogic.gdx.graphics.g2d.Sprite;
import jecs.core.World;
import org.townsimulator.TownSimWorld;
import org.townsimulator.components.TSSprite;

import java.util.ArrayList;
import java.util.List;

public class SpriteManager {
    private SpriteManager() {
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
