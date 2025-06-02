package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a basic immovable block in the game world.
 * Blocks have a fixed size and are used to build terrain or other static structures.
 * @author omer and rotem
 */
public class Block extends GameObject {
	public static final int SIZE = 30;

	/**
	 * Constructs a new Block at the given top-left corner with the specified renderable.
	 * The block has a fixed size and is immovable in the physics simulation.
	 *
	 * @param topLeftCorner The top-left position of the block in world coordinates.
	 * @param renderable    The visual representation of the block.
	 */
	public Block(Vector2 topLeftCorner, Renderable renderable) {
		super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
		physics().preventIntersectionsFromDirection(Vector2.ZERO);
		physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
	}
}
