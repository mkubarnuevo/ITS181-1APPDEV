package finalproj.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import finalproj.model.song;
import finalproj.model.songservice;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class right implements bottom.SongSelectionListener { // Kept 'implements' as it's needed for lyrics display

    private StackPane rightPane;

    private VBox lyricsView;
    private VBox uploadView;
    private VBox removeView;
    private TextArea lyricsArea;

    private TextField uploadVideoPathField;
    private TextField uploadLyricsPathField;
    private Label uploadStatusLabel;

    private bottom bottomPanel;

    public right(bottom bottomPanel) {
        this.bottomPanel = bottomPanel;
        this.bottomPanel.setSongSelectionListener(this); // Registering as listener for song selection
        initialize();
    }

    private void initialize() {
        rightPane = new StackPane();
        rightPane.setPadding(new Insets(10));
        rightPane.setStyle("-fx-background-color: #93B1A7;");

        // --- 1. Lyrics View Setup ---
        lyricsArea = new TextArea();
        lyricsArea.setWrapText(true);
        lyricsArea.setEditable(false);
        lyricsArea.setPromptText(
            "Lyrics will appear here when a song is selected."
        );
        lyricsArea.setPrefWidth(300);
        lyricsArea.setPrefHeight(580);
        lyricsArea.setStyle(
            "-fx-control-inner-background: #E8F4E8; -fx-text-fill: #3A3A3A;" +
            " -fx-font-size: 14px;"
        );

        ScrollPane scrollPane = new ScrollPane(lyricsArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(580);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        lyricsView = new VBox(scrollPane);
        lyricsView.setAlignment(Pos.CENTER);

        // --- 2. Upload Song Panel Setup (for the button that opens the popup) ---
        Button openUploadWindow = new Button("UPLOAD MEDIA");
        openUploadWindow.setPrefSize(170, 35);
        openUploadWindow.setStyle(
            "-fx-background-color: #DBFEB8; -fx-font-weight: bold;" +
            " -fx-text-fill: #99C2A2; -fx-font-size: 16px;"
        );
        openUploadWindow.setOnAction(e -> openUploadPopup());
        uploadView = new VBox(10, openUploadWindow);
        uploadView.setPadding(new Insets(10));
        uploadView.setAlignment(Pos.CENTER);

        // --- 3. Remove Song Panel Setup (Placeholder) ---
        Label removeLabel = new Label("Remove song view (placeholder)");
        removeView = new VBox(removeLabel);
        removeView.setPadding(new Insets(10));
        removeView.setAlignment(Pos.CENTER);

        rightPane.getChildren().addAll(lyricsView, uploadView, removeView);
        showLyrics();
    }

    public Node getView() {
        return rightPane;
    }

    public void setLyrics(String lyrics) {
        lyricsArea.setText(lyrics);
    }

    public void showLyrics() {
        lyricsView.setVisible(true);
        uploadView.setVisible(false);
        removeView.setVisible(false);
    }

    public void showUpload() {
        lyricsView.setVisible(false);
        uploadView.setVisible(true);
        removeView.setVisible(false);
    }

    public void showRemove() {
        lyricsView.setVisible(false);
        uploadView.setVisible(false);
        removeView.setVisible(true);
    }

    private void browseFile(
        TextField targetField, String description, String... extensions
    ) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select " + description);
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter(description, extensions)
        );

        Stage ownerStage = null;
        if (rightPane.getScene() != null &&
            rightPane.getScene().getWindow() instanceof Stage) {
            ownerStage = (Stage) rightPane.getScene().getWindow();
        } else {
             ownerStage = new Stage();
             ownerStage.initModality(Modality.APPLICATION_MODAL);
             ownerStage.initOwner(null);
        }

        File selectedFile = fileChooser.showOpenDialog(ownerStage);
        if (selectedFile != null) {
            targetField.setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Opens a new popup window for the song upload form.
     */
    private void openUploadPopup() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Upload New Song");

        // --- TextFields for File Paths ---
        uploadVideoPathField = new TextField();
        uploadVideoPathField.setPromptText("Video File Path");
        uploadVideoPathField.setEditable(false);
        uploadVideoPathField.setPrefWidth(200); // Set preferred width
        HBox.setHgrow(uploadVideoPathField, Priority.ALWAYS); // Allow growth

        uploadLyricsPathField = new TextField();
        uploadLyricsPathField.setPromptText("Lyrics File Path");
        uploadLyricsPathField.setEditable(false);
        uploadLyricsPathField.setPrefWidth(200); // Set preferred width (same as video path)
        HBox.setHgrow(uploadLyricsPathField, Priority.ALWAYS); // Allow growth


        uploadStatusLabel = new Label();
        uploadStatusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        // --- Buttons for File Selection ---
        Button selectVideoButton = new Button("SELECT VIDEO");
        selectVideoButton.setStyle(
            "-fx-background-color: #DBFEB8; -fx-font-weight: bold;" +
            " -fx-text-fill: #5D5C61; -fx-font-size: 14px;"
        );
        selectVideoButton.setPrefWidth(120); // Set preferred width for consistency
        selectVideoButton.setOnAction(e -> browseFile(
            uploadVideoPathField, "Video Files", "*.mp4", "*.mkv", "*.avi"
        ));

        Button selectLyricsButton = new Button("SELECT LYRICS");
        selectLyricsButton.setStyle(
            "-fx-background-color: #DBFEB8; -fx-font-weight: bold;" +
            " -fx-text-fill: #5D5C61; -fx-font-size: 14px;"
        );
        selectLyricsButton.setPrefWidth(120); // Set preferred width (same as video button)
        selectLyricsButton.setOnAction(e -> browseFile(
            uploadLyricsPathField, "Text Files", "*.txt"
        ));


        // --- HBoxes to group TextField and Button ---
        HBox videoFileChooserBox = new HBox(5, uploadVideoPathField, selectVideoButton);
        videoFileChooserBox.setAlignment(Pos.CENTER_LEFT); // Align content
        HBox lyricsFileChooserBox = new HBox(5, uploadLyricsPathField, selectLyricsButton);
        lyricsFileChooserBox.setAlignment(Pos.CENTER_LEFT); // Align content


        // --- Upload/Clear Buttons ---
        Button confirmUploadButton = new Button("UPLOAD MEDIA");
        confirmUploadButton.setPrefSize(170, 35);
        confirmUploadButton.setStyle(
            "-fx-background-color: #99C2A2; -fx-font-weight: bold;" +
            " -fx-text-fill: white; -fx-font-size: 14px;"
        );
        confirmUploadButton.setOnAction(e -> uploadSongAction(popupStage));

        Button clearFieldsButton = new Button("CLEAR FIELDS");
        clearFieldsButton.setPrefSize(170, 35);
        clearFieldsButton.setStyle(
            "-fx-background-color: #FF7F7F; -fx-font-weight: bold;" +
            " -fx-text-fill: white; -fx-font-size: 14px;"
        );
        clearFieldsButton.setOnAction(e -> clearUploadFields());

        HBox uploadButtonsContainer = new HBox(15, confirmUploadButton, clearFieldsButton);
        uploadButtonsContainer.setAlignment(Pos.CENTER);

        // --- Main Layout for Popup ---
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #7A918D;");

        layout.getChildren().addAll(
            new Label("UPLOAD NEW SONG") {{
                setStyle("-fx-text-fill: white; -fx-font-size: 18px;" +
                         " -fx-font-weight: bold;");
            }},
            videoFileChooserBox,
            lyricsFileChooserBox,
            uploadButtonsContainer,
            uploadStatusLabel
        );

        Scene popupScene = new Scene(layout, 400, 320);
        popupStage.setScene(popupScene);
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    /**
     * Handles the action of uploading a song: validates input, copies files,
     * and saves to DB. Closes the provided stage on success.
     * The song title is derived from the video file name.
     * @param stageToClose The Stage (popup) to close after a successful upload.
     */
    private void uploadSongAction(Stage stageToClose) {
        String videoPath = uploadVideoPathField.getText().trim();
        String lyricsPath = uploadLyricsPathField.getText().trim();

        if (videoPath.isEmpty() || lyricsPath.isEmpty()) {
            uploadStatusLabel.setText("Please select both video and lyrics files.");
            return;
        }

        String videoBaseName = new File(videoPath).getName();
        String lyricsBaseName = new File(lyricsPath).getName();

        String songTitle;
        int lastDotVideo = videoBaseName.lastIndexOf('.');
        if (lastDotVideo > 0 && lastDotVideo < videoBaseName.length() - 1) {
            songTitle = videoBaseName.substring(0, lastDotVideo);
        } else {
            songTitle = videoBaseName;
        }

        String lyricsFileTitle;
        int lastDotLyrics = lyricsBaseName.lastIndexOf('.');
        if (lastDotLyrics > 0 && lastDotLyrics < lyricsBaseName.length() - 1) {
            lyricsFileTitle = lyricsBaseName.substring(0, lastDotLyrics);
        } else {
            lyricsFileTitle = lyricsBaseName;
        }

        if (!songTitle.equalsIgnoreCase(lyricsFileTitle)) {
            uploadStatusLabel.setText(
                "Video and Lyrics filenames must match (e.g., song.mp4 & song.txt)."
            );
            return;
        }

        try {
            Path videoDir = Paths.get("videos");
            Path lyricsDir = Paths.get("lyrics");
            Files.createDirectories(videoDir);
            Files.createDirectories(lyricsDir);

            Path videoDest = videoDir.resolve(videoBaseName);
            Path lyricsDest = lyricsDir.resolve(lyricsBaseName);

            Files.copy(Paths.get(videoPath), videoDest, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(Paths.get(lyricsPath), lyricsDest, StandardCopyOption.REPLACE_EXISTING);

            song newSong = new song(songTitle, videoDest.toString(), lyricsDest.toString());
            songservice songService = new songservice();
            songService.saveSong(newSong);

            if (bottomPanel != null) {
                bottomPanel.refreshSongDropdown();
            }

            uploadStatusLabel.setText("Song '" + songTitle + "' uploaded successfully!");
            clearUploadFields();
            stageToClose.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            uploadStatusLabel.setText("Upload failed: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            uploadStatusLabel.setText("Error saving song to database: " + ex.getMessage());
        }
    }

    /**
     * Clears all input fields in the song upload popup form.
     */
    private void clearUploadFields() {
        if (uploadVideoPathField != null) uploadVideoPathField.clear();
        if (uploadLyricsPathField != null) uploadLyricsPathField.clear();
        if (uploadStatusLabel != null) uploadStatusLabel.setText("");
    }

    /**
     * This method is the callback from the bottom.SongSelectionListener interface.
     * It's called when a song is selected from the dropdown in the bottom panel.
     * @param songTitle The title of the selected song.
     */
    @Override
    public void onSongSelected(String songTitle) {
        System.out.println("DEBUG: Song selected in right panel callback: " + songTitle);

        showLyrics(); // Ensure the lyrics view is visible when a song is selected

        songservice songService = new songservice();
        Optional<song> selectedSongOptional = songService.getSongByTitle(songTitle);

        if (selectedSongOptional.isPresent()) {
            song selectedSong = selectedSongOptional.get();
            String lyricsFilePath = selectedSong.getLyricsPath();

            if (lyricsFilePath != null && !lyricsFilePath.isEmpty()) {
                File lyricsFile = new File(lyricsFilePath);
                if (lyricsFile.exists()) {
                    try {
                        String lyricsContent = Files.readString(lyricsFile.toPath());
                        lyricsArea.setText(lyricsContent);
                        System.out.println(
                            "DEBUG: Lyrics loaded successfully for: " + songTitle
                        );
                    } catch (IOException e) {
                        lyricsArea.setText(
                            "Error loading lyrics from file: " + e.getMessage()
                        );
                        System.err.println(
                            "ERROR: Could not read lyrics file: " + lyricsFilePath +
                            " - " + e.getMessage()
                        );
                        e.printStackTrace();
                    }
                } else {
                    lyricsArea.setText(
                        "Lyrics file not found at: " + lyricsFilePath +
                        "\n(File may have been moved or deleted externally)."
                    );
                    System.err.println(
                        "ERROR: Lyrics file does not exist: " + lyricsFilePath
                    );
                }
            } else {
                lyricsArea.setText("No lyrics file path stored for this song.");
                System.out.println(
                    "DEBUG: No lyrics path found for song: " + songTitle
                );
            }
        } else {
            lyricsArea.setText("Song details not found in the database for: " + songTitle);
            System.err.println("ERROR: Song not found in database for title: " + songTitle);
        }
    }
}