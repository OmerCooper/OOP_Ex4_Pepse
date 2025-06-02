package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the terrain in the game world.
 * Uses procedural noise to generate ground height and creates blocks to render it.
 * @author omer and rotem
 */
public class Terrain {
	public float groundHeightAtX0;
	public NoiseGenerator noiseGenerator;
	private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
	private static final int TERRAIN_DEPTH = 20;

	/**
	 * Constructs a new Terrain instance.
	 *
	 * @param windowDimensions The dimensions of the game window.
	 * @param seed             A seed value for generating consistent terrain using noise.
	 */
	public Terrain(Vector2 windowDimensions, int seed) {
		groundHeightAtX0 = windowDimensions.y() * 2 / 3;
		noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
	}

	/**
	 * Returns the terrain height (y-coordinate) at the specified x-coordinate.
	 *
	 * @param x The x-coordinate in the world.
	 * @return The computed ground height based on Perlin-like noise.
	 */
	public float groundHeightAt(float x) {
		float noise = (float) noiseGenerator.noise(x, Block.SIZE * 7);
		return groundHeightAtX0 + noise;
	}

	/**
	 * Creates blocks representing terrain between two x-values.
	 *
	 * @param minX The minimum x-value of the terrain range.
	 * @param maxX The maximum x-value of the terrain range.
	 * @return A list of Blocks forming the terrain within the given range.
	 */
	public List<Block> createInRange(int minX, int maxX) {
		int blockSize = Block.SIZE;
		minX = ((int) Math.floor(minX / blockSize)) * blockSize;
		maxX = (int) Math.floor(maxX / blockSize) * blockSize;
		List<Block> lst = new ArrayList<>();
		for (int x = minX; x <= maxX; x += blockSize) {
			int y = (int) Math.floor(groundHeightAt(x) / blockSize) * blockSize;
			for (int j = 0; j < TERRAIN_DEPTH; j++) {
				Renderable rend = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
				Vector2 v = new Vector2(x, y + j * blockSize);
				Block block = new Block(v, rend);
				block.setTag("ground");
				lst.add(block);
			}
		}
		return lst;
	}
}
