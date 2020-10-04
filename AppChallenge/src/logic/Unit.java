package logic;

import java.util.Collection;

/**
 * </p>A unit on a {@link Board}. May be either a {@link TeamUnit} or an {@link EnemyUnit}.</p>
 * 
 * <p>A {@code Unit} <i>can traverse</i> a {@link TileType} T if and only if {@code canTraverse(T)} returns {@code true}.
 * See {@link #canTraverse(TileType)}).</p>
 * 
 * 
 * @author Sam Hooper
 *
 */
public interface Unit extends GameObject {
	Collection<Ability> getAbilitiesUnmodifiable();
	
	boolean canTraverse(TileType type);
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
