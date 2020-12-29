import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Chinese Chess");

        Button button = new Button();
        button.setText("Button");

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

//        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setScene(new Scene(layout, 300, 275));
        primaryStage.show();
    }
}
