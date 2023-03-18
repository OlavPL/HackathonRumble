package com.mygdx.game.utils;

import com.badlogic.gdx.physics.box2d.*;

import static com.mygdx.game.utils.Constants.PPM;

public class Factories {

    public static Body createBox(
            World world,  float x, float y, float width, float height,
            boolean isStatic, boolean fixedRotation, boolean isSensor
    ){
        Body bBody = createBody(world, x,y, isStatic, fixedRotation);
        createFixtureDef(bBody, width, height, isSensor);
        return bBody;
    }

    public static Body createBody(World world,  float x, float y, boolean isStatic, boolean fixedRotation){
        BodyDef bdef = new BodyDef();
        bdef.type = isStatic? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = fixedRotation;
        bdef.position.set(x/Constants.PPM,y/Constants.PPM);
        return  world.createBody(bdef);
    }
    public static void createFixtureDef(Body body, float width, float height, boolean isSensor){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/ Constants.PPM/2, height/ Constants.PPM/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = isSensor;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public static Body createCircle(World world, float posX, float posY, float radius, boolean isStatic, boolean fixedRotation, boolean isSensor){
        Body bBody = createBody(world, posX, posY,isStatic, fixedRotation);
//        BodyDef def = new BodyDef();
//        def.type = isStatic? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
//        def.fixedRotation = fixedRotation;
//        def.position.set(posX/PPM,posY/PPM);

//        bBody = world.createBody(def);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        bBody.createFixture(shape,0f);
        if(isSensor)
            bBody.getFixtureList().get(0).isSensor();
        shape.dispose();

        return bBody;
    }
}
