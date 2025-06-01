package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
	public float groundHeightAtX0;
	public NoiseGenerator noiseGenerator;
	private static final Color BASE_GROUND_COLOR = new Color(212, 123,
			74);
	private static final int TERRAIN_DEPTH = 20;
	public Terrain(Vector2 windowDimensions, int seed){
		groundHeightAtX0=windowDimensions.y()*2/3;
		noiseGenerator=new NoiseGenerator(seed,(int)groundHeightAtX0);


	}
	public float groundHeightAt(float x) { float noise = (float) noiseGenerator.noise(x, Block.SIZE *7); return groundHeightAtX0 + noise; }

	public List<Block> createInRange(int minX, int maxX) {
		int blockSize=Block.SIZE;
		minX=((int)Math.floor(minX/blockSize))*blockSize;
		maxX=(int)Math.floor(maxX/blockSize)*blockSize;
		List<Block> lst = new ArrayList<>();
		for(int x=minX; x<=maxX; x+=blockSize){
			int y=(int)Math.floor(groundHeightAt(x)/blockSize)*blockSize;
			for(int j=0; j<TERRAIN_DEPTH; j++){
				Renderable rend = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
				Vector2 v = new Vector2(x, y+j*blockSize);
				Block block = new Block(v, rend);
				block.setTag("ground");
				lst.add(block);

			}

		}
		return lst;
	}
}
