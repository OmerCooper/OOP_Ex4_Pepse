package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.trees.Flora;

public class PepseGameManager extends GameManager {
	public static void main(String[] args) {
		new PepseGameManager().run();
	}

	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()), Layer.BACKGROUND);
		Terrain terrain=new Terrain(windowController.getWindowDimensions(),1);
		for (Block b:terrain.createInRange(0,(int)windowController.getWindowDimensions().x())){
			gameObjects().addGameObject(b,Layer.DEFAULT);
		}
		Flora flora=new Flora(terrain);
		for (GameObject treePart:flora.createInRange(0,(int)windowController.getWindowDimensions().x())){
			gameObjects().addGameObject(treePart,Layer.DEFAULT);
		}
	}
}
