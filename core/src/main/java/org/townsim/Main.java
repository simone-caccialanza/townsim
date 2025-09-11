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
import jecs.core.EntityManager;
import org.townsimulator.GlobalGrid;
import org.townsimulator.components.*;
import org.townsimulator.game.loader.TSGameLoader;

import static org.townsimulator.game.logic.GameLogicStore.BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter implements InputProcessor {
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private float lastDragX, lastDragY;
    private boolean isDragging = false;
    private int dragButton = -1; // track which button was pressed

    private Texture playerTexture;
    private Sprite playerSprite;
    private SpriteBatch batch;

    private TSGameLoader gLoader;
    private CollisionsGame.CollisionGameLoop gLoop;

    @Override
    public void create() {
        map = new TmxMapLoader().load("C:\\Users\\simon\\IdeaProjects\\townsim\\assets\\maps\\map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 2f); // upscale tiles

        playerTexture = new Texture("black-circle.png");
        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(32, 32);

        batch = new SpriteBatch();

        createCollisionGame();


        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.input.setInputProcessor(this); // Activate input handling
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        gLoop.step();
        playerSprite.setPosition(EntityManager.getComponent(0, Position.Component.class).xPos, EntityManager.getComponent(0, Position.Component.class).yPos);
//        playerSprite.setPosition(960*6/30, 640*11/20);

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        playerSprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        playerTexture.dispose();
        batch.dispose();
    }

    // --- InputProcessor methods ---

    @Override
    public boolean scrolled(float amountX, float amountY) {
        float zoomSpeed = 0.1f;
        camera.zoom += amountY * zoomSpeed;

        // Clamp zoom to reasonable range
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

    // Unused methods (you can return false or ignore them)
    public boolean keyDown(int keycode) {
        float Xstep = 960 / 30;
        float Ystep = 640 / 20;

        switch (keycode) {
            case Input.Keys.W:
                playerSprite.translateY(Ystep);
                break;
            case Input.Keys.S:
                playerSprite.translateY(-Ystep);
                break;
            case Input.Keys.A:
                playerSprite.translateX(-Xstep);
                break;
            case Input.Keys.D:
                playerSprite.translateX(Xstep);
                break;
            case Input.Keys.UP:
                playerSprite.translateY(Ystep);
                break;
            case Input.Keys.DOWN:
                playerSprite.translateY(-Ystep);
                break;
            case Input.Keys.LEFT:
                playerSprite.translateX(-Xstep);
                break;
            case Input.Keys.RIGHT:
                playerSprite.translateX(Xstep);
                break;
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
        EntityManager.createEntity(
            new Movement.Component(32, 32, 960 * 6 / 30, 640 * 11 / 20),
            new Position.Component(960 * 6 / 30, 640 * 11 / 20, true),
            new SpriteASCII.Component('A'),
            new Hunger.Component(100.0f),
            new Task.Component()
        );
        EntityManager.createEntity(
            new Position.Component(960 * 16 / 30, 640 * 5 / 20, true),
            new FoodProvider.Component(1)
        );


        var gLogic = new CollisionsGame.CollisionGameLogic(BASE_LOGIC_MOVEMENT_HUNGER_FOODSUPPLY);

        gLoop = new CollisionsGame.CollisionGameLoop(CollisionsGame.BASE_LOOP_STEP, gLogic);

        GlobalGrid.getInstance().bindSprites();
//        GlobalGrid.getInstance().cellAt(2, 2).movementWeight = 1;
//        GlobalGrid.getInstance().cellAt(2, 3).movementWeight = 1;
//        GlobalGrid.getInstance().cellAt(3, 3).movementWeight = 1;
//        GlobalGrid.getInstance().cellAt(4, 3).movementWeight = 1;
//        GlobalGrid.getInstance().cellAt(5, 3).movementWeight = 1;

        gLoader = new TSGameLoader();
        gLoader.run();
    }
}
