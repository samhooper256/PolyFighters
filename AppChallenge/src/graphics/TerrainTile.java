package graphics;

import java.util.*;

import fxutils.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
public class TerrainTile extends StackPane implements AbilityUseCandidate {
	
	public static final Paint DEFAULT_HIGHLIGHT = Color.LAWNGREEN;
	public static final EventHandler<? super MouseEvent> clickHandler = mouseEvent -> {
		TerrainTile source = (TerrainTile) mouseEvent.getSource();
		System.out.printf("Entered TerrainTile clickHandler, hasObstacle? = %s%n", source.getObstaclePane().hasObstacle());
		if(source.isUseCandidate()) {
			Level level = Level.current();
			Move move = level.getInfoPanel().getAbilityInfoPanel().getSelectedAbilityPane().getAbility().createMoveFor(source.getRow(), source.getCol(), null);
			level.getTerrainPane().getGrid().executeMove(move);
		}
		else {
			InfoPanel infoPanel = Level.current().getInfoPanel();
			infoPanel.clearContent();
			TileInfoPanel tileInfoPanel = infoPanel.getTileInfoPanel();
			tileInfoPanel.setContent(source.getTileInfo());
			infoPanel.displayOnlyTileInfoPanel();
		}
		mouseEvent.consume(); //there's nothing below the tile, so we consume it here.
	};
	
	private final ImageWrap tileWrap;
	private final UnitPane unitPane;
	private final ObstaclePane obstaclePane;
	private final int row, col;
	private final TerrainGrid grid;
	private TileInfo tileInfo;
	
	private int highlightCount;
	private boolean isUseCandidate;
	
	public static TerrainTile forBoardTile(TerrainGrid grid, BoardTile boardTile) {
		return new TerrainTile(grid, boardTile);
	}
	
	private TerrainTile(TerrainGrid grid, BoardTile boardTile) {
		super();
		this.grid = grid;
		this.row = boardTile.getRow();
		this.col = boardTile.getCol();
		highlightCount = 0;
		isUseCandidate = false;
		tileWrap = new ImageWrap(grid.getTheme().imageForTileType(boardTile.getType()));
		setBorder(Borders.of(Color.PURPLE));
		setMinSize(0, 0);
		unitPane = new UnitPane();
		setOnMouseClicked(clickHandler);
		obstaclePane = new ObstaclePane();
		getChildren().addAll(tileWrap, obstaclePane, unitPane);
	}
	
	public TileInfo getTileInfo() {
		if(tileInfo == null) {
			tileInfo = new TileInfo(this) {
				{
					getChildren().add(new Label(getTheme().tileDescription()));
				}
			};
		}
		return tileInfo;
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
	
	public ObstaclePane getObstaclePane() {
		return obstaclePane;
	}
	
	public void addObstacleOrThrow(Obstacle obstacle) {
		obstaclePane.setObstacle(obstacle, getTheme());
	}
	
	/**
	 * If the given value if {@code true}, Makes this {@code TerrainTile} a use candidate. Otherwise,
	 * makes this {@code TerrainTile} not a use candidate.
	 */
	@Override
	public void setUseCandidate(boolean value) {
		isUseCandidate = value;
	}
	
	@Override
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
	 * Sets every {@link Highlightable} on this {@link TerrainTile} to not be highlighted.
	 */
	public void clearGameObjectHighlights() {
		for(Node node : getChildren())
			if(node instanceof Highlightable)
				((Highlightable) node).clearHighlight();
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
	
	public Theme getTheme() {
		return getGrid().getTheme();
	}
	
	public BoardTile getBackingBoardTile() {
		return grid.getBackingBoardTile(row, col);
	}
	
	public Collection<GameObjectRepresentation> getGameObjectRepresentations() {
		if(unitPane.hasUnit()) {
			if(obstaclePane.hasObstacle())
				return Set.of(unitPane, obstaclePane);
			return Set.of(unitPane);
		}
		if(obstaclePane.hasObstacle())
			return Set.of(obstaclePane);
		return Collections.emptySet();
	}
	
}
