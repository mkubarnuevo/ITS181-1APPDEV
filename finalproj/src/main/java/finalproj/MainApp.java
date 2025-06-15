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
        // Instantiate views first
        left leftPane = new left();
        bottom bottomPane = new bottom();
        right rightPane = new right(bottomPane);
        top topPane = new top(rightPane);

        // Instantiate playbackcontroller with all necessary view references
        playbackcontroller playbackController = new playbackcontroller(bottomPane, leftPane, rightPane);

        // Setup BorderPane layout
        BorderPane root = new BorderPane();
        root.setCenter(leftPane.getView());
        root.setTop(topPane.getView());
        root.setBottom(bottomPane.getView());
        root.setRight(rightPane.getView());

        // maincontroller (if it handles other interactions not related to playback)
        new maincontroller(topPane, rightPane);

        // Set up the scene and stage
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Group 12 Music Player");
        primaryStage.show();
        primaryStage.setResizable(false);

        // --- CRITICAL: Register keybinds AFTER the scene is set on the stage ---
        // Pass the playbackController itself, as it implements the KeyEventHandler logic
        // The bottomPane.registerKeybinds method expects an EventHandler<KeyEvent>.
        playbackController.registerKeybinds(scene); // Pass the scene directly
    }

    public static void main(String[] args) {
        launch(args);
    }
}