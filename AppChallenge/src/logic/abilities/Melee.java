package logic.abilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;

import logic.Board;
import logic.BoardTile;
import logic.GameObject;
import logic.HasHealth;
import logic.Move;
import logic.Obstacle;
import logic.TileType;
import logic.Unit;
import logic.actions.ChangeHealth;
import logic.actions.FireProjectile;
import utils.IntRef;

/**
 * @author Ayuj Verma
 *
 */

public class Melee extends AbstractAnyAbility implements AttackAbility, TargetingAbility {
	
	private static final EnumSet<TileType> ATTACK_FROM = EnumSet.of(TileType.SOLID);
	
	private IntRef meleeDamage;
	public Melee(Unit unit, int md) 
	{
		super(unit);
		this.meleeDamage = new IntRef(md);
	}
	
	public IntRef damageProperty() 
	{
		return meleeDamage;
	}
	
	@Override
	public Collection<int[]> getLegals() 
	{
		Board b = unit.getBoard();
		int rows = b.getRows();
		int cols = b.getCols();
		int uRow = unit.getRow();
		int uCol = unit.getCol();
		ArrayList<int[]> arr = new ArrayList<int[]>();
		BoardTile tile = b.getTileAt(uRow, uCol);
		
		if(ATTACK_FROM.contains(tile.getType()))
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
			return new Move(this, new ChangeHealth((HasHealth) target, -meleeDamage.get()));
		throw new UnsupportedOperationException("Cannot change the health of: " + target);
	}

	@Override
	public boolean canAttackFrom(TileType type) {
		return ATTACK_FROM.contains(type);
	}
}