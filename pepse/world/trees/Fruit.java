package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.function.Consumer;

/**
 * TODO add class explenation
 *
 * @author omer and rotem
 */
public class Fruit extends GameObject {

	private static final Vector2 FRUIT_SIZE = new Vector2(Block.SIZE / 2, Block.SIZE / 2);
	private static final Color FRUIT_COLOR = new Color(255, 100, 100);
	private static final float REGENERATE_TIMER = 30f;
	private static final float ENERGY_ADDITION = 10f;
	private static final String FRUIT_TAG = "fruit";
	private final Consumer<Float> addEnergyFunc;

	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner Position of the object, in window coordinates (pixels).
	 *                      Note that (0,0) is the top-left corner of the window.
	 * @param dimensions    Width and height in window coordinates.
	 * @param renderable    The renderable representing the object. Can be null, in which case
	 *                      the GameObject will not be rendered.
	 */
	public Fruit(Vector2 position, Consumer<Float> addEnergy) {
		super(new Vector2(position.x() + FRUIT_SIZE.x() / 2, position.y() + FRUIT_SIZE.y() / 2), FRUIT_SIZE, new OvalRenderable(FRUIT_COLOR));
		this.addEnergyFunc=addEnergy;
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

	@Override
	/**
	 * Called on the first frame of a collision.
	 *
	 * @param other     The GameObject with which a collision occurred.
	 * @param collision Information regarding this collision.
	 *                  A reasonable elastic behavior can be achieved with:
	 *                  setVelocity(getVelocity().flipped(collision.getNormal()));
	 */
	public void onCollisionEnter(GameObject other, Collision collision) {
		super.onCollisionEnter(other, collision);
		if(other.getTag().equals("avatar")){
			this.renderer().setRenderable(null);
			new ScheduledTask(this, REGENERATE_TIMER, false, () -> {
				this.renderer().setRenderable(new OvalRenderable(ColorSupplier.approximateColor(FRUIT_COLOR)));
			});
			this.addEnergyFunc.accept(ENERGY_ADDITION);
		}
	}
}
