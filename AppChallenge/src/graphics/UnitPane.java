package graphics;

import java.util.*;

import fxutils.ImageWrap;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import logic.Ability;
import logic.Unit;
import logic.units.*;
import utils.SingleListener;

/**
 * @author Sam Hooper
 *
 */
public class UnitPane extends StackPane {
	
	private static final Map<Class<? extends Unit>, ImageInfo> infoMap;
	
	static {
		infoMap = new HashMap<>();
		infoMap.put(BasicUnit.class, new ImageInfo("BasicUnit.png"));
		infoMap.put(BasicEnemy.class, new ImageInfo("BasicEnemy.png"));
	}
	
	private static final EventHandler<? super MouseEvent> clickHandler = mouseEvent -> {
		UnitPane pane = ((UnitWrap) mouseEvent.getSource()).getEnclosingInstance();
		Unit unit = pane.getUnit();
		InfoPanel infoPanel = Main.currentLevel().getInfoPanel();
		AbilityPanel abilityPanel = infoPanel.getAbilityPanel();
		if(abilityPanel.getUnit() != unit) {
			infoPanel.clearContent();
			for(Ability ability : unit.getAbilitiesUnmodifiable()) {
				abilityPanel.addPane(pane.abilityPaneFor(ability));
			}
			abilityPanel.setUnit(unit);
		}
		infoPanel.displayAbilityPanel();
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
	
	private final HashMap<Ability, AbilityPane> paneMap = new HashMap<>();
	private final UnitWrap unitWrap;
	private final SingleListener<Ability> addListener = ability -> {
		if(paneMap.containsKey(ability))
			throw new IllegalStateException("Duplicate Abilities detected");
		paneMap.put(ability, AbilityPane.of(ability));
	};
	private final SingleListener<Ability> removeListener = ability -> {
		if(paneMap.remove(ability) == null)
			throw new IllegalStateException("Could not remove ability that does not exist");
	};
	
	private Unit unit;
	
	/** Creates an empty {@code UnitPane} with no {@link Unit} on it. */
	public UnitPane() {
		super();
		unitWrap = new UnitWrap();
		unitWrap.setOnMouseClicked(clickHandler);
		getChildren().add(unitWrap);
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
	 * {@code unit} must not be {@code null}. Note that {@link #removeUnit()} can be used to remove the unit from this pane.
	 * @param unit
	 */
	public void setUnit(Unit unit) {
		Objects.requireNonNull(unit);
		if(this.unit != null)
			removeListeners();
		this.unit = unit;
		addListeners();
		clearAndFillPaneMap();
		unitWrap.setImage(imageFor(unit));
	}

	/** Removes the listeners from {@code this.unit} */
	private void removeListeners() {
		this.unit.abilityCollectionRef().removeAddListener(addListener);
		this.unit.abilityCollectionRef().removeRemoveListener(removeListener);
	}

	/** Adds the listeners from {@code this.unit} */
	private void addListeners() {
		this.unit.abilityCollectionRef().addAddListener(addListener);
		this.unit.abilityCollectionRef().addRemoveListener(removeListener);
	}
	
	/**
	 * Removes and returns the {@link Unit} on this {@code UnitPane}, or returns {@code null} if there was no {@code Unit} on this {@code UnitPane}.
	 */
	public Unit removeUnit() {
		if(unit == null)
			return null;
		unitWrap.setImage(null);
		removeListeners();
		paneMap.clear();
		Unit unitTemp = unit;
		unit = null;
		return unitTemp;
	}
	
	public Unit getUnit() {
		return unit;
	}
	
}
