package finalproj.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class left {

    private BorderPane leftPane;
    private MediaView mediaView;
    private MediaPlayer currentMediaPlayer; 
    private Label placeholder; 

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

        mediaView = new MediaView();
        mediaView.setPreserveRatio(true);
        mediaView.fitWidthProperty().bind(leftPane.widthProperty().subtract(20));
        mediaView.fitHeightProperty().bind(leftPane.heightProperty().subtract(20));

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
        centerStack.getChildren().addAll(mediaView, placeholder);

        leftPane.setCenter(centerStack);
        
        showPlaceholder(); 
    }

    public Node getView() {
        return leftPane;
    }

    public void setMediaPlayer(MediaPlayer player) {
        if (this.currentMediaPlayer != null && this.currentMediaPlayer != player) {
            this.currentMediaPlayer.stop();
            this.currentMediaPlayer.dispose();
            System.out.println("DEBUG: Left: Old MediaPlayer disposed by setMediaPlayer.");
        }
        this.currentMediaPlayer = player; 
        mediaView.setMediaPlayer(player); 

        if (player != null) {
            Platform.runLater(() -> hidePlaceholder());
            System.out.println("DEBUG: Left: MediaPlayer assigned to MediaView. Hiding placeholder.");
        } else {
            Platform.runLater(() -> showPlaceholder());
            System.out.println("DEBUG: Left: MediaPlayer removed from MediaView. Showing placeholder.");
        }
    }

    public MediaPlayer getMediaPlayer() {
        return currentMediaPlayer;
    }

    public void showPlaceholder() {
        placeholder.setText("Choose A Song!");
        placeholder.setVisible(true);
        mediaView.setVisible(false); 
        leftPane.setStyle(
            "-fx-background-image: url('/images/playbackbackground2.jpg'); " +
            "-fx-background-size: cover; " +
            "-fx-background-repeat: no-repeat; " +
            "-fx-background-position: center center;"
        );
    }

    public void hidePlaceholder() {
        placeholder.setVisible(false);
        mediaView.setVisible(true);
        // The background image will now remain visible behind the video.
    }
}