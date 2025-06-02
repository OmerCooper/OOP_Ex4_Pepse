package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.Transition;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
/**
 * TODO add class explenation
 *
 * @author omer and rotem
 */
public class Flora {

	private static final Color TRUNK_COLOR = new Color(100, 50, 20);
	private static final Color LEAF_COLOR = new Color(50, 200, 30);
	private static final int BLOCK_SIZE = Block.SIZE;
	private static final int LEAF_SIZE = BLOCK_SIZE;
	private static final float LEAF_SIZE_CHANGE = 1.1f;
	private static final float LEAF_ANGLE_CHANGE = 15f;
	private static final int TRUNK_WIDTH = BLOCK_SIZE;
	private static final int MAX_TRUNK_HEIGHT = 5;
	private static final int MIN_TRUNK_HEIGHT = 3;
	private static final float TREES_PROB = 0.1f;
	private static final float LEAF_PROB = 0.8f;
	private static final String LEAF_TAG = "leaf";
	private static final String TRUNK_TAG = "trunk";
	private Random random = new Random();
	private Terrain terrain;
	private int seed;

	/**
	 * constructor that just use the terrain
	 *
	 * @param terrain .
	 */
	public Flora(Terrain terrain, int seed) {
		this.terrain = terrain;
		this.seed = seed;

	}

	/**
	 * method that get a range between minx to maxX,
	 * create all the flora on this area, and return in as list of objects
	 *
	 * @param minX int
	 * @param maxX int
	 * @return list of objects
	 */
	public List<GameObject> createInRange(int minX, int maxX) {
		List<GameObject> treeParts = new ArrayList<>();
		minX = (minX / BLOCK_SIZE) * BLOCK_SIZE;
		maxX = (maxX / BLOCK_SIZE) * BLOCK_SIZE;
		for (int x = minX; x < maxX; x += BLOCK_SIZE) {
			Random random1 = new Random(Objects.hash(x, seed));
			if (random1.nextFloat() > TREES_PROB) continue; // create trees sparsely

			float groundHeight = terrain.groundHeightAt(x);
			int trunkHeight = MIN_TRUNK_HEIGHT + random1.nextInt(MAX_TRUNK_HEIGHT + 1 - MIN_TRUNK_HEIGHT);

			// Create trunk
			for (int i = 1; i <= trunkHeight; i++) {
				Vector2 pos = new Vector2(x, groundHeight - Block.SIZE * i);
				GameObject trunkBlock = new Block(pos, new RectangleRenderable(TRUNK_COLOR));
				trunkBlock.setTag(TRUNK_TAG);
				treeParts.add(trunkBlock);
			}

			// Create leaves in a square of size commpared to the height of the tree.
			float xPos, yPos;
			float leafStartY = groundHeight - Block.SIZE * trunkHeight;
			for (int dx = -trunkHeight / 2; dx <= trunkHeight / 2; dx++) {
				for (int dy = -trunkHeight / 2; dy <= trunkHeight / 2; dy++) {
					xPos = x + dx * LEAF_SIZE;
					yPos = leafStartY + dy * LEAF_SIZE;
					Vector2 leafPos = new Vector2(xPos, yPos);
					// create fruits where there are no leaves in high prob
					if (random1.nextFloat() > LEAF_PROB) {
						if (random1.nextFloat() < LEAF_PROB) {
							treeParts.add(FruitFactory.createFruit(leafPos));
						}
					} else {
						GameObject leaf = new GameObject(leafPos, new Vector2(LEAF_SIZE, LEAF_SIZE),
								new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)));
						leaf.setTag(LEAF_TAG);
						animateLeafWithSwing(leaf);
						animateLeafWithSizeChange(leaf);
						treeParts.add(leaf);
					}
				}
			}
		}
		return treeParts;
	}


	private void animateLeafWithSwing(GameObject leaf) {
		float delay = random.nextFloat(); // random deley
		float interval = 0.02f; // the animation interval
		float cycleLength = 2f; // the full cycle of the animation

		float[] t = {this.random.nextFloat()};
		boolean[] forward = {true};

		new ScheduledTask(leaf, delay, false, () -> {
			new ScheduledTask(leaf, interval, true, () -> {
				t[0] += interval / cycleLength;
				if (t[0] >= 1f) {
					t[0] = 0f;
					forward[0] = !forward[0];
				}

				Transition.updateAngle(
						t[0],
						(angle, ignoredForward) -> leaf.renderer().setRenderableAngle(angle),
						-LEAF_ANGLE_CHANGE,
						LEAF_ANGLE_CHANGE,
						forward[0]
				);
			});
		});
	}

	private void animateLeafWithSizeChange(GameObject leaf) {
		float delay = random.nextFloat() * 1f;
		float interval = 0.02f;
		float cycleLength = 2f;

		float[] t = {random.nextFloat()};
		boolean[] forward = {true};

		new ScheduledTask(leaf, delay, false, () -> {
			new ScheduledTask(leaf, interval, true, () -> {
				t[0] += interval / cycleLength;
				if (t[0] >= 1f) {
					t[0] = 0f;
					forward[0] = !forward[0];
				}

				Transition.updateSize(
						t[0],
						(size, ignoredForward) -> leaf.setDimensions(size),
						new Vector2(LEAF_SIZE / LEAF_SIZE_CHANGE, LEAF_SIZE / LEAF_SIZE_CHANGE),
						new Vector2(LEAF_SIZE * LEAF_SIZE_CHANGE, LEAF_SIZE * LEAF_SIZE_CHANGE),
						forward[0]
				);
			});
		});
	}
}
