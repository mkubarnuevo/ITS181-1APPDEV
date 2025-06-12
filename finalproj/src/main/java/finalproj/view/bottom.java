package finalproj.view;

import java.util.List;

import finalproj.model.song;
import finalproj.model.songservice;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox; // <-- ADD THIS IMPORT

public class bottom {

    private VBox bottomPane;
    private ComboBox<String> songDropdown;
    private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private ProgressBar progressBar;
    private SongSelectionListener songListener;

    public bottom() {
        initialize();
        refreshSongDropdown();
    }

    private void initialize() {
        bottomPane = new VBox();
        bottomPane.setPadding(new Insets(10));
        bottomPane.setSpacing(10);
        bottomPane.setStyle("-fx-background-color: #7A918D;");

        HBox controls = new HBox();
        controls.setSpacing(10);
        controls.setPadding(new Insets(0, 0, 0, 0));
        controls.setAlignment(Pos.CENTER_LEFT);

        songDropdown = new ComboBox<>();
        songDropdown.setPromptText("Select a Song");
        songDropdown.setPrefWidth(200);
        songDropdown.setStyle(
            "-fx-background-color: #ffffff;" +
            "-fx-border-color: #DBFEB8;" +
            "-fx-border-radius: 5;" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 5 10;" +
            "-fx-text-fill: #FFFFFF;"
        );

        songDropdown.setOnAction(e -> {
            String selectedSong = songDropdown.getValue();
            if (selectedSong != null && songListener != null) {
                songListener.onSongSelected(selectedSong);
            }
        });

        // playing song label
        Label nowPlayingLabel = new Label("NOW PLAYING");
        nowPlayingLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        // playback buttons
        playButton = new Button("▶");
        pauseButton = new Button("⏪");
        stopButton = new Button("⏩");

        String buttonStyle =
            "-fx-background-color: #C5EDAC;" +
            "-fx-text-fill: #99C2A2;" +
            "-fx-font-size: 22px;" +
            "-fx-padding: 6 18;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.2), 3, 0, 0, 1);";

        playButton.setStyle(buttonStyle);
        pauseButton.setStyle(buttonStyle);
        stopButton.setStyle(buttonStyle);

        HBox buttonsBox = new HBox(15, playButton, pauseButton, stopButton);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);

        // component spacer
        Region spacerLeft = new Region();
        Region spacerRight = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        controls.getChildren().addAll(songDropdown, spacerLeft, nowPlayingLabel, spacerRight, buttonsBox);

        // progress bar
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        progressBar.setStyle("-fx-accent: #DBFEB8;");

        HBox progressContainer = new HBox(progressBar);
        progressContainer.setAlignment(Pos.CENTER);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progressBar, Priority.ALWAYS);

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

    public void refreshSongDropdown() {
        songservice songService = new songservice();
        List<song> songs = songService.getAllSongs();

        Platform.runLater(() -> {
            songDropdown.getItems().clear();
            for (song s : songs) {
                songDropdown.getItems().add(s.getTitle());
            }
        });
    }

    public interface SongSelectionListener {
        void onSongSelected(String songName);
    }

    public void setSongSelectionListener(SongSelectionListener listener) {
        this.songListener = listener;
    }
}