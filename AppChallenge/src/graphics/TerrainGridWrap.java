package graphics;

import javafx.scene.layout.*;

/**
 * @author Sam Hooper
 *
 */
public class TerrainGridWrap extends StackPane {
	private final TerrainGrid grid;
	private final Pane region;
//	final ImageWrap testWrap = new ImageWrap("BasicUnit.png"); //TODO remove/change
	public TerrainGridWrap(int size) {
		super();
		grid = new TerrainGrid(size);
		this.getChildren().add(grid);
		region = new Pane();
//		region.getChildren().add(testWrap); //TODO remove/change
		region.setPickOnBounds(false);
		this.getChildren().add(region);
	}
	
	public TerrainGrid getGrid() {
		return grid;
	}
	
}
