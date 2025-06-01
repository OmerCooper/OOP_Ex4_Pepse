package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Avatar extends GameObject{
	private static final float VELOCITY_X = 300;
	private static final float VELOCITY_Y = -600;
	private static final float GRAVITY = 600;

	public static final float AVATAR_WIDTH = 30;
	public static final float AVATAR_HEIGHT = 30;
	private static final double TIME_BETWEEN_CLIPS = 0.5;

	private UserInputListener inputListener;
	AnimationRenderable idleAnimation, runAnimation,jumpAnimation;

	private AvatarState avatarState;
	private float energy;

	private final List<AvatarListener> jumpListeners = new ArrayList<>();

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

		Renderable[] idleImages = {imageReader.readImage("assets/idle_0.png", false),
									imageReader.readImage("assets/idle_1.png", false),
									imageReader.readImage("assets/idle_2.png", false),
									imageReader.readImage("assets/idle_3.png", false),};
		this.idleAnimation = new AnimationRenderable(idleImages,TIME_BETWEEN_CLIPS);

		Renderable[] runImages = {imageReader.readImage("assets/run_0.png", false),
				imageReader.readImage("assets/run_1.png", false),
				imageReader.readImage("assets/run_2.png", false),
				imageReader.readImage("assets/run_3.png", false),
				imageReader.readImage("assets/run_4.png", false),
				imageReader.readImage("assets/run_5.png", false),};
		this.runAnimation = new AnimationRenderable(runImages,TIME_BETWEEN_CLIPS);

		Renderable[] jumpImages = {imageReader.readImage("assets/jump_0.png", false),
				imageReader.readImage("assets/jump_1.png", false),
				imageReader.readImage("assets/jump_2.png", false),
				imageReader.readImage("assets/jump_3.png", false),};
		this.jumpAnimation = new AnimationRenderable(idleImages,TIME_BETWEEN_CLIPS);

		this.avatarState = AvatarState.idle;
		this.energy = 100;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		float xVel = 0;
		boolean doneAction = false;
		boolean moveLeft = false;
		if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && !(inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
			&& this.energy >= 0.5) {
			xVel -= VELOCITY_X;
			this.avatarState = AvatarState.run;
			moveLeft = true;
			doneAction = true;
		}
		if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && !(inputListener.isKeyPressed(KeyEvent.VK_LEFT)
				&& this.energy >= 0.5)) {
			xVel += VELOCITY_X;
			this.avatarState = AvatarState.run;
			doneAction = true;
		}
		transform().setVelocityX(xVel);
		if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0
				&& this.energy >= 10) {
			transform().setVelocityY(VELOCITY_Y);
			this.avatarState = AvatarState.jump;
			doneAction = true;
			notifyJump();
		}
		if (!doneAction && getVelocity().y() == 0 && getVelocity().x() == 0){
			this.avatarState = AvatarState.idle;
		} else if (!doneAction) {
			this.avatarState = AvatarState.neither;
		}


		switch (avatarState) {
			case idle:
				renderer().setRenderable(idleAnimation);
				if(this.energy<100){
					addEnergy(1);
				}
				break;
			case run:
				renderer().setRenderable(runAnimation);
				if (moveLeft){
					renderer().setIsFlippedHorizontally(true);
				}
				else {
					renderer().setIsFlippedHorizontally(false);
				}
				subEnergy(0.5f);
				break;
			case jump:
				renderer().setRenderable(jumpAnimation);
				subEnergy(10);
				break;
			default:
				break;
		}
	}
	public void addEnergy(float addition) {
		this.energy = Math.min(this.energy+addition,100);
	}
	public void subEnergy(float subtruct) {
		this.energy = Math.max(this.energy-subtruct,0);
	}

	@Override
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		if(other.getTag().equals("ground")){
			this.transform().setVelocityY(0);
		}
	}
	public void addListener(AvatarListener listener) {
		jumpListeners.add(listener);
	}
	private void notifyJump() {
		for (AvatarListener listener : jumpListeners) {
			listener.onJump();
		}
	}
}

