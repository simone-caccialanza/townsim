package org.townsimulator;

import jecs.core.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.townsimulator.components.*;

import static jecs.core.utils.GlobalConstants.MAP_LENGTH;
import static jecs.core.utils.GlobalConstants.MAP_WIDTH;

class BuildingsTests {

    @BeforeAll
    static void beforeAll() {
//        TSGameLoader.run();
        //TODO must create some special entities each time (like the grid), should put them somewhere
        EntityManager.createEntity(
                new Grid.Component(MAP_WIDTH, MAP_LENGTH)
        );
    }

    @Test
    @DisplayName("Hungry entity goes to a building that provides food and feeds")
    void hungryEntityGoesToBuildingThatProvidesFoodAndFeeds() {
        EntityManager.createEntity(
                new Movement.Component(1, 1),
                new Position.Component(0, 0),
                new Hunger.Component(1, 100),
                new SpriteASCII.Component('E')
        );
        EntityManager.createEntity(
                new Position.Component(5, 7),
                new FoodProvider.Component(2),
                new SpriteASCII.Component('B', 5, 7)
        );


//        GameLoop.run();
    }

}