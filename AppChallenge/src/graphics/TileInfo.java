package graphics;

import javafx.scene.layout.StackPane;

/**
 * @author Sam Hooper
 *
 */
public class TileInfo extends StackPane {
	
	private final TerrainTile tile;
	
	public TileInfo(TerrainTile tile) {
		this.tile = tile;
	}
	
}
