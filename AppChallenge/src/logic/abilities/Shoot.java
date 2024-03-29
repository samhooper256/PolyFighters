package logic.abilities;

import java.util.*;

import logic.*;
import logic.actions.FireProjectile;
import utils.IntRef;

public class Shoot extends AbstractAnyAbility implements SingleProjectileAbility{
	private IntRef bulletDamage;
	
	
	public Shoot(Unit unit, int bd)
	{
		super(unit);
		this.bulletDamage = new IntRef(bd);
	}
	
	public IntRef damageProperty() 
	{
		return bulletDamage;
	}
	

	@Override
	public Collection<int[]> getLegals() 
	{
		Board b = unit.getBoard();
		int rows = b.getRows();
		int cols = b.getCols();
		int uRow = unit.getRow();
		int uCol = unit.getCol();
		ArrayList<int[]> arr = new ArrayList<>();
		BoardTile tile = b.getTileAt(uRow, uCol);
		
		if(tile.getType() != TileType.LIQUID)
		{
			boolean found1 = false;
			for(int r = uRow + 1; r < rows && !found1; r++)
			{
				if(b.hasUnit(r, uCol) || b.hasObstacle(r, uCol))
				{
					int[] tempArr = {r, uCol};
					arr.add(tempArr);
					found1 = true;
				}
		
			}
		
			boolean found2 = false;
			for(int r = uRow - 1; r >= 0 && !found2; r--)
			{
				if(b.hasUnit(r, uCol) || b.hasObstacle(r, uCol))
				{
					int[] tempArr = {r, uCol};
					arr.add(tempArr);
					found2 = true;
				}
			}
		
			boolean found3 = false;
			for(int c = uCol + 1; c < cols && !found3; c++)
			{
				if(b.hasUnit(uRow, c) ||  b.hasObstacle(uRow, c))
				{
					int[] tempArr = {uRow, c};
					arr.add(tempArr);
					found3 = true;
				}
		
			}
		
			boolean found4 = false;
			for(int c = uCol - 1; c >= 0 && !found4; c--)
			{
				if(b.hasUnit(uRow, c) || b.hasObstacle(uRow, c))
				{
					int[] tempArr = {uRow, c};
					arr.add(tempArr);
					found4 = true;
				}
		
			}
		}
		
		return arr;
	}

	/**
	 * Allows for all {@link GameObject}s to be targeted, even if they don't implement {@link HasHealth}. 
	 */
	@Override
	public Move createMoveFor(int destRow, int destCol, GameObject target) {
		Objects.requireNonNull(target);
		return new Move(this, new FireProjectile(unit.getRow(), unit.getCol(), bulletDamage.get(), target));
	}

	@Override
	public boolean canTarget(GameObject object) {
		return object instanceof Unit || object instanceof Obstacle;
	}
	
	@Override
	public boolean canAttackFrom(TileType type) {
		return type == TileType.SOLID;
	}
}			
