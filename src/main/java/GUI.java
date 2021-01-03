import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

public class GUI extends Application {

    private Integer locker = 0;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    private int interfaceScale = 16;

    private Button[][] buttons;
    private Pane layout;

    private int boardSize;
    private int numberOfPlayers;
    private Player fakePlayers[];

    private Board board;
    private boolean[][] grid;
    private Field[][] fields;

    private ArrayList<FieldCode> moveQueue = new ArrayList<>();
//    private int fieldFromX, fieldFromY;
//    private int fieldToX, fieldToY;

    private void socketSetup(){
        try {
            socket = new Socket("127.0.0.1", 6666);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void boardSetup(){
        fakePlayers = new Player[numberOfPlayers];
        board = new Board(boardSize, fakePlayers);
        grid = board.getGrid();
        fields= board.getFields();
    }


    @Override
    public void start(Stage primaryStage) throws IOException {

        socketSetup();
        boardSetup();
        primaryStage.setTitle("Chinese Chess");
        layout = new Pane();
        generateButtons(board);
        primaryStage.setScene(new Scene(layout, board.getX() * interfaceScale * 2, board.getY() * interfaceScale * 2));
        primaryStage.show();

        String response;

        try {
            while (in.hasNextLine()) {
                synchronized (locker) {
                    response = in.nextLine();
                    if (response.startsWith("MESSAGE")) {
                        System.out.println(response.substring(8));
                    } else if (response.startsWith("MOVE")) {       //Later move in  GUI

                        int temp1 = response.indexOf(" ", 1) + 1;
                        int temp2 = response.indexOf(" ", temp1);

                        String fieldFrom = response.substring(temp1, temp2);      // second word
                        String fieldTo = response.substring(temp2 + 1);       // third word

                        System.out.println("Move from " + fieldFrom + " to " + fieldTo);

                        makeMove(null, null);
                    } else if (response.startsWith("GAME_OVER")) {
                        System.out.println("Game over");
                        break;
                    }
                }
            }
            out.println("QUIT");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            socket.close();
        }



//        while(true) {
//            if(moveQueue.size() == 2) {
//                move();
//                primaryStage.setScene(new Scene(layout, board.getX() * interfaceScale * 2, board.getY() * interfaceScale * 2));
//            }
//            primaryStage.show();
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }


    }

//    private void move() {
//        FieldCode fieldFrom = moveQueue.get(0);
//        FieldCode fieldTo = moveQueue.get(1);
//        if(Prophet_2.move(fieldFrom, fieldTo, player, board)){
//            buttons[fieldFromX][fieldFromY].setStyle("-fx-background-color: brown;");
//            fields[fieldFromX][fieldFromY].setOccupant(null);
//
//            buttons[fieldToX][fieldToY].setStyle("-fx-background-color: color;"); //!!!!
//            fields[fieldToX][fieldToY].setOccupant(null); //!!!!!
//        }
//        else {
//            System.out.println("Illegal move");
//        }
//        moveQueue.clear();
//        //sendUpdatedFieldsAndButtons();
//    }

    private void sendMove(){
        out.println("MOVE " + moveQueue.get(0).getKey() + moveQueue.get(0).getValue() + " " + moveQueue.get(1).getKey() + moveQueue.get(1).getValue());
        moveQueue.clear();
    }

    private void makeMove(FieldCode fieldFrom, FieldCode fieldTo) {
        String color = new String();
        for (int y = 0; y < board.getY(); y++) {
            for (int x = 0; x < board.getX(); x++) {
                if (!isNull(fields[x][y])) {
                    if (fields[x][y].getColor() == fieldFrom.getKey() && fields[x][y].getNumber() == fieldFrom.getValue()) {
                        color = buttons[x][y].getStyle();
                        buttons[x][y].setStyle("-fx-background-color: brown;");
                        break;
                    }
                }
            }
        }
        for (int y = 0; y < board.getY(); y++) {
            for (int x = 0; x < board.getX(); x++) {
                if (!isNull(fields[x][y])) {
                    if (fields[x][y].getColor() == fieldTo.getKey() && fields[x][y].getNumber() == fieldTo.getValue()) {
                        buttons[x][y].setStyle(color);
                        break;
                    }
                }
            }
        }
    }

    public void generateButtons(Board board) {
        buttons = new Button[board.getX()][board.getY()];
        for (int y = 0; y < board.getY(); y++) {
            for (int x = 0; x < board.getX(); x++) {
                if(y % 2 == 0 && grid[x][y]){
                    buttons[x][y] = new Button();
                    buttons[x][y].setLayoutX(x * interfaceScale);
                    buttons[x][y].setLayoutY(y * interfaceScale);
                }
                else if(y % 2 == 1 && grid[x][y]) {
                    buttons[x][y] = new Button();
                    buttons[x][y].setLayoutX(x * interfaceScale + (interfaceScale/2));
                    buttons[x][y].setLayoutY(y * interfaceScale);
                }
                else {
                    continue;
                }
                buttons[x][y].setShape(new Circle(interfaceScale/2));
                buttons[x][y].setMinSize(interfaceScale/2,interfaceScale/2);
                buttons[x][y].setMaxSize(interfaceScale/2,interfaceScale/2);
                switch(fields[x][y].getColor()) {
                    case 'R':
                        buttons[x][y].setStyle("-fx-background-color: red;");
                        break;
                    case 'B':
                        buttons[x][y].setStyle("-fx-background-color: blue;");
                        break;
                    case 'V':
                        buttons[x][y].setStyle("-fx-background-color: violet;");
                        break;
                    case 'G':
                        buttons[x][y].setStyle("-fx-background-color: green;");
                        break;
                    case 'W':
                        buttons[x][y].setStyle("-fx-background-color: white;");
                        break;
                    case 'Y':
                        buttons[x][y].setStyle("-fx-background-color: yellow;");
                        break;
                    case 'H':
                        buttons[x][y].setStyle("-fx-background-color: brown;");
                        break;
                }
                int finalX = x;
                int finalY = y;
                buttons[x][y].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(moveQueue.size() == 0) {
                            moveQueue.add(new FieldCode(fields[finalX][finalY].getColor(), fields[finalX][finalY].getNumber()));
//                            fieldFromX = finalX;
//                            fieldFromY = finalY;
                        }
                        else if(moveQueue.get(moveQueue.size() - 1).getKey() == fields[finalX][finalY].getColor() && moveQueue.get(moveQueue.size() - 1).getValue() == fields[finalX][finalY].getNumber()) {}
                        else if(moveQueue.size() == 1) {
                            moveQueue.add(new FieldCode(fields[finalX][finalY].getColor(), fields[finalX][finalY].getNumber()));
//                            fieldToX = finalX;
//                            fieldToY = finalY;
                            sendMove();
                        }
                    }
                });
                layout.getChildren().add(buttons[x][y]);
            }
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
