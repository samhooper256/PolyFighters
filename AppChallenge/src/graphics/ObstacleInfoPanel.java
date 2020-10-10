package graphics;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * @author Sam Hooper
 *
 */
public class ObstacleInfoPanel extends VBox {
	
	public void setContent(Node... nodes) {
		getChildren().clear();
		getChildren().addAll(nodes);
	}
	
	public void clearContent() {
		getChildren().clear();
	}
	
}
