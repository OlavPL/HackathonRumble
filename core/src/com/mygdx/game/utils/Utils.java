package com.mygdx.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entities.Pickup.PowerUp;
import com.mygdx.game.entities.Player;
import com.mygdx.game.entities.projectiles.Projectile;
import com.mygdx.game.screens.Cobra;

import java.io.*;
import java.nio.file.Files;
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
                c.getTexture().dispose();
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

    public static void serialize(ArrayList<HighScore> scores, int points){
        if(scores.size() >=5 ){
            scores.sort(new Comparator<HighScore>() {
                @Override
                public int compare(HighScore o1, HighScore o2) {
                    return o2.getScore() - o1.getScore();
                }
            });
            if(points > scores.get(scores.size()-1).getScore()) {
                scores.remove(scores.size() - 1);
                scores.add(new HighScore(points));
                scores.sort(new Comparator<HighScore>() {
                    @Override
                    public int compare(HighScore o1, HighScore o2) {
                        return o2.getScore() - o1.getScore();
                    }
                });
            }
        }
        else {
            scores.add(new HighScore(points));
            scores.sort(new Comparator<HighScore>() {
                @Override
                public int compare(HighScore o1, HighScore o2) {
                    return o2.getScore() - o1.getScore();
                }
            });
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.SCORE_FILE_PATH))){
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<HighScore> deSerialize(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Constants.SCORE_FILE_PATH))){
            ArrayList<HighScore> scores = (ArrayList<HighScore>) ois.readObject();
            System.out.println("Scores Inc");
            for (HighScore hs : scores) {
                System.out.println("Score: "+hs.getScore());
            }
            return scores;
        } catch (IOException | ClassNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        return new ArrayList<>();
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
}
