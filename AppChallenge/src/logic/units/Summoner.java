package logic.units;

import java.util.*;

import logic.*;
import logic.abilities.*;
import utils.Coll;

/**
 * @author Sam Hooper
 *
 */
public class Summoner extends AbstractEnemyUnit {
	
	private static final int DEFAULT_MAX_HEALTH = 3;
	private static final int DEFAULT_MOVE_DISTANCE = 4;
	private static final int DEFAULT_SUMMON_RADIUS = 1;
	private static final int DEFAULT_HEAL_AMOUNT = 1;
	private static final double MOVE_CHANCE = 0.4;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID, TileType.LIQUID);
	
	private final RadiusSummon summonAbility;
	private final StepMove stepMoveAbility;
	private final SelfHeal selfHealAbility;
	
	public Summoner() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
		summonAbility = new RadiusSummon(this, Goob.class, Goob::new, DEFAULT_SUMMON_RADIUS);
		stepMoveAbility = new StepMove(this, DEFAULT_MOVE_DISTANCE);
		selfHealAbility = new SelfHeal(this, DEFAULT_HEAL_AMOUNT);
		this.abilities.addAll(summonAbility, stepMoveAbility, selfHealAbility);
	}

	@Override
	public Move chooseMove(Board board, int movesRemaining) {
		// case 1: if we're below half health and we can heal, do so.
		final int maxHealth = getMaxHealth();
		final Collection<int[]> healLegals = selfHealAbility.getLegals();
		if(healLegals.size() > 0 && getHealth() < maxHealth / 2.0) {
			return selfHealAbility.createMoveFor(healLegals.iterator().next(), null);
		}
		//case 2: Randomly move sometimes, because otherwise this Unit would never have a reason to do so:
		if(Math.random() < MOVE_CHANCE) {
			Collection<int[]> moveLegals = stepMoveAbility.getLegals();
			if(moveLegals.size() > 0) {
				return stepMoveAbility.createMoveFor(Coll.getRandom(moveLegals), null);
			} //otherwise, fall through and maybe we can heal or summon instead.
		}
		//case 3: we don't want to/can't heal, so if we can summon, do it:
		final Collection<int[]> summonLegals = summonAbility.getLegals();
		if(summonLegals.size() > 0) {
			return summonAbility.createMoveFor(Coll.getRandom(summonLegals), null);
		}
		//case 4: we're above half health, but we can't summon. Let's heal then (if we're not at full health).
		if(healLegals.size() > 0) {
			return selfHealAbility.createMoveFor(healLegals.iterator().next(), null);
		}
		//case 5: We're at full health and we can't summon. We can't move, since if we could move we could necessarily summon in one of the spots we could move to. Do nothing.
		return Move.EMPTY_MOVE;
	}
	
	@Override
	public String toString() {
		return String.format("Summoner[health=%d, maxHealth=%d, summonedUnit=%s]", getHealth(), getMaxHealth(), summonAbility.getUnitClass().getSimpleName());
	}
}
