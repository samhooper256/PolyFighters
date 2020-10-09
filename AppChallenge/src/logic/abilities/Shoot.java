package logic.abilities;

import java.util.ArrayList;
import java.util.Collection;

import logic.Board;
import logic.EnemyUnit;
import logic.Move;
import logic.Unit;
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
		ArrayList<int[]> arr = new ArrayList<int[]>();
		
		boolean found1 = false;
		for(int r = uRow; r < rows && !found1; r++)
		{
			if(	b.getTileAt(r, uCol).getUnit() instanceof EnemyUnit || 
				b.getTileAt(r, uCol).hasObstacle())
				
			{
				int[] tempArr = {r, uCol};
				arr.add(tempArr);
				found1 = true;
			}
		
		}
		
		boolean found2 = false;
		for(int r = uRow; r >= 0 && !found2; r--)
		{
			if(	b.getTileAt(r, uCol).getUnit() instanceof EnemyUnit || 
				b.getTileAt(r, uCol).hasObstacle())
				
			{
				int[] tempArr = {r, uCol};
				arr.add(tempArr);
				found2 = true;
			}
		}
		
		boolean found3 = false;
		for(int c = uCol; c < cols && !found3; c++)
		{
			if(	b.getTileAt(uRow, c).getUnit() instanceof EnemyUnit || 
				b.getTileAt(uRow, c).hasObstacle())
				
			{
				int[] tempArr = {uRow, c};
				arr.add(tempArr);
				found3 = true;
			}
		
		}
		
		boolean found4 = false;
		for(int c = uCol; c >= 0 && !found4; c--)
		{
			if(	b.getTileAt(uRow, c).getUnit() instanceof EnemyUnit || 
				b.getTileAt(uRow, c).hasObstacle())
				
			{
				int[] tempArr = {uRow, c};
				arr.add(tempArr);
				found4 = true;
			}
		
		}
		
		return arr;
	}

	@Override
	public Move createMoveFor(int destRow, int destCol) 
	{
		return null;
	}
}			
