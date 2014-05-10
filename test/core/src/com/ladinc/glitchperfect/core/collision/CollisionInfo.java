package com.ladinc.glitchperfect.core.collision;

import com.ladinc.glitchperfect.core.utilities.GenericEnums.Side;
import com.ladinc.glitchperfectcore.objects.HockeyPlayer;

public class CollisionInfo {
	
	public String text;
	public CollisionObjectType type;
	
	public Object object;
	
	
	public CollisionInfo(String text, CollisionObjectType type)
	{
		this.text = text;		
		this.type = type;
	}
	
	public CollisionInfo(String text, CollisionObjectType type, Object object)
	{
		this.text = text;		
		this.type = type;
		this.object = object;
	}
	
	public static enum CollisionObjectType{Wall, Sword, Player, Enemy};

}

