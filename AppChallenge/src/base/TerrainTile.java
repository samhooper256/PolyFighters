package base;

import fxutils.Borders;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * @author Sam Hooper
 *
 */
public class TerrainTile extends StackPane {
	
	public TerrainTile() {
		super();
		setBorder(Borders.of(Color.PURPLE));
	}
	
}
