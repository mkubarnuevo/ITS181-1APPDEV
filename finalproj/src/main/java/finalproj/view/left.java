package finalproj.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;

public class left {

    private BorderPane leftPane;
    private MediaView mediaView;

    public left() {
        initialize();
    }

    private void initialize() {
        leftPane = new BorderPane();
        leftPane.setPadding(new Insets(10));
        leftPane.setStyle("-fx-background-color: #ecf0f1;"); // light background color

        // MediaView for video playback in center
        mediaView = new MediaView();
        mediaView.setPreserveRatio(true);

        // Placeholder overlay when no video loaded
        Label placeholder = new Label("Video will be displayed here");
        placeholder.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");

        StackPane centerStack = new StackPane();
        centerStack.getChildren().addAll(mediaView, placeholder);

        leftPane.setCenter(centerStack);
    }

    public Node getView() {
        return leftPane;
    }

    public MediaView getMediaView() {
        return mediaView;
    }
}
