package com.mygdx.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.HackathonRumble;
import com.mygdx.game.utils.Constants;

public class Hud {
    public Stage stage;
    private Viewport viewport;
    private int kills;
    private Label pointsStatus;
    private Label lifeStatus;

    public Hud(SpriteBatch sBatch){
        viewport = new FitViewport(HackathonRumble.W_WIDTH, HackathonRumble.W_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sBatch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        pointsStatus = new Label("Points "+ String.format("%03d",0), Constants.skin);
        lifeStatus = new Label("Lives: "+ String.format("%02d",10), Constants.skin);
        table.add(pointsStatus);
        table.row().bottom();
        table.add(lifeStatus);
        stage.addActor(table);
    }

    public void updateScore(int n){pointsStatus.setText("Points "+ String.format("%03d",n));}
    public void updateLives(int n){lifeStatus.setText("Lives: "+ String.format("%02d",n));}

}
