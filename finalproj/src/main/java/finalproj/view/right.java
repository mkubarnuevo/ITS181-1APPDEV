package finalproj.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import finalproj.model.song;
import finalproj.model.songservice;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class right implements bottom.SongSelectionListener {

    private StackPane rightPane;

    private VBox lyricsView;
    private VBox uploadView;
    private VBox removeView;
    private TextArea lyricsArea;

    private TextField uploadVideoPathField;
    private TextField uploadLyricsPathField;
    private Label uploadStatusLabel;

    private VBox songListContainer;

    private bottom bottomPanel;

    public right(bottom bottomPanel) {
        this.bottomPanel = bottomPanel;
        this.bottomPanel.setSongSelectionListener(this);
        initialize();
    }

    private void initialize() {
        rightPane = new StackPane();
        rightPane.setPadding(new Insets(10));
        rightPane.setStyle("-fx-background-color: #93B1A7;");

        // view lyrics
        lyricsArea = new TextArea();
        lyricsArea.setWrapText(true);
        lyricsArea.setEditable(false);
        lyricsArea.setPromptText(
            "Lyrics will appear here when a song is selected."
        );
        lyricsArea.setPrefWidth(300);
        lyricsArea.setPrefHeight(424);

        lyricsArea.setMaxHeight(Double.MAX_VALUE);

        lyricsArea.setStyle(
            "-fx-control-inner-background: #E8F4E8; -fx-text-fill: #3A3A3A;" +
            " -fx-font-size: 14px;"
        );

        ScrollPane lyricsScrollPane = new ScrollPane(lyricsArea);
        lyricsScrollPane.setFitToWidth(true);
        lyricsScrollPane.setPrefHeight(580);

        lyricsScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        lyricsScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);

        VBox.setVgrow(lyricsScrollPane, Priority.ALWAYS);

        lyricsView = new VBox(lyricsScrollPane);
        lyricsView.setAlignment(Pos.CENTER);
        lyricsView.setMaxWidth(Double.MAX_VALUE);
        lyricsView.setMaxHeight(Double.MAX_VALUE);


        // view upload
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

        // view removing song
        songListContainer = new VBox(5);
        songListContainer.setPadding(new Insets(10));
        songListContainer.setAlignment(Pos.TOP_CENTER);

        ScrollPane removeScrollPane = new ScrollPane(songListContainer);
        removeScrollPane.setFitToWidth(true);
        removeScrollPane.setPrefHeight(580);
        removeScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        removeScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        removeScrollPane.setStyle("-fx-background-color: #E8F4E8; -fx-border-color: transparent;");

        VBox.setVgrow(removeScrollPane, Priority.ALWAYS);

        removeView = new VBox(10, new Label("UPLOADED SONGS:") {{
            setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        }}, removeScrollPane);
        removeView.setPadding(new Insets(10));
        removeView.setAlignment(Pos.TOP_CENTER);
        removeView.setMaxWidth(Double.MAX_VALUE);
        removeView.setMaxHeight(Double.MAX_VALUE);

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
        populateRemoveSongList();
    }

    private void populateRemoveSongList() {
        songListContainer.getChildren().clear();

        songservice service = new songservice();
        List<song> allSongs = service.getAllSongs();

        if (allSongs.isEmpty()) {
            Label noSongsLabel = new Label("No songs uploaded yet.");
            noSongsLabel.setStyle("-fx-text-fill: #3A3A3A; -fx-font-size: 14px;");
            songListContainer.getChildren().add(noSongsLabel);
            return;
        }

        for (song s : allSongs) {
            Label songTitleLabel = new Label(s.getTitle());
            songTitleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3A3A3A;");

            Button removeBtn = new Button("Remove");
            removeBtn.setStyle(
                "-fx-background-color: #FF7F7F; -fx-text-fill: white; -fx-font-weight: bold;" +
                " -fx-font-size: 12px; -fx-background-radius: 3;"
            );

            removeBtn.setOnAction(e -> confirmAndRemoveSong(s));

            HBox songEntry = new HBox(10, songTitleLabel);
            HBox.setHgrow(songTitleLabel, Priority.ALWAYS);
            songEntry.getChildren().add(removeBtn);
            songEntry.setAlignment(Pos.CENTER_LEFT);
            songEntry.setPadding(new Insets(5, 0, 5, 0));

            songListContainer.getChildren().add(songEntry);
        }
    }

    @SuppressWarnings("unchecked")
    private void confirmAndRemoveSong(song songToRemove) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete '" + songToRemove.getTitle() + "'?");
        alert.setContentText("This action cannot be undone. All associated files will be removed.");

        alert.getDialogPane().setStyle(
            "-fx-background-color: #7A918D;" +
            "-fx-border-color: #99C2A2;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;"
        );

        Node header = alert.getDialogPane().lookup(".header-panel");
        if (header != null) {
            header.setStyle(
                "-fx-background-color: #93B1A7;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
            );
        }

        Label contentLabel = (Label) alert.getDialogPane().lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;"
            );
        }

        // styling ok and cancel buttons
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);

        // styling ok button
        if (okButton != null) {
            okButton.setStyle(
                "-fx-background-color: #FF7F7F;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 13px;" +
                "-fx-background-radius: 5;"
            );
        }

        // styling cancel button
        if (cancelButton != null) {
            cancelButton.setStyle(
                "-fx-background-color: #DBFEB8;" +
                "-fx-text-fill: #93B1A7;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 13px;" +
                "-fx-background-radius: 5;"
            );
        }
        // --- End Styling ---

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            removeSongAction(songToRemove);
        }
    }

    private void removeSongAction(song songToRemove) {
        songservice service = new songservice();
        try {
            // delete video file
            Path videoPath = Paths.get(songToRemove.getVideoPath());
            if (Files.exists(videoPath)) {
                Files.delete(videoPath);
                System.out.println("Deleted video file: " + videoPath.getFileName());
            } else {
                System.out.println("Video file not found for deletion: " + videoPath.getFileName() + ". Skipping file deletion.");
            }

            // delete lyrics file
            Path lyricsPath = Paths.get(songToRemove.getLyricsPath());
            if (Files.exists(lyricsPath)) {
                Files.delete(lyricsPath);
                System.out.println("Deleted lyrics file: " + lyricsPath.getFileName());
            } else {
                System.out.println("Lyrics file not found for deletion: " + lyricsPath.getFileName() + ". Skipping file deletion.");
            }

            // removing from database
            service.deleteSong(songToRemove.getId());
            System.out.println("Removed song from database: " + songToRemove.getTitle());

            // refreshes UI
            populateRemoveSongList(); // re-populate the remove song list
            if (bottomPanel != null) {
                bottomPanel.refreshSongDropdown(); // refresh the song dropdown
            }
            showLyrics();

        } catch (IOException e) {
            System.err.println("Error deleting files for song " + songToRemove.getTitle() + ": " + e.getMessage());
            e.printStackTrace();
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Deletion Error");
            errorAlert.setHeaderText("Failed to delete song files for '" + songToRemove.getTitle() + "'");
            errorAlert.setContentText("Reason: " + e.getMessage() + "\nDatabase entry might still exist.");
            errorAlert.showAndWait();
        } catch (Exception e) {
            System.err.println("Error removing song " + songToRemove.getTitle() + " from database: " + e.getMessage());
            e.printStackTrace();
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Deletion Error");
            errorAlert.setHeaderText("Failed to remove song '" + songToRemove.getTitle() + "' from database.");
            errorAlert.setContentText("Reason: " + e.getMessage());
            errorAlert.showAndWait();
        }
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

    // this opens up a window panel for uploading media files
    private void openUploadPopup() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Upload New Song");

        // textfields for filepath display
        uploadVideoPathField = new TextField();
        uploadVideoPathField.setPromptText("Video File Path");
        uploadVideoPathField.setEditable(false);
        uploadVideoPathField.setPrefWidth(200);
        HBox.setHgrow(uploadVideoPathField, Priority.ALWAYS);

        uploadLyricsPathField = new TextField();
        uploadLyricsPathField.setPromptText("Lyrics File Path");
        uploadLyricsPathField.setEditable(false);
        uploadLyricsPathField.setPrefWidth(200);
        HBox.setHgrow(uploadLyricsPathField, Priority.ALWAYS);


        uploadStatusLabel = new Label();
        uploadStatusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        Button selectVideoButton = new Button("SELECT VIDEO");
        selectVideoButton.setStyle(
            "-fx-background-color: #DBFEB8; -fx-font-weight: bold;" +
            " -fx-text-fill: #5D5C61; -fx-font-size: 14px;"
        );
        selectVideoButton.setPrefWidth(120);
        selectVideoButton.setOnAction(e -> browseFile(
            uploadVideoPathField, "Video Files", "*.mp4", "*.mkv", "*.avi"
        ));

        Button selectLyricsButton = new Button("SELECT LYRICS");
        selectLyricsButton.setStyle(
            "-fx-background-color: #DBFEB8; -fx-font-weight: bold;" +
            " -fx-text-fill: #5D5C61; -fx-font-size: 14px;"
        );
        selectLyricsButton.setPrefWidth(120);
        selectLyricsButton.setOnAction(e -> browseFile(
            uploadLyricsPathField, "Text Files", "*.txt"
        ));


        // HBoxes for input fields and buttons
        HBox videoFileChooserBox = new HBox(5, uploadVideoPathField, selectVideoButton);
        videoFileChooserBox.setAlignment(Pos.CENTER_LEFT);
        HBox lyricsFileChooserBox = new HBox(5, uploadLyricsPathField, selectLyricsButton);
        lyricsFileChooserBox.setAlignment(Pos.CENTER_LEFT);


        // upload and clear buttons
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

        // layout for popup window
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

    // this handles uploading
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

    // this clears the input fields
    private void clearUploadFields() {
        if (uploadVideoPathField != null) uploadVideoPathField.clear();
        if (uploadLyricsPathField != null) uploadLyricsPathField.clear();
        if (uploadStatusLabel != null) uploadStatusLabel.setText("");
    }

    @Override
    public void onSongSelected(String songTitle) {
        System.out.println("DEBUG: Song selected in right panel callback: " + songTitle);

        showLyrics(); // Always show lyrics when a song is selected

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