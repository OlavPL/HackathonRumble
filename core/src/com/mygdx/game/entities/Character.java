package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.Factories;
import lombok.Getter;

import static com.mygdx.game.utils.Constants.PPM;
@Getter
public abstract class Character {
    // body
    protected World world;
    protected Body body;
    protected Texture texture;
    protected int width = 12;
    protected  int height = 16;
    protected final float TEXTUREOFFSET = 2f;

    // movement
    protected float speed = 10;
    protected final double DIAGONAL_MULTI = Math.cos(Math.PI/4);
    protected float leftMove, rightMove, upMove, downMove, xMovement, yMovement;
    protected Vector2 lastDirection = new Vector2();

    //status
    protected float maxHealth = 10;
    protected float health = maxHealth;

    public Character(World world, float posX, float posY, int width, int height, Texture texture){
        this.world = world;
        this.width = width;
        this.height= height;
        body = Factories.createBody(world,posX, posY,false, true);
        Factories.createFixtureDef(body, width, height,false);
        body.getFixtureList().get(0).setUserData(this);
        this.texture = texture; //new Texture("sprites/characterSprites/SingularMouse.png");
    }

    public abstract void update(float delta);
    public abstract void render(SpriteBatch sBatch);

    protected void updateMovement(){
        xMovement = 0;
        yMovement = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            xMovement -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            xMovement += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            yMovement += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            yMovement -= 1;
        }


        if(xMovement !=0 && yMovement!= 0){
            setLinearVelocity((float)(xMovement * speed * DIAGONAL_MULTI) , (float)(yMovement * speed * DIAGONAL_MULTI));
        }
        else
            setLinearVelocity(xMovement * speed, yMovement * speed);
    }

    public void receiveDamage(float damage, String source){
        health-= damage;
        System.out.println("Player took "+ (int)damage +" damage from "+ source);
    }

    protected void setLinearVelocity(float x, float y){
        body.setLinearVelocity(x,y);
    }
    public Vector2 getPosition(){return body.getPosition();}

}
