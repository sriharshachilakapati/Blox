package com.shc.blox.states;

import com.shc.blox.entities.Floor;
import com.shc.blox.entities.Player;
import com.shc.silenceengine.collision.colliders.DynamicSceneCollider3D;
import com.shc.silenceengine.collision.colliders.ISceneCollider3D;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.cameras.PerspCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.lights.PointLight;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    private Scene scene;
    private PerspCam camera;
    private Player player;

    private PointLight camLight;

    private ISceneCollider3D collider;

    public PlayState()
    {
        camera = new PerspCam().initProjection(70, Display.getAspectRatio(), 0.01f, 100f);

        scene = new Scene();
        for (int x = -5; x < 6; x++)
        {
            for (int z = -5; z < 6; z++)
                scene.addChild(new Floor(new Vector3(x, 0, z)));
        }

        scene.addChild(player = new Player(new Vector3(0, 5, 0)));
        scene.addComponent(camLight = new PointLight(player.getPosition(), Color.WHITE));
        scene.init();

        collider = new DynamicSceneCollider3D();
        collider.setScene(scene);

        collider.register(Player.class, Floor.class);

        camera.setPosition(new Vector3(0, 4, 4));
        camera.lookAt(Vector3.ZERO);
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.end();

        scene.update(delta);
        collider.checkCollisions();

        Vector3 temp = Vector3.REUSABLE_STACK.pop();

        camera.setPosition(temp.set(player.getPosition()).addSelf(0, 2, 3));
        camera.lookAt(temp.set(player.getPosition()).subtractSelf(0, 0, 3));

        camLight.setPosition(camera.getPosition());

        Vector3.REUSABLE_STACK.push(temp);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        camera.apply();
        scene.render(delta, batcher);
    }

    @Override
    public void resize()
    {
        camera.initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
    }
}
