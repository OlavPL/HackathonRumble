package com.mygdx.game.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.projectiles.Snake;
import com.mygdx.game.utils.Factories;

import static com.mygdx.game.utils.Constants.PPM;

public class SnakeSpawner {
    Body body;
    float spawnTimer = 0;
    float radius, angle;
    float snakeMaxDist = 50;

    public SnakeSpawner(World world, float posX, float posY, float angle){
        body = Factories.createBox(world, posX, posY, 5,5,true,true,true);
        body.setUserData(this);
        this.angle = angle;
    }

    public Snake spawnSnake(World world){
        Vector2 dir = getRandQuarterVector(angle);
        return new Snake(world, snakeMaxDist,20,
            body.getPosition().x * PPM + (Snake.WIDTH + radius) * dir.x,
            body.getPosition().y * PPM + (Snake.HEIGHT + radius) * dir.y,
            dir);
    }

    // Gets angle and returns vector with random angle between angle and angle + 90 degrees
    private Vector2 getRandQuarterVector(float angle){
        angle += (float)(90*Math.random());
        Vector2 vec = new Vector2();
        vec.set(MathUtils.cos(MathUtils.degreesToRadians * angle) * 1, MathUtils.sin(MathUtils.degreesToRadians * angle) * 1);
        return vec;
    }
}