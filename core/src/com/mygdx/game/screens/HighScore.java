package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.HackathonRumble;
import com.mygdx.game.utils.Utils;

import java.util.ArrayList;

import static com.mygdx.game.utils.Constants.skin;

public class HighScore implements Screen {
    HackathonRumble parent;
    Stage stage;
    ArrayList<com.mygdx.game.utils.HighScore> scores;
    public HighScore(HackathonRumble parent){
        this.parent = parent;
        stage = new Stage(new ScreenViewport());
        scores = Utils.deSerialize();

    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("Top 5 High Scores",skin);

        TextButton mainMenu = new TextButton("Main Menu",skin);

        table.add(title).fillX().uniformX();
        table.row().pad(20,0,20,0);
        for (com.mygdx.game.utils.HighScore hs : scores) {
            table.add(new Label("Score: "+hs.getScore()+", time:"+hs.getDate(),skin)).fillX().uniformX();
            table.row().pad(20,0,20,0);
        }
        table.add(mainMenu).fillX().uniformX();

        stage.getViewport().update(HackathonRumble.W_WIDTH, HackathonRumble.W_HEIGHT,true);

        mainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(ScreenType.MENUSCREEN);
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
