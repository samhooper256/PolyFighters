package logic.actions;

import java.util.Objects;

import logic.Ability;
import logic.Action;
import logic.Board;
import logic.GameObject;
import logic.HasHealth;

/**
 * An {@link Action} that represents a {@link Unit} firing a projectile from one that does a non-negative amount of damage to another {@link GameObject}.
 * The {@code GameObject} receiving the damage must be specified when an object of this class is created. This class is immutable.
 * @author Sam Hooper
 *
 */
public class FireProjectile implements Action{
	
	private final int startRow, startCol, destRow, destCol, damage;
	private final GameObject target;
	

	/**
	 * The target {@link GameObject} must not be {@code null}. The damage must be non-negative.
	 * @param ability the {@link Ability} that caused this {@link Move} to be created.
	 * @throws NullPointerException if the target is {@code null}.
	 * @throws IllegalArgumentException if the damage is negative.
	 */
	public FireProjectile(int startRow, int startCol, int destRow, int destCol, int damage, GameObject target) {
		Objects.requireNonNull(target);
		if(damage < 0)
			throw new IllegalArgumentException("given damage (" + damage + ") must not be negative");
		this.startRow = startRow;
		this.startCol = startCol;
		this.destRow = destRow;
		this.destCol = destCol;
		this.damage = damage;
		this.target = target;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getStartCol() {
		return startCol;
	}

	public int getDestRow() {
		return destRow;
	}

	public int getDestCol() {
		return destCol;
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
				hh.aliveProperty().set(false);
				target.setBoard(null);
			}
		}
	}
	
}
