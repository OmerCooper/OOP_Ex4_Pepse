package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {

	private static final Color SUN__HALO_COLOR = Color.YELLOW;
	private static final float INITIAL_ANGLE = 0;
	private static final float FINAL_ANGLE = 360;
	private static final int RED_COLOR_COMPOMENT = 255;
	private static final int GREEN_COLOR_COMPOMENT = 255;
	private static final int BLUE_COLOR_COMPOMENT = 0;
	private static final int ALPHA_COLOR_COMPOMENT = 50;
	private static final float SIZE_BIGGER_THEN_SUN = 50;

	public static GameObject create(GameObject sun) {
		Vector2 initialSunHaloCenter = new Vector2(sun.getCenter());
		Vector2 sunHaloSize = new Vector2(sun.getDimensions().x()+SIZE_BIGGER_THEN_SUN,
				sun.getDimensions().y()+SIZE_BIGGER_THEN_SUN);
		GameObject sunHalo = new GameObject(initialSunHaloCenter, sunHaloSize, new OvalRenderable
				(new Color(RED_COLOR_COMPOMENT,GREEN_COLOR_COMPOMENT,BLUE_COLOR_COMPOMENT,ALPHA_COLOR_COMPOMENT)));
		sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		sun.setTag("sunHalo");
		sunHalo.addComponent((deltaTime) ->sunHalo.setCenter(sun.getCenter()));
		return sunHalo;
	}
}
