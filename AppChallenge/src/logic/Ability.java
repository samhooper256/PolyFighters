package logic;

/**
 * An ability that a {@link Unit} can have. This interface should not be extended directly; rather, one of its subinterfaces
 * {@link TeamAbility}, {@link EnemyAbility} or {@link AnyAbility} should be implemented.
 * @author Sam Hooper
 *
 */
public interface Ability {
	Unit getUnit();
}
