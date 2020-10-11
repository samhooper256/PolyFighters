package logic.units;

import java.util.EnumSet;

import logic.Board;
import logic.Move;
import logic.TileType;
import logic.abilities.*;

/**
 * @author Sam Hooper
 *
 */
public class Lobber extends AbstractEnemyUnit {
	
	private static final int DEFAULT_MAX_HEALTH = 6;
	private static final int DEFAULT_MOVE_DISTANCE = 1;
	private static final int DEFAULT_LOB_MIN_DISTANCE = 2;
	private static final int DEFAULT_LOB_DAMAGE = 2;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID);
	
	private final StepMove stepMoveAbility;
	private final Lob lobAbility;
	
	public Lobber() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		this.stepMoveAbility = new StepMove(this, DEFAULT_MOVE_DISTANCE);
		this.lobAbility = new Lob(this, DEFAULT_LOB_DAMAGE, DEFAULT_LOB_MIN_DISTANCE);
		this.abilities.addAll(stepMoveAbility, lobAbility);
	}

	@Override
	public Move chooseMove(Board board, int movesRemaining) {
		// TODO Auto-generated method stub
		return Move.EMPTY_MOVE;
	}
}
