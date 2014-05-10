package com.ladinc.glitchperfect;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ladinc.glitchperfect.core.controls.MyControllerManager;
import com.ladinc.glitchperfect.screens.GameScreen;

public class GlitchPerfectGame extends Game {
	SpriteBatch batch;
	Texture img;
	private GameScreen gameScreen;

	public int screenWidth = 1920;
	public int screenHeight = 1080;
	public MyControllerManager mcm;

	@Override
	public void create() {
		batch = new SpriteBatch();

		this.mcm = new MyControllerManager();
		createScreens();
		setScreen(gameScreen);
	}

	private void createScreens() {
		this.gameScreen = new GameScreen(this);

	}
}
