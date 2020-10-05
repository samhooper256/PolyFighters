package graphics;

import java.io.InputStream;
import java.util.Optional;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.Board;
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
		primaryStage.setTitle(TITLE);
		Level scene = new Level();
		primaryStage.setScene(scene);
		scene.getTerrainPane().getGridWrap().getGrid().addUnit(new BasicUnit(), 1, 1);
		primaryStage.show();
	}
}
