package org.townsim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import org.townsimulator.GlobalGrid;
import org.townsimulator.TownSimWorld;
import org.townsimulator.components.*;
import org.townsimulator.components.manager.SpriteManager;
import org.townsimulator.game.loader.TSGameLoader;

import java.util.List;

import static org.townsimulator.game.logic.GameLogicStore.BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY;
import static org.townsimulator.game.loop.GameLoopStore.SINGLE_FRAME_LOOP;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter implements InputProcessor {
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private float lastDragX, lastDragY;
    private boolean isDragging = false;
    private int dragButton = -1;

    private Texture playerTexture;
    private Sprite playerSprite;
    private SpriteBatch batch;

    private TSGameLoader gLoader;
    private CollisionsGame.CollisionGameLoop gLoop;

    @Override
    public void create() {
        map = new TmxMapLoader().load("C:\\Users\\simon\\IdeaProjects\\townsim\\assets\\maps\\map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 2f);

        playerTexture = new Texture("black-circle.png");
        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(32, 32);

        batch = new SpriteBatch();

        createCollisionGame();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        gLoop.step();

        var world = TownSimWorld.get();
        int playerEntityId = TownSimWorld.playerEntityId();
        if (playerEntityId >= 0) {
            var position = world.getComponent(playerEntityId, Position.Component.class);
            if (position != null) {
                playerSprite.setPosition(position.xPos, position.yPos);
            }
        }

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        SpriteManager.getActiveSprites(world).forEach(sprite -> sprite.draw(batch));
        batch.end();
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        playerTexture.dispose();
        batch.dispose();
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        float zoomSpeed = 0.1f;
        camera.zoom += amountY * zoomSpeed;
        camera.zoom = Math.max(0.5f, Math.min(camera.zoom, 3f));
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.RIGHT) {
            lastDragX = screenX;
            lastDragY = screenY;
            isDragging = true;
            dragButton = button;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == dragButton) {
            isDragging = false;
            dragButton = -1;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDragging && dragButton == Input.Buttons.RIGHT) {
            float dx = screenX - lastDragX;
            float dy = screenY - lastDragY;

            camera.position.x -= dx * camera.zoom;
            camera.position.y += dy * camera.zoom;

            lastDragX = screenX;
            lastDragY = screenY;
        }
        return true;
    }

    public boolean keyDown(int keycode) {
        float Xstep = 960 / 30;
        float Ystep = 640 / 20;

        switch (keycode) {
            case Input.Keys.W, Input.Keys.UP -> playerSprite.translateY(Ystep);
            case Input.Keys.S, Input.Keys.DOWN -> playerSprite.translateY(-Ystep);
            case Input.Keys.A, Input.Keys.LEFT -> playerSprite.translateX(-Xstep);
            case Input.Keys.D, Input.Keys.RIGHT -> playerSprite.translateX(Xstep);
            default -> {
                return false;
            }
        }
        return true;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    void createCollisionGame() {
        var world = TownSimWorld.create();

        var sprites = SpriteManager.createSprite(
            List.of("blue_man_walking_left.png",
                "blue_man_walking_right.png",
                "blue_man_walking_vertical.png")
        );
        var tsSpriteComponent = new TSSprite.Component(12);
        tsSpriteComponent.setSprites(sprites);

        var player = world.createEntity(
            new Movement.Component(32, 32, 960 * 6 / 30, 640 * 11 / 20),
            new Position.Component(960 * 6 / 30, 640 * 11 / 20, true),
            new SpriteASCII.Component('A'),
            tsSpriteComponent,
            new Hunger.Component(100.0f),
            new Task.Component()
        );
        TownSimWorld.setPlayerEntityId(player.id());

        world.createEntity(
            new Position.Component(960 * 16 / 30, 640 * 5 / 20, true),
            new FoodProvider.Component(1)
        );

        var gLogic = new CollisionsGame.CollisionGameLogic(BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY);
        gLoop = new CollisionsGame.CollisionGameLoop(SINGLE_FRAME_LOOP, gLogic);

        GlobalGrid.getInstance().bindSprites(world);

        gLoader = new TSGameLoader();
        gLoader.run();
    }
}
