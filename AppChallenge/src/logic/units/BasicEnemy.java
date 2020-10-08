package logic.units;

import java.util.EnumSet;

import logic.EnemyUnit;
import logic.TileType;
/**
 * @author Sam Hooper
 *
 */
public class BasicEnemy extends AbstractUnit implements EnemyUnit {
	
	private static final int DEFAULT_MAX_HEALTH = 3;
	private static final EnumSet<TileType> DEFAULT_TRAVERSABLE_TILETYPES = EnumSet.of(TileType.SOLID);
	
	public BasicEnemy() {
		super(DEFAULT_MAX_HEALTH);
		this.traversableTileTypes.addAll(DEFAULT_TRAVERSABLE_TILETYPES);
	}
	
}
