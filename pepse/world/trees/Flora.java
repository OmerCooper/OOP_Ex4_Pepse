package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flora {

	private static final Color TRUNK_COLOR = new Color(100, 50, 20);
	private static final Color LEAF_COLOR = new Color(50, 200, 30);
	private static final int BLOCK_SIZE = Block.SIZE;
	private static final int LEAF_SIZE = BLOCK_SIZE;
	private static final int TRUNK_WIDTH = BLOCK_SIZE;
	private static final int MAX_TRUNK_HEIGHT = 5;
	private static final int MIN_TRUNK_HEIGHT = 3;
	private static final float TREES_PROB = 0.1f;

	private static final Random random = new Random();
	private Terrain terrain;

	public Flora(Terrain terrain) {
		this.terrain = terrain;
	}


	public List<GameObject> createInRange(int minX, int maxX) {
		List<GameObject> treeParts = new ArrayList<>();
		minX=(minX/BLOCK_SIZE)*BLOCK_SIZE;
		maxX=(maxX/BLOCK_SIZE)*BLOCK_SIZE;
		for (int x = minX; x < maxX; x += BLOCK_SIZE) {
			if (random.nextFloat() > TREES_PROB) continue; // create trees sparsely

			float groundHeight = terrain.groundHeightAt(x);
			int trunkHeight = MIN_TRUNK_HEIGHT + random.nextInt(MAX_TRUNK_HEIGHT - MIN_TRUNK_HEIGHT);

			// Create trunk
			for (int i = 1; i <= trunkHeight; i++) {
				Vector2 pos = new Vector2(x, groundHeight - Block.SIZE * i);
				GameObject trunkBlock = new Block(pos, new RectangleRenderable(TRUNK_COLOR));
				trunkBlock.setTag("trunk");
				treeParts.add(trunkBlock);
			}

			// Create leaves in a square
			float leafStartY = groundHeight - Block.SIZE * trunkHeight;
			for (int dx = -1; dx <= 1; dx++) {
				for (int dy = -1; dy <= 1; dy++) {
					Vector2 leafPos = new Vector2(x + dx * LEAF_SIZE, leafStartY + dy * LEAF_SIZE);
					GameObject leaf = new GameObject(leafPos, new Vector2(LEAF_SIZE, LEAF_SIZE),
							new OvalRenderable(LEAF_COLOR));
					leaf.setTag("leaf");
					treeParts.add(leaf);

					// TODO: Add Transition here to make leaf sway or pulse
				}
			}

			// TODO: Add fruit on top of leaves with small chance
		}

		return treeParts;
	}
}
