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

	public Vector2 getMovementofAItowardsPlayer(Vector2 aiLocation, Vector2 playerLocation) {
		Vector2 temp = new Vector2(aiLocation.x - playerLocation.x, aiLocation.y - playerLocation.y);
		if(Math.abs(temp.x) > Math.abs(temp.y)){
			if(temp.x > 1){
				temp.x = 1;
				temp.y = temp.y/temp.x;
			}
			else{
				temp.x = -1;
				temp.y = temp.y/temp.x;
			}
		}
		else{
			if(temp.y > 1){
				temp.y = 1;
				temp.x = temp.x/temp.y;				
			}
			else{
				temp.y = -1;
				temp.x = temp.x/temp.y;
			}
		}
		return temp;

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

}
