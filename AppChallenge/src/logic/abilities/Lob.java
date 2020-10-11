package logic.abilities;

import java.util.*;

import logic.*;
import utils.IntRef;

public class Lob extends AbstractAnyAbility implements SingleProjectileAbility
{
	private IntRef damage, minimumDistance;
	
	
	public Lob(Unit unit, int damage, int minimumDistance)
	{
		super(unit);
		this.damage = new IntRef(damage);
		this.minimumDistance = new IntRef(minimumDistance);
	}
	
	public IntRef damageProperty() {
		return damage;
	}
	
	public IntRef minimumDistanceProperty() {
		return minimumDistance;
	}
	
	@Override
	public Collection<int[]> getLegals() 
	{
		Board board = unit.getBoard();
		int uRow = unit.getRow();
		int uCol = unit.getCol();
		int minimumDistance = this.minimumDistance.get();
		ArrayList<int[]> legals = new ArrayList<>();
		BoardTile myTile = board.getTileAt(uRow, uCol);
		if(!canAttackFrom(myTile.getType()))
			return Collections.emptySet();
		for(int[] mult : Board.ADJACENT_4) {
			int rMult = mult[0], cMult = mult[1];
			int r = uRow + minimumDistance * rMult, c = uCol + minimumDistance * cMult;
			while(board.inBounds(r, c)) {
				BoardTile tile = board.getTileAt(r, c);
				if(tile.hasUnit() || tile.hasObstacle())
					legals.add(new int[] {r, c});
				r += rMult;
				c += cMult;
			}
		}
		return legals;
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

	@Override
	public boolean canAttackFrom(TileType type) {
		return type == TileType.SOLID;
	}
}
