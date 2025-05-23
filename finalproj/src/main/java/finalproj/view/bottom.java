package finalproj.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class bottom {

    private VBox bottomPane;
    private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private ProgressBar progressBar;

    public bottom() {
        initialize();
    }

    private void initialize() {
        bottomPane = new VBox();
        bottomPane.setPadding(new Insets(10));
        bottomPane.setSpacing(10);
        bottomPane.setStyle("-fx-background-color: #bdc3c7;"); // edit niyo color

        HBox controls = new HBox(15);
        controls.setAlignment(Pos.CENTER);

        playButton = new Button("Play");
        pauseButton = new Button("Pause");
        stopButton = new Button("Stop");

        controls.getChildren().addAll(playButton, pauseButton, stopButton);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(860);

        bottomPane.getChildren().addAll(controls, progressBar);
    }

    public Node getView() {
        return bottomPane;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getPauseButton() {
        return pauseButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
