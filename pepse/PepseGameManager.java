package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.weather.Cloud;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.weather.Rain;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Main game manager for the Pepse world.
 * Handles initialization of terrain, avatar, weather effects, day-night cycle, and chunk-based world loading.
 */
public class PepseGameManager extends GameManager {
	private static final float CYCLE_LENGTH_SEC = 30;
	private static final float STARTING_X = 0;
	private static final float TEXT_WIDTH = 60;
	private static final float TEXT_HEIGHT = 60;

	private Map<Integer, List<GameObject>> loadedChunks = new HashMap<>();
	private int chunkSize;
	private Vector2 windowDimensions;
	private Avatar avatar;
	private Terrain terrain;
	private Flora flora;
	private GameObject energyText;

	/**
	 * Entry point of the program. Launches the game.
	 * @param args arguments from user
	 * @author omer and rotem
	 */
	public static void main(String[] args) {
		new PepseGameManager().run();
	}

	@Override
	/**
	 * Initializes the game by setting up all visual and logical components including:
	 * terrain, sky, day-night cycle, avatar, weather, and dynamic chunk loading.
	 */
	public void initializeGame(ImageReader imageReader,
							   SoundReader soundReader,
							   UserInputListener inputListener,
							   WindowController windowController) {
		super.initializeGame(imageReader, soundReader, inputListener, windowController);
		windowDimensions = windowController.getWindowDimensions();
		// Sky
		gameObjects().addGameObject(Sky.create(windowDimensions), Layer.BACKGROUND);

		// Terrain
		terrain = new Terrain(windowController.getWindowDimensions(), 1);
		// Avatar
		createAvatar(imageReader,inputListener);
		// flora
		flora = new Flora(terrain, 1,avatar::addEnergy);
		chunkSize = (int) windowController.getWindowDimensions().x() / 2;
		for (int i = -1; i <= 1; i++) {
			addTerrainAndFlora(i, terrain, flora);
		}
		// Night cycle
		GameObject night = Night.create(windowDimensions, CYCLE_LENGTH_SEC);
		gameObjects().addGameObject(night, Layer.FOREGROUND);

		// Sun and sun halo
		GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH_SEC, terrain.groundHeightAt(0));
		gameObjects().addGameObject(sun, Layer.BACKGROUND + 1);
		GameObject sunHalo = SunHalo.create(sun);
		gameObjects().addGameObject(sunHalo, Layer.BACKGROUND + 2);



		// Cloud
		Cloud cloud = new Cloud();
		for (Block b : cloud.create(windowDimensions)) {
			gameObjects().addGameObject(b, Layer.BACKGROUND);
		}
		// Rain
		Rain rain = new Rain(cloud,
				(gameObject, layer) -> gameObjects().addGameObject(gameObject, layer),
				gameObjects()::removeGameObject);
		avatar.addListener(rain);

		// Energy display text
		displayText();
	}

	private void createAvatar(ImageReader imageReader,
							  UserInputListener inputListener) {
		this.avatar = new Avatar(Vector2.of(STARTING_X,
				terrain.groundHeightAt(STARTING_X) - Avatar.AVATAR_HEIGHT), inputListener, imageReader);
		setCamera(new Camera(avatar, Vector2.ZERO, windowDimensions, windowDimensions));
		avatar.setTag("avatar");
		gameObjects().addGameObject(avatar, Layer.DEFAULT);
	}

	private void displayText() {
		TextRenderable textRenderable = new TextRenderable(Integer.toString((int) avatar.getEnergy()) + '%');
		textRenderable.setColor(Color.BLACK);
		Vector2 textLoc = new Vector2(0, 0);
		Vector2 textSize = new Vector2(TEXT_WIDTH, TEXT_HEIGHT);
		energyText = new GameObject(textLoc, textSize, textRenderable);
	}

	/**
	 * Adds terrain and flora for a given chunk number and stores them in loadedChunks.
	 *
	 * @param chunk_num The chunk index to generate.
	 * @param terrain   The Terrain generator.
	 * @param flora     The Flora generator.
	 */
	private void addTerrainAndFlora(int chunk_num, Terrain terrain, Flora flora) {
		int minX = chunk_num * chunkSize;
		int maxX = minX + chunkSize;
		List<GameObject> list = new ArrayList<>();

		for (Block b : terrain.createInRange(minX, maxX)) {
			gameObjects().addGameObject(b, Layer.DEFAULT);
			list.add(b);
		}
		for (GameObject treePart : flora.createInRange(minX, maxX)) {
			int layer = treePart.getTag().equals("leaf") ? Layer.FOREGROUND : Layer.DEFAULT;
			this.gameObjects().addGameObject(treePart, layer);
			list.add(treePart);
		}
		loadedChunks.put(chunk_num, list);
	}

	/**
	 * Removes all terrain and flora associated with the given chunk number.
	 *
	 * @param chunk_num The chunk index to remove.
	 */
	private void removeTerrainAndFlora(int chunk_num) {
		for (GameObject object : loadedChunks.get(chunk_num)) {
			gameObjects().removeGameObject(object);
		}
		loadedChunks.remove(chunk_num);
	}

	@Override
	/**
	 * Called once per frame. Updates energy display and handles dynamic loading/unloading of world chunks.
	 *
	 * @param deltaTime Time in seconds since the last frame.
	 */
	public void update(float deltaTime) {
		super.update(deltaTime);

		// Update energy text
		if (energyText != null) {
			gameObjects().removeGameObject(energyText, Layer.UI);
		}
		TextRenderable textRenderable = new TextRenderable(Integer.toString((int) avatar.getEnergy()) + '%');
		textRenderable.setColor(Color.BLACK);
		Vector2 textLoc = new Vector2(0, 0);
		Vector2 textSize = new Vector2(TEXT_WIDTH, TEXT_HEIGHT);
		energyText = new GameObject(textLoc, textSize, textRenderable);
		energyText.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
		gameObjects().addGameObject(energyText, Layer.UI);

		// Determine and update chunks around the avatar
		int currChunk = (int) Math.floor(avatar.getCenter().x() / chunkSize);
		int range = 1;

		for (int i = currChunk - range; i <= currChunk + range; i++) {
			if (!loadedChunks.containsKey(i)) {
				addTerrainAndFlora(i, terrain, flora);
			}
		}

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
