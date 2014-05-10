package com.ladinc.glitchperfect.core.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.ladinc.glitchperfect.core.collision.CollisionInfo;
import com.ladinc.glitchperfect.core.collision.CollisionInfo.CollisionObjectType;
import com.ladinc.glitchperfect.core.controls.IControls;
import com.ladinc.glitchperfect.core.utilities.GenericEnums.Identifier;
import com.ladinc.glitchperfect.core.utilities.GenericEnums.Side;
//import com.ladinc.glitchperfect.core.utilities.GenericEnums.Identifier;
//import com.ladinc.glitchperfect.core.utilities.GenericEnums.Side;

public class AIPlayer {

	public static float SPEED_AI = 10;

	public static float playerSize = 2f;

	private static int PIXELS_PER_METER = 10;

	public Sprite sprite;

	public Body body;
	private World world;

	public int playerNumber;

	public IControls controller;
	StartingPosition startPos;

	public Side side;

	private float power;

	private OrthographicCamera camera;
	
	public boolean toBeKilled = false;

	public AIPlayer(World world, int number, StartingPosition startPos,
			IControls controller, OrthographicCamera camera) {
		playerNumber = number;

		this.camera = camera;
		this.controller = controller;
		this.world = world;
		this.startPos = startPos;
		// this.power = 15000f;
		this.power = 10000f;

		createBody();

		resetPosition();

		this.sprite = AIPlayer.getPlayerSprite(side);
	}

	public void resetPosition() {
		this.body.setTransform(startPos.location, 0f);

		this.body.setLinearVelocity(0f, 0f);
		this.body.setAngularVelocity(0f);
	}

	private void createBody() {
		// Dynamic Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position
				.set(this.startPos.location.x, this.startPos.location.y);

		// This keeps it that the force up is applied relative to the screen,
		// rather than the direction that the player is facing
		bodyDef.fixedRotation = true;
		this.body = world.createBody(bodyDef);

		CircleShape dynamicCircle = new CircleShape();
		dynamicCircle.setRadius(playerSize);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 10.0f;
		fixtureDef.friction = 0.3f;
		fixtureDef.restitution = 0.5f;
		fixtureDef.shape = dynamicCircle;

		this.body.createFixture(fixtureDef);
		
		this.body.setUserData(new CollisionInfo("", CollisionObjectType.Enemy, this));
	}

	public void updateSprite(SpriteBatch spriteBatch) {
		setSpritePosition(sprite, PIXELS_PER_METER, body, null);
		sprite.draw(spriteBatch);
	}

	public void updateMovement() {
		// sticks direction
		Vector2 movement = this.controller.getMovementInput();

		Gdx.app.debug(
				"HockeyPlayer - updateMovement",
				"Movement: x=" + String.valueOf(movement.x) + " y="
						+ String.valueOf(movement.y));

		// this.body.applyForce(this.body.getWorldVector(new
		// Vector2(forceVector.x, forceVector.y)), position, true );
		this.body.setLinearVelocity(new Vector2(SPEED_AI * movement.x, SPEED_AI
				* movement.y));
	}

	public void setSpritePosition(Sprite spr, int PIXELS_PER_METER,
			Body forLocation, Body forAngle) {

		spr.setPosition(
				PIXELS_PER_METER * forLocation.getPosition().x - spr.getWidth()
						/ 2, PIXELS_PER_METER * forLocation.getPosition().y
						- spr.getHeight() / 2);
		spr.setRotation((MathUtils.radiansToDegrees * forAngle.getAngle()));
	}

	private Vector3 tempVec = new Vector3(0.0f, 0.0f, 0.0f);
	private float movementSquareMax = 6f;
	private Vector2 moveForce = new Vector2();
	private Vector2 stickCenter = new Vector2();

	public void updateStick(float delta, Vector2 rotation,
			Vector2 playerPosition) {
		Gdx.app.debug(
				"HockeyPlayer - updateStick",
				"rotation: x=" + String.valueOf(rotation.x) + " y="
						+ String.valueOf(rotation.y));

		if (rotation.x == 0f && rotation.y == 0) {
			// No movement detected
			return;
		}

		if (controller.isRotationRelative()) {
			// We are being given a co-ordinate from the screen, we need to
			// convert this to a movement action
			// relative to the players position

			tempVec.set(rotation.x, rotation.y, 0);
			camera.unproject(tempVec);
			// temp vec is now the value of the input, but adjusted to match
			// box2ds world

			// Camera unproject gives us cameras co-ordinates of the input, this
			// needs to be converted to Box2d co-ords
			moveForce.x = tempVec.x / PIXELS_PER_METER - playerPosition.x;
			moveForce.y = tempVec.y / PIXELS_PER_METER - playerPosition.y;
			Gdx.app.debug("HockeyPlayer - Rotation Relative",
					"moveForce: x=" + String.valueOf(moveForce.x) + " y="
							+ String.valueOf(moveForce.y));
		} else {
			moveForce.x = rotation.x * movementSquareMax;
			moveForce.y = rotation.y * movementSquareMax;

		}

		// stickPositionRelativeToPlayer
		Vector2 stickRelativePlayer = new Vector2(stickCenter.x
				- playerPosition.x, stickCenter.y - playerPosition.y);
		float angleStick = stickRelativePlayer.angle();
		float angleMovement = moveForce.angle();

		Gdx.app.debug("HockeyPlayer - Angle Stuff",
				"rotation: angleStick=" + String.valueOf(angleStick)
						+ " angleMovement=" + String.valueOf(angleMovement));

	}

	public static Sprite getStickSprite() {
		// Texture stickTexture = new
		// Texture(Gdx.files.internal("Images/Objects/stick.png"));

		// return new Sprite(stickTexture);
		return null;
	}

	public static Sprite getPlayerSprite(Side side) {
		Texture playerTexture;

		playerTexture = null;

		// return new Sprite(playerTexture);
		return null;
	}

	private static int IDENTIFIER_WIDTH = 89;
	private static int IDENTIFIER_HEIGHT = 88;

	private static Sprite getIdentifierSprite(Identifier ident) {
		// Texture identifierTexture = new
		// Texture(Gdx.files.internal("Images/Objects/PlayerIdentifier.png"));

		int offset;

		switch (ident) {
		case blue:
			offset = 1;
			break;
		case darkblue:
			offset = 5;
			break;
		case green:
			offset = 3;
			break;
		case orange:
			offset = 4;
			break;
		case purple:
			offset = 6;
			break;
		case red:
			offset = 0;
			break;
		case yellow:
			offset = 2;
			break;
		default:
			offset = 0;
			break;

		}

		// Idenfiers share one pixel, thats why the minus 1 on the offset
		// return new Sprite(identifierTexture, offset*(IDENTIFIER_WIDTH - 1),
		// 0, IDENTIFIER_WIDTH, IDENTIFIER_HEIGHT);
		return null;
	}

}
