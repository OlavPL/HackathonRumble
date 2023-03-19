package com.mygdx.game.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utils.Constants;

public abstract class AnimatedProjectile extends Projectile{
    protected static final float FRAME_LENGTH = 0.1f;
    protected final int HEIGHT;
    protected final int WIDTH;
    private Animation<TextureRegion> animation = null;
    float statetime;

    public AnimatedProjectile(World world, float radius, boolean rotationFixed, float maxDist,
                              float speed, Texture texture, float x, float y, int width, int height) {
        super(world, radius, false, rotationFixed, maxDist, speed, texture, x, y);
        HEIGHT = height;
        WIDTH = width;
        statetime = 0;

        if(animation == null){
            animation = new Animation<>(FRAME_LENGTH,TextureRegion.split(texture, WIDTH,HEIGHT)[0]);
        }
    }

    @Override
    public abstract void update(float delta);

    public void updateAnimation (float delta){
        statetime += delta;
    }

    @Override
    public void render(SpriteBatch batch){
        batch.draw(
                animation.getKeyFrame(statetime,true),
                body.getPosition().x * Constants.PPM - WIDTH/2,
                body.getPosition().y * Constants.PPM - HEIGHT/2
        );
    }
    @Override
    public void dispose(){
        texture.dispose();
    }
}
