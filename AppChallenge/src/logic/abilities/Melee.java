package logic.abilities;

import java.util.*;

import logic.*;
import logic.actions.*;
import utils.*;

/**
 * A melee {@link AttackAbility} that allows a {@link Unit} to attack a {@link GameObject}
 * in the 8 adjacent tiles
 * @author Ayuj Verma
 *
 */

public class Melee extends AbstractAnyAbility implements AttackAbility, TargetingAbility {
	
	private static final EnumSet<TileType> DEFAULT_ATTACK_FROM = EnumSet.of(TileType.SOLID);
	
	private final EnumSet<TileType> attackFrom;
	
	private IntRef damage;
	
	public Melee(Unit unit, int damage) {
		this(unit, damage, DEFAULT_ATTACK_FROM);
	}
	/**
	 * Copies the given {@link EnumSet} (does not keep a reference to it or modify it at all).
	 */
	public Melee(Unit unit, int damage, EnumSet<TileType> attackFrom) 
	{
		super(unit);
		this.damage = new IntRef(damage);
		this.attackFrom = EnumSet.copyOf(attackFrom);
	}
	
	public IntRef damageProperty() 
	{
		return damage;
	}
	
	@Override
	public Collection<int[]> getLegals() 
	{
		Board b = unit.getBoard();
		int uRow = unit.getRow();
		int uCol = unit.getCol();
		ArrayList<int[]> arr = new ArrayList<>();
		BoardTile tile = b.getTileAt(uRow, uCol);
		
		if(attackFrom.contains(tile.getType()))
		{
			for(int i = uRow - 1; i < uRow + 2; i++)
			{
				if(b.inBounds(i, uCol - 1))
				{
					int[] arrC1 = new int[] {i, uCol - 1};
					arr.add(arrC1);
				}
				
				if(i != uRow && b.inBounds(i, uCol))
				{
					int[] arrC2 = new int[] {i, uCol};
					arr.add(arrC2);
				}
				
				if(b.inBounds(i, uCol + 1))
				{
					int[] arrC3 = new int[] {i, uCol + 1};
					arr.add(arrC3);
				}
			}
		}
		
		return arr;
	}
	
	@Override
	public boolean canTarget(GameObject object) {
		return object instanceof Unit || object instanceof Obstacle;
	}

	@Override
	public Move createMoveFor(int destRow, int destCol, GameObject target) {
		if(target instanceof HasHealth)
			return new Move(this, new ChangeHealth((HasHealth) target, -damage.get()));
		throw new UnsupportedOperationException("Cannot change the health of: " + target);
	}

	@Override
	public boolean canAttackFrom(TileType type) {
		return attackFrom.contains(type);
	}
}