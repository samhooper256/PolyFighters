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
	private static final int MIN_STACKROOT_WIDTH = 640, MIN_STACKROOT_HEIGHT = 360; //16:9 ratio
	private static final int DEFAULT_WIDTH = MIN_STACKROOT_WIDTH, DEFAULT_HEIGHT = MIN_STACKROOT_HEIGHT;
	
	private final Pane root;
	private final StackPane stackRoot;
	private final BorderPane borderPane;
	/** The left component of {@link #borderPane} */
	private Pane left;
	private TerrainPane terrainPane;
	
	private final Theme theme = Theme.TEST_THEME;
	
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
		
		root = (Pane) getRoot();
		
		stackRoot = new StackPane();
		stackRoot.setMinSize(MIN_STACKROOT_WIDTH, MIN_STACKROOT_HEIGHT);
		stackRoot.prefWidthProperty().bind(this.widthProperty());
		stackRoot.prefHeightProperty().bind(this.heightProperty());
		stackRoot.setBorder(Borders.of(Color.RED));
		stackRoot.setBackground(Backgrounds.of(Color.SKYBLUE));
		stackRoot.getChildren().add(0, borderPane);
		
		root.getChildren().add(stackRoot);
	}
	
	/** It is static so that we can call it inside the "super" call in the constructor. */
	private static Pane makeRoot() {
		return new Pane();
	}
	
	public TerrainPane getTerrainPane() {
		return terrainPane;
	}
}
