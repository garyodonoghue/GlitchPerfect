package com.ladinc.glitchperfect.core.hacks;

import java.util.Random;

import com.ladinc.glitchperfect.core.objects.AIPlayer;
import com.ladinc.glitchperfect.core.objects.HockeyPlayer;
import com.ladinc.glitchperfect.screens.GameScreen;
import com.ladinc.mcp.MCP;

public class HackEventManager 
{
	public static MCP moreControllers;
	
	//public static enum Hacks {removeScreenClear, disableSword, increaseEnemySpawnRate, increaseEnemySpeed, decreaseHumanSpeed, invertControls}
	public static enum Hacks { increaseEnemySpeed, invertControls}
	
	public static void recievedHackEvent(String ratingStr)
	{
		int rating = 3;
		rating = Integer.parseInt(ratingStr);
		Random r = new Random();
		Hacks hack = Hacks.values()[r.nextInt(Hacks.values().length)];
		
		applyHack(hack, rating);
	}
	
	public static void applyHack(Hacks hack, int rating)
	{
		increaseSpeedOfAI(rating);
		switch(hack)
		{
			case increaseEnemySpeed:
				increaseSpeedOfAI(rating);
				break;
			case invertControls:
				invertControlsOfHuman(rating);
				break;
		}
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
		
		if(rating == 0)
		{
			AIPlayer.SPEED_AI =  AIPlayer.SPEED_AI / 1.2f;
		}
		else
		{
			AIPlayer.SPEED_AI = AIPlayer.SPEED_AI *((multi) * (rating / 5));
		}
	}

}
