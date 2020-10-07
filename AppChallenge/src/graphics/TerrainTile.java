package graphics;

import fxutils.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import logic.BoardTile;

/**
 * @author Sam Hooper
 *
 */
public class TerrainTile extends StackPane {
	
	public static final Paint DEFAULT_HIGHLIGHT = Color.LAWNGREEN;
	private final ImageWrap tileWrap;
	private final UnitPane unitPane;
	public int highlightCount = 0;
	
	public static TerrainTile forBoardTile(BoardTile boardTile, Theme theme) {
		return new TerrainTile(theme); //TODO - make this actually reflect the given boardTile.
	}
	
	private TerrainTile(Theme theme) {
		super();
		highlightCount = 0;
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
	 * Highlights this tile (using a {@link Highlight}) in the color indicated by the given {@link Paint} at the given opacity.
	 * Throws an exception if this tile already has one or more {@code Highlight}s applied.
	 * @param color
	 * @throws IllegalStateException if this {@code TerrainTile} is already highlighted.
	 */
	public void highlightOrThrow(Paint color, double opacity) {
		if(isHighlighted())
			throw new IllegalStateException("This tile is already highlighted.");
		Highlight highlight = new Highlight(this, color, opacity);
		getChildren().add(highlight);
		highlightCount++;
	}
	
	public void highlightOrThrow(Paint color) {
		highlightOrThrow(color, Highlight.DEFAULT_HIGHLIGHT_OPACITY);
	}
	
	/**
	 * Returns {@code true} if there is at least one {@link Highlight} applied to this {@code TerrainTile}, {@code false} otherwise.
	 */
	public boolean isHighlighted() {
		return highlightCount > 0;
	}
	
	/**
	 * Returns the number of layers of {@link Highlight}s applied to this {@code TerrainTile}.
	 * @return
	 */
	public int getHighlightCount() {
		return highlightCount;
	}
}
