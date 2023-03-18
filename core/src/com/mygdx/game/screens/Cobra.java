package com.mygdx.game.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entities.Character;
import com.mygdx.game.entities.Player;
import com.mygdx.game.utils.Constants;
import com.mygdx.game.utils.Factories;
import lombok.Getter;
import lombok.Setter;

import static com.mygdx.game.utils.Constants.PPM;

@Getter
@Setter
public class Cobra extends Character {
    float AGGRO_RANGE = 50;
    boolean aggressive = false;
    private final static int WIDTH = 15;
    private final static int HEIGHT = 15;
    private final static Texture texture = new Texture("");
    Player player;
    Body aggroRange;
    Animation<TextureRegion> movementAnimation;

    public Cobra(World world, float posX, float posY, Player player) {
        super(world, posX, posY, WIDTH, HEIGHT, texture);
        this.player = player;
        createCobraSensor(world,posX, posY);
    }
    private void createCobraSensor(World world, float x, float y){
        BodyDef bdef = new BodyDef();
        bdef.fixedRotation = true;
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x/ Constants.PPM,y/Constants.PPM);
        this.aggroRange = world.createBody(bdef);

        CircleShape shape = new CircleShape();
        shape.setRadius(AGGRO_RANGE/ Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.isSensor = true;

        this.body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch sBatch) {
        sBatch.draw(texture,getPosition().x * PPM - texture.getWidth()/2 - TEXTUREOFFSET, getPosition().y * PPM - texture.getHeight()/2);

    }
}
