package graphics;

import java.util.*;

import fxutils.ImageWrap;
import javafx.scene.image.Image;
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
	
	public static Image imageFor(Unit unit) {
		return imageFor(unit.getClass());
	}
	
	public static Image imageFor(Class<? extends Unit> unitClazz) {
		ImageInfo info = infoMap.get(unitClazz);
		if(info == null)
			throw new IllegalArgumentException("There is no image associated with: " + unitClazz);
		return info.getImage();
	}
	
	private final HashMap<Ability, AbilityPane> paneMap = new HashMap<>();
	private final ImageWrap unitWrap;
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
		unitWrap = new ImageWrap();
		getChildren().add(unitWrap);
	}
	
	private void clearAndFillPaneMap() {
		paneMap.clear();
		for(Ability ability : unit.getAbilitiesUnmodifiable())
			paneMap.put(ability, AbilityPane.of(ability));
	}
	
	public AbilityPane paneFor(final Ability ability) {
		AbilityPane pane = paneMap.get(ability);
		if(pane != null)
			return pane;
		pane = AbilityPane.of(ability);
		paneMap.put(ability, pane);
		return pane;
	}
	
	public void setUnit(Unit unit) {
		this.unit = unit;
		clearAndFillPaneMap();
		unitWrap.setImage(imageFor(unit));
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
