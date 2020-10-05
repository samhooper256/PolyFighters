package graphics;

import fxutils.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import logic.BoardTile;

/**
 * @author Sam Hooper
 *
 */
public class TerrainTile extends StackPane {
	private final UnitPane unitPane;
	
	public static TerrainTile forBoardTile(BoardTile boardTile) {
		return new TerrainTile(); //TODO - make this actually reflect the given boardTile.
	}
	
	private TerrainTile() {
		super();
		setBorder(Borders.of(Color.PURPLE));
		setMinSize(0, 0);
		unitPane = new UnitPane();
		getChildren().add(unitPane);
	}
	
	public UnitPane getUnitPane() {
		return unitPane;
	}
	
}
