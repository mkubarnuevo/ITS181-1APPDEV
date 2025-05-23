package finalproj.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import finalproj.view.bottom;
import finalproj.view.left;
import finalproj.view.right;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class musiccontroller {

    private left leftView;
    private right rightView;
    private bottom bottomView;

    private MediaPlayer mediaPlayer;

    public musiccontroller(left leftView, right rightView, bottom bottomView) {
        this.leftView = leftView;
        this.rightView = rightView;
        this.bottomView = bottomView;

        setupListeners();
    }

    private void setupListeners() {
        leftView.getSongListView().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                playSelectedSong(newVal);
            }
        });

        bottomView.getPlayButton().setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.play();
        });
        bottomView.getPauseButton().setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.pause();
        });
        bottomView.getStopButton().setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                bottomView.getProgressBar().setProgress(0);
            }
        });
    }

    private void playSelectedSong(String songTitle) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }

            String mediaPath = "videos/" + songTitle + ".mp4";
            File mediaFile = new File(mediaPath);
            if (!mediaFile.exists()) {
                System.out.println("Media file not found: " + mediaPath);
                return;
            }

            Media media = new Media(mediaFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            // Load lyrics from lyrics folder
            String lyricsPath = "lyrics/" + songTitle + ".txt";
            String lyrics = "";
            try {
                lyrics = Files.readString(Paths.get(lyricsPath));
            } catch (Exception ex) {
                lyrics = "Lyrics not found.";
            }
            rightView.setLyrics(lyrics);

            mediaPlayer.play();

            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                Duration total = mediaPlayer.getTotalDuration();
                if (total != null && !total.isUnknown()) {
                    double progress = newTime.toMillis() / total.toMillis();
                    Platform.runLater(() -> bottomView.getProgressBar().setProgress(progress));
                }
            });
            
            mediaPlayer.setOnEndOfMedia(() -> {
                Platform.runLater(() -> bottomView.getProgressBar().setProgress(0));
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
