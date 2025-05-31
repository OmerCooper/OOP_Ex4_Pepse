package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {

	private static final Color SUN_COLOR = Color.YELLOW;
	private static final float INITIAL_ANGLE = 0;
	private static final float FINAL_ANGLE = 360;

	public static GameObject create(Vector2 windowDimensions,float cycleLength) {
		Vector2 initialSunCenter = new Vector2(windowDimensions.x() / 2, 0);
		Vector2 sunSize = new Vector2(windowDimensions.x() / 10, windowDimensions.x() / 10);
		GameObject sun = new GameObject(initialSunCenter, sunSize, new OvalRenderable(SUN_COLOR));
		sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		sun.setTag("sun");
		//TODO y of cycleCenter is to be ground level from Rotem
		Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
		new Transition<Float>(
				sun,
				(Float angle) -> sun.setCenter
				(initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter)),
				INITIAL_ANGLE,
				FINAL_ANGLE,
				Transition.LINEAR_INTERPOLATOR_FLOAT,
				cycleLength,
				Transition.TransitionType.TRANSITION_LOOP,
				null
		);
		return sun;
	}
}
