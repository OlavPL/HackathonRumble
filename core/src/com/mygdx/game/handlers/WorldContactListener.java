package com.mygdx.game.handlers;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.entities.Pickup.PowerUp;
import com.mygdx.game.entities.Player;
import com.mygdx.game.entities.projectiles.PlayerProjectile;
import com.mygdx.game.entities.projectiles.Projectile;
import com.mygdx.game.entities.projectiles.Snake;
import com.mygdx.game.entities.Cobra;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        if(fA == null || fB == null) return;
        if(fA.getUserData() == null || fB.getUserData() == null) return;


        if(isPlayer_Cobra(fA, fB)){
            Fixture cobraf = fA.getUserData() instanceof Cobra ? fA : fB;
            Fixture playerf = cobraf == fA ? fB: fA;
            Cobra cobra = (Cobra) cobraf.getUserData();
            Player player = (Player) playerf.getUserData();
            if(cobraf.isSensor()){
                if(cobra.isRetreating())
                    return;

                cobra.setAggressive();
            }
            else {
                player.receiveDamage(cobra.getAttack());
                cobra.retreat();
            }
            return;
        }

        if(isPlayerProjectile_Cobra(fA, fB)){
            Fixture cobraf = fA.getUserData() instanceof Cobra ? fA : fB;
            Fixture projectile = cobraf == fA ? fB: fA;
            Cobra cobra = (Cobra) cobraf.getUserData();
            if(!cobraf.isSensor()){
                cobra.die();
                ((PlayerProjectile) projectile.getUserData()).setDestroy();
            }
            return;
        }

        if(isPlayer_Projectile(fA, fB)){
            Fixture projectile = fA.getUserData() == "Snake" ? fA : fB;
            Fixture player = projectile == fA ? fB: fA;
            if(!(projectile.getUserData().getClass().isAssignableFrom(PlayerProjectile.class))){
                Snake snake = (Snake) projectile.getUserData();
                ((Player)player.getUserData()).receiveDamage(snake.getDamage());
                snake.setDestroy();
                return;
            }
        }

        if(isPlayer_Power(fA, fB)){
            Fixture power = PowerUp.class.isAssignableFrom(fA.getUserData().getClass()) ? fA : fB;
            Fixture player = power == fA ? fB: fA;
            ( (PowerUp) power.getUserData()).consume( ( Player)player.getUserData() );
            return;
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
    private boolean isPlayer_Power(Fixture a, Fixture b){
        if( PowerUp.class.isAssignableFrom(a.getUserData().getClass()) || PowerUp.class.isAssignableFrom(b.getUserData().getClass())){
            return a.getUserData() instanceof Player || b.getUserData() instanceof Player;
        }
        return false;
    }
    private boolean isPlayer_Projectile(Fixture a, Fixture b){
        if( Projectile.class.isAssignableFrom(a.getUserData().getClass()) || Projectile.class.isAssignableFrom(b.getUserData().getClass())){
            return a.getUserData() instanceof Player || b.getUserData() instanceof Player;
        }
        return false;
    }
    private boolean isPlayerProjectile_Cobra(Fixture a, Fixture b){
        if( a.getUserData() instanceof PlayerProjectile || b.getUserData() instanceof PlayerProjectile ){
            return a.getUserData() instanceof Cobra || b.getUserData() instanceof Cobra;
        }
        return false;
    }
    private boolean isPlayer_Cobra(Fixture a, Fixture b){
        if( a.getUserData() instanceof Player || b.getUserData() instanceof Player ){
            return a.getUserData() instanceof Cobra || b.getUserData() instanceof Cobra;
        }
        return false;
    }
}