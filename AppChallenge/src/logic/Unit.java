package logic;

import java.util.Collection;

/**
 * A unit on a {@link Board}. May be either a {@link TeamUnit} or an {@link EnemyUnit}.
 * @author Sam Hooper
 *
 */
public interface Unit extends GameObject {
	Collection<Ability> getAbilitiesUnmodifiable();
	boolean canTraversable(TileType type);
	/**
	 * @return the {@link Board} that this unit is on, or {@code null} if this unit is not on a {@code Board}.
	 */
	Board getBoard();
	/**
	 * @return the row of this {@code Unit} on its board.
	 * @throws IllegalStateException if the unit is not currently associated with a board.
	 */
	int getRow();
	
	/**
	 * @return the column of this {@code Unit} on its board.
	 * @throws IllegalStateException if the unit is not currently associated with a board.
	 */
	int getCol();
	
	default boolean isOnBoard() {
		return getBoard() != null;
	}
}
