package finalproj;

import finalproj.controller.musiccontroller;
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
        // view sections
        left leftPane = new left();
        top topPane = new top();
        bottom bottomPane = new bottom();
        right rightPane = new right();

        BorderPane root = new BorderPane();

        root.setLeft(leftPane.getView());
        root.setTop(topPane.getView());
        root.setBottom(bottomPane.getView());
        root.setRight(rightPane.getView());

        musiccontroller controller = new musiccontroller(leftPane, rightPane, bottomPane);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Group () Music Player");
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
