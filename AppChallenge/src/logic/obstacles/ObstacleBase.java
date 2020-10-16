package logic.obstacles;

import logic.*;
import utils.IntRef;

/**
 * Base class for {@link Obstacle}s. It is not abstract, so it may be used directly.
 * @author Sam Hooper
 *
 */
public class ObstacleBase extends AbstractHasHealth implements Obstacle {
	private final ObstacleSize size;
	
	private int row, col;
	private Board board;
	
	public ObstacleBase(ObstacleSize size, int maxHealth) {
		this(size, maxHealth, maxHealth);
	}
	
	public ObstacleBase(ObstacleSize size, int maxHealth, int currentHealth) {
		this(null, size, maxHealth, currentHealth);
	}
	
	public ObstacleBase(Board board, ObstacleSize size, int maxHealth, int currentHealth) {
		this(board, -1, -1, size, maxHealth, currentHealth);
	}
	
	public ObstacleBase(Board board, int row, int col, ObstacleSize size, int maxHealth, int currentHealth) {
		super(maxHealth, currentHealth);
		this.board = board;
		this.row = row;
		this.col = col;
		this.size = size;
	}

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public void setBoard(Board board) {
		this.board = board;
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getCol() {
		return col;
	}

	@Override
	public ObstacleSize getSize() {
		return size;
	}

	@Override
	public void setRow(int row) {
		this.row = row;
	}

	@Override
	public void setCol(int col) {
		this.col = col;
	}
}
