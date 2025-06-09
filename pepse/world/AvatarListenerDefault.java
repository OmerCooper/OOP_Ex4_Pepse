package pepse.world;
/**
 * impliments the interface AvatarListener for default functions,
 * for the onJump gives for "free" the empty implimantion. so in the future if
 * a class wants to listen to the avatr, but for the jump, it woukld have already
 * an empty implimantion.
 * @author omer and rotem
 */
public class AvatarListenerDefault implements AvatarListener {
	@Override
	/**
	 * Called when the avatar performs a jump action.
	 * gives for "free" the implimintaion for all classes that extend it
	 * the defualt empty implimantion for onJump
	 */
	public void onJump() {
	}
}
