package logic;

import java.util.*;

/**
 * @author Sam Hooper
 *
 */
public abstract class EnemyUnit implements Unit {
	
	private List<Ability> abilities;
	
	public EnemyUnit(List<Ability> abilities) {
		this.abilities = Objects.requireNonNull(abilities);
	}
	
	@Override
	public List<Ability> getAbilities() {
		return abilities;
	}

}
