//package pepse.world;
//
//import danogl.GameObject;
//import danogl.components.CoordinateSpace;
//import danogl.components.Transition;
//import danogl.gui.rendering.OvalRenderable;
//import danogl.gui.rendering.RectangleRenderable;
//import danogl.util.Vector2;
//import pepse.util.ColorSupplier;
//
//import java.awt.*;
//
//public class Cloud {
//
//	private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
//
//	public static GameObject create(Vector2 windowDimensions) {
//
//
//		List<List<Integer>> cloud = List.of(
//				List.of(0, 1, 1, 0, 0, 0),
//				List.of(1, 1, 1, 0, 1, 0),
//				List.of(1, 1, 1, 1, 1, 1),
//				List.of(1, 1, 1, 1, 1, 1),
//				List.of(0, 1, 1, 1, 0, 0),
//				List.of(0, 0, 0, 0, 0, 0)
//		);
//		for(int col=0;col<cloud.getWidth();col++) {
//			for(int row=0;row<cloud.getHeight();row++) {
//				new Block()
//
//			}
//
//		}
//		new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR)
//		GameObject sun = new GameObject();
//
//
//
////		GameObject cloud = new GameObject(initialSunCenter, sunSize, new OvalRenderable(SUN_COLOR));
//		cloud_shape.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
//		cloud_shape.setTag("cloud");
//		Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
//		new Transition<Float>(
//				sun,
//				(Float angle) -> sun.setCenter
//						(initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter)),
//				INITIAL_ANGLE,
//				FINAL_ANGLE,
//				Transition.LINEAR_INTERPOLATOR_FLOAT,
//				cycleLength,
//				Transition.TransitionType.TRANSITION_LOOP,
//				null
//		);
//		return sun;
//	}
//}
