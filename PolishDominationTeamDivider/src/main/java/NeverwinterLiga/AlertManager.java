package NeverwinterLiga;

import javafx.scene.control.Alert;

/**
 * Created by Michal on 2016-04-07.
 */
public class AlertManager {
    public void setError(String text)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
