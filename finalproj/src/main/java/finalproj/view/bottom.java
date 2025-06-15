package finalproj.view;

import java.util.ArrayList;
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
import javafx.scene.input.KeyEvent;
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
    private boolean isPlaying = false; // This reflects the UI state, not MediaPlayer state

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

        // --- Song Dropdown Action Handler (NOTIFIES LISTENER and updates UI state) ---
        songDropdown.setOnAction(e -> {
            String selectedSong = songDropdown.getValue();
            if (selectedSong != null) {
                currentlyPlayingSongLabel.setText(selectedSong);
                if (songListener != null) {
                    songListener.onSongSelected(selectedSong); // Delegate to listener for media playback
                }
                // The actual `isPlaying` state and play button text will be set by playbackcontroller
                // once the media is ready and starts playing.
            } else {
                currentlyPlayingSongLabel.setText("No song selected");
                isPlaying = false; // UI state for play/pause button
                playButton.setText("▶"); // UI state for play/pause button
                if (songListener != null) {
                    songListener.onSongSelected(null); // Indicate no song selected/stop
                }
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

        // --- Play/Pause Button Logic (Updates UI locally, listener for playback) ---
        playButton.setOnAction(e -> {
            if (currentlyPlayingSongLabel.getText().equals("No song selected") ||
                currentlyPlayingSongLabel.getText().equals("Select a song")) {
                System.out.println("DEBUG: Cannot play/pause. No song selected.");
                return;
            }

            if (isPlaying) { // If currently playing, pause it
                // We'll let playbackcontroller update the UI state.
                if (songListener != null) songListener.onPause(); // Delegate pause action
                System.out.println("DEBUG: Paused playback request for " + currentlyPlayingSongLabel.getText());
            } else { // If currently paused, play it
                // We'll let playbackcontroller update the UI state.
                if (songListener != null) songListener.onPlay(); // Delegate play/resume action
                System.out.println("DEBUG: Play/Resume playback request for " + currentlyPlayingSongLabel.getText());
            }
            // isPlaying state will be updated by setPlayButtonState from playbackcontroller
        });

        // --- Rewind Button Logic (Delegates to listener) ---
        rewindButton.setOnAction(e -> {
            if (songListener != null) {
                songListener.onRewind();
            }
            System.out.println("DEBUG: Rewind button pressed.");
        });

        // --- Forward Button Logic (Delegates to listener) ---
        forwardButton.setOnAction(e -> {
            if (songListener != null) {
                songListener.onForward();
            }
            System.out.println("DEBUG: Forward button pressed.");
        });

        // --- Shuffle Button Logic (Ensures a song is always selected if available) ---
        shuffleButton.setOnAction(e -> {
            songservice songService = new songservice();
            List<song> allSongs = songService.getAllSongs();
            String currentlyPlayingTitle = currentlyPlayingSongLabel.getText();

            List<song> availableSongsForShuffle = new ArrayList<>(allSongs);

            // Remove the currently playing song from the list if it exists and is valid
            if (!currentlyPlayingTitle.equals("No song selected") && !currentlyPlayingTitle.equals("Select a song")) {
                availableSongsForShuffle.removeIf(s -> s.getTitle().equals(currentlyPlayingTitle));
                System.out.println("DEBUG: Removed '" + currentlyPlayingTitle + "' from shuffle candidates.");
            }

            if (!availableSongsForShuffle.isEmpty()) {
                Collections.shuffle(availableSongsForShuffle);

                Platform.runLater(() -> {
                    String nextShuffledSong = availableSongsForShuffle.get(0).getTitle();
                    songDropdown.getSelectionModel().select(nextShuffledSong); // This will trigger onAction
                    // The onAction of songDropdown already calls songListener.onSongSelected(nextShuffledSong)
                    // and playbackcontroller handles setting the UI state (isPlaying and playButton.setText).
                    System.out.println("DEBUG: Songs shuffled. Next song selected: " + nextShuffledSong);
                });
            } else if (!allSongs.isEmpty() && allSongs.size() == 1) { // Only one song available
                Platform.runLater(() -> {
                    System.out.println("DEBUG: Only one song available. No new song to shuffle to.");
                    // You might want to re-select the existing song to restart it, or do nothing.
                    // For now, it will simply stay on the current song if it's the only one.
                    // If no song is playing, and there's only one, select it.
                    if (currentlyPlayingTitle.equals("No song selected") || currentlyPlayingTitle.equals("Select a song")) {
                        songDropdown.getSelectionModel().select(allSongs.get(0).getTitle());
                    }
                });
            } else { // No songs at all
                Platform.runLater(() -> {
                    currentlyPlayingSongLabel.setText("No songs to shuffle.");
                    System.out.println("DEBUG: Attempted to shuffle, but no songs are available.");
                    // Update UI state to stopped
                    isPlaying = false;
                    playButton.setText("▶");
                    // Also clear playback if any phantom media was playing
                    if (songListener != null) songListener.onStop(); 
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

        VBox nowPlayingDisplayBox = new VBox(5, nowPlayingTitleLabel, currentlyPlayingSongLabel);
        nowPlayingDisplayBox.setAlignment(Pos.CENTER);

        topControlsBox.getChildren().clear();
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

    public Button getShuffleButton() {
        return shuffleButton;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void registerKeybinds(javafx.event.EventHandler<KeyEvent> handler) {
        bottomPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (oldScene != null) {
                oldScene.removeEventFilter(KeyEvent.KEY_PRESSED, handler);
                System.out.println("DEBUG: Removed old keybind handler from scene.");
            }
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, handler);
                System.out.println("DEBUG: Added new keybind handler to scene.");
            }
        });
    }

    public void refreshSongDropdown() {
        songservice songService = new songservice();
        List<song> songs = songService.getAllSongs();

        Platform.runLater(() -> {
            String currentSelection = songDropdown.getValue();
            songDropdown.getItems().clear();
            songs.forEach(s -> songDropdown.getItems().add(s.getTitle()));
            
            if (songs.isEmpty()) {
                songDropdown.setPromptText("No Songs Available");
                currentlyPlayingSongLabel.setText("No song selected");
                setPlayButtonState(false);
                updateProgressBar(0); // Also reset progress bar if no songs
                if (songListener != null) {
                    songListener.onSongSelected(null); // Notify listener no song selected
                }
            } else {
                songDropdown.setPromptText("Select a Song");
                if (currentSelection != null && songDropdown.getItems().contains(currentSelection)) {
                    songDropdown.getSelectionModel().select(currentSelection);
                    currentlyPlayingSongLabel.setText(currentSelection);
                } else {
                    currentlyPlayingSongLabel.setText("Select a song");
                    setPlayButtonState(false); // Reset to play icon
                    updateProgressBar(0); // Reset progress bar
                }
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
    
    public void setPlayButtonState(boolean playing) {
        this.isPlaying = playing; // Update internal UI state
        Platform.runLater(() -> playButton.setText(playing ? "𝗹𝗹" : "▶"));
    }

    public void updateProgressBar(double progress) {
        Platform.runLater(() -> progressBar.setProgress(progress));
    }

    public void clearCurrentSongDisplay() {
        Platform.runLater(() -> {
            currentlyPlayingSongLabel.setText("No song selected");
            songDropdown.getSelectionModel().clearSelection();
            songDropdown.setPromptText("Select a Song"); // Reset prompt text
            setPlayButtonState(false);
            updateProgressBar(0);
        });
    }

    // --- SongSelectionListener Interface ---
    public interface SongSelectionListener {
        void onSongSelected(String songName); 
        void onPlay();
        void onPause();
        void onRewind();
        void onForward();
        void onStop(); 
        void onShuffle(); 
    }

    public void setSongSelectionListener(SongSelectionListener listener) {
        this.songListener = listener;
    }
}