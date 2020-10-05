package graphics;

import javafx.scene.image.Image;

/**
 * @author Sam Hooper
 *
 */
public enum Theme {
	TEST_THEME {
		ImageInfo tileInfo = new ImageInfo("desert.png");
		@Override
		Image tileImage() {
			return tileInfo.getImage();
		}
	};
	
	abstract Image tileImage();
	
	
}
