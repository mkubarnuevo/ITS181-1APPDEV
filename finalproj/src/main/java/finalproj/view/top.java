package finalproj.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class top {

    private HBox topPane;
    private Button uploadButton;
    private Button removeButton;

    public top() {
        initialize();
    }

    private void initialize() {
        topPane = new HBox();
        topPane.setPadding(new Insets(15));
        topPane.setSpacing(10);
        topPane.setStyle(
            "-fx-background-color: #7A918D;" +
            "-fx-border-color: #99C2A2;" +
            "-fx-border-width: 0 0 5 0;" +
            "-fx-border-style: solid;"
        );

        // Title Label
        Label titleLabel = new Label("Group () Music Player");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;");

        // Spacer to push buttons to the right
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Upload & Remove Buttons
        uploadButton = new Button("UPLOAD SONG");
        removeButton = new Button("REMOVE SONG");

        styleButton(uploadButton);
        styleButton(removeButton);

        HBox buttonBox = new HBox(10, uploadButton, removeButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        topPane.getChildren().addAll(titleLabel, spacer, buttonBox);
    }

    private void styleButton(Button button) {
        button.setStyle(
            "-fx-background-color: #DBFEB8;" +
            "-fx-text-fill: #99C2A2;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;"
        );
    }

    public Node getView() {
        return topPane;
    }

    public Button getUploadButton() {
        return uploadButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }
}
