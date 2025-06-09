package pepse.util;

import danogl.util.Vector2;

import java.util.function.BiConsumer;
/**
 * class that creates the call back functions for the transitions.
 *
 * @author omer and rotem
 */
public class Transition {

	/**
	 * gives us an update for the angle, depend on going backward or forword
	 *
	 * @param t        where we at
	 * @param onUpdate the lambda
	 * @param min      - min angle
	 * @param max      - max angle
	 * @param forward  - boolean
	 */
	public static void updateAngle(float t,
								   BiConsumer<Float, Boolean> onUpdate,
								   float min,
								   float max,
								   boolean forward) {
		float interp = forward ? t : 1 - t;
		float value = getValBetween(min, max, interp);
		onUpdate.accept(value, forward);
	}

	/**
	 * update the size for setDimentions
	 *
	 * @param t        val
	 * @param onUpdate the func lambda
	 * @param min      -min
	 * @param max      - max
	 * @param forward  -bool
	 */
	public static void updateSize(float t,
								  BiConsumer<Vector2, Boolean> onUpdate,
								  Vector2 min,
								  Vector2 max,
								  boolean forward) {
		float interp = forward ? t : 1 - t;
		float x = getValBetween(min.x(), max.x(), interp);
		float y = getValBetween(min.y(), max.y(), interp);
		onUpdate.accept(new Vector2(x, y), forward);
	}

	/**
	 * calculate the advance value in compare to min and max
	 *
	 * @param a - min
	 * @param b -max
	 * @param t - value between 0 to 1
	 * @return float
	 */
	private static float getValBetween(float a, float b, float t) {
		return a + t * (b - a);
	}
}
