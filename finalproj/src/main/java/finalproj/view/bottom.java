package finalproj.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class bottom {

    private VBox bottomPane;
    private ComboBox<String> songDropdown;
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
        bottomPane.setStyle("-fx-background-color: #bdc3c7;");

        // Controls HBox containing dropdown (left) and buttons (center)
        HBox controls = new HBox();
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setSpacing(10);

        // Dropdown - fixed width, left aligned by default
        songDropdown = new ComboBox<>();
        songDropdown.setPromptText("Select a Song");
        songDropdown.setPrefWidth(250);

        // Spacer that will take remaining space pushing buttons to center
        Region spacerLeft = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);

        // Buttons HBox centered horizontally by adding spacers on left and right
        playButton = new Button("Play");
        pauseButton = new Button("Pause");
        stopButton = new Button("Stop");

        HBox buttonsBox = new HBox(15, playButton, pauseButton, stopButton);
        buttonsBox.setAlignment(Pos.CENTER);

        Region spacerButtonsLeft = new Region();
        Region spacerButtonsRight = new Region();
        HBox.setHgrow(spacerButtonsLeft, Priority.ALWAYS);
        HBox.setHgrow(spacerButtonsRight, Priority.ALWAYS);

        // Wrap buttons with spacers to center them in the space available after dropdown
        HBox centeredButtons = new HBox(spacerButtonsLeft, buttonsBox, spacerButtonsRight);
        centeredButtons.setAlignment(Pos.CENTER);
        centeredButtons.setPrefWidth(300);  // Set a width for the buttons area (adjust as needed)

        controls.getChildren().addAll(songDropdown, spacerLeft, centeredButtons);

        // Progress bar centered below controls
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300); // narrower width for better center look
        VBox.setMargin(progressBar, new Insets(0, 0, 0, 0)); // no extra margins
        progressBar.setStyle("-fx-accent: #3498db;"); // optional: style color


        // Center the progress bar by putting it in an HBox with center alignment
        HBox progressContainer = new HBox(progressBar);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progressBar, Priority.ALWAYS);
        progressContainer.setAlignment(Pos.CENTER);

        bottomPane.getChildren().addAll(controls, progressContainer);
    }

    public Node getView() {
        return bottomPane;
    }

    public ComboBox<String> getSongDropdown() {
        return songDropdown;
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
