package graphics;

import java.util.*;

import fxutils.ImageWrap;
import fxutils.Images;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import logic.Unit;
import logic.units.BasicUnit;

/**
 * @author Sam Hooper
 *
 */
public class UnitPane extends StackPane {
	
	public static final class UnitInfo {
		private final String filename;
		private Image image; //lazy initialized, so not final
		private UnitInfo(String filename) {
			Objects.requireNonNull(filename);
			this.filename = filename;
			image = null;
		}
		
		public String getFilename() {
			return filename;
		}
		public Image getImage() {
			if(image == null)
				image = Images.getImage(filename);
			return image;
		}
	}
	
	private static final Map<Class<? extends Unit>, UnitInfo> infoMap;
	
	static {
		infoMap = new HashMap<>();
		infoMap.put(BasicUnit.class, new UnitInfo("BasicUnit.png"));
	}
	
	public static Image imageFor(Unit unit) {
		return imageFor(unit.getClass());
	}
	
	public static Image imageFor(Class<? extends Unit> unitClazz) {
		UnitInfo info = infoMap.get(unitClazz);
		if(info == null)
			throw new IllegalArgumentException("There is no image associated with: " + unitClazz);
		return info.getImage();
	}
	
	private final ImageWrap unitWrap;
	private Unit unit;
	
	/** Creates an empty {@code UnitPane} with no {@link Unit} on it. */
	public UnitPane() {
		super();
		unitWrap = new ImageWrap();
	}
	
	public void setUnit(Unit unit) {
		this.unit = unit;
		unitWrap.setImage(imageFor(unit));
	}
	
	public Unit getUnit() {
		return unit;
	}	
	
}
