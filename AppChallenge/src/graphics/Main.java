package graphics;

import java.io.InputStream;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import logic.ObstacleSize;
import logic.obstacles.ObstacleBase;
import logic.units.BasicUnit;

/**
 * The {@link Application} class for the project. No instances of this class should be created. It only has a public constructor
 * because it is required to by {@code Application}.
 * @author Sam Hooper
 * @author Ayuj Verma
 *
 */
public class Main extends Application {
	
	public static final String TITLE = "Congressional App Challenge";
	
	private static final String RESOURCES_PREFIX = "/resources/";
	
	private static final Player player = new Player();
	
	static {
		player.addUnits(new BasicUnit(), new BasicUnit(), new BasicUnit());
	}
	
	private static Stage primaryStage;
	
	/**
	 * Produces an {@link Optional} of the {@link InputStream} for a resource in the "resources" folder.
	 * If the resource could not be located, the returned {@code Optional} will be empty. Otherwise, it
	 * will contain the {@code InputStream}.
	 * @param filename the name of the resource file, including its file extension. Must be in the "resources" folder.
	 * @return an {@link Optional} possibly containing the {@link InputStream}.
	 */
	public static Optional<InputStream> getOptionalResourceStream(String filename) {
		return Optional.ofNullable(Main.class.getResourceAsStream(RESOURCES_PREFIX + filename));
	}
	
	/**
	 * Produces the {@link InputStream} for a resource in the "resources" folder.
	 * @param filename the name of the file, including its file extension. Must be in the "resources" folder.
	 * @return the {@link InputStream} for the resource indicated by the given filename.
	 * @throws IllegalArgumentException if the file does not exist.
	 */
	public static InputStream getResourceStream(String filename) {
		Optional<InputStream> stream = getOptionalResourceStream(filename);
		if(stream.isEmpty())
			throw new IllegalArgumentException("The resource at " + RESOURCES_PREFIX + filename + " does not exist");
		return stream.get();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Main.primaryStage = primaryStage;
		primaryStage.setTitle(TITLE);
		Level scene = new Level();
		primaryStage.setScene(scene);
//		TerrainGrid grid = scene.getTerrainPane().getGridWrap().getGrid();
//		grid.addUnit(new BasicUnit(), 1, 1);
//		grid.addUnit(new BasicEnemy(), 2, 1);
//		grid.addObstacleOrThrow(new ObstacleBase(ObstacleSize.SMALL, 3), 2, 2);
//		grid.addObstacleOrThrow(new ObstacleBase(ObstacleSize.LARGE, 3), 5, 5);
		primaryStage.show();
	}
	
	public static Stage getStage() {
		return primaryStage;
	}
	
	/**
	 * Returns the current {@link Level} that is being displayed, or {@code null} if no level
	 * is currently being displayed.
	 */
	public static Level currentLevel() {
		if(primaryStage.getScene() instanceof Level)
			return (Level) primaryStage.getScene();
		return null;
	}
	
	/**
	 * Must NOT be called on FX Thread.
	 * @param runnable
	 */
	public static void blockUntilFinished(Runnable runnable) {
		class Blocker {
			Object lock;
			volatile boolean notified;
			Blocker(Runnable r) {
				lock = new Object();
				notified = false;
				Platform.runLater(() -> {
					r.run();
					synchronized(lock) {
						lock.notify();
					}
					notified = true;
				});
				while(!notified) {
					try {
						synchronized(lock) {
							lock.wait();
						}
					}
					catch (InterruptedException e) {}
				}
				
			}
		}
		
		new Blocker(runnable);
	}
	
	public static Player getPlayer() {
		return player;
	}
}
