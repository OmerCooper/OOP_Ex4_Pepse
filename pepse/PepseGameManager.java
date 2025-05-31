package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Block;
import pepse.world.Sky;
<<<<<<< HEAD
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
=======
import pepse.world.Terrain;
>>>>>>> a72c363986ee79632deda6722fe3eb1b9396edcc

public class PepseGameManager extends GameManager {
	private static final float CYCLE_LENGTH_SEC = 30;

	public static void main(String[] args) {
		new PepseGameManager().run();
	}

	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		// sky
		gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()), Layer.BACKGROUND);
<<<<<<< HEAD

		//night (day cycle)
		GameObject night = pepse.world.daynight.Night.create(windowController.getWindowDimensions(),CYCLE_LENGTH_SEC);
		gameObjects().addGameObject(night, Layer.FOREGROUND);

		// sun
		GameObject sun = Sun.create(windowController.getWindowDimensions(),CYCLE_LENGTH_SEC);
		gameObjects().addGameObject(sun, Layer.BACKGROUND+1);
		// sunHalo
		GameObject sunHalo = SunHalo.create(sun);
		gameObjects().addGameObject(sunHalo, Layer.BACKGROUND+2);
=======
		Terrain terrain=new Terrain(windowController.getWindowDimensions(),1);
		for (Block b:terrain.createInRange(0,(int)windowController.getWindowDimensions().x())){
			gameObjects().addGameObject(b,Layer.DEFAULT);
		}
>>>>>>> a72c363986ee79632deda6722fe3eb1b9396edcc
	}
}
