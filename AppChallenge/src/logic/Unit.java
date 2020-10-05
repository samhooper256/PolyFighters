package logic;

import java.util.Collection;

/**
 * </p>A unit on a {@link Board}. May be either a {@link TeamUnit} or an {@link EnemyUnit}.</p>
 * 
 * <p>A {@code Unit} <i>can traverse</i> a {@link TileType} <b>T</b> if and only if {@code canTraverse(T)} returns {@code true}.
 * (See {@link #canTraverse(TileType)}).</p>
 * 
 * <p>Every {@code Unit} has a row and column value that can be set and retrieved using the {@link #getRow()}, {@link #getCol()},
 * {@link #setRow()}, {@link #setCol()} methods. These row and column values represent the location of the {@code Unit} on its {@code Board}.
 * Behavior of the getter methods is undefined if the {@code Unit} is not on a {@code Board}. Note that the method {@link #isOnBoard()} can
 * be used to check if a {@code Unit} is on a {@code Board}.</p>
 * 
 * @author Sam Hooper
 *
 */
public interface Unit extends GameObject {
	/**
	 * @return an unmodifiable {@link Collection} containing this {@code Unit's} abilities.
	 */
	Collection<Ability> getAbilitiesUnmodifiable();
	
	/** Returns {@code true} if this {@code Unit} can traverse {@link BoardTile}s of the given {@link TileType}. */
	boolean canTraverse(TileType type);
	/**
	 * @return the {@link Board} that this unit is on, or {@code null} if this unit is not on a {@code Board}.
	 */
	Board getBoard();
	
	/** Sets the {@link Board} that this unit is associated with. */
	void setBoard(Board board);
	
	/**
	 * Returns the 0-based index of the row of this {@code Unit} on its {@link Board}, unless this {@code Unit} is not on a {@code Board}, in which
	 * case this method's behavior is undefined.
	 * @return the row of this {@code Unit} on its {@link Board}.
	 */
	int getRow();
	
	/**
	 * Returns the 0-based index of the column of this {@code Unit} on its {@link Board}, unless this {@code Unit} is not on a {@code Board}, in which
	 * case this method's behavior is undefined.
	 * @return the column of this {@code Unit} on its {@link Board}.
	 */
	int getCol();
	
	/** 
	 * Sets the row value of this {@code Unit}. This value lines up with its location on its associated {@link Board} at the time of invocation
	 * as described by {@link #getRow()}.
	 */
	void setRow(int row);
	
	/** 
	 * Sets the row value of this {@code Unit}. This value lines up with its location on its associated {@link Board} at the time of invocation
	 * as described by {@link #getCol()}.
	 */
	void setCol(int col);
	
	/** Returns {@code true} if this {@code Unit} is currently associated with a {@link Board}, {@code false} otherwise. */
	default boolean isOnBoard() {
		return getBoard() != null;
	}
}
