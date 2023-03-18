package com.mygdx.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entities.Character;
import com.mygdx.game.entities.Player;
import com.mygdx.game.utils.Constants;
import lombok.Getter;

import static com.mygdx.game.utils.Constants.PPM;

@Getter
public class Cobra extends Character {
    float AGGRO_RANGE = 50;
    boolean aggressive = false;
    private final static int WIDTH = 16;
    private final static int HEIGHT = 16;
    private final int FRAME_COL = 8;
    private final int FRAME_ROW = 3;
    private final static float SPEED = 7;
    private Body aggroRange;
    private float stateTime;
    boolean destroy = false;
    private final Player player;

    //stats
    private final float POINTS_VALUE = 5;

    // "AI"
    private final float WANDER_DURATION  = 2;
    private float wanderCountdown = 0;
    private boolean wandering = false;
    private Vector2 WanderVector = new Vector2(0,0);


    public Cobra(World world, float posX, float posY, Player player) {
        super(world, posX, posY, WIDTH, HEIGHT, new Texture("sprites/characterSprites/cobraSheet.png"));
        this.player = player;
        attack = 2;
        speed = SPEED;
        createCobraSensor(world,posX, posY);

        // Split up sprite sheet and make arrays for the different animations
        TextureRegion[][] tmp = TextureRegion.split(texture,WIDTH, HEIGHT);
        moveLeftFrames = new TextureRegion[FRAME_COL];
        moveRightFrames = new TextureRegion[FRAME_COL];
        moveDownFrames = new TextureRegion[FRAME_COL];
        moveUpFrames = new TextureRegion[FRAME_COL];
        for (int i = 0; i < FRAME_COL; i++) {
            moveRightFrames[i] = tmp[0][i];
            moveLeftFrames[i] = tmp[0][i];
            moveDownFrames[i] = tmp[1][i];
            moveUpFrames[i] = tmp[2][i];
        }

        moveRightAnim = new Animation<>(0.1f,moveLeftFrames);
        moveLeftAnim = new Animation<>(0.1f,moveRightFrames);
        moveDownAnim = new Animation<>(0.1f,moveDownFrames);
        moveUpAnim = new Animation<>(0.1f,moveUpFrames);


    }
    private void createCobraSensor(World world, float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x/ Constants.PPM,y/Constants.PPM);
        this.aggroRange = world.createBody(bdef);

        CircleShape shape = new CircleShape();
        shape.setRadius(AGGRO_RANGE / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.isSensor = true;

        this.body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void update(float delta) {
        stateTime += delta;
        if(wandering){
            wanderCountdown += delta;
            setLinearVelocity(getWanderVector().x * speed/2, getWanderVector().y * speed/2);

            //Turn back to aggressive and reset wander when wander is ran out
            if(wanderCountdown >= WANDER_DURATION){
                wandering = false;
                aggressive = true;
                wanderCountdown = 0;
            }
        }
        else if(aggressive){
//            Get point between player and Cobra and set movement towards that point
            moveToPlayer();
        }
    }

    @Override
    public void render(SpriteBatch sBatch) {
        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = selectFrame();

        sBatch.draw(currentFrame, body.getPosition().x * PPM - (float)WIDTH/2, body.getPosition().y * PPM - (float)HEIGHT/2);
    }



    public void die(){
        destroy = true;
        player.addPoints(POINTS_VALUE);
    }
    public void setAggressive(){
        aggressive = true;
    }

    public void wander(){
        wandering = true;
//        float angle = MathUtils.atan2(body.getPosition().y + player.getPosition().y,body.getPosition().y + player.getPosition().y);
        float angle = MathUtils.atan2(
                player.getPosition().y+ body.getPosition().y,
                player.getPosition().x - body.getPosition().x
        );
        Vector2 vec = new Vector2().set(MathUtils.cos( angle ) * 1, MathUtils.sin( angle) * 1). nor();
        setWanderVector(vec);
    }

    public void setWanderVector(Vector2 wanderVector) {
        WanderVector = wanderVector;
    }

    private void moveToPlayer(){
        float angle = MathUtils.atan2(
                player.getPosition().y - body.getPosition().y,
                player.getPosition().x - body.getPosition().x
        );
        Vector2 vec = new Vector2().set(MathUtils.cos( angle) * 1, MathUtils.sin( angle) * 1). nor();
        setLinearVelocity(vec.x * speed, vec.y * speed);
    }
}
