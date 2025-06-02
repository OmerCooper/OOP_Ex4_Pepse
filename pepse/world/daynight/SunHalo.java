package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents a glowing halo around the sun.
 * The halo is a semi-transparent yellowish oval that follows the sun's movement across the screen.
 * @author omer and rotem
 */
public class SunHalo {

	private static final int RED_COLOR_COMPOMENT = 255;
	private static final int GREEN_COLOR_COMPOMENT = 255;
	private static final int BLUE_COLOR_COMPOMENT = 0;
	private static final int ALPHA_COLOR_COMPOMENT = 30;
	private static final float SIZE_BIGGER_THEN_SUN = 50;

	/**
	 * Creates a sun halo GameObject that visually follows the provided sun object.
	 * The halo is rendered as a larger, transparent yellow oval.
	 *
	 * @param sun The sun GameObject to follow.
	 * @return A GameObject representing the sun halo.
	 */
	public static GameObject create(GameObject sun) {
		Vector2 initialSunHaloCenter = new Vector2(sun.getCenter());
		Vector2 sunHaloSize = new Vector2(
				sun.getDimensions().x() + SIZE_BIGGER_THEN_SUN,
				sun.getDimensions().y() + SIZE_BIGGER_THEN_SUN);
		GameObject sunHalo = new GameObject(
				initialSunHaloCenter,
				sunHaloSize,
				new OvalRenderable(
						new Color(
								RED_COLOR_COMPOMENT,
								GREEN_COLOR_COMPOMENT,
								BLUE_COLOR_COMPOMENT,
								ALPHA_COLOR_COMPOMENT
						)
				)
		);
		sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		sunHalo.setTag("sunHalo");
		sunHalo.addComponent((deltaTime) -> sunHalo.setCenter(sun.getCenter()));
		return sunHalo;
	}
}
