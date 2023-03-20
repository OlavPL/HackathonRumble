package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Constants {
    // Pixel per meter, used to scale values to project pixel size
    public static final float  PPM = 16;
//    public static final Skin skin = new Skin(Gdx.files.internal("skins/flat/skin/skin.json"));
    public static final Skin skin = new Skin(Gdx.files.internal("skins/skin/pixthulhu-ui.json"));
    public static final String SCORE_FILE_PATH = "scoreFile.ser";
    public static final int MAP_WIDTH = 30;
    public static final int MAP_HEIGHT = 20;
    public static final int TILE_SIZE = 16;

}
