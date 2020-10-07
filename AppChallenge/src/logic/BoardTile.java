package logic;

import java.util.*;

/**
 * A tile in a {@link Board}. Each board tile knows what board it is a part of. The {@code Board} that a {@code BoardTile} is a part
 * of is set at construction time and cannot be changed afterwards. A {@code BoardTile} can have a maximum of one {@code Unit} on it.
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
	
	private TileType type;
	
	/** Creates a {@code BoardTile} of the given {@code TileType} that is a part of the given {@code Board}.
	 */
	public BoardTile(Board board, TileType type) {
		this.board = Objects.requireNonNull(board);
		this.type = Objects.requireNonNull(type);
		this.objects = new ArrayList<>();
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
	
	public void addObject(GameObject object) {
		objects.add(object);
	}
	
	/** Removes the given {@link GameObject} from this tile, if it is present. Returns {@code true}
	 * if the object was present and has been removed, and {@code false} if it was not present.
	 * @throws IllegalArgumentException if the given {@code GameObject} is not on this {@code BoardTile}.
	 * @param object
	 */
	public boolean removeObjectIfPresent(GameObject object) {
		return objects.remove(object);
	}
	
	/** Removes the given {@link GameObject} from this tile.
	 * @throws IllegalArgumentException if the given {@code GameObject} is not on this {@code BoardTile}.
	 * @param object
	 */
	public void removeObject(GameObject object) {
		if(!objects.remove(object))
			throw new IllegalArgumentException("The object " + object + " was not on the tile.");
	}
	
	/** Returns the {@link Unit} on the tile, or {@code null} if there is no {@code Unit} on this tile.
	 * @return
	 */
	public Unit getUnit() {
		for(int i = objects.size() - 1; i >= 0; i--) {
			GameObject obj = objects.get(i);
			if(obj instanceof Unit) {
				return (Unit) obj;
			}
		}
		return null;
	}
	
	public boolean hasUnit() {
		return getUnit() != null;
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
	 * values of the {@code Unit} removed, nor does it update that {@code Unit}'s  associated {@link Board}.</b>
	 * @throws IllegalStateException if there is already a {@code Unit} on this {@code BoardTile}.
	 */
	public void addUnitOrThrow(Unit unit) {
		if(hasUnit())
			throw new IllegalStateException("There is already a unit on this tile");
		objects.add(unit);
	}
}