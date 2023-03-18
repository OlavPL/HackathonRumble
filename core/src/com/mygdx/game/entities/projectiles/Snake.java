package com.mygdx.game.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import lombok.Getter;

@Getter

public class Snake extends AnimatedProjectile{
    protected Vector2 direction;
    public static final int WIDTH = 16;
    public static final int HEIGHT = 15;
    public static final float RADIUS = (float)WIDTH / 2;
    public float damage = 1;

//    private static final Texture texture = new Texture("sprites/boulder.png");
    private static final Texture texture = new Texture("sprites/characterSprites/snakeWalk.png");
    public Snake(World world, float maxDist, float speed, float x, float y, Vector2 direction) {
        super(world, RADIUS, false, maxDist, speed, texture,x, y, WIDTH, HEIGHT);
        this.direction = direction;
    }

    @Override
    public void update(float delta) {
        body.setLinearVelocity(direction.x * speed,direction.y * speed);
        distanceTraveled += speed * delta;

        if(distanceTraveled >= MAX_DISTANCE)
            destroy = true;

        updateAnimation(delta);
    }

    public void bounce(){
        System.out.println("bounce");
    }
}
