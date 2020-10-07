package graphics;

import javafx.scene.layout.*;
import logic.Board;
import logic.Unit;

/**
 * @author Sam Hooper
 *
 */
public class TerrainGrid extends GridPane {
	
	private final int rows, cols;
	private final Board backingBoard;
	private final TerrainTile[][] terrainTiles;
	
	/** Creates a new {@code TerrainGrid} with {@code size} rows and {@code size} columns. */
	public TerrainGrid(int size) {
		this(size, size);
	}
	
	/** Creates a new {@code TerrainGrid} with the given amount of rows and columns */
	public TerrainGrid(int rows, int cols) {
		super();
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
				TerrainTile tile = TerrainTile.forBoardTile(backingBoard.getTileAt(i, j), Theme.TEST_THEME); //TODO use level theme
				/*//Test/demo code for moving units around:
				//TODO remove this lambda:
				tile.setOnMouseClicked(mouseEvent -> {
					System.out.println("Clicked");
					TerrainGridWrap pane = ((TerrainGridWrap) TerrainGrid.this.getParent());
					pane.testWrap.setLayoutX(tile.getLayoutX());
					pane.testWrap.setLayoutY(tile.getLayoutY());
				});
				*/
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
}
