package com.ladinc.glitchperfect.core.hacks;

import java.util.Random;

import org.json.simple.JSONObject;

import com.badlogic.gdx.Gdx;
import com.ladinc.glitchperfect.core.controls.listeners.MCPListenerClient;
import com.ladinc.glitchperfect.core.objects.AIPlayer;
import com.ladinc.glitchperfect.core.objects.HockeyPlayer;
import com.ladinc.glitchperfect.screens.GameScreen;
import com.ladinc.mcp.MCP;

public class HackEventManager 
{
	public static MCP moreControllers;
	
	//public static enum Hacks {removeScreenClear, disableSword, increaseEnemySpawnRate, increaseEnemySpeed, decreaseHumanSpeed, invertControls}
	public static enum Hacks { increaseEnemySpeed, invertControls, increaseEnemySpawnRate, disableSword, removeScreenClear, decreaseHumanSpeed}
	
	public static void recievedHackEvent(String ratingStr, String id)
	{
		int rating = 3;
		rating = Integer.parseInt(ratingStr);
		Random r = new Random();
		Hacks hack = Hacks.values()[r.nextInt(Hacks.values().length)];
		
		applyHack(hack, rating, id);
	}
	
	public static void applyHack(Hacks hack, int rating, String id)
	{
		switch(hack)
		{
			case increaseEnemySpeed:
				increaseSpeedOfAI(rating);
				leaveHeartbeatMessage("Increased Speed Of Enemy", rating, id);
				Gdx.app.error("HackEventManager", "increaseEnemySpeed");
				setHackedMessage("Increased Speed Of Enemy", rating);
				break;
			case invertControls:
				invertControlsOfHuman(rating);
				leaveHeartbeatMessage("Inverted Controls of players", rating, id);
				Gdx.app.error("HackEventManager", "invertControls");
				setHackedMessage("Inverted Controls of players", rating);
				break;
			case increaseEnemySpawnRate:
				increaseEnemySpawnRate(rating);
				leaveHeartbeatMessage("Increased Enemy Spawn rate", rating, id);
				Gdx.app.error("HackEventManager", "increaseEnemySpawnRate");
				setHackedMessage("Increased Speed Of Enemy", rating);
				break;
			case disableSword:
				disablePlayerSword(rating);
				leaveHeartbeatMessage("Disabled player's sword", rating, id);
				Gdx.app.error("HackEventManager", "disableSword");
				setHackedMessage("Disabled player's sword", rating);
				break;
			case removeScreenClear:
				removeScreenClear(rating);
				leaveHeartbeatMessage("Removing screen clear", rating, id);
				Gdx.app.error("HackEventManager", "removeScreenClear");
				setHackedMessage("Removing screen clear", rating);
				break;
			case decreaseHumanSpeed:
				decreaseHumanSpeed(rating);
				leaveHeartbeatMessage("Decrease Human Speed", rating, id);
				break;
		}
	}
	
	private static void decreaseHumanSpeed(int rating) {
		int decreaseRate = rating*5;
		for(HockeyPlayer hp : GameScreen.hockeyPlayerList)
		{
			if(hp.decreasePlayerSpeedValue > 0f)
			{
				hp.decreasePlayerSpeedValue = hp.decreasePlayerSpeedValue + decreaseRate;
			}
			else
			{
				hp.decreasePlayerSpeedValue = decreaseRate;
			}
		}
		
	}

	private static void removeScreenClear(int rating) {
		if(GameScreen.removeScreenClearTimer > 0)
		{
			GameScreen.removeScreenClearTimer = GameScreen.removeScreenClearTimer + rating;
		}
		else
		{
			GameScreen.removeScreenClearTimer = rating;
		}
		
	}

	private static void disablePlayerSword(int rating) {
		for(HockeyPlayer hp : GameScreen.hockeyPlayerList)
		{
			if(hp.disableSwordTimer > 0f)
			{
				hp.disableSwordTimer = hp.disableSwordTimer + rating;
			}
			else
			{
				hp.disableSwordTimer = rating;
			}
		}
		
	}

	private static void increaseEnemySpawnRate(int rating) {
		GameScreen.AI_CREATION_RATE = GameScreen.AI_CREATION_RATE / (3/(rating+1)); 
		
	}

	private static void invertControlsOfHuman(int rating)
	{
		for(HockeyPlayer hp : GameScreen.hockeyPlayerList)
		{
			if(hp.invertControlsTimer > 0)
			{
				hp.invertControlsTimer = hp.invertControlsTimer + rating;
			}
			else
			{
				hp.invertControlsTimer = rating;
			}
		}
	}
	
	private static void increaseSpeedOfAI(int rating)
	{ 
		int multi = 2;
		
		if(GameScreen.AI_SPEED_INCREASED_TIMER > 0f)
		{
			GameScreen.AI_SPEED_INCREASED_TIMER = GameScreen.AI_SPEED_INCREASED_TIMER + rating;
		}
		else
		{
			GameScreen.AI_SPEED_INCREASED_TIMER = rating;
		}
		
		if(rating == 0)
		{
			AIPlayer.SPEED_AI =  AIPlayer.SPEED_AI / 1.2f;
		}
		else
		{
			AIPlayer.SPEED_AI = AIPlayer.SPEED_AI *((multi) * (rating / 5));
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void leaveHeartbeatMessage(String message, int rating, String id)
	{
		JSONObject obj = new JSONObject();
		obj.put("message", message);
		moreControllers.hearbeatResponses.put(id, obj);
	}
	
	public static void leaveHeartbeatMessageAll(String message)
	{
		for(String id : MCPListenerClient.ids)
		{
			leaveHeartbeatMessage(message, 0, id);
		}
	}
	
	public static void setHackedMessage(String message, int rating)
	{
		GameScreen.SHOW_HACKED_TIMER = 3f;
		GameScreen.HACKED_MESSAGE = message + " - " + rating + " stars.";
	}

}
