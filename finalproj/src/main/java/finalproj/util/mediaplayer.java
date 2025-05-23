package finalproj.util;

import java.nio.file.Path;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class mediaplayer {

    private MediaPlayer mediaPlayer;

    public void loadMedia(Path mediaPath) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        Media media = new Media(mediaPath.toUri().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void setOnEndOfMedia(Runnable handler) {
        if (mediaPlayer != null) {
            mediaPlayer.setOnEndOfMedia(handler);
        }
    }

    public void dispose() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
    }
}
