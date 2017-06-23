package Program.IDE;

import Program.IDE.Managers.ConsoleManager;
import Program.IDE.Managers.TabAndTreeManager;
import Program.IDE.Objects.Editor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import static javafx.scene.layout.HBox.setHgrow;

public class Main extends Application {

    private boolean maximized = false;
    private Label maxLabel;

    class WindowButtons extends HBox {

        public WindowButtons(Stage stage) {
            Label closeLabel = new Label("");
            closeLabel.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/img/window/close.png"))));
            Button closeBtn = new Button();
            closeBtn.setId("window_button");
            closeBtn.setGraphic(closeLabel);

            Label minLabel = new Label("");
            minLabel.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/img/window/min.png"))));
            Button minBtn = new Button();
            minBtn.setId("window_button");
            minBtn.setGraphic(minLabel);

            maxLabel = new Label("");
            maxLabel.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/img/window/max1.png"))));
            Button maxBtn = new Button();
            maxBtn.setId("window_button");
            maxBtn.setGraphic(maxLabel);

            closeBtn.setOnAction(actionEvent -> Platform.exit());
            minBtn.setOnAction(actionEvent -> stage.setIconified(true));
            maxBtn.setOnAction(actionEvent -> {
                if(maximized) {
                    stage.setMaximized(false);
                    maxLabel.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/img/window/max1.png"))));
                    maximized = false;
                } else {
                    stage.setMaximized(true);
                    maxLabel.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/img/window/max2.png"))));
                    maximized = true;
                }
            });

            this.getChildren().add(minBtn);
            this.getChildren().add(maxBtn);
            this.getChildren().add(closeBtn);
        }
    }

    private BorderPane borderPane;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/compiler_view.fxml"));


        primaryStage.setTitle("Asm IDE & Compiler 2.0");
        Scene scene = new Scene(root, 1000, 650);
        boolean isDark = false;
        String theme = "light";
        if(isDark) {
            theme = "dark";
        }

        Image icon = new Image(getClass().getResourceAsStream("/img/logo.png"));

        scene.getStylesheets().add(getClass().getResource("/"+theme+"/main.css").toExternalForm());
        scene.getStylesheets().add(Editor.class.getResource("/"+theme+"/keywords-style.css").toExternalForm());
        scene.getStylesheets().add(TabAndTreeManager.class.getResource("/"+theme+"/treeview.css").toExternalForm());
        scene.getStylesheets().add(TabAndTreeManager.class.getResource("/"+theme+"/tabpane.css").toExternalForm());
        scene.getStylesheets().add(ConsoleManager.class.getResource("/"+theme+"/console.css").toExternalForm());
        primaryStage.getIcons().add(icon);


        View view = new View(root.getScene());

        borderPane = view.getBorderPane();

//        setCustomBar(primaryStage, icon, "Asm IDE & Compiler 2.0");

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

        new Controller(view);
    }

    private static double xOffset = 0;
    private static double yOffset = 0;
    private void setCustomBar(Stage primaryStage, Image icon, String title) {
        primaryStage.initStyle(StageStyle.UNDECORATED);

        ToolBar toolBar = new ToolBar();

        int height = 35;
        toolBar.setPrefHeight(height);
        toolBar.setMinHeight(height);
        toolBar.setMaxHeight(height);


        Label titleLabel = new Label(title);
        //wypelnienie hboxem aby wyrownac do prawej przyciski zamykania itp
        HBox filler = new HBox();
        setHgrow(filler, Priority.ALWAYS);
        ImageView iconImageView = new ImageView(icon);
        iconImageView.setFitHeight(24);
        iconImageView.setFitWidth(24);
        toolBar.getItems().add(iconImageView);
        toolBar.getItems().add(titleLabel);
        toolBar.getItems().add(filler);
        toolBar.getItems().add(new WindowButtons(primaryStage));

        borderPane.setTop(toolBar);

        setDragAction(toolBar, primaryStage);
    }

    private void setDragAction(ToolBar toolBar, Stage primaryStage) {
        toolBar.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });

        toolBar.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
