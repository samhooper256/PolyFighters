package graphics;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.*;

/**
 * The {@link StackPane} that will contain the {@link TerrainGrid} for a {@link Level}.
 * @author Sam Hooper
 *
 */
public class TerrainPane extends StackPane {
	private final TerrainGridWrap gridWrap;
	public TerrainPane() {
		super();
		gridWrap = new TerrainGridWrap(8);
		getChildren().add(gridWrap);
		bindGridWrap();
		
	}
	
	/** Must only be called from the constructor. {@link #gridWrap} must be initialized.*/
	private void bindGridWrap() {
		//StackPane resizes children to be as large as possible, ignoring their pref widths/heights.
		NumberBinding min = Bindings.min(widthProperty(), heightProperty());
		gridWrap.maxWidthProperty().bind(min);
		gridWrap.maxHeightProperty().bind(min);
		gridWrap.setMinSize(0, 0);
	}
	
	public TerrainGridWrap getGridWrap() {
		return gridWrap;
	}
	
	/**
	 * {@code t.getGrid()} is equivalent to {@code t.getGridWrap().getGrid()}.
	 * @return
	 */
	public TerrainGrid getGrid() {
		return gridWrap.getGrid();
	}
}
