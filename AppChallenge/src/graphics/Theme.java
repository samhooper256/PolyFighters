package graphics;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.Obstacle;
import logic.TileType;

/**
 * @author Sam Hooper
 *
 */
public enum Theme {
	TEST_THEME {
		private static final String TILE_DESCRIPTION = "A Desert Tile.";
		
		private final ImageInfo solidTileInfo = new ImageInfo("desert.png");
		private final ImageInfo liquidTileInfo = new ImageInfo("Water.png");
		private final ImageInfo smallObstacleInfo = new ImageInfo("small_obstacle.png");
		private final ImageInfo largeObstacleInfo = new ImageInfo("large_obstacle.png");
		private final Effect highlightEffect = new InnerShadow(BlurType.GAUSSIAN, Color.LAWNGREEN, 8, .1, 0, 0);
		@Override
		public Image solidTileImage() {
			return solidTileInfo.getImage();
		}
		@Override
		public Image imageFor(Obstacle obstacle) {
			return switch(obstacle.getSize()) {
			case SMALL -> smallObstacleInfo.getImage();
			case LARGE -> largeObstacleInfo.getImage();
			default -> throw new UnsupportedOperationException("Unsupported size: " + obstacle.getSize());
			};
			
		}
		@Override
		public String tileDescription() {
			return TILE_DESCRIPTION;
		}
		@Override
		public Image liquidTileImage() {
			return liquidTileInfo.getImage();
		}
		@Override
		public Effect highlightEffect() {
			return highlightEffect;
		}
	};
	
	public Image imageForTileType(TileType type) {
		return switch(type) {
		case SOLID -> solidTileImage();
		case LIQUID -> liquidTileImage();
		default -> throw new UnsupportedOperationException("Tile type is unsupported: " + type);
		};
	}
	public abstract Image solidTileImage();
	public abstract Image liquidTileImage();
	public abstract Image imageFor(Obstacle obstacle);
	/**
	 * The description that will be displayed in the {@link InfoPanel} when a tile is clicked on.
	 */
	public abstract String tileDescription();
	public abstract Effect highlightEffect();
}
