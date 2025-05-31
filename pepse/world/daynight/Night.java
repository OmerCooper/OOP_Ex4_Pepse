package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {

	private static final Color NIGHT_COLOR = Color.BLACK;
	private static final float MIDNIGHT_OPACITY = 0.5F;
	private static final float INITIAL_OPACITY = 0;

	public static GameObject create(Vector2 windowDimensions,float cycleLength){
		GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
				new RectangleRenderable(NIGHT_COLOR));
		night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		night.setTag("night");
		new Transition<Float>(
				night,
				night.renderer()::setOpaqueness,
				INITIAL_OPACITY,
				MIDNIGHT_OPACITY,
				Transition.CUBIC_INTERPOLATOR_FLOAT,
				cycleLength/2,
				Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
				null
		);
		return night;
	}
}
