package graphics;

import fxutils.ImageWrap;
import javafx.scene.layout.StackPane;
import logic.Obstacle;

/**
 * A {@link StackPane} for displaying an {@link Obstacle}.
 * @author Sam Hooper
 *
 */
public class ObstaclePane extends StackPane {
	
	private final ImageWrap obstacleWrap;
	
	private Obstacle obstacle;
	
	public ObstaclePane(Obstacle obstacle, Theme theme) {
		this.obstacle = obstacle;
		obstacleWrap = new ImageWrap(theme.imageFor(obstacle));
		getChildren().add(obstacleWrap);
	}
	
	public ObstaclePane() {
		this.obstacle = null;
		obstacleWrap = new ImageWrap();
		getChildren().add(obstacleWrap);
	}
	
	public void setObstacle(Obstacle obstacle, Theme theme) {
		this.obstacle = obstacle;
		obstacleWrap.setImage(theme.imageFor(obstacle));
	}
	
	public Obstacle getObstacle() {
		return obstacle;
	}
	
}
