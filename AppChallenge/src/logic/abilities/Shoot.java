package logic.abilities;
import java.util.Collection;

import logic.Board;
import logic.Unit;
import utils.IntRef;
public class Shoot extends AbstractAnyAbility{
	private IntRef bulletDamage;
	private IntRef distance;
	
	public Shoot(int bd, Unit unit)
	{
		this(unit, distance, null);
	}
	
	public static void shoot()
	{
		
	}

	@Override
	public Collection<int[]> getLegals() 
	{
		final Board board = unit.getBoard();
		final int distance = this.distance.get();
		final int boardSize = Math.max(board.getRows(), board.getCols());
		final int boxSize = Math.min(distance * 2 + 1, boardSize * 2 + 1);
		final boolean[][] beenInList = new boolean[boxSize][boxSize];  
		
	}
}			
