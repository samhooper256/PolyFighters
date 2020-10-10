package graphics;

import java.util.*;

import fxutils.ImageWrap;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import logic.Ability;
import logic.Move;
import logic.Unit;
import logic.units.*;
import utils.BooleanChangeListener;
import utils.IntChangeListener;
import utils.SingleListener;

/**
 * @author Sam Hooper
 *
 */
public class UnitPane extends StackPane implements GameObjectRepresentation {
	
	private static final Map<Class<? extends Unit>, ImageInfo> infoMap;
	
	static {
		infoMap = new HashMap<>();
		infoMap.put(BasicUnit.class, new ImageInfo("BasicUnit.png"));
		infoMap.put(Goob.class, new ImageInfo("Goob.png"));
	}
	
	private static final EventHandler<? super MouseEvent> clickHandler = mouseEvent -> {
		System.out.printf("Entered UnitPane click handler%n");
		UnitPane pane = ((UnitWrap) mouseEvent.getSource()).getEnclosingInstance();
		Unit unit = pane.getGameObject();
		if(pane.isUseCandidate()) {
			Level level = Level.current();
			Move move = level.getInfoPanel().getAbilityInfoPanel().getSelectedAbilityPane()
					.getAbility().createMoveFor(unit.getRow(), unit.getCol(), unit);
			level.getTerrainPane().getGrid().executeMove(move);
		}
		else {
			InfoPanel infoPanel = Main.currentLevel().getInfoPanel();
			AbilityInfoPanel abilityPanel = infoPanel.getAbilityInfoPanel();
			if(abilityPanel.getUnit() != unit) {
				infoPanel.clearContent();
				for(Ability ability : unit.getAbilitiesUnmodifiable()) {
					abilityPanel.addPane(pane.abilityPaneFor(ability));
				}
				abilityPanel.setUnit(unit);
			}
			infoPanel.displayOnlyAbilityInfoPanel();
		}
		mouseEvent.consume();
	};
	
	public static Image imageFor(Unit unit) {
		return imageFor(unit.getClass());
	}
	
	public static Image imageFor(Class<? extends Unit> unitClazz) {
		ImageInfo info = infoMap.get(unitClazz);
		if(info == null)
			throw new IllegalArgumentException("There is no image associated with: " + unitClazz);
		return info.getImage();
	}
	
	private class UnitWrap extends ImageWrap {
		UnitPane getEnclosingInstance() {
			return UnitPane.this;
		}
	}
	
	private Unit unit; //defined above the change listeners so that they can refer to it.
	private final HashMap<Ability, AbilityPane> paneMap = new HashMap<>();
	private final UnitWrap unitWrap;
	private final BorderPane healthBarPane;
	private final HealthBar healthBar;
	private final SingleListener<Ability> addListener = ability -> {
		if(paneMap.containsKey(ability))
			throw new IllegalStateException("Duplicate Abilities detected");
		paneMap.put(ability, AbilityPane.of(ability));
	};
	private final SingleListener<Ability> removeListener = ability -> {
		if(paneMap.remove(ability) == null)
			throw new IllegalStateException("Could not remove ability that does not exist");
	};
	private final BooleanChangeListener aliveListener = (oldValue, newValue) -> {
		if(newValue == true)
			throw new UnsupportedOperationException("Revivial is not supported");
		removeUnit(); //this is safe because a BooleanChangeListener is allowed to remove itself from its BooleanRef during its action.
	};
	
	private boolean isUseCandidate, isHighlighted;
	
	/** Creates an empty {@code UnitPane} with no {@link Unit} on it. */
	public UnitPane() {
		this.isUseCandidate = false;
		this.isHighlighted = false;
		unitWrap = new UnitWrap();
		unitWrap.setOnMouseClicked(clickHandler);
		unitWrap.setPickOnBounds(false);
		healthBarPane = new BorderPane();
		healthBar = new HealthBar(true);
		healthBar.setVisible(true);
		healthBar.prefHeightProperty().bind(healthBarPane.heightProperty().multiply(HealthBar.HEALTH_BAR_PERCENT));
		healthBarPane.setBottom(healthBar);
		healthBarPane.setMouseTransparent(true);
		this.setPickOnBounds(false);
		getChildren().addAll(unitWrap, healthBarPane);
	}
	
	private void clearAndFillPaneMap() {
		paneMap.clear();
		for(Ability ability : unit.getAbilitiesUnmodifiable())
			paneMap.put(ability, AbilityPane.of(ability));
	}
	
	public AbilityPane abilityPaneFor(final Ability ability) {
		AbilityPane pane = paneMap.get(ability);
		if(pane != null)
			return pane;
		pane = AbilityPane.of(ability);
		paneMap.put(ability, pane);
		return pane;
	}
	
	/**
	 * {@code unitArg} must not be {@code null}. Note that {@link #removeUnit()} can be used to remove the unit from this pane.
	 * @param unitArg
	 * @throws NullPointerException if the given {@link Unit} is {@code null}.
	 */
	public void setUnit(final Unit unitArg) {
		Objects.requireNonNull(unitArg);
		if(this.unit != null)
			removeListenersFrom(this.unit);
		this.unit = unitArg;
		healthBar.setGameObject(this.unit); //adds the healthlistener in HealthBar
		healthBar.setVisible(true);
		addListenersTo(this.unit);
		clearAndFillPaneMap();
		unitWrap.setImage(imageFor(this.unit));
	}

	/** Removes this {@link UnitPane}'s listeners from the given {@link Unit}. Should only be called when the given {@code Unit} has the listeners. */
	private void removeListenersFrom(Unit unitArg) {
		unitArg.abilityCollectionRef().removeAddListener(addListener);
		unitArg.abilityCollectionRef().removeRemoveListener(removeListener);
		unitArg.aliveProperty().removeChangeListener(aliveListener);
	}

	/** Adds the listeners from {@code this.unit} */
	private void addListenersTo(Unit unitArg) {
		unitArg.abilityCollectionRef().addAddListener(addListener);
		unitArg.abilityCollectionRef().addRemoveListener(removeListener);
		unitArg.aliveProperty().addChangeListener(aliveListener);
	}
	
	
	
	/**
	 * Removes and returns the {@link Unit} on this {@code UnitPane}, or returns {@code null} if there was no {@code Unit} on this {@code UnitPane}.
	 */
	public Unit removeUnit() {
		if(unit == null)
			return null;
		removeListenersFrom(this.unit);
		unitWrap.setImage(null);
		healthBar.clearAndHide();
		paneMap.clear();
		Unit unitTemp = unit;
		unit = null;
		return unitTemp;
	}
	
	public boolean hasUnit() {
		return unit != null;
	}
	
	@Override
	public Unit getGameObject() {
		return unit;
	}

	@Override
	public void setUseCandidate(boolean value) {
		isUseCandidate = value;
	}

	@Override
	public boolean isUseCandidate() {
		return isUseCandidate;
	}
	
	/**
	 * Uses the {@link Level#current() current level}'s theme to highlight this {@link UnitPane}. 
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
}
