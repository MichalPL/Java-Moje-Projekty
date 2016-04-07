package NeverwinterLiga;

import javafx.scene.Scene;

/**
 * Created by Michal on 2016-04-06.
 */
public class Controller {
    Scene scene;

    public Controller(Scene scene) {
        this.scene = scene;
    }

    public void run()
    {
        AlertManager alertManager = new AlertManager();
        try {
            Weight weight = new Weight();
            InputWorker inputWorker = new InputWorker(scene, weight, alertManager);
            inputWorker.saveToFile();
            DataWorker dataWorker = new DataWorker(inputWorker, alertManager);
            Utils utils = new Utils();
            Matcher matcher = new Matcher(dataWorker, utils, inputWorker.getAllPlayerCount(),
                    inputWorker.getAllPlayerCount(), weight, alertManager);
            try {
                matcher.match();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Printer printer = new Printer(matcher, inputWorker, alertManager);
            printer.printAllData();
        } catch (Exception ignored) {
            alertManager.setError("Coś poszło nie tak...!");
        }
    }
}
