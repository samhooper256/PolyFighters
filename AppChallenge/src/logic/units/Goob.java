package logic.units;

import java.util.Collection;

import logic.Ability;
import logic.Board;
import logic.EnemyUnit;
import logic.TileType;
import utils.BooleanRef;
import utils.CollectionRef;
import utils.IntRef;

/**
 * @author Sam Hooper
 *
 */
public class Goob implements EnemyUnit {

	@Override
	public CollectionRef<Ability> abilityCollectionRef() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canTraverse(TileType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<int[]> getLegalSpots(Ability ability) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntRef maxHealthProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntRef healthProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanRef aliveProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Board getBoard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBoard(Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRow() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCol() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRow(int row) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCol(int col) {
		// TODO Auto-generated method stub
		
	}
	
}
