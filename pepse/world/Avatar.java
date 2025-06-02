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

/**
 * Represents the player-controlled avatar in the game.
 * The avatar can move left, right, and jump based on user input,
 * and its visual state (idle, run, jump) is animated accordingly.
 * It also consumes and regenerates energy based on actions.
 * @author omer and rotem
 */
public class Avatar extends GameObject {
	private static final float VELOCITY_X = 300;
	private static final float VELOCITY_Y = -300;
	private static final float GRAVITY = 300;

	public static final float AVATAR_WIDTH = 60;
	public static final float AVATAR_HEIGHT =90;
	private static final double TIME_BETWEEN_CLIPS = 0.5;
	private static final float MOVE_ENERGY = 0.5f;
	private static final float JUMP_ENERGY = 10;
	private static final float MAX_ENERGY = 100;

	private UserInputListener inputListener;
	AnimationRenderable idleAnimation, runAnimation, jumpAnimation;

	private AvatarState avatarState;
	private float energy;

	private final List<AvatarListener> jumpListeners = new ArrayList<>();

	/**
	 * Constructs a new Avatar instance.
	 *
	 * @param topLeftCorner The top-left position of the avatar in world coordinates.
	 * @param inputListener User input listener for handling movement and jumping.
	 * @param imageReader   Used to load animation frames for different avatar states.
	 */
	public Avatar(Vector2 topLeftCorner, UserInputListener inputListener, ImageReader imageReader) {
		super(topLeftCorner,
				new Vector2(AVATAR_WIDTH, AVATAR_HEIGHT),
				imageReader.readImage("assets/idle_0.png", false));

		physics().preventIntersectionsFromDirection(Vector2.ZERO);
		transform().setAccelerationY(GRAVITY);
		this.inputListener = inputListener;

		Renderable[] idleImages = {
				imageReader.readImage("assets/idle_0.png", false),
				imageReader.readImage("assets/idle_1.png", false),
				imageReader.readImage("assets/idle_2.png", false),
				imageReader.readImage("assets/idle_3.png", false)
		};
		this.idleAnimation = new AnimationRenderable(idleImages, TIME_BETWEEN_CLIPS);

		Renderable[] runImages = {
				imageReader.readImage("assets/run_0.png", false),
				imageReader.readImage("assets/run_1.png", false),
				imageReader.readImage("assets/run_2.png", false),
				imageReader.readImage("assets/run_3.png", false),
				imageReader.readImage("assets/run_4.png", false),
				imageReader.readImage("assets/run_5.png", false)
		};
		this.runAnimation = new AnimationRenderable(runImages, TIME_BETWEEN_CLIPS);

		Renderable[] jumpImages = {
				imageReader.readImage("assets/jump_0.png", false),
				imageReader.readImage("assets/jump_1.png", false),
				imageReader.readImage("assets/jump_2.png", false),
				imageReader.readImage("assets/jump_3.png", false)
		};
		this.jumpAnimation = new AnimationRenderable(jumpImages, TIME_BETWEEN_CLIPS);

		this.avatarState = AvatarState.idle;
		this.energy = MAX_ENERGY;
	}

	/**
	 * Updates the avatar's physics, animations,
	 * and state transitions based on user input.
	 *
	 * @param deltaTime The time passed since the last update (in seconds).
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		float xVel = 0;
		boolean doneAction = false;
		boolean moveLeft = false;

		if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&
				!inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&
				this.energy >= MOVE_ENERGY) {
			xVel -= VELOCITY_X;
			this.avatarState = AvatarState.run;
			moveLeft = true;
			doneAction = true;
		}

		if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&
				!(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && this.energy >= MOVE_ENERGY)) {

			xVel += VELOCITY_X;
			this.avatarState = AvatarState.run;
			doneAction = true;
		}

		transform().setVelocityX(xVel);

		if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
				getVelocity().y() == 0 &&
				this.energy >=JUMP_ENERGY) {
			transform().setVelocityY(VELOCITY_Y);
			this.avatarState = AvatarState.jump;
			doneAction = true;
			notifyJump();
		}
		if (!doneAction && getVelocity().y() == 0 && getVelocity().x() == 0) {
			this.avatarState = AvatarState.idle;
		} else if (!doneAction) {
			this.avatarState = AvatarState.neither;
		}
		updateByState(moveLeft);
	}

	private void updateByState(boolean moveLeft) {
		switch (avatarState) {
			case idle:
				renderer().setRenderable(idleAnimation);
				if (this.energy < MAX_ENERGY) {
					addEnergy(1);
				}
				break;
			case run:
				renderer().setRenderable(runAnimation);
				renderer().setIsFlippedHorizontally(moveLeft);
				subEnergy(MOVE_ENERGY);
				break;
			case jump:
				renderer().setRenderable(jumpAnimation);
				subEnergy(JUMP_ENERGY);
				break;
			default:
				break;
		}
	}

	/**
	 * Adds energy to the avatar up to a maximum of 100.
	 *
	 * @param addition Amount of energy to add.
	 */
	public void addEnergy(float addition) {
		this.energy = Math.min(this.energy + addition, MAX_ENERGY);
	}

	/**
	 * Subtracts energy from the avatar down to a minimum of 0.
	 *
	 * @param subtruct Amount of energy to subtract.
	 */
	public void subEnergy(float subtruct) {
		this.energy = Math.max(this.energy - subtruct, 0);
	}

	/**
	 * Returns the current energy level of the avatar.
	 *
	 * @return Current energy level, between 0 and 100.
	 */
	public float getEnergy() {
		return energy;
	}

	@Override
	/**
	 * Handles collisions. If the avatar touches the ground, vertical velocity is reset to zero.
	 *
	 * @param other     The GameObject the avatar collided with.
	 * @param collision Collision details.
	 */
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		if (other.getTag().equals("ground")) {
			this.transform().setVelocityY(0);
		}
	}

	/**
	 * Registers a listener to be notified when the avatar jumps.
	 *
	 * @param listener An AvatarListener to add.
	 */
	public void addListener(AvatarListener listener) {
		jumpListeners.add(listener);
	}

	/**
	 * Notifies all registered listeners that the avatar has jumped.
	 */
	private void notifyJump() {
		for (AvatarListener listener : jumpListeners) {
			listener.onJump();
		}
	}
}
