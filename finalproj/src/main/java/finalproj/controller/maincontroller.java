package finalproj.controller;

import finalproj.view.right;
import finalproj.view.top;

public class maincontroller {

    private top topBar;
    private right rightPanel;
    private boolean lyricsVisible = true;

    public maincontroller(top topBar, right rightPanel) {
        this.topBar = topBar;
        this.rightPanel = rightPanel;

        setupButtonActions();
    }

    private void setupButtonActions() {
        topBar.getLyricsButton().setOnAction(e -> {
            rightPanel.showLyrics();
            lyricsVisible = true;
        });

        topBar.getUploadButton().setOnAction(e -> {
            rightPanel.showUpload();
            lyricsVisible = false;
        });

        topBar.getRemoveButton().setOnAction(e -> {
            rightPanel.showRemove();
            lyricsVisible = false;
        });
    }
}
