package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

public class PepseGameManager extends GameManager {
	private static final float CYCLE_LENGTH_SEC = 30;
	private static final float STARTING_X = 0;

	public static void main(String[] args) {
		new PepseGameManager().run();
	}

	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		// sky
		gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()), Layer.BACKGROUND);

		//Terrain
		Terrain terrain=new Terrain(windowController.getWindowDimensions(),1);
		for (Block b:terrain.createInRange(0,(int)windowController.getWindowDimensions().x())){
			gameObjects().addGameObject(b,Layer.DEFAULT);
		}

		//night (day cycle)
		GameObject night = pepse.world.daynight.Night.create(windowController.getWindowDimensions(),CYCLE_LENGTH_SEC);
		gameObjects().addGameObject(night, Layer.FOREGROUND);

		// sun
		GameObject sun = Sun.create(windowController.getWindowDimensions(),CYCLE_LENGTH_SEC);
		gameObjects().addGameObject(sun, Layer.BACKGROUND+1);
		// sunHalo
		GameObject sunHalo = SunHalo.create(sun);
		gameObjects().addGameObject(sunHalo, Layer.BACKGROUND+2);


		//Avatar
		Avatar avatar = new Avatar(Vector2.of(STARTING_X, terrain.groundHeightAt(STARTING_X)-Avatar.AVATAR_HEIGHT), inputListener,imageReader);
		setCamera(new Camera(avatar, Vector2.ZERO,
				windowController.getWindowDimensions(),
				windowController.getWindowDimensions()));
		gameObjects().addGameObject(avatar, Layer.DEFAULT);

		//Cloud
		Cloud cloud = new Cloud();
		for (Block b:cloud.create(windowController.getWindowDimensions())) {
			gameObjects().addGameObject(b, Layer.BACKGROUND);
		}
		Flora flora = new Flora(terrain);

		for(GameObject treePart : flora.createInRange(0, (int)windowController.getWindowDimensions().x())) {
			int layer = Layer.DEFAULT;
			if (treePart.getTag().equals("leaf")) {
				layer = Layer.FOREGROUND;
			}

			this.gameObjects().addGameObject(treePart, layer);
		}



	}
}
