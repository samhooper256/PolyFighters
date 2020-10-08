package graphics;

import javafx.scene.layout.*;
import logic.*;
import logic.actions.*;

/**
 * @author Sam Hooper
 *
 */
public class TerrainGrid extends GridPane {
	
	private final int rows, cols;
	private final Board backingBoard;
	private final TerrainTile[][] terrainTiles;
	private final Theme theme;
	
	/** Creates a new {@code TerrainGrid} with {@code size} rows and {@code size} columns. */
	public TerrainGrid(Theme theme, int size) {
		this(theme, size, size);
	}
	
	/** Creates a new {@code TerrainGrid} with the given amount of rows and columns */
	public TerrainGrid(Theme theme, int rows, int cols) {
		super();
		this.theme = theme;
		this.rows = rows;
		this.cols = cols;
		backingBoard = new Board(rows, cols);
		terrainTiles = new TerrainTile[rows][cols];
		initConstraints();
		initTiles();
	}
	
	/** Must only be called from constructor. {@link #rows} and {@link #cols} must be initialized. */
	private void initConstraints() {
		initRowConstraints();
		initColumnConstraints();
	}
	
	/** Must only be called from {@link #initConstraints()}. {@link #rows} must be initialized. */
	private void initRowConstraints() {
		for(int i = 0; i < rows; i++) {
			RowConstraints rc = new RowConstraints();
			rc.setPercentHeight(100.0/rows);
			getRowConstraints().add(rc);
		}
	}
	
	/** Must only be called from {@link #initConstraints()}. {@link #cols} must be initialized. */
	private void initColumnConstraints() {
		for(int i = 0; i < cols; i++) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setPercentWidth(100.0/cols);
			getColumnConstraints().add(cc);
		}
	}
	
	/** Creates the tiles for this {@code TerrainGrid}. Must only be called from the constructor.
	 * {@link #rows}, {@link #cols}, and {@link #backingBoard} must be initialized.*/
	private void initTiles() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				TerrainTile tile = TerrainTile.forBoardTile(this, backingBoard.getTileAt(i, j)); //TODO use level theme
				terrainTiles[i][j] = tile;
				GridPane.setConstraints(tile, j, i); //this method takes (col, row) instead of (row, col). That's why j comes before i.
				getChildren().add(tile);
			}
		}
	}
	
	public Board getBoard() {
		return backingBoard;
	}
	
	public TerrainTile getTileAt(int row, int col) {
		return terrainTiles[row][col];
	}
	
	public void addUnit(Unit unit, int row, int col) {
		backingBoard.addUnit(unit, row, col);
		terrainTiles[row][col].getUnitPane().setUnit(unit);
	}
	
	public void addObstacleOrThrow(Obstacle obstacle, int row, int col) {
		backingBoard.addObstacle(obstacle, row, col);
		terrainTiles[row][col].addObstacleOrThrow(obstacle);
	}
	
	public void executeMove(final Move move) {
		Level.current().getInfoPanel().getAbilityPanel().getSelectedAbilityPane().deselect();
		for(Action a : move.getActionsUnmodifiable()) {
			if(a instanceof Relocate) {
				Relocate r = (Relocate) a;
				r.execute(backingBoard);
				TerrainTile startTile = terrainTiles[r.getStartRow()][r.getStartCol()];
				UnitPane startUnitPane = startTile.getUnitPane();
				Unit unit = startUnitPane.removeUnit();
				TerrainTile destTile = terrainTiles[r.getDestRow()][r.getDestCol()];
				UnitPane destUnitPane = destTile.getUnitPane();
				destUnitPane.setUnit(unit);
			}
			else {
				throw new UnsupportedOperationException("Unsupported action type: " + a.getClass());
			}
		}
	}

	public Theme getTheme() {
		return theme;
	}
}
