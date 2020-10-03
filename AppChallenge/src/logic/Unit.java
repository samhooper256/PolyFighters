package logic;

import java.util.Collection;

/**
 * A unit on a {@link Board}. May be either a {@link TeamUnit} or a {@link EnemyUnit}.
 * @author Sam Hooper
 *
 */
public interface Unit extends GameObject {
	Collection<Ability> getAbilities();
}
