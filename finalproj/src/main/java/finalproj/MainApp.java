package finalproj;

import finalproj.controller.maincontroller;
import finalproj.controller.playbackcontroller;
import finalproj.view.bottom;
import finalproj.view.left;
import finalproj.view.right;
import finalproj.view.top;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        left leftPane = new left();
        bottom bottomPane = new bottom();
        playbackcontroller controller = new playbackcontroller(bottomPane);
        right rightPane = new right(bottomPane);
        top topPane = new top(rightPane);

        BorderPane root = new BorderPane();
        root.setCenter(leftPane.getView());
        root.setTop(topPane.getView());
        root.setBottom(bottomPane.getView());
        root.setRight(rightPane.getView());

        // controller
        new maincontroller(topPane, rightPane);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Group 12 Music Player");
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}