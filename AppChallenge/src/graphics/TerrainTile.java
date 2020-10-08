package graphics;

import java.util.Iterator;

import fxutils.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import logic.*;

/**
 * <p>A {@code TerrainTile} can be made a <i>use candidate</i> which means that it, when clicked on, will execute the {@link Move} produced
 * by the currently selected {@link Ability} with this tile as the destination.</p>
 * @author Sam Hooper
 *
 */
public class TerrainTile extends StackPane {
	
	public static final Paint DEFAULT_HIGHLIGHT = Color.LAWNGREEN;
	public static final EventHandler<? super MouseEvent> clickHandler = mouseEvent -> {
		TerrainTile source = (TerrainTile) mouseEvent.getSource();
		if(source.isUseCandidate()) {
			Level level = Level.current();
			Move move = level.getInfoPanel().getAbilityPanel().getSelectedAbilityPane().getAbility().createMoveFor(source.getRow(), source.getCol());
			level.getTerrainPane().getGrid().executeMove(move);
		}
		else {
			Level.current().getInfoPanel().clearContent();
			//TODO - show tile info
		}
		mouseEvent.consume(); //there's nothing below the tile, so we consume it here.
	};
	
	private final ImageWrap tileWrap;
	private final UnitPane unitPane;
	private final int row, col;
	private final TerrainGrid grid;
	
	private int highlightCount;
	private boolean isUseCandidate;
	
	public static TerrainTile forBoardTile(TerrainGrid grid, BoardTile boardTile) {
		return new TerrainTile(grid, boardTile.getRow(), boardTile.getCol()); //TODO - make this actually reflect the given boardTile.
	}
	
	private TerrainTile(TerrainGrid grid, int row, int col) {
		super();
		this.grid = grid;
		this.row = row;
		this.col = col;
		highlightCount = 0;
		isUseCandidate = false;
		tileWrap = new ImageWrap(grid.getTheme().tileImage());
		setBorder(Borders.of(Color.PURPLE));
		setMinSize(0, 0);
		unitPane = new UnitPane();
		setOnMouseClicked(clickHandler);
		getChildren().addAll(tileWrap, unitPane);
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public UnitPane getUnitPane() {
		return unitPane;
	}
	
	public void addObstalcePane(ObstaclePane pane) {
		ObservableList<Node> children = getChildren();
		int addIndex = children.size();
		for(int i = children.size() - 1; i >= 0; i--)
			addIndex--;
		children.add(addIndex, pane);
	}
	
	/**
	 * If the given value if {@code true}, Makes this {@code TerrainTile} a use candidate. Otherwise,
	 * makes this {@code TerrainTile} not a use candidate.
	 */
	public void setUseCandidate(boolean value) {
		isUseCandidate = value;
	}
	
	public boolean isUseCandidate() {
		return isUseCandidate;
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
	
	public void clearAllHighlights() {
		for(Iterator<Node> itr = getChildren().iterator(); itr.hasNext();) {
			Node next = itr.next();
			if(next instanceof Highlight) {
				itr.remove();
				highlightCount--;
			}
		}
		assert highlightCount == 0;
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
	
	public TerrainGrid getGrid() {
		return grid;
	}
	
}
