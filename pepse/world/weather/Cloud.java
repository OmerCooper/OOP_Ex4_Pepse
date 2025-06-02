package pepse.world.weather;

import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Avatar;
import pepse.world.AvatarListener;
import pepse.world.Block;
import danogl.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents a moving cloud made of multiple blocks that animates across the screen.
 * @author omer and rotem
 */
public class Cloud {

	private static final float WINDOW_DIMENSION_DIVIDE_BY = 8;

	private final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
	private final float CYCLE_LENGTH = 10;

	private Vector2 cloudMostLeftXCenterY;
	private List<Block> cloudBlocks = new ArrayList<>();

	/**
	 * Creates a cloud composed of multiple blocks based on a predefined pattern.
	 * Each block moves from right to left in a loop, forming a continuous cloud animation.
	 *
	 * @param windowDimensions The dimensions of the game window.
	 * @return A list of Block objects that make up the animated cloud.
	 */
	public List<Block> create(Vector2 windowDimensions) {
		//TODO set top left of cloud from earth
		Vector2 cloudTopLeftLoc =
				new Vector2(windowDimensions.x(), windowDimensions.y() / WINDOW_DIMENSION_DIVIDE_BY);

		java.util.List<java.util.List<Integer>> cloudShape = generateCloudShape();
		float cloudWidth = cloudShape.get(0).size() * Block.SIZE;
		float startX = windowDimensions.x() + cloudWidth;
		float endX = -cloudWidth;
		for (int row = 0; row < cloudShape.size(); row++) {
			for (int col = 0; col < cloudShape.get(row).size(); col++) {
				if (cloudShape.get(row).get(col) == 1) {
					Vector2 relativeOffset = new Vector2(col * Block.SIZE, row * Block.SIZE);
					Vector2 blockInitialPos = cloudTopLeftLoc.add(relativeOffset);

					Block block = new Block(blockInitialPos,
							new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR)));
					block.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
					block.setTag("cloud");
					Vector2 initialblockCenter = block.getCenter();
					int finalRow = row;
					int finalCol = col;
					new Transition<Float>(
							block,
							(x) -> {
								block.setCenter(new Vector2(x, block.getCenter().y()));
								updateCloudMostLeft();
							},
							startX - relativeOffset.x(),
							endX - relativeOffset.x(),
							Transition.LINEAR_INTERPOLATOR_FLOAT,
							CYCLE_LENGTH,
							Transition.TransitionType.TRANSITION_LOOP,
							null
					);
					cloudBlocks.add(block);
				}
			}
		}
		return cloudBlocks;
	}

	private List<List<Integer>> generateCloudShape() {
		java.util.List<java.util.List<Integer>> cloudShape =
		List.of(
				List.of(0, 1, 1, 0, 0, 0),
				List.of(1, 1, 1, 0, 1, 0),
				List.of(1, 1, 1, 1, 1, 1),
				List.of(1, 1, 1, 1, 1, 1),
				List.of(0, 1, 1, 1, 0, 0),
				List.of(0, 0, 0, 0, 0, 0)
		);
		return cloudShape;
	}

	/**
	 * Updates the center Y position and the leftmost X coordinate of the cloud,
	 * based on the current positions of all its blocks.
	 */
	private void updateCloudMostLeft() {
		float mostLeftX = cloudBlocks.get(0).getCenter().x();
		float sumY = cloudBlocks.get(0).getCenter().y();
		for (Block b : cloudBlocks) {
			if (b.getTopLeftCorner().x() < mostLeftX) {
				mostLeftX = b.getTopLeftCorner().x();
			}
			sumY = sumY + b.getCenter().y();
		}
		float centerY = sumY / cloudBlocks.size();
		cloudMostLeftXCenterY = new Vector2(mostLeftX, centerY);
	}

	/**
	 * Returns the center Y coordinate and the leftmost X coordinate of the animated cloud.
	 *
	 * @return A Vector2 containing the leftmost X and average center Y of the cloud.
	 */
	public Vector2 getCloudCenterLeft() {
		return cloudMostLeftXCenterY;
	}
}
