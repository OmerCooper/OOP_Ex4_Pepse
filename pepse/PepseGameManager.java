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
import pepse.world.weather.Cloud;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.weather.Rain;

import java.util.*;

public class PepseGameManager extends GameManager {
	private static final float CYCLE_LENGTH_SEC = 30;
	private static final float STARTING_X = 0;
	private Map<Integer, List<GameObject>> loadedChunks = new HashMap<>();
	private int chunkSize;
	private Vector2 windowDimensions;
	private Avatar avatar;
	private Terrain terrain;
	private Flora flora;

	public static void main(String[] args) {
		new PepseGameManager().run();
	}

	@Override
	public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		windowDimensions = windowController.getWindowDimensions();
		// sky
		gameObjects().addGameObject(Sky.create(windowDimensions), Layer.BACKGROUND);

		//Terrain and flora
		terrain=new Terrain(windowController.getWindowDimensions(),1);
		flora = new Flora(terrain,1);
		//add the terrain and the flora for the first chncks
		chunkSize = (int) windowController.getWindowDimensions().x()/2;
		for (int i = -1; i <= 1; i++) {
			addTerrainAndFlora(i,terrain,flora);
		}

		//night (day cycle)
		GameObject night = pepse.world.daynight.Night.create(windowDimensions, CYCLE_LENGTH_SEC);
		gameObjects().addGameObject(night, Layer.FOREGROUND);

		// sun
		GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH_SEC);
		gameObjects().addGameObject(sun, Layer.BACKGROUND + 1);
		// sunHalo
		GameObject sunHalo = SunHalo.create(sun);
		gameObjects().addGameObject(sunHalo, Layer.BACKGROUND + 2);


		//Avatar
		this.avatar = new Avatar(Vector2.of(STARTING_X, terrain.groundHeightAt(STARTING_X) - Avatar.AVATAR_HEIGHT), inputListener, imageReader);
		setCamera(new Camera(avatar, Vector2.ZERO,
				windowDimensions,
				windowDimensions));
		gameObjects().addGameObject(avatar, Layer.DEFAULT);

		//Cloud
		Cloud cloud = new Cloud();
		for (Block b : cloud.create(windowDimensions)) {
			gameObjects().addGameObject(b, Layer.BACKGROUND);
		}
		//Rain
		Rain rain = new Rain(cloud,
				(gameObject, layer) -> gameObjects().addGameObject(gameObject, layer),
				gameObjects()::removeGameObject);
		avatar.addListener(rain);

	}

	// add a the terrain part and flora for this chunk.
	private void addTerrainAndFlora(int chunk_num,Terrain terrain, Flora flora) {
		int minX = chunk_num * chunkSize;
		int maxX = minX + chunkSize;
		List<GameObject> list=new ArrayList<>();
		//Terrain
		for (Block b : terrain.createInRange(minX, maxX)) {
			gameObjects().addGameObject(b, Layer.DEFAULT);
			list.add(b);
		}
		//flora
		for (GameObject treePart : flora.createInRange(minX, maxX)) {
			int layer = Layer.DEFAULT;
			if (treePart.getTag().equals("leaf")) {
				layer = Layer.FOREGROUND;
			}
			this.gameObjects().addGameObject(treePart, layer);
			list.add(treePart);
		}
		loadedChunks.put(chunk_num,list);

	}

	// delete the terrain and flora of this chunk
	private void removeTerrainAndFlora(int chunk_num) {
		for (GameObject object : loadedChunks.get(chunk_num)) {
			gameObjects().removeGameObject(object);
		}
		loadedChunks.remove(chunk_num);
	}


	@Override
	/**
	 * override of the regular update, also add and remove chunck when needed.
	 */
	public void update(float deltaTime) {
		super.update(deltaTime);

		int currChunk = (int)Math.floor(avatar.getCenter().x() / chunkSize);
		int range = 1; // how many chunks to keep around avatar

		// Load chunks within range
		for (int i = currChunk - range; i <= currChunk + range; i++) {
			if (!loadedChunks.containsKey(i)) {
				addTerrainAndFlora(i, terrain, flora);
			}
		}

		// Remove chunks that are out of range
		List<Integer> toRemove = new ArrayList<>();
		for (int chunkNum : loadedChunks.keySet()) {
			if (Math.abs(chunkNum - currChunk) > range) {
				toRemove.add(chunkNum);
			}
		}
		for (int chunkNum : toRemove) {
			removeTerrainAndFlora(chunkNum);
		}
	}


}
