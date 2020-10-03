package graphics;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.StackPane;

/**
 * The {@link StackPane} that will contain the {@link TerrainGrid} for a {@link Level}.
 * @author Sam Hooper
 *
 */
public class TerrainPane extends StackPane {
	private final TerrainGrid grid;
	public TerrainPane() {
		super();
		grid = new TerrainGrid(8);
		getChildren().add(grid);
		bindGrid();
		
	}
	
	/** Must only be called from the constructor. {@link #grid} must be initialized.*/
	private void bindGrid() {
		//StackPane resizes children to be as large as possible, ignoring their pref widths/heights.
		NumberBinding min = Bindings.min(widthProperty(), heightProperty());
		grid.maxWidthProperty().bind(min);
		grid.maxHeightProperty().bind(min);
		grid.setMinSize(0, 0);
	}
	
	public TerrainGrid getGrid() {
		return grid;
	}
}
