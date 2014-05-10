package com.ladinc.glitchperfect.core.controls.listeners;

import java.util.Map;

import com.ladinc.glitchperfect.core.hacks.HackEventManager;
import com.ladinc.mcp.interfaces.MCPContorllersListener;

public class MCPListenerClient implements  MCPContorllersListener
{

	
	
	@Override
	public void analogMove(int arg0, String arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonDown(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonUp(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orientation(int arg0, float arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pass(Map<String, String> header, Map<String, String> params,
			Map<String, String> files) 
	{
		String rating;
		String id;
		
		if(params != null)
		{
			rating = params.get("rating");
			id = params.get("id");
			
			HackEventManager.recievedHackEvent(rating, id);
		}
		
	}

}
