package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.EllipseShapeBuilder;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class TiledUtils {

    // Get all objects in specified layer, for each PolygonObject -> create object in World
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
