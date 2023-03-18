package com.mygdx.game.entities.Pickup;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entities.Player;
import com.mygdx.game.utils.Constants;
import lombok.Getter;

@Getter
    public abstract class PowerUp {
    protected Body body;
    protected String id;
    private final int WIDTH = 16;
    private final int HEIGHT = 15;
    // Felt better with a little smaller hitboxes
    private final int HITBOX = 10;
    private Texture texture;
    float statetime;
    boolean destroy = false;

    public PowerUp(World world, String id, float x, float y, Texture tex){
        createPickupBody(world,x,y);
        this.id = id;
        body.getFixtureList().get(0).setUserData(this);
        texture = tex;
    }

    private void createPickupBody(World world, float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x/Constants.PPM,y/Constants.PPM);
        this.body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(HITBOX/ Constants.PPM / 2, HITBOX/ Constants.PPM /2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = true;

        this.body.createFixture(fixtureDef).setUserData(this);
    }

    public void render(SpriteBatch sBatch){
        sBatch.draw(
                texture,
                body.getPosition().x * Constants.PPM - WIDTH/2,
                body.getPosition().y * Constants.PPM - HEIGHT/2
        );
    }

    public abstract void consume(Player player);

}
