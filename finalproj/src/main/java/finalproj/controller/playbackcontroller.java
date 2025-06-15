package finalproj.controller;

import finalproj.view.bottom;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

public class playbackcontroller {

    private bottom bottomView;

    public playbackcontroller(bottom bottomView) {
        this.bottomView = bottomView;
        setupKeybinds();
    }

    private void setupKeybinds() {
        // get buttons from the bottom view
        Button playButton = bottomView.getPlayButton();
        Button rewindButton = bottomView.getRewindButton();
        Button forwardButton = bottomView.getForwardButton();
        Button shuffleButton = bottomView.getShuffleButton();

        // registering the key event handler with bottom view
        bottomView.registerKeybinds(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                // spacebar for play/pause
                playButton.fire();
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT) {
                // left arrow for rewind
                rewindButton.fire();
                event.consume();
            } else if (event.getCode() == KeyCode.RIGHT) {
                // right arrow for fast forward
                forwardButton.fire();
                event.consume();
            } else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                // crtl + s for shuffle
                shuffleButton.fire();
                event.consume();
            }
        });
    }
}