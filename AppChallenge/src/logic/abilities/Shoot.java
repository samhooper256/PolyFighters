package logic.abilities;

import java.util.ArrayList;
import java.util.Collection;

import logic.Board;
import logic.Move;
import logic.Unit;
import utils.IntRef;

public class Shoot extends AbstractAnyAbility{
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
	
	public static void shoot() 
	{
		//for(int[] arr: unit.getLegals())
		{
			
		}
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
		
		for(int r = uRow; r <= uRow; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				int[] tempArr = new int[]{r, c};
				arr.add(tempArr);
			}
		}
		
		for(int c = uCol; c <= uCol; c++)
		{
			for(int r = 0; r < rows; r++)
			{
				int[] tempArr = new int[]{r, c};
				arr.add(tempArr);
			}
		}
		
		return arr;
	}

	@Override
	public Move createMoveFor(int destRow, int destCol) {
		// TODO Auto-generated method stub
		return null;
	}
}			
