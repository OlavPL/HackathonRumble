package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entities.Pickup.PowerUp;
import com.mygdx.game.entities.projectiles.Projectile;
import com.mygdx.game.entities.Cobra;

import java.io.*;
import java.util.*;

public class Utils {

    public static void iterateProjectiles(World world, float delta, ArrayList<Projectile> projectiles){
        for(Iterator<Projectile> iter = projectiles.iterator(); iter.hasNext(); ) {
            Projectile p = iter.next();
            p.update(delta);
            if(p.isDestroy()) {
                world.destroyBody(p.getBody());
                iter.remove();
            }
        }
    }
    public static void iterateCobras(World world, float delta, ArrayList<Cobra> cobras){
        for(Iterator<Cobra> iter = cobras.iterator(); iter.hasNext(); ) {
            Cobra c = iter.next();
            c.update(delta);
            if(c.isDestroy()) {
                world.destroyBody(c.getBody());
                c.getSpriteSheet().dispose();
                iter.remove();
            }
        }
    }
    public static void cleanPowerUps(World world, ArrayList<PowerUp> powers){
        for(Iterator<PowerUp> iter = powers.iterator(); iter.hasNext(); ) {
            PowerUp p = iter.next();
            if(p.isDestroy()) {
                world.destroyBody(p.getBody());
                iter.remove();
            }
        }
    }


    public static void parseTiledObjectLayer(World world, MapObjects objects){
        for (MapObject o : objects) {
            if( !(o instanceof PolygonMapObject))
                continue;

            Shape shape = createPolygon( (PolygonMapObject)o);
            BodyDef bDef = new BodyDef();
            bDef.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(bDef);
            body.createFixture(shape,1.0f);
            shape.dispose();
        }
    }

    // Create a Shape from all the points on a Tiled PolygonMapObject
    private static ChainShape createPolygon(PolygonMapObject object){
        float[] vertices = object.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2 +1];
        for (int i = 0; i < worldVertices.length-1; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2+1 ] / Constants.PPM);
            if(i == worldVertices.length-2){
                worldVertices[i+1] = new Vector2(vertices[0] / Constants.PPM, vertices[1] / Constants.PPM);
            }
        }
        ChainShape cs = new ChainShape();
        cs.createChain(worldVertices);
        return cs;
    }

    /**
     *
     * @param spriteSheet Sprite sheet for animations
     * @param frameColumns columns in sprite sheet
     * @param frameRows rows in sprite sheet
     * @return returns a list Animation<TextureRegion></> for movement in order -> right, left, up down
     */
    public static Animation<TextureRegion>[] createMovementAnimations(Texture spriteSheet, int frameColumns, int frameRows){
        //         Split up sprite sheet and make arrays for the different animations
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,spriteSheet.getWidth()/frameColumns, spriteSheet.getHeight()/frameRows);
        TextureRegion[]   moveLeftFrames = new TextureRegion[frameColumns];
        TextureRegion[]   moveRightFrames = new TextureRegion[frameColumns];
        TextureRegion[]   moveDownFrames = new TextureRegion[frameColumns];
        TextureRegion[]   moveUpFrames = new TextureRegion[frameColumns];
        for (int i = 0; i < frameColumns; i++) {
            moveRightFrames[i] = tmp[0][i];

            moveLeftFrames [i] = new TextureRegion(tmp[0][i]);
            moveLeftFrames [i].flip(true,false);

            moveDownFrames [i] = tmp[1][i];
            moveUpFrames   [i] = tmp[2][i];

        }

        return  new Animation[]{
                new Animation<>(0.2f,moveRightFrames),
                new Animation<>(0.2f,moveLeftFrames),
                new Animation<>(0.2f,moveUpFrames),
                new Animation<>(0.2f,moveDownFrames)
        };
    }
}
