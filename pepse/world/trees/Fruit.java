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
 * class that creates fruit object, and hendle its disapperance.
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
	 * creates new fruit
	 * @param position - the vector possition for the fruit
	 * @param addEnergy - energy call back from avatar
	 */
	public Fruit(Vector2 position, Consumer<Float> addEnergy) {
		super(new Vector2(position.x() + FRUIT_SIZE.x() / 2, position.y() + FRUIT_SIZE.y() / 2), FRUIT_SIZE,
				new OvalRenderable(FRUIT_COLOR));
		this.addEnergyFunc=addEnergy;
		this.setTag("fruit");
	}


	@Override
	/**
	 * Called on the first frame of a collision.
	 * here it adds energy and make the fruit disappear and come back after 30 sec
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
