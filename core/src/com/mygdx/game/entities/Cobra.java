package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.Constants;
import lombok.Getter;

import static com.mygdx.game.utils.Constants.PPM;

@Getter
public class Cobra extends Character {
    float AGGRO_RANGE = 50;
    boolean aggressive = false;
    private final static int WIDTH = 16;
    private final static int HEIGHT = 16;

    private final static float SPEED = 7;
    private Body aggroSensor;
    boolean destroy = false;
    private final Player player;

    //stats
    private final float POINTS_VALUE = 5;

    // "AI"
    private final float RETREAT_DURATION  = 2;
    private float retreatCountdown = 0;
    private boolean retreating = false;
    private Vector2 retreatVector = new Vector2(0,0);


    public Cobra(World world, float posX, float posY, Player player) {
        super(world, posX, posY, WIDTH, HEIGHT, new Texture("sprites/characterSprites/cobraSheet.png"),3, 8);
        this.player = player;
        attack = 2;
        speed = SPEED;
        createCobraSensor(world,posX, posY);
        idleAnimation = new Animation<>(0.4f,moveRightAnim.getKeyFrames());
    }

    @Override
    public void update(float delta) {
        stateTime += delta;
        if(aggressive){
//            Get point between player and Cobra and set movement towards that point
            moveToPlayer();
        }
        if(retreating){
            retreatCountdown += delta;
            setLinearVelocity(getRetreatVector().x * speed/2, getRetreatVector().y * speed/2);

            //Turn back to aggressive and reset retreat when retreat is ran out
            if(retreatCountdown >= RETREAT_DURATION){
                retreating = false;
                aggressive = true;
                retreatCountdown = 0;
            }
        }

        // Get current frame of animation for the current stateTime
        currentFrame = selectFrame();
    }

    @Override
    public void render(SpriteBatch sBatch) {
        sBatch.draw(currentFrame, body.getPosition().x * PPM - (float)WIDTH/2, body.getPosition().y * PPM - (float)HEIGHT/2);
    }

    public void die(){
        destroy = true;
        player.addPoints(POINTS_VALUE);
    }
    public void setAggressive(){
        aggressive = true;
    }

    public void retreat(){
        retreating = true;
//        float angle = MathUtils.atan2(body.getPosition().y + player.getPosition().y,body.getPosition().y + player.getPosition().y);
        float angle = MathUtils.atan2(
                body.getPosition().y - player.getPosition().y,
                body.getPosition().x - player.getPosition().x
        );
        Vector2 vec = new Vector2().set(MathUtils.cos( angle ) * 1, MathUtils.sin( angle) * 1). nor();
        setRetreatVector(vec);
    }

    public void setRetreatVector(Vector2 retreatVector) {
        this.retreatVector = retreatVector;
    }

    private void moveToPlayer(){
        float angle = MathUtils.atan2(
                player.getPosition().y - body.getPosition().y,
                player.getPosition().x - body.getPosition().x
        );
        Vector2 vec = new Vector2().set(MathUtils.cos( angle) * 1, MathUtils.sin( angle) * 1). nor();
        setLinearVelocity(vec.x * speed, vec.y * speed);
    }

    private void createCobraSensor(World world, float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x/ Constants.PPM,y/Constants.PPM);
        this.aggroSensor = world.createBody(bdef);

        CircleShape shape = new CircleShape();
        shape.setRadius(AGGRO_RANGE / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.isSensor = true;

        this.body.createFixture(fixtureDef).setUserData(this);
    }
}
