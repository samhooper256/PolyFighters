package logic;

/**
 * <p>Interface to be implemented by anything that can be on a {@link BoardTile} (and thus on a {@link Board}).</p>
 * 
 * <p>Every {@code GameObject} may optionally be associated with a single {@link Board}, which can be retrieved by {@link #getBoard()}. The {@code Board} a
 * {@code GameObject} is associated with must be the {@code Board} that the {@code GameObject} is on. {@link #getBoard()} returns {@code null} if the
 * {@code GameObject} is not associated with a {@code Board}.</p>
 * 
 * <p>Every {@code GameObject} has a row and column value that can be set and retrieved using the {@link #getRow()}, {@link #getCol()},
 * {@link #setRow()}, {@link #setCol()} methods. These row and column values represent the location of the {@code GameObject} on its {@code Board}.
 * Behavior of the getter methods is undefined if the {@code GameObject} is not on a {@code Board}. Note that the method {@link #hasBoard()} can
 * be used to check if a {@code GameObject} is on a {@code Board}.</p>
 * 
 * @author Sam Hooper
 *
 */
public interface GameObject {
	
	/**
	 * Returns the {@link Board} that this {@code GameObject} is on, or {@code null} if this {@code GameObject} is not on a {@code Board}. If this
	 * method does not return {@code null} for a {@code GameObject} <b>U</b>,
	 * <pre><code>U.getBoard().isOnBoard(U)</code></pre>
	 * is {@code true}.
	 */
	Board getBoard();
	
	/**
	 * Sets the {@link Board} that this {@code GameObject} is associated with.
	 */
	void setBoard(Board board);
	
	/**
	 * Returns the 0-based index of the row of this {@code GameObject} on its {@link Board}, unless this {@code GameObject} is not on a {@code Board}, in which
	 * case this method's behavior is undefined.
	 * @return the row of this {@code GameObject} on its {@link Board}.
	 */
	int getRow();
	
	/**
	 * Returns the 0-based index of the column of this {@code GameObject} on its {@link Board}, unless this {@code GameObject} is not on a {@code Board}, in which
	 * case this method's behavior is undefined.
	 * @return the column of this {@code GameObject} on its {@link Board}.
	 */
	int getCol();
	
	/**
	 * Returns {@code true} if this {@code GameObject} is currently associated with a {@link Board}, {@code false} otherwise.
	 * */
	default boolean hasBoard() {
		return getBoard() != null;
	}
	
}
