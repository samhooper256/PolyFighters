package logic.abilities;

import java.util.ArrayList;
import java.util.Collection;

import logic.Board;
import logic.BoardTile;
import logic.GameObject;
import logic.Move;
import logic.Obstacle;
import logic.TileType;
import logic.Unit;
import utils.IntRef;

public class Lob extends AbstractAnyAbility implements SingleProjectileAbility
{
	private IntRef lobDamage;
	
	
	public Lob(Unit unit, int ld)
	{
		super(unit);
		this.lobDamage = new IntRef(ld);
	}
	
	public IntRef damageProperty() 
	{
		return lobDamage;
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
			for(int i = uRow - 3; i < uRow + 4; i++)
			{
				if(isInBounds(i, uCol - 3))
				{
					int[] arrC1 = new int[] {i, uCol - 3};
					arr.add(arrC1);
				}
				
				if(isInBounds(i, uCol - 2))
				{
					int[] arrC2 = new int[] {i, uCol - 2};
					arr.add(arrC2);
				}
				
				if(isInBounds(i, uCol - 1))
				{
					int[] arrC3 = new int[] {i, uCol - 1};
					arr.add(arrC3);
				}
				
				if(isInBounds(i, uCol))
				{
					int[] arrC4 = new int[] {i, uCol};
					arr.add(arrC4);
				}
				
				if(isInBounds(i, uCol + 1))
				{
					int[] arrC5 = new int[] {i, uCol + 1};
					arr.add(arrC5);
				}
				
				if(isInBounds(i, uCol + 2))
				{
					int[] arrC6 = new int[] {i, uCol + 2};
					arr.add(arrC6);
				}
				
				if(isInBounds(i, uCol + 3))
				{
					int[] arrC7 = new int[] {i, uCol + 3};
					arr.add(arrC7);
				}
			}
		}
		return arr;
	}
		
	private boolean isInBounds(int row, int col)
	{
		Board b = unit.getBoard();
		int rows = b.getRows();
		int cols = b.getCols();
		if((row >= 0 && row < rows) && (col >= 0 && col < cols))
		{
			return true;
		}
			
			return false;
		}
	@Override
	public boolean canTarget(GameObject object) 
	{
		return object instanceof Unit || object instanceof Obstacle;
	}

	@Override
	public Move createMoveFor(int destRow, int destCol, GameObject target) 
	{
	// TODO Auto-generated method stub
	return null;
	}
}
