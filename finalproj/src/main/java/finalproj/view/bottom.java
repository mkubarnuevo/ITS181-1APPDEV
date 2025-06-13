package finalproj.view;

import java.util.Collections;
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
import javafx.scene.layout.VBox;

public class bottom {

    private VBox bottomPane;
    private ComboBox<String> songDropdown;
    private Button playButton;
    private Button rewindButton;
    private Button forwardButton;
    private Button shuffleButton;
    private ProgressBar progressBar;
    private SongSelectionListener songListener;
    private Label currentlyPlayingSongLabel;

    public bottom() {
        initialize();
        refreshSongDropdown();
    }

    private void initialize() {
        bottomPane = new VBox();
        bottomPane.setPadding(new Insets(10));
        bottomPane.setSpacing(10);
        bottomPane.setStyle("-fx-background-color: #7A918D;");

        HBox topControlsBox = new HBox();
        topControlsBox.setSpacing(10);
        topControlsBox.setPadding(new Insets(0, 0, 0, 0));
        topControlsBox.setAlignment(Pos.CENTER_LEFT);

        songDropdown = new ComboBox<>();
        songDropdown.setPromptText("Select a Song");
        songDropdown.setPrefWidth(200);
        songDropdown.setStyle(
            "-fx-background-color: #C5EDAC;" +
            "-fx-border-color: #99C2A2;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 1px;" +
            "-fx-padding: 5px 10px;" +
            "-fx-font-size: 13px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.5), 3, 0, 0, 1);" +
            "-fx-background-insets: 0;"
        );

        songDropdown.setButtonCell(new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-text-fill: #232023;");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #232023;");
                }
            }
        });
        
        songDropdown.setOnAction(e -> {
            String selectedSong = songDropdown.getValue();
            if (selectedSong != null) {
                // update currently playing song
                currentlyPlayingSongLabel.setText(selectedSong);
                if (songListener != null) {
                    songListener.onSongSelected(selectedSong);
                }
            } else {
                currentlyPlayingSongLabel.setText("No song selected");
            }
        });

        // playing song label
        Label nowPlayingTitleLabel = new Label("NOW PLAYING");
        nowPlayingTitleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        currentlyPlayingSongLabel = new Label("No song selected");
        currentlyPlayingSongLabel.setStyle("-fx-text-fill: #DBFEB8; -fx-font-size: 16px; -fx-font-weight: bold;");

        // playback buttons
        playButton = new Button("▶");
        rewindButton = new Button("⏪");
        forwardButton = new Button("⏩");
        shuffleButton = new Button("⇄");

        // Common button style string
        String buttonStyle =
            "-fx-background-color: #C5EDAC;" +
            "-fx-text-fill: #99C2A2;" +
            "-fx-font-size: 22px;" +
            "-fx-padding: 6 18;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.5), 3, 0, 0, 1);";

        rewindButton.setStyle(buttonStyle);
        playButton.setStyle(buttonStyle);
        forwardButton.setStyle(buttonStyle);
        shuffleButton.setStyle(buttonStyle);

        shuffleButton.setOnAction(e -> {
            songservice songService = new songservice();
            List<song> allSongs = songService.getAllSongs();

            if (!allSongs.isEmpty()) {
                Collections.shuffle(allSongs); // Shuffle the list of songs

                Platform.runLater(() -> {
                    songDropdown.getItems().clear(); // Clear current items
                    allSongs.forEach(s -> songDropdown.getItems().add(s.getTitle())); // Add shuffled titles

                    // Select the first song in the shuffled list
                    String firstShuffledSong = allSongs.get(0).getTitle();
                    songDropdown.getSelectionModel().select(firstShuffledSong);
                    currentlyPlayingSongLabel.setText(firstShuffledSong); // Update label immediately

                    // Notify listener of the newly selected (first shuffled) song
                    if (songListener != null) {
                        songListener.onSongSelected(firstShuffledSong);
                    }
                    System.out.println("DEBUG: Songs shuffled. First song selected: " + firstShuffledSong);
                });
            } else {
                Platform.runLater(() -> {
                    currentlyPlayingSongLabel.setText("No songs to shuffle.");
                    System.out.println("DEBUG: Attempted to shuffle, but no songs are available.");
                });
            }
        });

        HBox playbackButtonsBox = new HBox(15, rewindButton, playButton, forwardButton, shuffleButton);
        playbackButtonsBox.setAlignment(Pos.CENTER_RIGHT);

        // component spacer
        Region spacerLeft = new Region();
        Region spacerRight = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        VBox nowPlayingDisplayBox = new VBox(5, nowPlayingTitleLabel, currentlyPlayingSongLabel); // Uses nowPlayingTitleLabel
        nowPlayingDisplayBox.setAlignment(Pos.CENTER);

        // Clear and re-add all children to the main top HBox
        topControlsBox.getChildren().clear(); // This is crucial to ensure correct order and no duplicates
        topControlsBox.getChildren().addAll(songDropdown, spacerLeft, nowPlayingDisplayBox, spacerRight, playbackButtonsBox);

        // progress bar
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);
        progressBar.setStyle("-fx-accent: #DBFEB8;");

        HBox progressContainer = new HBox(progressBar);
        progressContainer.setAlignment(Pos.CENTER);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progressBar, Priority.ALWAYS);

        // adding the correctly assembled HBox and progress bar
        bottomPane.getChildren().addAll(topControlsBox, progressContainer);
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

    public Button getRewindButton() {
        return rewindButton;
    }

    public Button getForwardButton() {
        return forwardButton;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void refreshSongDropdown() {
        songservice songService = new songservice();
        List<song> songs = songService.getAllSongs();

        Platform.runLater(() -> {
            songDropdown.getItems().clear();
            songs.forEach(s -> songDropdown.getItems().add(s.getTitle()));
            if (songs.isEmpty() && songDropdown.getValue() == null) {
                currentlyPlayingSongLabel.setText("No song selected");
            } else if (songDropdown.getValue() == null && !songs.isEmpty()) {
                currentlyPlayingSongLabel.setText("Select a song");
            }
        });
    }

    public void setCurrentlyPlayingSong(String songTitle) {
        Platform.runLater(() -> {
            if (songTitle != null && !songTitle.isEmpty()) {
                currentlyPlayingSongLabel.setText(songTitle);
            } else {
                currentlyPlayingSongLabel.setText("No song selected");
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