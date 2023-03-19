package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.Factories;
import com.mygdx.game.utils.Utils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Character {
    // body
    protected World world;
    protected Body body;
    protected int width = 12;
    protected int height = 16;

    // movement
    protected float speed = 10;
    protected final double DIAGONAL_MULTI = Math.cos(Math.PI/4);
    protected Vector2 lastDirection = new Vector2();

    //stats
    protected float maxHealth = 10;
    protected float health = maxHealth;
    protected float attack = 1;


    // animation
    protected Texture spriteSheet;
    protected Animation<TextureRegion> moveRightAnim, moveLeftAnim, moveUpAnim, moveDownAnim, idleAnimation;

    protected TextureRegion currentFrame;
    protected int frameColumns;
    protected int frameRows;
    protected float stateTime;

    public Character(World world, float posX, float posY, int width, int height, Texture spriteSheet, int frameRows, int frameColumns){
        this.world = world;
        this.width = width;
        this.height= height;
        body = Factories.createBody(world,posX, posY,false, true);
        Factories.createFixtureDef(body, width, height,false);
        body.getFixtureList().get(0).setUserData(this);
        this.spriteSheet = spriteSheet;
        this.frameColumns = frameColumns;
        this.frameRows = frameRows;

        Animation<TextureRegion>[] animations = Utils.createMovementAnimations(spriteSheet, frameColumns, frameRows);
        for (int i = 0; i < animations.length; i++) {
            switch (i){
                case 0: moveRightAnim = animations[i];
                case 1: moveLeftAnim = animations[i];
                case 2: moveUpAnim = animations[i];
                case 3: moveDownAnim = animations[i];
            }
        }
        stateTime = 0;
        currentFrame = getCurrentFrame();
    }

    public abstract void update(float delta);
    public abstract void render(SpriteBatch sBatch);



    public void receiveDamage(float damage){
        health-= damage;
    }

    protected void setLinearVelocity(float x, float y){
        body.setLinearVelocity(x,y);
    }
    public Vector2 getPosition(){return body.getPosition();}

    public TextureRegion selectFrame(){
        if( Math.abs(getBody().getLinearVelocity().x) > Math.abs(getBody().getLinearVelocity().y ) ){
            if(getBody().getLinearVelocity().x < 0) {
                return moveLeftAnim.getKeyFrame(stateTime, true);
            }

            return moveRightAnim.getKeyFrame(stateTime,true);
        }
        else {
            if (getBody().getLinearVelocity().y > 0)
                return moveUpAnim.getKeyFrame(stateTime, true);

            moveDownAnim.getKeyFrame(stateTime, true);
        }

        if(idleAnimation != null)
            return idleAnimation.getKeyFrame(stateTime,true);

        return moveDownAnim.getKeyFrame(stateTime, true);
    }

}
