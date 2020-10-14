package logic.abilities;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import logic.*;
import logic.actions.ChangeHealth;
import utils.*;

/**
 * An {@link Ability} that deals damage to all units (of any team) in a {@link Board#forTilesInSquare(int, int, int, logic.Board.TileConsumer, boolean)
 * square of a given radius}
 * around the user. The {@link #createMoveFor(int, int, logic.GameObject) createMoveFor(...)} methods
 * do not take a target, so the target parameter of those methods can be {@code null}. This {@code Ability} does <b>not</b> implement {@link TargetingAbility}.
 * 
 * @author Sam Hooper
 *
 */
public class Smash extends AbstractAnyAbility implements AttackAbility {

	private static final EnumSet<TileType> ATTACK_FROM = EnumSet.of(TileType.SOLID);
	
	private final IntRef damage, radius;
	
	public Smash(Unit unit, int damage, int radius) {
		super(unit);
		this.damage = new IntRef(damage);
		this.radius = new IntRef(radius);
	}
	
	public IntRef damageProperty() {
		return damage;
	}
	
	public IntRef radiusProperty() {
		return radius;
	}
	
	public int getRadius() {
		return radius.get();
	}
	
	/**
	 * Returns an empty {@link Collection} if using this {@link Smash} would have no effect. (In other words, you can't use {@link Smash} unless it would actually
	 * do something). Otherwise, returns a {@link Collection} containing a single element, the location of this {@link Ability Ability's} {@link Unit}.
	 */
	@Override
	public Collection<int[]> getLegals() {
		final int myRow = unit.getRow();
		final int myCol = unit.getCol();
		final Board board = unit.getBoard();
		if(ATTACK_FROM.contains(board.getTileAt(myRow, myCol).getType()) && Types.containsInstanceOf(board.get8AdjacentGameObjects(myRow, myCol), HasHealth.class))
			return Set.of(new int[] {myRow, myCol});
		return Collections.emptySet();
	}

	/**
	 * The {@link GameObject} parameter is ignored and can be {@code null}.
	 * @throws IllegalArgumentException if the arguments to not form a legal move.
	 */
	@Override
	public Move createMoveFor(int destRow, int destCol, GameObject target) {
		Move move = new Move(this);
		final Board board = unit.getBoard();
		final int myRow = unit.getRow();
		final int myCol = unit.getCol();
		if(destRow != myRow || destCol != myCol || !ATTACK_FROM.contains(board.getTileAt(myRow, myCol).getType()))
			throw new IllegalArgumentException(String.format("Not a legal move. This unit is at (%d,%d). Dest tile was: (%d,%d)", myRow,myCol,destRow,destCol));
		final int radius = this.radius.get();
		final int damage = this.damage.get();
		board.forTilesInSquare(myRow, myCol, radius, tile -> {
			for(GameObject obj : tile.getObjectsUnmodifiable())
				if(obj instanceof HasHealth)
					move.addAction(new ChangeHealth((HasHealth) obj, -damage));
		});
		return move;
	}

	@Override
	public boolean canAttackFrom(TileType type) {
		return ATTACK_FROM.contains(type);
	}

}
