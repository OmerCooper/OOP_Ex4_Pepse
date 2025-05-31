package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
	public float groundHeightAtX0;
	private static final Color BASE_GROUND_COLOR = new Color(212, 123,
			74);
	public Terrain(Vector2 windowDimensions, int seed){
		groundHeightAtX0=windowDimensions.y()*2/3;


	}
	public float groundHeightAt(float x) { return groundHeightAtX0; }

	public List<Block> createInRange(int minX, int maxX) {
		Renderable rend =new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
		Vector2 v=new Vector2(100,groundHeightAt(100));
		Block block=new Block(v, rend);
		List<Block> lst=new ArrayList<>();
		lst.add(block);
		return lst;
	}
}
