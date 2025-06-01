package pepse.world;

/**
 * An interface for listening to avatar events.
 * Implementers of this interface can react to specific actions performed by the avatar.
 */
public interface AvatarListener {

	/**
	 * Called when the avatar performs a jump action.
	 * Can be used to trigger side effects like environmental changes (e.g., rain).
	 */
	void onJump();
}
