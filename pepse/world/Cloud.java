package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Cloud {

	private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
	private static final float CYCLE_LENGTH = 10;

	public static List<Block> create(Vector2 windowDimensions) {
		//TODO set top left of cloud from earth
		Vector2 cloudTopLeftLoc = new Vector2(windowDimensions.x(), windowDimensions.y()/8);

		List<Block> cloudBlocks = new ArrayList<>();

		java.util.List<java.util.List<Integer>> cloudShape = List.of(
				List.of(0, 1, 1, 0, 0, 0),
				List.of(1, 1, 1, 0, 1, 0),
				List.of(1, 1, 1, 1, 1, 1),
				List.of(1, 1, 1, 1, 1, 1),
				List.of(0, 1, 1, 1, 0, 0),
				List.of(0, 0, 0, 0, 0, 0)
		);
		float cloudWidth = cloudShape.get(0).size() * Block.SIZE;
		float startX = windowDimensions.x() + cloudWidth;
		float endX = -cloudWidth;


		for(int row=0;row<cloudShape.size();row++) {
			for (int col=0;col<cloudShape.get(row).size();col++) {
				if(cloudShape.get(row).get(col)==1) {
					Vector2 relativeOffset = new Vector2(col * Block.SIZE, row * Block.SIZE);
					Vector2 blockInitialPos = cloudTopLeftLoc.add(relativeOffset);

					Block block = new Block(blockInitialPos,
							new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR)));
					block.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
					block.setTag("cloud");

					//annimation
					Vector2 initialblockCenter = block.getCenter();
					new Transition<Float>(
							block,
							(x) -> block.setCenter(new Vector2(x, block.getCenter().y())),
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
}
