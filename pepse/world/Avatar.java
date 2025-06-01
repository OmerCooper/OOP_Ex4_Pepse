package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Avatar extends GameObject{
	private static final float VELOCITY_X = 300;
	private static final float VELOCITY_Y = -350;
	private static final float GRAVITY = 400;

	public static final float AVATAR_WIDTH = 30;
	public static final float AVATAR_HEIGHT = 30;

	private UserInputListener inputListener;

	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner Position of the object, in window coordinates (pixels).
	 *                      Note that (0,0) is the top-left corner of the window.
	 * @param inputListener  input from user.
	 * @param imageReader   reader for image of avatar.
	 */
	public Avatar(Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader) {
		super(topLeftCorner,
				new Vector2(AVATAR_WIDTH,AVATAR_HEIGHT),
				imageReader.readImage("assets/idle_0.png", false));

		physics().preventIntersectionsFromDirection(Vector2.ZERO);
		transform().setAccelerationY(GRAVITY);
		this.inputListener = inputListener;
	}
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		float xVel = 0;
		if(inputListener.isKeyPressed(KeyEvent.VK_LEFT))
			xVel -= VELOCITY_X;
		if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
			xVel += VELOCITY_X;
		transform().setVelocityX(xVel);
		if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0)
			transform().setVelocityY(VELOCITY_Y);
	}

}
