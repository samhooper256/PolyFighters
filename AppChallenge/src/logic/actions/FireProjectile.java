package logic.actions;

import java.util.Objects;

import logic.Ability;
import logic.Action;
import logic.GameObject;

/**
 * An {@link Action} that represents a {@link Unit} firing a projectile from one that does a non-negative amount of damage to another {@link GameObject}.
 * The {@code GameObject} receiving the damage must be specified when an object of this class is created. This class is immutable.
 * @author Sam Hooper
 *
 */
public class FireProjectile {
	
	private final int startRow, startCol, destRow, destCol, damage;
	private final Ability ability;
	private final GameObject target;
	

	/**
	 * The target {@link GameObject} must not be {@code null}. The damage must be non-negative.
	 * @param ability the {@link Ability} that caused this {@link Move} to be created.
	 * @throws NullPointerException if the target is {@code null}.
	 * @throws IllegalArgumentException if the damage is negative.
	 */
	public FireProjectile(int startRow, int startCol, int destRow, int destCol, int damage, Ability ability, GameObject target) {
		Objects.requireNonNull(target);
		if(damage < 0)
			throw new IllegalArgumentException("given damage (" + damage + ") must not be negative");
		this.startRow = startRow;
		this.startCol = startCol;
		this.destRow = destRow;
		this.destCol = destCol;
		this.damage = damage;
		this.ability = ability;
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
	
	public Ability getAbility() {
		return ability;
	}
	
	public GameObject getTarget() {
		return target;
	}
	
}
