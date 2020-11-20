package graphics;

import fxutils.*;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoublePropertyBase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import logic.Turn;

/**
 * @author Sam Hooper
 *
 */
public class Level extends Scene {
	public static final Theme DEFAULT_THEME = Theme.DEFAULT_THEME;
	
	private static final int MIN_STACKROOT_WIDTH = 640, MIN_STACKROOT_HEIGHT = 360; //16:9 ratio TODO maybe make these proportional to the user's screen dimensions?
	private static final int DEFAULT_WIDTH = MIN_STACKROOT_WIDTH, DEFAULT_HEIGHT = MIN_STACKROOT_HEIGHT;
	private static final double SIDE_PANEL_SCREEN_PERCENT = 0.15; //percentage of the screen the InfoPanel will take up
	private static final int MOVES_PER_ENEMY_UNIT = 2;
	private static final int MOVES_PER_PLAYER_UNIT = 2;
	private static final ImageInfo buttonInfo = new ImageInfo("EndTurnButton.png");
	private static final ImageInfo buttonDisabledInfo = new ImageInfo("EndTurnButtonDisabled.png");
	/**
	 * {@code Level.current()} is equivalent to {@link Main#currentLevel()}.
	 */
	public static final Level current() {
		return Main.currentLevel();
	}
	
	private final Pane root;
	private final StackPane stackRoot;
	private final BorderPane borderPane;
	/** The right component of {@link #borderPane} */
	private final InfoPanel infoPanel;
	/** The left component of {@link #borderPane} */
	private final VBox buttonPane;
	private final Button endTurnButton;
	/** The center component of {@link #borderPane} */
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
		this.turnDifficulty = 6; //must be set before terrainPane construction. TODO make it a constant
		
		borderPane = new BorderPane();
		final DoubleBinding sidePanelWidthBinding = borderPane.widthProperty().multiply(SIDE_PANEL_SCREEN_PERCENT);
		endTurnButton = new Button("END TURN");
		endTurnButton.setFont(Font.font("Lucida Console",FontWeight.BOLD, 16));
		endTurnButton.setOnMouseClicked(actionEvent -> {
			endTurnButton.setDisable(true);
			playEnemyTurn();
		});
		buttonPane = new VBox(endTurnButton);
		buttonPane.setPadding(new Insets(10));
		buttonPane.setAlignment(Pos.TOP_CENTER);
		buttonPane.prefWidthProperty().bind(sidePanelWidthBinding);
		
		terrainPane = new TerrainPane(this, theme);
		
		infoPanel = new InfoPanel();
		infoPanel.prefWidthProperty().bind(sidePanelWidthBinding);
		
		borderPane.setLeft(buttonPane);
		borderPane.setRight(infoPanel);
		borderPane.setCenter(terrainPane);
		
		root = (Pane) getRoot();
		
		stackRoot = new StackPane();
		stackRoot.setMinSize(MIN_STACKROOT_WIDTH, MIN_STACKROOT_HEIGHT);
		stackRoot.prefWidthProperty().bind(stackRootWidth);
		stackRoot.prefHeightProperty().bind(stackRootHeight);
		stackRoot.setBackground(new Background(
				new BackgroundImage(getTheme().backgroundImage(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)
		));
		stackRoot.getChildren().add(0, borderPane);
		
		root.getChildren().add(stackRoot);
	}
	/**
	 * Assumes that the current {@link #getTurn() turn} is {@link Turn#PLAYER the player's}. Sets the turn to {@link Turn#ENEMY the enemy's}.
	 * And plays out the enemy's turn.
	 */
	private void playEnemyTurn() {
		if(getTurn() == Turn.ENEMY)
			throw new IllegalStateException("Cannot play enemy turn because it is already the enemy's turn");
		terrainPane.playEnemyTurn();
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
	
	public Turn getTurn() {
		return terrainPane.getTurn();
	}

	/**
	 * Called to notify when the enemy turn finishes. Must not be called on FX Thread.
	 */
	public void enemyTurnFinished() {
		Main.runOnFXAndBlock(() -> {
			getInfoPanel().getAbilityInfoPanel().updateMovesRemaining();
			endTurnButton.setDisable(false);
			
		});
	}
	
	public int getMovesPerEnemyUnit() {
		return MOVES_PER_ENEMY_UNIT;
	}
	
	public int getMovesPerPlayerUnit() {
		return MOVES_PER_PLAYER_UNIT;
	}
}
