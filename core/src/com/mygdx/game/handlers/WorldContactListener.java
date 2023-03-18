package com.mygdx.game.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entities.Pickup.PowerUp;
import com.mygdx.game.entities.Player;
import com.mygdx.game.entities.projectiles.PlayerProjectile;
import com.mygdx.game.entities.projectiles.Projectile;
import com.mygdx.game.entities.projectiles.Snake;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        if(fA == null || fB == null) return;
        if(fA.getUserData() == null || fB.getUserData() == null) return;

        if(isPlayerProjectile(fA, fB)){
            Fixture projectile = fA.getUserData() == "Snake" ? fA : fB;
            Fixture player = projectile == fA ? fB: fA;
            if(!(projectile.getUserData().getClass().isAssignableFrom(PlayerProjectile.class))){
                ((Player)player.getUserData()).receiveDamage(((Snake)projectile.getUserData()).getDamage(),"Snake");
            }
            else {
                System.out.println("Player Proj");
            }
        }
        if(isPlayerPower(fA, fB)){
            Fixture power = PowerUp.class.isAssignableFrom(fA.getUserData().getClass()) ? fA : fB;
            Fixture player = power == fA ? fB: fA;
            ( (PowerUp) power.getUserData()).consume( ( Player)player.getUserData() );
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
    private boolean isPlayerPower(Fixture a, Fixture b){
        if( PowerUp.class.isAssignableFrom(a.getUserData().getClass()) || PowerUp.class.isAssignableFrom(b.getUserData().getClass())){
            if(a.getUserData() instanceof Player || b.getUserData() instanceof Player){
                return true;
            }
        }
        return false;
    }
    private boolean isPlayerProjectile(Fixture a, Fixture b){
        if( Projectile.class.isAssignableFrom(a.getUserData().getClass()) || Projectile.class.isAssignableFrom(b.getUserData().getClass())){
            if(a.getUserData() instanceof Player || b.getUserData() instanceof Player){
                return true;
            }
        }
        return false;
    }
}
