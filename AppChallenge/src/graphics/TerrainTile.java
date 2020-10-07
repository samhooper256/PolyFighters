package graphics;

import fxutils.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import logic.BoardTile;

/**
 * @author Sam Hooper
 *
 */
public class TerrainTile extends StackPane {
	
	public static final double DEFAULT_HIGHLIGHT_OPACITY = .5;
	
	private final ImageWrap tileWrap;
	private final UnitPane unitPane;
	
	public static TerrainTile forBoardTile(BoardTile boardTile, Theme theme) {
		return new TerrainTile(theme); //TODO - make this actually reflect the given boardTile.
	}
	
	private TerrainTile(Theme theme) {
		super();
		tileWrap = new ImageWrap(theme.tileImage());
		setBorder(Borders.of(Color.PURPLE));
		setMinSize(0, 0);
		unitPane = new UnitPane();
		getChildren().addAll(tileWrap, unitPane);
	}
	
	public UnitPane getUnitPane() {
		return unitPane;
	}
	
	/**
	 * Highlights this tile in the color indicated by the given {@link Paint} at the given opacity.
	 * @param color
	 */
	public void highlight(Paint color, double opacity) {
		Rectangle rect = new Rectangle();
		rect.setFill(color);
		rect.setOpacity(opacity);
		rect.widthProperty().bind(TerrainTile.this.widthProperty());
		rect.heightProperty().bind(TerrainTile.this.heightProperty());
		getChildren().add(rect);
	}
	
	public void highlight(Paint color) {
		highlight(color, DEFAULT_HIGHLIGHT_OPACITY);
	}
}
