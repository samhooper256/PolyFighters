package logic.actions;

import logic.Action;
import logic.Board;
import logic.GameObject;

/**
 * An {@link Action} that places a {@link GameObject} on the board.
 * @author Sam Hooper
 *
 */
public class PlaceObject implements Action {

	private final GameObject object;
	private final int row, col;
	
	public PlaceObject(GameObject object, int row, int col) {
		super();
		this.object = object;
		this.row = row;
		this.col = col;
	}

	public GameObject getObject() {
		return object;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public void execute(Board board) {
		board.addOrThrow(object, row, col);
	}

}
