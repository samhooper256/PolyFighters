package logic.actions;

import java.util.Objects;

import logic.Ability;
import logic.Action;
import logic.Board;
import logic.GameObject;
import logic.HasHealth;

/**
 * An {@link Action} that represents a {@link Unit} firing a projectile from one that does a non-negative amount of damage to another {@link GameObject}.
 * The {@code GameObject} receiving the damage must be specified when an object of this class is created. This class is immutable. If the {@code GameObject}
 * does not implement {@link HasHealth}, {@link #execute(Board) executing} this {@code Action} has no effect.
 * @author Sam Hooper
 *
 */
public class FireProjectile implements Action{
	
	private final int startRow, startCol, damage;
	private final GameObject target;

	/**
	 * The target {@link GameObject} must not be {@code null}. The damage must be non-negative.
	 * @param ability the {@link Ability} that caused this {@link Move} to be created.
	 * @throws NullPointerException if the target is {@code null}.
	 * @throws IllegalArgumentException if the damage is negative.
	 */
	public FireProjectile(int startRow, int startCol, int damage, GameObject target) {
		Objects.requireNonNull(target);
		if(damage < 0)
			throw new IllegalArgumentException("given damage (" + damage + ") must not be negative");
		this.startRow = startRow;
		this.startCol = startCol;
		this.damage = damage;
		this.target = target;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getStartCol() {
		return startCol;
	}

	public int getDamage() {
		return damage;
	}
	
	public GameObject getTarget() {
		return target;
	}

	@Override
	public void execute(Board board) {
		if(target instanceof HasHealth) {
			HasHealth hh = (HasHealth) target;
			hh.healthProperty().set(Math.max(0, hh.healthProperty().get() - damage));
			if(hh.healthProperty().get() == 0) {
				target.getBoard().removeGameObject(target, target.getRow(), target.getCol());
				hh.aliveProperty().set(false);
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("FireProjectile[start=(%d,%d), damage=%d, target=%s]", startRow, startCol, damage, target);
	}
}
