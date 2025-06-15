/*
package finalproj.controller;

import finalproj.view.left;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import finalproj.view.bottom;
import finalproj.view.left;
import finalproj.view.right;
import javafx.application.Platform;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

// Implement the SongSelectionListener interface
public class musiccontroller implements bottom.SongSelectionListener {

    private left leftView;
    private right rightView;
    private bottom bottomView;

    private MediaPlayer currentMediaPlayer;

    public musiccontroller(left leftView, right rightView, bottom bottomView) {
        this.leftView = leftView;
        this.rightView = rightView;
        this.bottomView = bottomView;

        // Register this controller as the song selection listener for the bottom view
        // THIS IS THE KEY CHANGE HERE
        this.bottomView.setSongSelectionListener(this);

        setupButtonListeners(); // Renamed to clarify it only sets up button actions
    }

    // Renamed this method to be more specific
    private void setupButtonListeners() {
        // Play/Pause Button Logic
        bottomView.getPlayButton().setOnAction(e -> {
            currentMediaPlayer = leftView.getMediaPlayer();
            if (currentMediaPlayer != null) {
                if (currentMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    currentMediaPlayer.pause();
                    System.out.println("DEBUG: MediaPlayer paused.");
                } else if (currentMediaPlayer.getStatus() == MediaPlayer.Status.PAUSED ||
                           currentMediaPlayer.getStatus() == MediaPlayer.Status.STOPPED ||
                           currentMediaPlayer.getStatus() == MediaPlayer.Status.READY) {
                    currentMediaPlayer.play();
                    System.out.println("DEBUG: MediaPlayer played/resumed.");
                }
            } else {
                System.out.println("DEBUG: Play button pressed but no MediaPlayer is set. Attempting to select first song.");
                if (!bottomView.getSongDropdown().getItems().isEmpty() && bottomView.getSongDropdown().getValue() == null) {
                    bottomView.getSongDropdown().getSelectionModel().selectFirst();
                }
            }
        });

        // Rewind/Previous Button Logic
        bottomView.getRewindButton().setOnAction(e -> {
            currentMediaPlayer = leftView.getMediaPlayer();
            if (currentMediaPlayer != null) {
                currentMediaPlayer.seek(Duration.ZERO);
                System.out.println("DEBUG: MediaPlayer seeked to beginning.");
            } else {
                System.out.println("DEBUG: Rewind button pressed but no MediaPlayer is set.");
            }
        });

        // Forward/Next Button Logic
        bottomView.getForwardButton().setOnAction(e -> {
            currentMediaPlayer = leftView.getMediaPlayer();
            if (currentMediaPlayer != null) {
                currentMediaPlayer.stop();
                Platform.runLater(() -> bottomView.getProgressBar().setProgress(0));
                System.out.println("DEBUG: MediaPlayer stopped/forwarded (simulated).");
            } else {
                System.out.println("DEBUG: Forward button pressed but no MediaPlayer is set.");
            }
        });
    }

    // This method is now required by the bottom.SongSelectionListener interface
    @Override
    public void onSongSelected(String songName) {
        System.out.println("DEBUG: MusicController received song selection: " + songName);
        playSelectedSong(songName);
    }

    private void playSelectedSong(String songTitle) {
        try {
            if (currentMediaPlayer != null) {
                currentMediaPlayer.stop();
                currentMediaPlayer.dispose();
                currentMediaPlayer = null;
            }

            File mediaFile = new File("videos" + File.separator + songTitle + ".mp4");
            String mediaPathURI = mediaFile.toURI().toString();

            System.out.println("Attempting to load media from: " + mediaPathURI);
            
            if (!mediaFile.exists() || !mediaFile.isFile()) {
                System.out.println("Media file not found or is not a file: " + mediaFile.getAbsolutePath());
                Platform.runLater(() -> {
                    leftView.showPlaceholder();
                    rightView.setLyrics("Video file not found for: " + songTitle + "\nExpected at: " + mediaFile.getAbsolutePath());
                    bottomView.getProgressBar().setProgress(0);
                    bottomView.setCurrentlyPlayingSong(null);
                });
                return;
            }

            leftView.setMediaPlayer(mediaPathURI);
            currentMediaPlayer = leftView.getMediaPlayer();

            if (currentMediaPlayer != null) {
                currentMediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                    Duration total = currentMediaPlayer.getTotalDuration();
                    if (total != null && !total.isUnknown() && total.toMillis() > 0) {
                        if (currentMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING ||
                            currentMediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                            Platform.runLater(() -> bottomView.getProgressBar().setProgress(newTime.toMillis() / total.toMillis()));
                        }
                    }
                });

                currentMediaPlayer.setOnEndOfMedia(() -> {
                    Platform.runLater(() -> {
                        bottomView.getProgressBar().setProgress(0);
                        currentMediaPlayer.stop();
                        bottomView.setCurrentlyPlayingSong(null);
                        leftView.showPlaceholder();
                        System.out.println("DEBUG: End of media reached for " + songTitle);
                    });
                });
                
                currentMediaPlayer.setOnError(() -> {
                    System.err.println("MediaPlayer encountered an error: " + currentMediaPlayer.getError());
                    Platform.runLater(() -> {
                        leftView.showPlaceholder();
                        rightView.setLyrics("An error occurred during playback: " + currentMediaPlayer.getError().getMessage());
                        bottomView.getProgressBar().setProgress(0);
                        bottomView.setCurrentlyPlayingSong(null);
                    });
                });

                currentMediaPlayer.setOnReady(() -> {
                    currentMediaPlayer.play();
                    System.out.println("DEBUG: MediaPlayer is READY and playing: " + songTitle);
                    Platform.runLater(() -> bottomView.setCurrentlyPlayingSong(songTitle));
                });
                
                if (currentMediaPlayer.getStatus() == MediaPlayer.Status.READY) {
                     currentMediaPlayer.play();
                     System.out.println("DEBUG: MediaPlayer already READY, playing immediately: " + songTitle);
                     Platform.runLater(() -> bottomView.setCurrentlyPlayingSong(songTitle));
                }

                Path lyricsFilePath = Paths.get("lyrics", songTitle + ".txt");
                String lyrics = "";
                try {
                    if (Files.exists(lyricsFilePath) && Files.isReadable(lyricsFilePath)) {
                        lyrics = Files.readString(lyricsFilePath);
                    } else {
                        lyrics = "Lyrics file not found: " + lyricsFilePath.toString();
                        System.out.println(lyrics);
                    }
                } catch (Exception ex) {
                    lyrics = "Error loading lyrics for: " + songTitle + " (" + ex.getMessage() + ")";
                    System.err.println(lyrics);
                }
                rightView.setLyrics(lyrics);

            } else {
                System.out.println("DEBUG: MediaPlayer could not be initialized for: " + songTitle + ". Check mediaPathURI and file accessibility.");
                Platform.runLater(() -> {
                    leftView.showPlaceholder();
                    rightView.setLyrics("Could not load song: " + songTitle);
                    bottomView.setCurrentlyPlayingSong(null);
                });
            }

        } catch (Exception e) {
            System.err.println("Unexpected error in playSelectedSong: " + e.getMessage());
            e.printStackTrace();
            Platform.runLater(() -> {
                leftView.showPlaceholder();
                rightView.setLyrics("An unexpected error occurred: " + e.getMessage());
                bottomView.setCurrentlyPlayingSong(null);
            });
        }
    }
}
    */