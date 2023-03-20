package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.HackathonRumble;

import static com.mygdx.game.utils.Constants.skin;

public class MenuScreen implements Screen {
    HackathonRumble parent;
    Stage stage;
    public MenuScreen(HackathonRumble parent){
        this.parent = parent;
        stage = new Stage(new ScreenViewport());
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton singlePlayerGame = new TextButton("New Singleplayer Game",skin);
        TextButton exitGame = new TextButton("Exit Game", skin);
        TextButton gameEnd = new TextButton("High Scores", skin);

        table.add(singlePlayerGame).fillX().uniformX();
        table.row().pad(20,0,20,0);
        table.add(gameEnd).fillX().uniformX();
        table.row().pad(20,0,20,0);
        table.add(exitGame).fillX().uniformX();

        stage.getViewport().update(HackathonRumble.W_WIDTH, HackathonRumble.W_HEIGHT,true);

        singlePlayerGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(ScreenType.SINGLEPLAYER_GAME);
            }
        });
        exitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        gameEnd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(ScreenType.HIGH_SCORE);
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
