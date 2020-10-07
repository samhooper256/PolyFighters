package logic.actions;

import logic.*;

/**
 * An {@link Action} that describes a {@code Unit} being moved to another tile. This class is immutable. Note
 * that {@link #execute(Board)} throws an {@link IllegalStateException} if there is no {@link Unit} on the start tile or if there
 * is a {@code Unit} on the destination tile.
 * @author Sam Hooper
 *
 */
public class Relocate implements Action {
	
	private final int startRow, startCol, destRow, destCol;
	
	public Relocate(int startRow, int startCol, int destRow, int destCol) {
		this.startRow = startRow;
		this.startCol = startCol;
		this.destRow = destRow;
		this.destCol = destCol;
	}
	
	@Override
	public void execute(Board board) {
		BoardTile startTile = board.getTileAt(startRow, startCol);
		Unit unit = startTile.removeUnitIfPresent();
		if(unit == null)
			throw new IllegalStateException("No unit on the start tile.");
		board.getTileAt(destRow, destCol).addUnitOrThrow(unit); //throws IllegalStateException for us
	}

}
