import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static java.util.Objects.isNull;

public class GUI extends Application {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    private int windowSize = 1000;
    private double spaceLength;
    private double buttonSize;

    private Button[][] buttons;
    private Pane layout;
    private Label label;

    private int boardSize;
    private int numberOfPlayers;
    private Player fakePlayers[];

    private Board board;
    private boolean[][] grid;
    private Field[][] fields;

    private ArrayList<FieldCode> moveQueue = new ArrayList<>();

    private void socketSetup(){
        try {
            Parameters parameters = getParameters();
            socket = new Socket(parameters.getRaw().get(0), 6666);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void boardSetup(){
        String response;
        response = in.nextLine();
        if (response.startsWith("SETUP")) {
            int temp1 = response.indexOf(" ", 1) + 1;
            int temp2 = response.indexOf(" ", temp1);


            String boardSizeS = response.substring(temp1, temp2);      // second word
            String numberOfPlayersS = response.substring(temp2 + 1);       // third word

            try {
                this.boardSize = Integer.parseInt(boardSizeS);
                this.numberOfPlayers = Integer.parseInt(numberOfPlayersS);
            } catch (NumberFormatException e) {
                System.out.println("Wrong SETUP");
            }
        }
        else{
            System.out.println("Critical error");
        }

        fakePlayers = new Player[numberOfPlayers];
        board = new Board(boardSize, fakePlayers);
        grid = board.getGrid();
        fields= board.getFields();
    }


    @Override
    public void start(Stage primaryStage) throws IOException {

        socketSetup();
        boardSetup();
        spaceLength = ((windowSize)/ (2 + 2 * Math.sqrt(3) * boardSize)) / 2;
        buttonSize = (windowSize)/ (2 + 2 * Math.sqrt(3) * boardSize);

//        spaceLength *= (1 + 3 * boardSize) / (1 + 2 * boardSize + 2 * Math.sqrt(3) * boardSize);
//        buttonSize *= (1 + 3 * boardSize) / (1 + 2 * boardSize + 2 * Math.sqrt(3) * boardSize);


        int windowSizeX = windowSize;
        int windowSizeY = windowSize;
        primaryStage.setTitle("Chinese Chess");

        layout = new Pane();

        label = new Label("HEJO!");
        label.setStyle("-fx-background-color: white;");
        label.setMaxWidth(windowSize);
        label.setMinWidth(windowSize);
        label.setLayoutX(0);
        label.setLayoutY(0);

        layout.getChildren().add(label);

        BackgroundImage backgroundImage = new BackgroundImage(new Image("background.jpeg", windowSizeX, windowSizeY, false, false), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        layout.setBackground(new Background(backgroundImage));

        generateButtons(board);

//        Button button1 = new Button();
//        button1.setLayoutX(0);
//        button1.setLayoutY(0);
//        button1.setShape(new Circle(100));
//        button1.setMinSize(100, 100);
//        button1.setMaxSize(100, 100);
//        Button button2 = new Button();
//        button2.setLayoutX(50);
//        button2.setLayoutY(50);
//        button2.setShape(new Circle(100));
//        button2.setMinSize(100, 100);
//        button2.setMaxSize(100, 100);
//        layout.getChildren().add(button1);
//        layout.getChildren().add(button2);
        primaryStage.setScene(new Scene(layout, windowSizeX, windowSizeY));



        primaryStage.show();


        Thread thread = new Thread(() -> {
            String response;
            try {
            while (in.hasNextLine()) {
                    response = in.nextLine();
                    if (response.startsWith("MESSAGE")) {
                        String finalResponse = response;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                label.setText(finalResponse.substring(8));
                            }
                        });
                        Thread.sleep(1000);
                    } else if (response.startsWith("MOVE")) {       //Later move in  GUI

                        int temp1 = response.indexOf(" ", 1) + 1;
                        int temp2 = response.indexOf(" ", temp1);


                        String fieldFromS = response.substring(temp1, temp2);      // second word
                        String fieldToS = response.substring(temp2 + 1);       // third word

                        try {
                            FieldCode fieldFrom = new FieldCode(fieldFromS.charAt(0), Integer.parseInt(fieldFromS.substring(1)));
                            FieldCode fieldTo = new FieldCode(fieldToS.charAt(0), Integer.parseInt(fieldToS.substring(1)));
                            makeMove(fieldFrom, fieldTo);
                        } catch (NumberFormatException e) {
                            System.out.println("Wrong field codes: " + fieldFromS + " " + fieldToS);
                        }

                    } else if (response.startsWith("GAME_OVER")) {
                        break;
                }
            }
            out.println("QUIT");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Platform.exit(); //Close application
            }
        });
        thread.start();
    }

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
                if(grid[x][y]){
                    buttons[x][y] = new Button();
                    buttons[x][y].setLayoutX(x * spaceLength + (windowSize - (3 * boardSize + 1) * buttonSize) / 2);
                    buttons[x][y].setLayoutY(Math.sqrt(3) * y  * spaceLength + buttonSize / 2);
                }
                else {
                    continue;
                }
                buttons[x][y].setShape(new Circle(buttonSize));
                buttons[x][y].setMinSize(buttonSize, buttonSize);
                buttons[x][y].setMaxSize(buttonSize, buttonSize);
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
                        }
                        else if(moveQueue.get(moveQueue.size() - 1).getKey() == fields[finalX][finalY].getColor() && moveQueue.get(moveQueue.size() - 1).getValue() == fields[finalX][finalY].getNumber()) {}
                        else if(moveQueue.size() == 1) {
                            moveQueue.add(new FieldCode(fields[finalX][finalY].getColor(), fields[finalX][finalY].getNumber()));
                            sendMove();
                        }
                    }
                });
                layout.getChildren().add(buttons[x][y]);
            }
        }
    }

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Input: hostname");
            return;
        }
        Application.launch(args);
    }
}
