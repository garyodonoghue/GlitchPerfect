package com.ladinc.glitchperfect.core.hacks;

import java.util.Random;

import com.ladinc.mcp.MCP;

public class HackEventManager 
{
	public static MCP moreControllers;
	
	public static enum Hacks {removeScreenClear, disableSword, increaseEnemySpawnRate, increaseEnemySpeed, decreaseHumanSpeed, invertControls}
	
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
		switch(hack)
		{
			case removeScreenClear:
			break;
		}
	}

}
