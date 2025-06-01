package pepse.world;

/**
 * Represents the different states the avatar can be in during the game.
 * These states determine the avatar's animation and behavior logic.
 */
public enum AvatarState {
	/** Avatar is standing still on the ground. */
	idle,

	/** Avatar is running left or right. */
	run,

	/** Avatar is jumping upward. */
	jump,

	/** Avatar is moving or falling but not performing a clear action (idle/run/jump).
	 * Not losing energy and not gaining energy */
	neither;
}
