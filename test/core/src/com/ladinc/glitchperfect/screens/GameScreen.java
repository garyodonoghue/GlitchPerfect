package com.ladinc.glitchperfect.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.glitchperfect.GlitchPerfectGame;
import com.ladinc.glitchperfect.core.ai.SimpleAi;
import com.ladinc.glitchperfect.core.collision.CollisionHelper;
import com.ladinc.glitchperfect.core.controls.IControls;
import com.ladinc.glitchperfect.core.hacks.HackEventManager;
import com.ladinc.glitchperfect.core.objects.AIPlayer;
import com.ladinc.glitchperfect.core.objects.BoxProp;
import com.ladinc.glitchperfect.core.objects.HockeyPlayer;
import com.ladinc.glitchperfect.core.objects.StartingPosition;

public class GameScreen implements Screen {
	public static float AI_CREATION_RATE = 1;
	private Box2DDebugRenderer debugRenderer;
	private GlitchPerfectGame game;
	// Used for sprites etc
	private int screenWidth;
	private int screenHeight;

	// Used for Box2D
	private float worldWidth;
	private float worldHeight;
	private static int PIXELS_PER_METER = 10;

	private Vector2 center;
	private OrthographicCamera camera;
	public static List<HockeyPlayer> hockeyPlayerList;
	private World world;
	private List<SimpleAi> AiList;
	private float aiTimer;
	
	private SpriteBatch spriteBatch;
	
	private BitmapFont font;
	
	private boolean gameOver = false;
	
	private boolean gameStarted = false;
	
	public static float SHOW_HACKED_TIMER = 0f;
	public static String HACKED_MESSAGE = "";
	

	private static final float GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE = 3.0f;
	public static float AI_SPEED_INCREASED_TIMER = 0;
	
	public static Map<Integer, Vector2> listAIPositions;
	public static float removeScreenClearTimer = 0f;
	private static float delta = 0f;
	
	private boolean allDead = false;

	public GameScreen(GlitchPerfectGame game) {
		this.game = game;

		this.screenWidth = this.game.screenWidth;
		this.screenHeight = this.game.screenHeight;

		this.worldHeight = this.screenHeight / PIXELS_PER_METER;
		this.worldWidth = this.screenWidth / PIXELS_PER_METER;

		this.center = new Vector2(worldWidth / 2, worldHeight / 2);

		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, this.screenWidth, this.screenHeight);
		
		spriteBatch = new SpriteBatch();

		// Map used to reference the different spawning points for the AI
		listAIPositions = new HashMap<Integer, Vector2>();
		listAIPositions.put(1, new Vector2(10, 102));
		listAIPositions.put(2, new Vector2(10, 10));
		listAIPositions.put(3, new Vector2(187, 102));
		listAIPositions.put(4, new Vector2(187, 10));

		this.debugRenderer = new Box2DDebugRenderer();
		
