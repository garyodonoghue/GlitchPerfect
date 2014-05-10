package com.ladinc.glitchperfect.core.ai;

import com.badlogic.gdx.math.Vector2;
import com.ladinc.glitchperfect.core.objects.AIPlayer;
import com.ladinc.glitchperfect.core.utilities.GenericEnums.Identifier;

public class SimpleAi implements
		com.ladinc.glitchperfect.core.controls.IControls {

	private Vector2 leftMovement = new Vector2(0, 0);

	private Vector2 rightMovement = new Vector2(0, 0);

	private boolean divePressed;

	public AIPlayer player;

	@Override
	public Vector2 getMovementInput() {
		return leftMovement;
	}

	@Override
	public Vector2 getRotationInput() {
		return rightMovement;
	}

	@Override
	public boolean getDivePressed() {
		// TODO Auto-generated method stub
		return this.divePressed;
	}

	@Override
	public boolean isActive() {
		// If AI exists its active
		return true;
	}

	@Override
	public boolean isRotationRelative() {
		// TODO Auto-generated method stub
		return false;
	}

	public void getMovementofAItowardsPlayer(Vector2 aiLocation, Vector2 playerLocation) {
		
		Vector2 temp = new Vector2(playerLocation.x - aiLocation.x, (playerLocation.y - aiLocation.y));
		
		int direcitonX = 1;
		int directionY = 1;
		
		if(temp.x < 1)
		{
			direcitonX = -1;
		}
		
		if(temp.y < 1)
		{
			directionY = -1;
		}
		
		float absX = Math.abs(temp.x);
		float absY = Math.abs(temp.y);
		
		if(absX > absY)
		{
			temp.x = direcitonX * 1;
			temp.y = directionY * (absY/absX);
		}
		else
		{

			temp.y = directionY * 1;
			temp.x = direcitonX * (absX/absY);				

		}
		leftMovement =  temp;

	}

	@Override
	public void setIdentifier(Identifier identifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public Identifier getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getStartPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}
