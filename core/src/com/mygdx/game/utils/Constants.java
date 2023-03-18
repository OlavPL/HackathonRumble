package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.io.File;
import java.util.ArrayList;

public class Constants {
    // Pixel per meter, used to scale values to project pixel size
    public static final float  PPM = 16;
    public static final Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
    public static final String SCORE_FILE_PATH = "scoreFile.ser";


}