		font = new BitmapFont(Gdx.files.internal("Swis-721-50.fnt"), Gdx.files.internal("Swis-721-50.png"), false);
		this.font.setColor(Color.WHITE);
	}

	private void createAIPlayer() {
		if (AiList == null) {
			AiList = new ArrayList<SimpleAi>();
		}

		SimpleAi ai = new SimpleAi();
		AiList.add(ai);

		StartingPosition sp = getRandomStartingPositionForAI();

		AIPlayer aiPlayer = new AIPlayer(world, 0, sp, ai, camera);
		ai.player = aiPlayer;
	}

	private StartingPosition getRandomStartingPositionForAI() {
		Random rand = new Random();
		int randomNum = rand.nextInt((4 - 1) + 1) + 1;

		StartingPosition sp = new StartingPosition(
				GameScreen.listAIPositions.get(randomNum), 0f);
		return sp;
	}

	@Override
	public void render(float delta) 
	{
		// Clear the canvas to avoid 'trail' effect if the hack is no longer being applied
		if(GameScreen.removeScreenClearTimer==0f){
			Gdx.gl.glClearColor(0f, 0f, 0f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}
		
		GameScreen.delta  = delta;

		updateTimers();
		
		aiTimer = aiTimer + delta;
		
		camera.zoom = 1.1f;
		camera.update();
		// TODO: spriteBatch.setProjectionMatrix(camera.combined);

		if (this.game.mcm.checkForNewControllers()) {
			createPlayers();
		}
		
		if(!this.gameStarted)
		{
			for(IControls cont : this.game.mcm.controls)
			{
				if(cont.getStartPressed())
				{
					this.gameStarted = true;
					break;
				}
				
			}
		}
		
		if(gameOver)
		{
			for(IControls cont : this.game.mcm.controls)
			{
				if(cont.getStartPressed())
				{
					
					this.game.setScreen(new GameScreen(game));
					break;
				}
				
			}
		}
		
		checkForDeaths();
		
		if (aiTimer >= AI_CREATION_RATE && hockeyPlayerList.size()>0 && gameStarted) {

			createAIPlayer();
			aiTimer = 0f;
		}

		// move ai players towards closest user player
		if (AiList != null && GameScreen.hockeyPlayerList.size() != 0) {
			for (SimpleAi ai : AiList) {
				if (ai != null) {
					ai.getMovementofAItowardsPlayer(ai.player.body.getWorldCenter(),
							GameScreen.hockeyPlayerList.get(0).body
									.getWorldCenter());

					ai.player.updateMovement();
				}
			}
		}

		for (HockeyPlayer hp : hockeyPlayerList) {
			// This allows the scoring team to beat the other team around
			hp.updateMovement(delta);
		}

		world.step(Gdx.app.getGraphics().getDeltaTime(), 10, 10);
		// world.clearForces();
		// world.step(1/60f, 3, 3);
		world.clearForces();

		 this.spriteBatch.begin();
		 if(!gameStarted)
		 {
			 this.font.draw(spriteBatch, "Press Start To Begin Game", this.screenWidth/2 - this.font.getBounds("Press Start To Begin Game").width/2, this.screenHeight/2);
		 }
		 
		 if(SHOW_HACKED_TIMER > 0 )
		 {
			 SHOW_HACKED_TIMER = SHOW_HACKED_TIMER - delta;
			 
			 this.font.draw(spriteBatch, HACKED_MESSAGE, this.screenWidth/2 - this.font.getBounds(HACKED_MESSAGE).width/2, this.screenHeight/2);
		 }
		 
		 this.font.draw(spriteBatch, this.game.mcm.ipAddr, this.screenWidth/2 - this.font.getBounds(this.game.mcm.ipAddr).width/2, 1000);
		 
		 this.spriteBatch.end();

		debugRenderer.render(world, camera.combined.scale(PIXELS_PER_METER,
				PIXELS_PER_METER, PIXELS_PER_METER));
	}
	
	private synchronized void updateTimers() {
		GameScreen.removeScreenClearTimer -= GameScreen.delta;
		if(GameScreen.removeScreenClearTimer < 0f){
			GameScreen.removeScreenClearTimer = 0f;
		}
		
		//for the rate of enemy spawning, the rate could be above or below 1, 
		//either way we want to gradually bring the spawning rate back to 1 (default)
		if(GameScreen.AI_CREATION_RATE > 1){
			GameScreen.AI_CREATION_RATE -= GameScreen.delta;
		}
		else if(GameScreen.AI_CREATION_RATE < 1){
			GameScreen.AI_CREATION_RATE += GameScreen.delta;
		}
		
		//return the speed to its normal value (10f) when the timer reaches 0
		if(GameScreen.AI_SPEED_INCREASED_TIMER > 0f){
			GameScreen.AI_SPEED_INCREASED_TIMER -= GameScreen.delta;
		}
		else if(GameScreen.AI_SPEED_INCREASED_TIMER < 0f){
			GameScreen.AI_SPEED_INCREASED_TIMER = 0f;
			AIPlayer.SPEED_AI = AIPlayer.DEFAULT_SPEED_AI;
		}
		
		for(HockeyPlayer hp : hockeyPlayerList){
			hp.disableSwordTimer -= GameScreen.delta;
			if(hp.disableSwordTimer < 0f){
				hp.disableSwordTimer = 0f;
			}
			
			hp.decreasePlayerSpeedValue -= GameScreen.delta;
			if(hp.decreasePlayerSpeedValue < 0f){
				hp.decreasePlayerSpeedValue = 0f;
			}
		}
		
	}

	private void checkForDeaths()
	{
		List<SimpleAi> deathListAi = new ArrayList<SimpleAi>();
		List<HockeyPlayer> deathListHuman = new ArrayList<HockeyPlayer>();
		
		if (AiList != null)
		{
			for(SimpleAi ai : AiList)
			{
				if(ai.player.toBeKilled)
				{
					deathListAi.add(ai);
				}
			}
		}
		
		for(SimpleAi ai : deathListAi)
		{
			AiList.remove(ai);
			world.destroyBody(ai.player.body);
		}
		
		for(HockeyPlayer hp : hockeyPlayerList)
		{
			if(hp.toBeKilled)
			{
				deathListHuman.add(hp);
				HackEventManager.leaveHeartbeatMessageAll("die");
			}
		}
		
		for(HockeyPlayer hp : deathListHuman)
		{
			hockeyPlayerList.remove(hp);
			world.destroyBody(hp.stick);
			world.destroyBody(hp.body);
			
			if(hockeyPlayerList.size() == 0 && gameStarted)
			{
				gameOver = true;
			}
		}
		
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	private void createPlayers() {
		Gdx.app.error("Hockey Screen", "createPlayers()");

		if (hockeyPlayerList == null) {
			hockeyPlayerList = new ArrayList<HockeyPlayer>();
		}

		int nextPlayerNumber = this.hockeyPlayerList.size() + 1;

		for (IControls ic : this.game.mcm.controls) {
			Gdx.app.debug("Hockey Screen",
					"createPlayers() - looping through controllers");

			boolean controllerHasPlayer = false;
			for (HockeyPlayer hp : hockeyPlayerList) {
				Gdx.app.debug("Hockey Screen",
						"createPlayers() - checking to see is controller already assigned");
				if (hp.controller == ic)
					controllerHasPlayer = true;

			}

			if (!controllerHasPlayer) {
				Gdx.app.debug("Hockey Screen",
						"createPlayers() - creating new player");
				hockeyPlayerList
						.add(new HockeyPlayer(world, nextPlayerNumber, null,
								ic, new StartingPosition(center, 0),
								this.camera));
				nextPlayerNumber++;
			}
		}
	}

	@Override
	public void show() {
		world = new World(new Vector2(0.0f, 0.0f), true);
		new BoxProp(world, screenWidth, 1, new Vector2(3,
						GAP_BETWEEN_TOPBOTTOMWALL_AND_EDGE)); // bottom
		 	new BoxProp(world, screenWidth, 1, new Vector2(screenWidth / 2, 105)); // top
		 		new BoxProp(world, 1, screenHeight, new Vector2(3, screenHeight / 2));// left
		 	new BoxProp(world, 1, screenHeight, new Vector2(190, screenHeight / 2)); // right
		 		  		createPlayers();
		
		world.setContactListener(new CollisionHelper());
		
		createPlayers();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
