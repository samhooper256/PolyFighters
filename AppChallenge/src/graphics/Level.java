package graphics;

import fxutils.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * @author Sam Hooper
 *
 */
public class Level extends Scene {
	private static final int DEFAULT_WIDTH = 600, DEFAULT_HEIGHT = 400;
	
	private final StackPane root;
	private BorderPane borderPane;
	/** The left component of {@link #borderPane} */
	private Pane left;
	private TerrainPane terrainPane;
	
	public Level() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	public Level(int width, int height) {
		super(makeRoot(), width, height);
		
		left = new VBox(new Button("Button 1"));
		
		terrainPane = new TerrainPane();
		terrainPane.setBorder(Borders.of(Color.BLACK));
		
		borderPane = new BorderPane();
		borderPane.setLeft(left);
		borderPane.setCenter(terrainPane);
		
		root = (StackPane) getRoot();
		root.setBorder(Borders.of(Color.RED));
		root.setBackground(Backgrounds.of(Color.SKYBLUE));
		root.getChildren().add(0, borderPane);
	}
	
	/** It is static so that we can call it inside the "super" call in the constructor. */
	private static StackPane makeRoot() {
		return new StackPane();
	}
}
