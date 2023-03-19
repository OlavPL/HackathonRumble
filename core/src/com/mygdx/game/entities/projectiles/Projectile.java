package com.mygdx.game.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utils.Factories;
import lombok.Getter;

@Getter
public abstract class Projectile {
    protected final float MAX_DISTANCE;
    protected float distanceTraveled;
    protected float speed;
    protected Texture texture;
    protected float x,y;
    protected boolean destroy = false;
    Body body;

    public Projectile(World world, float radius, boolean isStatic, boolean rotationFixed, float maxDist, float speed, Texture texture, float x, float y){
        MAX_DISTANCE = maxDist;
        distanceTraveled = 0;
        this.speed = speed;
        this.texture = texture;
        this.x = x;
        this.y = y;
        body = Factories.createCircle(world, x,y, radius, isStatic, rotationFixed, false);
        body.getFixtureList().get(0).setUserData(this);
        body.setLinearDamping(1f);
    }

    public void update(float delta){
        body.applyForceToCenter(speed,0,false);
        distanceTraveled += speed * delta;
        if(distanceTraveled >=MAX_DISTANCE)
            destroy = true;
    }

    public void destroy(World world){
        world.destroyBody(body);
    }

    public void render(SpriteBatch batch){
        batch.draw(texture,x,y);
    }
    public void setDestroy(){destroy = true;}
    public abstract void dispose();

}
