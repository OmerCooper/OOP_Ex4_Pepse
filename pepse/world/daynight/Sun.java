package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sun in the game world.
 * The sun moves in a circular arc to simulate its motion during a day-night cycle.
 */
public class Sun {

	private static final Color SUN_COLOR = Color.YELLOW;
	private static final float INITIAL_ANGLE = 0;
	private static final float FINAL_ANGLE = 360;

	/**
	 * Creates a sun GameObject that moves in a circular trajectory around a cycle center point
	 * to simulate the sun's movement during a day-night cycle.
	 *
	 * @param windowDimensions The dimensions of the game window.
	 * @param cycleLength The total length of the day-night cycle (in seconds).
	 * @param groundLevel The vertical position representing the groun level; used as the center of the sun's circular path.
	 * @return A GameObject representing the sun.
	 */
	public static GameObject create(Vector2 windowDimensions, float cycleLength, float groundLevel) {
		Vector2 initialSunCenter = new Vector2(windowDimensions.x() / 2, 0);
		Vector2 sunSize = new Vector2(windowDimensions.x() / 11, windowDimensions.x() / 11);
		GameObject sun = new GameObject(initialSunCenter, sunSize, new OvalRenderable(SUN_COLOR));
		sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		sun.setTag("sun");

		// The point around which the sun will rotate
		Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2, groundLevel);

		new Transition<Float>(
				sun,
				(Float angle) -> sun.setCenter(
						initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter)
				),
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
