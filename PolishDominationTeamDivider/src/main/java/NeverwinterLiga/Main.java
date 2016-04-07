package NeverwinterLiga;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main extends Application {
    private Scene scene;
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/pd_never.fxml"));
        primaryStage.setTitle("PolishDomination Team Divider");
        scene = new Scene(root, 558, 430);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        Button btn = (Button) scene.lookup("#licz");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new Controller(scene).run();
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
