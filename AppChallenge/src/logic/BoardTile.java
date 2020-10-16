package logic;

import java.util.*;

/**
 * <p>A tile in a {@link Board}. Each {@code BoardTile} knows what {@code Board} it is a part of. The {@code Board} that a {@code BoardTile} is a part
 * of is set at construction time and cannot be changed afterwards. A {@code BoardTile} can have a maximum of one {@link Unit} on it and a maximum of one
 * {@link Obstacle} on it. If a {@code BoardTile} has a {@code Unit}, it will be on top of all other {@link GameObject}s on the {@code BoardTile}.
 * 
 * @author Sam Hooper
 *
 */
public class BoardTile {
	
	private final Board board;
	/** A list of the things that are on this tile. They are ordered from bottom to top (so, for example, the {@link GameObject} at
	 * index 0 is below all the remaining objects in the list)
	 */
	private final ArrayList<GameObject> objects;
	private final int row, col;
	
	private TileType type;
	
	/** Creates a {@code BoardTile} of the given {@code TileType} that is a part of the given {@code Board}.
	 */
	public BoardTile(int row, int col, Board board, TileType type) {
		this.row = row;
		this.col = col;
		this.board = Objects.requireNonNull(board);
		this.type = Objects.requireNonNull(type);
		this.objects = new ArrayList<>();
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public TileType getType() {
		return type;
	}
	
	public void setType(TileType type) {
		this.type = type;
	}
	
	/** Removes the given {@link GameObject} from this tile, if it is present. Returns {@code true}
	 * if the object was present and has been removed, and {@code false} if it was not present. <b>This method
	 * DOES NOT adjust the row and column values of the {@code Unit} removed, nor does it update that {@code Unit}'s  associated {@link Board}.</b>
	 * @throws IllegalArgumentException if the given {@code GameObject} is not on this {@code BoardTile}.
	 */
	public boolean removeObjectIfPresent(GameObject object) {
		return objects.remove(object);
	}
	
	/** Removes the given {@link GameObject} from this tile. <b>This method DOES NOT adjust the row and column
	 * values of the {@code Unit} removed, nor does it update that {@code Unit}'s  associated {@link Board}.</b>
	 * Note that this method throws {@link IllegalArgumentException} if the given {@code GameObject} is {@code null}.
	 * @throws IllegalArgumentException if the given {@code GameObject} is not on this {@code BoardTile}.
	 */
	public void removeObjectOrThrow(GameObject object) {
		if(!objects.remove(object))
			throw new IllegalArgumentException("The object \"" + object + "\" was not on the tile.");
	}
	
	/** Returns the {@link Unit} on the tile, or {@code null} if there is no {@code Unit} on this tile.
	 */
	public Unit getUnitOrNull() {
		for(int i = objects.size() - 1; i >= 0; i--) {
			GameObject obj = objects.get(i);
			if(obj instanceof Unit) {
				return (Unit) obj;
			}
		}
		return null;
	}
	
	public PlayerUnit getPlayerUnitOrThrow() {
		Unit u = getUnitOrNull();
		if(u instanceof PlayerUnit)
			return (PlayerUnit) u;
		throw new IllegalStateException("This tile does not have a PlayerUnit");
	}
	
	public boolean hasUnit() {
		return getUnitOrNull() != null;
	}
	
	public boolean hasPlayerUnit() {
		return getUnitOrNull() instanceof PlayerUnit;
	}
	
	public boolean hasEnemyUnit() {
		return getUnitOrNull() instanceof EnemyUnit;
	}
	
	/**
	 * Returns {@code true} if this {@link BoardTile} has one or more {@link HasHealth HasHealths} on it.
	 */
	public boolean hasHasHealth() {
		for(GameObject obj : objects)
			if(obj instanceof HasHealth)
				return true;
		return false;
	}
	
	/** If there is a {@link Unit} on this {@code BoardTile}, removes that {@code Unit} and returns it.
	 *  Otherwise, does nothing but return {@code null}.
	 *  <b>This method DOES NOT adjust the row and column values of the {@code Unit} removed, nor does it update
	 *  that {@code Unit}'s  associated {@link Board}.</b>*/
	public Unit removeUnitIfPresent() {
		for(int i = objects.size() - 1; i >= 0; i--) {
			GameObject obj = objects.get(i);
			if(obj instanceof Unit) {
				objects.remove(i);
				return (Unit) obj;
			}
		}
		return null;
	}
	/**
	 * Adds the given {@link Unit} to the top of this {@code BoardTile}. <b>This method DOES NOT adjust the row and column
	 * values of the {@code Unit} added, nor does it update that {@code Unit}'s  associated {@link Board}.</b>
	 * @throws IllegalStateException if there is already a {@code Unit} on this {@code BoardTile}.
	 */
	public void addUnitOrThrow(Unit unit) {
		if(hasUnit())
			throw new IllegalStateException("There is already a unit on this tile");
		objects.add(unit);
	}
	
	/**
	 * Adds the given {@link Obstacle} to the top of this {@code BoardTile}. <b>This method DOES NOT adjust the row, column, or board
	 * pointers of the {@code Obstacle} added.</b>
	 * @throws IllegalStateException if there is already an {@code Obstacle} on this {@code BoardTile}.
	 */
	public void addObstacleOrThrow(Obstacle obstacle) {
		if(hasObstacle())
			throw new IllegalStateException("There is already an obstacle on this tile.");
		int addIndex = objects.size();
		if(objects.size() > 0 && objects.get(objects.size() - 1) instanceof Unit)
			addIndex--;
		objects.add(addIndex, obstacle);
	}
	
	/**
	 * Returns {@code true} if this {@code BoardTile} has an {@link Obstacle}, {@code false} otherwise.
	 */
	public boolean hasObstacle() {
		for(int i = 0; i < objects.size(); i++)
			if(objects.get(i) instanceof Obstacle)
				return true;
		return false;
	}
	
	/**
	 * Returns {@code true} if this {@code BoardTile} has an {@link Obstacle} and that {@code Obstacle} has that given size,
	 * {@code false} otherwise.
	 */
	public boolean hasObstacle(ObstacleSize size) {
		for(int i = 0; i < objects.size(); i++) {
			GameObject obj = objects.get(i);
			if(obj instanceof Obstacle && ((Obstacle) obj).getSize() == size)
				return true;
		}
		return false;
	}
	
	public Obstacle getObstacleOrNull() {
		for(GameObject obj : objects)
			if(obj instanceof Obstacle)
				return (Obstacle) obj;
		return null;
	}
	
	public List<GameObject> getObjectsUnmodifiable() {
		return Collections.unmodifiableList(objects);
	}
	
	/**
	 * Returns a {@link Collection} containing 0 or 1 elements. If it contains 0 elements, this tile has no {@link Unit Units} on it.
	 * If it contains 1 element, that element is the {@link Unit} on this tile.
	 * @return
	 */
	public Collection<Unit> getUnitsUnmodifiable() {
		Unit u = getUnitOrNull();
		return u == null ? Collections.emptyList() : Set.of(u);
	}
	
	/**
	 * Returns {@code true} if there are any {@link GameObject GameObject}s on this {@code BoardTile}, {@code false} otherwise.
	 */
	public boolean isOccupied() {
		return objects.size() > 0;
	}
	
	@Override
	public String toString() {
		return String.format("BoardTile[location=(%d, %d), type=%s, objects=%s]", row, col, type, objects);
	}
}