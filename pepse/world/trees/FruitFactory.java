package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
/**
 * TODO add class explenation
 *
 * @author omer and rotem
 */
public class FruitFactory {

	private static final Vector2 FRUIT_SIZE = new Vector2(Block.SIZE / 2, Block.SIZE / 2);
	private static final Color FRUIT_COLOR = new Color(255, 100, 100);
	private static final float REGENERATE_TIMER = 30f;
	private static final String FRUIT_TAG = "fruit";

	/**
	 * static method that allow as to create the fruit, and tag it.
	 *
	 * @param position- the location where the fruit will be located
	 * @return fruit
	 */
	public static GameObject createFruit(Vector2 position) {
		Vector2 newPos = new Vector2(position.x() + FRUIT_SIZE.x() / 2, position.y() + FRUIT_SIZE.y() / 2);
		GameObject fruit = new GameObject(newPos, FRUIT_SIZE, new OvalRenderable(FRUIT_COLOR));
		fruit.setTag(FRUIT_TAG);
		return fruit;
	}

	/**
	 * Hide the fruit and schedule it to reappear after 30 seconds
	 *
	 * @param fruit - the fruit Game object
	 */
	public static void eatFruit(GameObject fruit) {
		fruit.renderer().setRenderable(null); // Hide the fruit
		new ScheduledTask(fruit, REGENERATE_TIMER, false, () -> {
			fruit.renderer().setRenderable(new OvalRenderable(ColorSupplier.approximateColor(FRUIT_COLOR)));
		});
	}
}
