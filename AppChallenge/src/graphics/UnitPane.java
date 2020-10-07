package graphics;

import java.util.*;

import fxutils.ImageWrap;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import logic.Ability;
import logic.Unit;
import logic.units.BasicUnit;
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
	}
	
	private static final EventHandler<? super MouseEvent> clickHandler = mouseEvent -> {
		UnitPane pane = ((UnitWrap) mouseEvent.getSource()).getEnclosingInstance();
		Unit unit = pane.getUnit();
		InfoPanel infoPanel = Main.currentLevel().getInfoPanel();
		infoPanel.clearContent();
		AbilityPanel abilityPanel = infoPanel.getAbilityPanel();
		for(Ability ability : unit.getAbilitiesUnmodifiable()) {
			abilityPanel.addPane(pane.abilityPaneFor(ability));
		}
		infoPanel.displayAbilityPanel();
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
	 * @return {@code true} if this {@code UnitPane} had a {@code Unit} and it has been removed, {@code false} otherwise.
	 */
	public boolean removeUnit() {
		if(unit == null)
			return false;
		unitWrap.setImage(null);
		paneMap.clear();
		return true;
	}
	
	public Unit getUnit() {
		return unit;
	}
	
}
