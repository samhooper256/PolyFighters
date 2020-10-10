package graphics;

import java.util.Objects;

import fxutils.ImageWrap;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import logic.Move;
import logic.Obstacle;
import utils.BooleanChangeListener;

/**
 * A {@link StackPane} for displaying an {@link Obstacle}.
 * @author Sam Hooper
 *
 */
public class ObstaclePane extends StackPane implements GameObjectRepresentation {
	
	private static final EventHandler<? super MouseEvent> clickHandler = mouseEvent -> {
		System.out.printf("Entered ObstaclePane clickHandler%n");
		ObstacleWrap wrap = (ObstacleWrap) mouseEvent.getSource();
		ObstaclePane pane = wrap.getEnclosingInstance();
		Obstacle obstacle = pane.getGameObject();
		if(pane.isUseCandidate()) {
			Level level = Level.current();
			Move move = level.getInfoPanel().getAbilityInfoPanel().getSelectedAbilityPane()
					.getAbility().createMoveFor(obstacle.getRow(), obstacle.getCol(), obstacle);
			level.getTerrainPane().getGrid().executeMove(move);
		}
		else {
			InfoPanel infoPanel = Level.current().getInfoPanel();
			infoPanel.clearContent();
			ObstacleInfoPanel obstacleInfoPanel = infoPanel.getObstacleInfoPanel();
			obstacleInfoPanel.setContent(pane.getObstacleInfo());
			infoPanel.displayOnlyObstacleInfoPanel();
		}
		mouseEvent.consume();
		
	};
	private final ObstacleWrap obstacleWrap;
	private final BorderPane healthBarPane;
	private final HealthBar healthBar;
	private final BooleanChangeListener aliveListener = (oldValue, newValue) -> {
		if(newValue == true)
			throw new UnsupportedOperationException("Revivial is not supported");
		removeObstacle(); //this is safe because a BooleanChangeListener is allowed to remove itself from its BooleanRef during its action.
	};
	
	private Obstacle obstacle;
	private boolean isUseCandidate, isHighlighted;
	private ObstacleInfo obstacleInfo;
	
	private class ObstacleWrap extends ImageWrap {
		ObstaclePane getEnclosingInstance() {
			return ObstaclePane.this;
		}
	}
	public ObstaclePane() {
		this.obstacle = null;
		this.isUseCandidate = false;
		this.isHighlighted = false;
		obstacleWrap = new ObstacleWrap();
		obstacleWrap.setOnMouseClicked(clickHandler);
		obstacleWrap.setPickOnBounds(false);
		healthBarPane = new BorderPane();
		healthBar = new HealthBar(false);
		healthBar.setVisible(false);
		healthBar.prefHeightProperty().bind(healthBarPane.heightProperty().multiply(HealthBar.HEALTH_BAR_PERCENT));
		healthBarPane.setBottom(healthBar);
		healthBarPane.setMouseTransparent(true);
		getChildren().addAll(obstacleWrap, healthBarPane);
	}
	
	public ObstacleInfo getObstacleInfo() {
		if(obstacleInfo == null) {
			obstacleInfo = new ObstacleInfo(this) {
				{
					String descriptor = switch(obstacle.getSize()) {
					case SMALL -> "small";
					case LARGE -> "large";
					default -> "extremely strange";
					};
					getChildren().add(new Label(String.format("A %s obstacle.", descriptor)));
				}
			};
		}
		return obstacleInfo;
	}
	
	/**
	 * The given {@link Obstacle} must not be {@code null}.
	 */
	public void setObstacle(Obstacle obstacle, Theme theme) {
		Objects.requireNonNull(obstacle);
		this.obstacle = obstacle;
		healthBar.setGameObject(obstacle);
		healthBar.setVisible(true);
		addListeners();
		obstacleWrap.setImage(theme.imageFor(obstacle));
	}
	
	public Obstacle removeObstacle() {
		if(obstacle == null)
			return null;
		removeListeners();
		obstacleWrap.setImage(null);
		healthBar.clearAndHide();
		Obstacle obstacleTemp = this.obstacle;
		this.obstacle = null;
		return obstacleTemp;
	}
	
	private void addListeners() {
		this.obstacle.aliveProperty().addChangeListener(aliveListener);
	}
	
	private void removeListeners() {
		this.obstacle.aliveProperty().removeChangeListener(aliveListener);
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
