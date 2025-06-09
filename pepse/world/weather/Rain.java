package pepse.world.weather;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.AvatarListener;
import pepse.world.AvatarListenerDefault;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents rain in the game world. When the avatar jumps,
 * blocks representing raindrops are created and fall with gravity while fading out.
 * the onJump is given from the extention to AvatarListenerDefault
 * which on it's turn impliments AvatarListener interface
 * @author omer and rotem
 */
public class Rain extends AvatarListenerDefault {

	private static final Color RAIN_COLOR = new Color(93, 116, 250);
	private static final float GRAVITY = 600;
	private static final float INITIAL_OPACITY = 1f;
	private static final float FINAL_OPACITY = 0f;
	private static final float TIME_OF_FALL = 2;
	private static final int RAIN_ROWS = 6;
	private static final int RAIN_COL = 5;
	private static final int MIN_RAIN_DROPS = 4;
	private static final int MAX_RAIN_DROPS = 6;

	private Cloud cloud;
	private final BiConsumer<GameObject, Integer> addGameObjectCallback;
	private final Consumer<GameObject> removeGameObjectCallback;

	/**
	 * Constructs a Rain instance.
	 *
	 * @param cloud                    The cloud object from which the rain will fall.
	 * @param addGameObjectCallback    A callback to add raindrop GameObjects to the game.
	 * @param removeGameObjectCallback A callback to remove raindrop GameObjects from the game.
	 */
	public Rain(Cloud cloud,
				BiConsumer<GameObject, Integer> addGameObjectCallback,
				Consumer<GameObject> removeGameObjectCallback) {
		this.cloud = cloud;
		this.addGameObjectCallback = addGameObjectCallback;
		this.removeGameObjectCallback = removeGameObjectCallback;
	}

	/**
	 * Creates a collection of raindrop blocks that fall from the cloud.
	 * Each raindrop is animated to fall with gravity and fade out during its fall.
	 *
	 * @param cloud The cloud object from which the rain originates.
	 * @return A list of GameObject blocks representing raindrops.
	 */
	public List<Block> create(Cloud cloud) {
		Vector2 cloudCenterLeftLoc = cloud.getCloudCenterLeft();

		java.util.List<Block> rainBlocks = new ArrayList<>();
		java.util.List<java.util.List<Integer>> rainShape =
				generateRainShape(RAIN_ROWS, RAIN_COL, MIN_RAIN_DROPS, MAX_RAIN_DROPS);
		for (int row = 0; row < rainShape.size(); row++) {
			for (int col = 0; col < rainShape.get(row).size(); col++) {
				if (rainShape.get(row).get(col) == 1) {
					Vector2 relativeOffset = new Vector2(col * Block.SIZE, row * Block.SIZE);
					Vector2 blockInitialPos = cloudCenterLeftLoc.add(relativeOffset);

					Block block = new Block(blockInitialPos,
							new RectangleRenderable(RAIN_COLOR));
					block.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
					block.setTag("rain");

					// Set falling animation and fading effect
					block.transform().setAccelerationY(GRAVITY);
					new Transition<Float>(
							block,
							(opaqueness) -> {
								block.renderer().setOpaqueness(opaqueness);
								if (opaqueness == FINAL_OPACITY) {
									this.removeGameObjectCallback.accept(block);
								}
							},
							INITIAL_OPACITY,
							FINAL_OPACITY,
							Transition.LINEAR_INTERPOLATOR_FLOAT,
							TIME_OF_FALL,
							Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
							null
					);
					rainBlocks.add(block);
				}
			}
		}
		return rainBlocks;
	}

	@Override
	/**
	 * Triggered when the avatar jumps.
	 * This method causes rain to fall by creating and adding raindrop blocks.
	 */
	public void onJump() {
		Rain rain = this;
		for (Block b : rain.create(this.cloud)) {
			this.addGameObjectCallback.accept(b, Layer.BACKGROUND);
		}
	}

	/**
	 * Generates a random 2D rain shape matrix with a given number of ones (raindrops) scattered in a grid.
	 *
	 * @param rows    Number of rows in the shape.
	 * @param cols    Number of columns in the shape.
	 * @param minOnes Minimum number of raindrops.
	 * @param maxOnes Maximum number of raindrops.
	 * @return A 2D List of integers where 1 represents a raindrop and 0 represents an empty space.
	 */
	private List<List<Integer>> generateRainShape(int rows, int cols, int minOnes, int maxOnes) {
		int totalCells = rows * cols;
		int numOnes = new Random().nextInt(maxOnes - minOnes + 1) + minOnes;

		List<Integer> flatList = new ArrayList<>(Collections.nCopies(totalCells, 0));

		for (int i = 0; i < numOnes; i++) {
			flatList.set(i, 1);
		}

		Collections.shuffle(flatList);

		// Convert to 2D list
		List<List<Integer>> result = new ArrayList<>();
		for (int i = 0; i < rows; i++) {
			result.add(flatList.subList(i * cols, (i + 1) * cols));
		}
		return result;
	}
}
