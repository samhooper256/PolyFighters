package graphics;

import fxutils.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import logic.BoardTile;

/**
 * @author Sam Hooper
 *
 */
public class TerrainTile extends StackPane {
	private final ImageWrap tileWrap;
	private final UnitPane unitPane;
	
	public static TerrainTile forBoardTile(BoardTile boardTile, Theme theme) {
		return new TerrainTile(theme); //TODO - make this actually reflect the given boardTile.
	}
	
	private TerrainTile(Theme theme) {
		super();
		tileWrap = new ImageWrap(theme.tileImage());
		setBorder(Borders.of(Color.PURPLE));
		setMinSize(0, 0);
		unitPane = new UnitPane();
		getChildren().addAll(tileWrap, unitPane);
	}
	
	public UnitPane getUnitPane() {
		return unitPane;
	}
	
}
