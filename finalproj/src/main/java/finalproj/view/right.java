package finalproj.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class right {

    private VBox rightPane;
    private TextArea lyricsArea;

    public right() {
        initialize();
    }

    private void initialize() {
        rightPane = new VBox();
        rightPane.setPadding(new Insets(10));
        rightPane.setStyle("-fx-background-color: #93B1A7;"); //edit niyo color

        lyricsArea = new TextArea();
        lyricsArea.setWrapText(true);
        lyricsArea.setEditable(false);
        lyricsArea.setPrefWidth(300);
        lyricsArea.setPrefHeight(580);

        ScrollPane scrollPane = new ScrollPane(lyricsArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(580);

        rightPane.getChildren().add(scrollPane);
    }

    public Node getView() {
        return rightPane;
    }

    public void setLyrics(String lyrics) {
        lyricsArea.setText(lyrics);
    }
}
