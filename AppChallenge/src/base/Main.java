package base;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Sam Hooper
 *
 */
public class Main extends Application {
	public static final String TITLE = "<title>";
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(TITLE);
		primaryStage.show();
	}
}
