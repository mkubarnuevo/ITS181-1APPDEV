package finalproj.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class top {

    private HBox topPane;

    public top() {
        initialize();
    }

    private void initialize() {
        topPane = new HBox();
        topPane.setPadding(new Insets(15));
        topPane.setStyle("-fx-background-color: #34495e;"); // edit niyo color
        topPane.setSpacing(10);

        Label titleLabel = new Label("Group () Music Player");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;");

        topPane.getChildren().add(titleLabel);
    }

    public Node getView() {
        return topPane;
    }
}
