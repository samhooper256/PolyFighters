package logic;

/**
 * All {@code EnemyUnit}s must define a no-arg constructor.
 * @author Sam Hooper
 *
 */
public interface EnemyUnit extends Unit {
	@Override
	default Turn playingTurn() {
		return Turn.ENEMY;
	}
	
	/**
	 * Chooses and returns a {@link Move} for this {@link EnemyUnit} to make. {@code moveRemaining} must be a
	 * positive integer indicating the number of moves this {@code EnemyUnit} has left to make this turn, including this one.
	 * (For example, if this unit can make this move and one more, then {@code movesRemaining} should be {@code 2}).
	 */
	Move chooseMove(Board board, int movesRemaining);
}
