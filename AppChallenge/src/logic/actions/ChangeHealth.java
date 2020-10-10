package logic.actions;

import logic.Action;
import logic.Board;
import logic.HasHealth;

/**
 * An {@link Action} that changes the health of some {@link HasHealth}. It can be used as a healing ability (if the {@link #getChange() change}) is positive
 * or a damaging ability (if the change is negative). This class is immutable.
 * @author Sam Hooper
 *
 */
public class ChangeHealth implements Action {
	
	private final HasHealth gameObject;
	private final int change;
	
	/**
	 * @param gameObject the {@link HasHealth} whose health will change
	 * @param change the change in the objects health, where positive heals and negative deals damage.
	 */
	public ChangeHealth(HasHealth gameObject, int change) {
		this.gameObject = gameObject;
		this.change = change;
	}

	public HasHealth getGameObject() {
		return gameObject;
	}

	public int getChange() {
		return change;
	}

	@Override
	public void execute(Board board) {
		if(change < 0) {
			gameObject.healthProperty().set(Math.max(0, gameObject.healthProperty().get() + change));
			if(gameObject.healthProperty().get() == 0) {
				gameObject.getBoard().removeGameObject(gameObject, gameObject.getRow(), gameObject.getCol());
				gameObject.aliveProperty().set(false);
			}
		}
		else {
			gameObject.healthProperty().set(Math.min(gameObject.getMaxHealth(), gameObject.healthProperty().get() + change));
		}
	}
	
}
