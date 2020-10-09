package logic.abilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import logic.Board;
import logic.BoardTile;
import logic.GameObject;
import logic.Move;
import logic.Obstacle;
import logic.TileType;
import logic.Unit;
import logic.actions.FireProjectile;
import utils.IntRef;

/**
 * @author Ayuj Verma
 *
 */

public class Melee extends AbstractAnyAbility implements SingleProjectileAbility
{
	private IntRef meleeDamage;
	Melee(Unit unit, int md) 
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
		
		if(tile.getType() != TileType.LIQUID)
		{
			if((uRow != 0 && uRow != rows - 1) && (uCol != 0 && uCol != cols - 1))
			{
				for(int i = uRow - 1; i < uRow + 2; i++)
				{
					int[] arrC1 = new int[] {i, uCol - 1};
					int[] arrC2 = new int[] {i, uCol};
					int[] arrC3 = new int[] {i, uCol + 1};
					arr.add(arrC1);
					arr.add(arrC2);
					arr.add(arrC3);
				}
			}
			
			if(uRow == 0)
			{
				for(int i = 0; i < 2; i++)
				{
					int[] arrC1 = new int[] {i, uCol - 1};
					int[] arrC2 = new int[] {i, uCol};
					int[] arrC3 = new int[] {i, uCol + 1};
					arr.add(arrC1);
					arr.add(arrC2);
					arr.add(arrC3);
				}
			}
			
			if(uRow == rows)
			{
				for(int i = rows; i > rows - 2; i--)
				{
					int[] arrC1 = new int[] {i, uCol - 1};
					int[] arrC2 = new int[] {i, uCol};
					int[] arrC3 = new int[] {i, uCol + 1};
					arr.add(arrC1);
					arr.add(arrC2);
					arr.add(arrC3);
				}
			}
			
			if(uCol == 0)
			{
				for(int i = 0; i < 2; i++)
				{
					int[] arrC1 = new int[] {uRow - 1, i};
					int[] arrC2 = new int[] {uRow, i};
					int[] arrC3 = new int[] {uRow + 1, i};
					arr.add(arrC1);
					arr.add(arrC2);
					arr.add(arrC3);
				}
			}
			
			if(uCol == 0)
			{
				for(int i = 0; i < 2; i++)
				{
					int[] arrC1 = new int[] {uRow - 1, i};
					int[] arrC2 = new int[] {uRow, i};
					int[] arrC3 = new int[] {uRow + 1, i};
					arr.add(arrC1);
					arr.add(arrC2);
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
		// TODO Auto-generated method stub
		return null;
	}
}