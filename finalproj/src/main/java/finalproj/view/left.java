package finalproj.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media; // Import Media
import javafx.scene.media.MediaPlayer; // Import MediaPlayer
import javafx.scene.media.MediaView;

public class left {

    private BorderPane leftPane;
    private MediaView mediaView;
    private MediaPlayer mediaPlayer; // Keep a reference to the MediaPlayer
    private Label placeholder; // Make placeholder an instance variable

    public left() {
        initialize();
    }

    private void initialize() {
        leftPane = new BorderPane();
        leftPane.setPadding(new Insets(10));

        leftPane.setStyle(
            "-fx-background-image: url('/images/playbackbackground2.jpg'); " +
            "-fx-background-size: cover; " +
            "-fx-background-repeat: no-repeat; " +
            "-fx-background-position: center center;"
        );

        // MediaView for video playback in center
        mediaView = new MediaView();
        mediaView.setPreserveRatio(true);

        mediaView.fitWidthProperty().bind(leftPane.widthProperty().subtract(20));
        mediaView.fitHeightProperty().bind(leftPane.heightProperty().subtract(20));

        // Initialize placeholder as an instance variable
        placeholder = new Label("Choose A Song!");
        placeholder.setStyle(
            "-fx-text-fill: #7A918D;" +
            "-fx-font-size: 20px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: #C5EDAC; " +
            "-fx-padding: 18px 30px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 0); " +
            "-fx-alignment: center;"
        );
        
        StackPane centerStack = new StackPane();
        centerStack.getChildren().addAll(mediaView, placeholder); // Add both to the stack

        leftPane.setCenter(centerStack);
    }

    public Node getView() {
        return leftPane;
    }

    public MediaView getMediaView() {
        return mediaView;
    }

    /**
     * Sets the media to be played in the MediaView.
     * Stops any currently playing media and loads the new one.
     * @param mediaPath The URL string of the media file (e.g., "file:///path/to/song.mp4").
     */
    public void setMedia(String mediaPath) {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop current playback
            mediaPlayer.dispose(); // Release resources
        }

        if (mediaPath != null && !mediaPath.isEmpty()) {
            try {
                Media media = new Media(mediaPath);
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);

                // Hide placeholder when media is loaded
                placeholder.setVisible(false);

                // Optional: Auto-play the media, or handle playback via controls
                // mediaPlayer.play();

                // Add listeners for potential errors
                mediaPlayer.setOnError(() -> {
                    System.err.println("MediaPlayer Error: " + mediaPlayer.getError());
                    // Show placeholder or display an error message
                    placeholder.setText("Error loading media!");
                    placeholder.setVisible(true);
                });

            } catch (Exception e) {
                System.err.println("Error creating Media or MediaPlayer: " + e.getMessage());
                // Show placeholder or display an error message
                placeholder.setText("Error: Invalid media path!");
                placeholder.setVisible(true);
                mediaView.setMediaPlayer(null); // Clear the media view
            }
        } else {
            // No media path provided, clear media and show placeholder
            mediaView.setMediaPlayer(null);
            placeholder.setText("Choose A Song!");
            placeholder.setVisible(true);
        }
    }

    /**
     * Gets the current MediaPlayer instance associated with this view.
     * This is useful for external controllers to manage playback (play, pause, etc.).
     * @return The current MediaPlayer, or null if none is set.
     */
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * Shows the "Choose A Song!" placeholder label.
     */
    public void showPlaceholder() {
        placeholder.setText("Choose A Song!");
        placeholder.setVisible(true);
    }

    /**
     * Hides the "Choose A Song!" placeholder label.
     */
    public void hidePlaceholder() {
        placeholder.setVisible(false);
    }
}