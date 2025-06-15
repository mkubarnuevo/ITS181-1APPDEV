package finalproj.controller;

import java.io.File;

import finalproj.model.song;
import finalproj.model.songservice;
import finalproj.view.bottom;
import finalproj.view.left;
import finalproj.view.right;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class playbackcontroller implements bottom.SongSelectionListener {

    private bottom bottomView;
    private left leftView;
    private right rightView;
    private songservice songService;
    private MediaPlayer mediaPlayer;
    private song currentSong;
    private boolean userInitiatedPause = false;

    public playbackcontroller(bottom bottomView, left leftView, right rightView) {
        this.bottomView = bottomView;
        this.leftView = leftView;
        this.rightView = rightView;
        this.songService = new songservice();
        this.bottomView.setSongSelectionListener(this);
    }

    // --- Keybind Registration Method ---
    public void registerKeybinds(Scene scene) {
        if (scene == null) {
            System.err.println("ERROR: Cannot register keybinds. Scene is null.");
            return;
        }
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        System.out.println("DEBUG: Keybind listener registered on scene.");
    }

    // --- Key Press Handler ---
    private void handleKeyPress(KeyEvent event) {
        System.out.println("DEBUG: Key Pressed: " + event.getCode() + ", Control Down: " + event.isControlDown());

        // --- MODIFIED: Allow shuffle keybind even if no song is loaded ---
        if (event.getCode() == KeyCode.S && event.isControlDown()) {
            onShuffle();
            event.consume();
            return; // Consume and return for shuffle, as it doesn't depend on current media
        }

        // For other playback controls (Play, Pause, Rewind, Forward), a song must be loaded
        if (currentSong == null || mediaPlayer == null) {
            System.out.println("DEBUG: Key press ignored, no song loaded for playback control.");
            return;
        }

        switch (event.getCode()) {
            case SPACE:
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    onPause();
                } else { // PAUSED, STOPPED, READY
                    onPlay();
                }
                event.consume();
                break;
            case LEFT: // Left arrow for rewind
                onRewind();
                event.consume();
                break;
            case RIGHT: // Right arrow for fast-forward
                onForward();
                event.consume();
                break;
            // Ctrl+S is handled at the top
            default:
                break;
        }
    }

    @Override
    public void onSongSelected(String songTitle) {
        System.out.println("DEBUG: PlaybackController received onSongSelected: " + songTitle);

        // Stop and dispose of any existing media player
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null; // Ensure mediaPlayer is nullified after disposal
            System.out.println("DEBUG: Old MediaPlayer stopped and disposed.");
        }

        if (songTitle == null || songTitle.isEmpty() || "No song selected".equals(songTitle)) {
            currentSong = null;
            leftView.setMediaPlayer(null);
            bottomView.clearCurrentSongDisplay();
            bottomView.updateProgressBar(0);
            bottomView.setPlayButtonState(false);
            if (rightView != null) {
                rightView.setLyrics("Lyrics will appear here when a song is selected.");
            }
            System.out.println("DEBUG: No song selected, clearing playback elements and lyrics.");
            return;
        }

        songService.getSongByTitle(songTitle).ifPresentOrElse(
            song -> {
                currentSong = song;
                System.out.println("DEBUG: Found song in DB: " + song.getTitle());
                
                String videoPath = song.getVideoPath();
                if (videoPath != null && !videoPath.isEmpty()) {
                    File videoFile = new File(videoPath);
                    if (videoFile.exists()) {
                        try {
                            Media media = new Media(videoFile.toURI().toURL().toExternalForm());
                            mediaPlayer = new MediaPlayer(media);
                            leftView.setMediaPlayer(mediaPlayer);

                            bottomView.updateProgressBar(0); // Reset progress bar

                            mediaPlayer.setOnReady(() -> {
                                System.out.println("DEBUG: MediaPlayer is READY for " + song.getTitle());
                                mediaPlayer.play();
                                bottomView.setPlayButtonState(true);
                                userInitiatedPause = false;
                                System.out.println("DEBUG: Automatically playing after selection (onReady).");
                            });

                            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                                Platform.runLater(() -> {
                                    // --- FIX: Add null check for mediaPlayer before accessing getTotalDuration() ---
                                    if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null && 
                                        !mediaPlayer.getTotalDuration().isUnknown() && mediaPlayer.getTotalDuration() != Duration.ZERO) {
                                        bottomView.updateProgressBar(newTime.toMillis() / mediaPlayer.getTotalDuration().toMillis());
                                    }
                                });
                            });

                            mediaPlayer.setOnEndOfMedia(() -> {
                                System.out.println("DEBUG: End of media reached for " + song.getTitle());
                                Platform.runLater(() -> onStop());
                            });

                            mediaPlayer.setOnError(() -> {
                                System.err.println("MediaPlayer Error for " + song.getTitle() + ": " + mediaPlayer.getError());
                                Platform.runLater(() -> {
                                    bottomView.clearCurrentSongDisplay();
                                    leftView.showPlaceholder(); 
                                    if (rightView != null) {
                                        rightView.setLyrics("An error occurred while playing this song. Please check the video file.");
                                    }
                                });
                            });

                            System.out.println("DEBUG: MediaPlayer created and set for: " + song.getTitle());

                            if (rightView != null) {
                                rightView.onSongSelected(songTitle);
                            }

                        } catch (Exception e) {
                            System.err.println("ERROR: Could not create Media for " + videoPath + " (" + song.getTitle() + "): " + e.getMessage());
                            e.printStackTrace();
                            bottomView.clearCurrentSongDisplay();
                            leftView.showPlaceholder();
                            if (rightView != null) {
                                rightView.setLyrics("Error loading media file for this song.");
                            }
                        }
                    } else {
                        System.err.println("ERROR: Video file not found at path: " + videoPath + " for song: " + song.getTitle());
                        bottomView.clearCurrentSongDisplay();
                        leftView.showPlaceholder();
                        if (rightView != null) {
                            rightView.setLyrics("Video file not found for this song.");
                        }
                    }
                } else {
                    System.err.println("ERROR: No video path defined for song: " + song.getTitle());
                    bottomView.clearCurrentSongDisplay();
                    leftView.showPlaceholder();
                    if (rightView != null) {
                        rightView.setLyrics("No video content for this song.");
                    }
                }
            },
            () -> {
                System.err.println("ERROR: Song not found in database for title: " + songTitle);
                bottomView.clearCurrentSongDisplay();
                leftView.showPlaceholder();
                if (rightView != null) {
                    rightView.setLyrics("Song details not found for " + songTitle + ".");
                }
            }
        );
    }

    @Override
    public void onPlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED || 
                mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED ||
                mediaPlayer.getStatus() == MediaPlayer.Status.READY) {
                mediaPlayer.play();
                bottomView.setPlayButtonState(true);
                userInitiatedPause = false;
                System.out.println("DEBUG: Playback started/resumed.");
            } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                System.out.println("DEBUG: Already playing.");
            }
        } else if (currentSong != null) {
            // If currentSong is set but mediaPlayer is null (e.g., after initial load or stop)
            System.out.println("DEBUG: MediaPlayer is null but song selected. Re-initializing playback via onSongSelected.");
            // This will re-select the current song, which auto-plays.
            // This is safe because currentSong should hold the last selected song.
            onSongSelected(currentSong.getTitle()); 
        } else {
            System.out.println("DEBUG: No media to play.");
        }
    }

    @Override
    public void onPause() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            bottomView.setPlayButtonState(false);
            userInitiatedPause = true;
            System.out.println("DEBUG: Playback paused.");
        } else {
            System.out.println("DEBUG: No media playing to pause.");
        }
    }

    @Override
    public void onRewind() {
        if (mediaPlayer != null) {
            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration newTime = currentTime.subtract(Duration.seconds(10));
            if (newTime.lessThan(Duration.ZERO)) {
                newTime = Duration.ZERO;
            }
            mediaPlayer.seek(newTime);
            System.out.println("DEBUG: Rewinding 10 seconds. New time: " + newTime);
        } else {
            System.out.println("DEBUG: No media to rewind.");
        }
    }

    @Override
    public void onForward() { 
        if (mediaPlayer != null) {
            Duration currentTime = mediaPlayer.getCurrentTime();
            Duration totalDuration = mediaPlayer.getTotalDuration();
            
            if (totalDuration == Duration.UNKNOWN || totalDuration == Duration.INDEFINITE) {
                System.out.println("DEBUG: Cannot forward: total duration unknown/indefinite.");
                return;
            }

            Duration newTime = currentTime.add(Duration.seconds(10));
            if (newTime.greaterThan(totalDuration)) {
                newTime = totalDuration; 
                mediaPlayer.seek(newTime);
                System.out.println("DEBUG: Forwarded to end of media, stopping.");
                onStop(); 
            } else {
                mediaPlayer.seek(newTime);
                System.out.println("DEBUG: Forwarding 10 seconds. New time: " + newTime);
            }
        } else {
            System.out.println("DEBUG: No media to forward.");
        }
    }

    @Override
    public void onStop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            System.out.println("DEBUG: Playback stopped and MediaPlayer disposed.");
        }
        bottomView.setPlayButtonState(false);
        bottomView.updateProgressBar(0);
        leftView.showPlaceholder();
        userInitiatedPause = false; 
        bottomView.clearCurrentSongDisplay();
        
        if (rightView != null) {
            rightView.setLyrics("Lyrics will appear here when a song is selected.");
            System.out.println("DEBUG: Lyrics cleared by onStop.");
        } 
    }

    @Override
    public void onShuffle() {
        bottomView.getShuffleButton().fire(); 
        System.out.println("DEBUG: Shuffle event handled by playbackcontroller, delegating to bottomView's shuffle logic.");
    }
}