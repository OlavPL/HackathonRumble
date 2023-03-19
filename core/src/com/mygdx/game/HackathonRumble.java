package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.mygdx.game.entities.screens.HighScore;
import com.mygdx.game.entities.screens.MenuScreen;
import com.mygdx.game.entities.screens.ScreenType;
import com.mygdx.game.entities.screens.SinglePlayerGame;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HackathonRumble extends Game {
	public static int W_WIDTH = 1280;
	public static int W_HEIGHT = 720;
	private Screen menuScreen, singlePlayerGame, highScore;

	@Override
	public void create () {
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	public void changeScreen(ScreenType screen){
		switch (screen){
			case MENUSCREEN: {
				if (menuScreen == null) menuScreen = new MenuScreen(this);
				setScreen(menuScreen);
				break;
			}
			case SINGLEPLAYER_GAME: {
				singlePlayerGame = new SinglePlayerGame(this);
				setScreen(singlePlayerGame);
				break;
			}
			case HIGH_SCORE: {
				System.out.println("TEST");
				highScore = new HighScore(this);
				setScreen(highScore);
				break;
			}
//			case MULTIPLAYER_GAME: {
//				if (multiplayerGame == null) multiplayerGame = new MultiplayerGame(this);
//				this.setScreen(multiplayerGame);
//				break;
//			}
		}
	}
}
