package graphics;

import javafx.scene.image.Image;
import logic.Obstacle;
import logic.ObstacleSize;

/**
 * @author Sam Hooper
 *
 */
public enum Theme {
	TEST_THEME {
		ImageInfo tileInfo = new ImageInfo("desert.png");
		ImageInfo smallObstacleInfo = new ImageInfo("small_obstacle.png");
		
		@Override
		Image tileImage() {
			return tileInfo.getImage();
		}
		@Override
		Image imageFor(Obstacle obstacle) {
			ObstacleSize size = obstacle.getSize();
			if(size == ObstacleSize.SMALL) {
				return smallObstacleInfo.getImage();
			}
			throw new UnsupportedOperationException("Unsupported size: " + size);
		}
	};
	
	abstract Image tileImage();
	abstract Image imageFor(Obstacle obstacle);
	
}
