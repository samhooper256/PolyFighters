package graphics;

import java.util.Objects;

import fxutils.ImageWrap;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import logic.Ability;
import logic.Move;
import logic.Obstacle;
import logic.Unit;

/**
 * A {@link StackPane} for displaying an {@link Obstacle}.
 * @author Sam Hooper
 *
 */
public class ObstaclePane extends StackPane implements GameObjectRepresentation {
	
	private static final EventHandler<? super MouseEvent> clickHandler = mouseEvent -> {
		ObstaclePane pane = (ObstaclePane) mouseEvent.getSource();
		System.out.printf("Entered ObstaclePane clickHandler%n");
		Obstacle obstacle = pane.getGameObject();
		if(pane.isUseCandidate()) {
			Level level = Level.current();
			Move move = level.getInfoPanel().getAbilityInfoPanel().getSelectedAbilityPane()
					.getAbility().createMoveFor(obstacle.getRow(), obstacle.getCol(), obstacle);
			level.getTerrainPane().getGrid().executeMove(move);
		}
		mouseEvent.consume();
	};
	private final ImageWrap obstacleWrap;
	private Obstacle obstacle;
	private boolean isUseCandidate, isHighlighted;
	
	public ObstaclePane(Obstacle obstacle, Theme theme) {
		this.obstacle = obstacle;
		this.isUseCandidate = false;
		this.isHighlighted = false;
		this.setOnMouseClicked(clickHandler);
		obstacleWrap = new ImageWrap(theme.imageFor(obstacle));
		getChildren().add(obstacleWrap);
	}
	
	public ObstaclePane() {
		this.obstacle = null;
		obstacleWrap = new ImageWrap();
		getChildren().add(obstacleWrap);
	}
	
	/**
	 * The given {@link Obstacle} must not be {@code null}.
	 */
	public void setObstacle(Obstacle obstacle, Theme theme) {
		Objects.requireNonNull(obstacle);
		this.obstacle = obstacle;
		obstacleWrap.setImage(theme.imageFor(obstacle));
	}
	
	public void removeObstacle() {
		this.obstacle = null;
		obstacleWrap.setImage(null);
	}
	
	@Override
	public Obstacle getGameObject() {
		return obstacle;
	}
	
	public boolean hasObstacle() {
		return obstacle != null;
	}
	
	/**
	 * Uses the {@link Level#current() current level}'s theme to highlight this {@link ObstaclePane}. 
	 */
	@Override
	public void highlight() {
		if(!isHighlighted()) {
			this.setEffect(Level.current().getTheme().highlightEffect());
			isHighlighted = true;
		}
	}
	
	@Override
	public void clearHighlight() {
		if(isHighlighted()) {
			this.setEffect(null);
			isHighlighted = false;
		}
	}

	@Override
	public boolean isHighlighted() {
		return isHighlighted;
	}

	@Override
	public void setUseCandidate(boolean value) {
		isUseCandidate = value;
	}

	@Override
	public boolean isUseCandidate() {
		return isUseCandidate;
	}
}
