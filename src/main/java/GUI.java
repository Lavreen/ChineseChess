import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.BatchUpdateException;
import java.util.Scanner;

public class GUI extends Application {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    private void socketSetup(){
        try {
            socket = new Socket("127.0.0.1", 6666);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void start(Stage primaryStage) throws IOException {

        socketSetup();
//        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Chinese Chess");

        Button button = new Button();
        //button.setText("Button");
        button.setShape(new Circle(100));
        button.setMinSize(100,100);
        button.setMaxSize(100,100);
        button.setStyle("-fx-background-color: green;");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Hej moje imie to ");
            }
        });

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

//        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setScene(new Scene(layout, 1000, 1000));
        primaryStage.show();
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}
