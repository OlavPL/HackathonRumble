package com.mygdx.game.entities.Pickup;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.Player;

public class PPHeal extends PowerUp{
    private static final Texture texture = new Texture("sprites/healCheese.png");
    private final int HEAL_POTENCY = 2;
    public PPHeal(World world, String id, float x, float y) {
        super(world, id, x, y, PPHeal.texture);
    }

    public void consume(Player player){
        destroy = true;
        player.heal(HEAL_POTENCY);
    }

}
