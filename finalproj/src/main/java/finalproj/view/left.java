package finalproj.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class left {

    private VBox leftPane;
    private ListView<String> songListView;

    public left() {
        initialize();
    }

    private void initialize() {
        leftPane = new VBox();
        leftPane.setPadding(new Insets(10));
        leftPane.setSpacing(10);
        leftPane.setStyle("-fx-background-color: #ecf0f1;");  // edit niyo yung color

        Label header = new Label("Songs");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        songListView = new ListView<>();
        songListView.setPrefWidth(200);
        songListView.setPrefHeight(400);

        leftPane.getChildren().addAll(header, songListView);
    }

    public Node getView() {
        return leftPane;
    }

    public ListView<String> getSongListView() {
        return songListView;
    }
}
