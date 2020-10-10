package graphics;

import fxutils.*;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * @author Sam Hooper
 *
 */
public class Level extends Scene {
	public static final Theme DEFAULT_THEME = Theme.TEST_THEME;
	
	private static final int MIN_STACKROOT_WIDTH = 640, MIN_STACKROOT_HEIGHT = 360; //16:9 ratio TODO maybe make these proportional to the user's screen dimensions?
	private static final int DEFAULT_WIDTH = MIN_STACKROOT_WIDTH, DEFAULT_HEIGHT = MIN_STACKROOT_HEIGHT;
	private static final double INFO_SCREEN_PERCENT = 0.15; //percentage of the screen the InfoPanel will take up
	
	/**
	 * {@code Level.current()} is equivalent to {@link Main#currentLevel()}.
	 */
	public static final Level current() {
		return Main.currentLevel();
	}
	
	private final Pane root;
	private final StackPane stackRoot;
	private final BorderPane borderPane;
	private final InfoPanel infoPanel;
	/** The left component of {@link #borderPane} */
	private final Pane left;
	private final TerrainPane terrainPane;
	private final Theme theme;
	//the sum of the difficulties of the EnemyUnits added on each turn must be no more than twice turnDifficulty.
	private final double turnDifficulty;
	/**The Property that {@link #stackRoot}'s widthProperty will be bound to. */
	private final DoublePropertyBase stackRootWidth = new DoublePropertyBase() {
		@Override
		public double get() {
			return Math.max(MIN_STACKROOT_WIDTH, Level.this.getWidth());
		}
		@Override public Object getBean() { return null; }
		@Override public String getName() { return "stackRootWidth"; }
	};
	/**The Property that {@link #stackRoot}'s heightProperty will be bound to. */
	private final DoublePropertyBase stackRootHeight = new DoublePropertyBase() {
		@Override
		public double get() {
			return Math.max(MIN_STACKROOT_HEIGHT, Level.this.getHeight());
		}
		@Override public Object getBean() { return null; }
		@Override public String getName() { return "stackRootHeight"; }
	};
	
	
	
	
	
	public Level() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_THEME);
	}
	
	public Level(int width, int height, Theme theme) {
		super(makeRoot(), width, height);
		
		this.theme = theme;
		this.turnDifficulty = 2; //must be set before terrainPane construction.
		
		left = new VBox(new Button("Button 1"));
		
		terrainPane = new TerrainPane(this, theme);
		terrainPane.setBorder(Borders.of(Color.BLACK));
		borderPane = new BorderPane();
		
		infoPanel = new InfoPanel();
		infoPanel.prefWidthProperty().bind(borderPane.widthProperty().multiply(INFO_SCREEN_PERCENT));
		
		borderPane.setLeft(left);
		borderPane.setRight(infoPanel);
		borderPane.setCenter(terrainPane);
		
		root = (Pane) getRoot();
		
		stackRoot = new StackPane();
		stackRoot.setMinSize(MIN_STACKROOT_WIDTH, MIN_STACKROOT_HEIGHT);
		stackRoot.prefWidthProperty().bind(stackRootWidth);
		stackRoot.prefHeightProperty().bind(stackRootHeight);
		stackRoot.setBorder(Borders.of(Color.RED));
		stackRoot.setBackground(Backgrounds.of(Color.SANDYBROWN));
		stackRoot.getChildren().add(0, borderPane);
		
		root.getChildren().add(stackRoot);
	}
	
	/* It is static so that we can call it inside the "super" call in the constructor. */
	private static Pane makeRoot() {
		return new Pane();
	}
	
	public TerrainPane getTerrainPane() {
		return terrainPane;
	}
	
	public InfoPanel getInfoPanel() {
		return infoPanel;
	}
	
	public Theme getTheme() {
		return theme;
	}
	
	public double getTurnDifficulty() {
		return turnDifficulty;
	}
}
