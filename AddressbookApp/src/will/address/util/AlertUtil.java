package will.address.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;

/**
 * Helper Class for handling message alerts
 * 
 * @author Wilfred Dube
 */

public class AlertUtil {

	/**
	 * Displays error message dialog
	 * 
	 * @param alertType
	 * @param window
	 */
	public static void reportError(AlertType alertType, Window window, String title, String headerText, String contentText) {
		Alert alert = new Alert(alertType);

		alert.initOwner(window);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		alert.showAndWait();
	}

	/**
	 * Displays information like about 
	 * 
	 * @param title
	 * @param headerText
	 * @param contentText
	 */
	public static void displayInformation(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.INFORMATION);

		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		alert.showAndWait();
	}

}
