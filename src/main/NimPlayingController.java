package main;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.IA.NimComputer;
import main.entities.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by perri on 18/05/2018.
 */
public class NimPlayingController {

    private ArrayList<Node> graphNodes;
    private ArrayList<NimEdge> graphEdges;

    @FXML
    private Pane pane;

    private Ground ground;

    private String gameType;
    private boolean isRegularGame;
    @FXML
    private Text turnDisplay;

    private int turnCounter;

    private Player[] players;

    private Color gameColor;

    private boolean allComputerGame;

    @FXML
    private Slider speedSlider;
    @FXML
    private Label speedLabel;

    private Timeline timeline;
    private double regularTiming = 5;

    @FXML
    public void initialize() {
        this.pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.speedSlider.setMin(0);
        this.speedSlider.setMax(3);
        this.speedSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            timeline.stop();
            timeline = new Timeline(new KeyFrame(Duration.seconds(newValue.doubleValue() * regularTiming), ev -> {
                computerMove();
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        });

        this.pane.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            if(! allComputerGame) {
                if(me.getButton().equals(MouseButton.SECONDARY)) {

                    javafx.scene.Node clickedOn = me.getPickResult().getIntersectedNode();

                    if(clickedOn instanceof NimEdge) {
                        if(players[turnCounter] instanceof NimComputer) {

                        } else if(players[turnCounter] instanceof Player){

                            if(playAMove((NimEdge)clickedOn)) {
                                if(gameType.equals("Regular")) {

                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                                    if(turnCounter%2 ==0) {
                                        if(gameType.equals("Regular")){
                                            alert = new Alert(Alert.AlertType.CONFIRMATION, "Player 2 won ! Replay ?", ButtonType.YES, ButtonType.NO);
                                        } else if(gameType.equals("Misery")) {
                                            alert = new Alert(Alert.AlertType.CONFIRMATION, "Player 1 won ! Replay ?", ButtonType.YES, ButtonType.NO);
                                        }
                                    } else {
                                        if(gameType.equals("Regular")){
                                            alert = new Alert(Alert.AlertType.CONFIRMATION, "Player 1 won ! Replay ?", ButtonType.YES, ButtonType.NO);
                                        } else if(gameType.equals("Misery")) {
                                            alert = new Alert(Alert.AlertType.CONFIRMATION, "Player 2 won ! Replay ?", ButtonType.YES, ButtonType.NO);
                                        }
                                    }

                                    alert.showAndWait();

                                    if (alert.getResult() == ButtonType.YES) {
                                        try{
                                            returnToEditing();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else if(alert.getResult() == ButtonType.NO){
                                        try{
                                            returnToMainMenu();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            if(players[turnCounter] instanceof NimComputer) {
                                computerMove();
                            }
                        }

                    }
                }
            }
        });


    }

    public void retrieveGameData(ArrayList<Node> graphNodes, ArrayList<NimEdge> graphEdges,
                                   Ground ground, String gameType, Color gameColor, String player1, String player2,
                                 String player1Strat, String player2Strat) {

        this.graphNodes = graphNodes;
        this.graphEdges = graphEdges;
        this.ground = ground;
        this.gameType = gameType;
        if(this.gameType.equals("Regular")) {
            this.isRegularGame = true;
        } else {
            this.isRegularGame = false;
        }
        this.players = new Player[2];
        if(player1.equals("Computer") && player2.equals("Computer")) {
            this.allComputerGame = true;
        } else {
            this.allComputerGame = false;
        }
        if(player1.equals("Human")){
            this.players[0] = new Player(1, gameColor);
        } else {
            this.players[0] = new NimComputer(1, gameColor, player1Strat);
        }
        if(player2.equals("Human")) {
            this.players[1] = new Player(2, gameColor);

        } else {
            this.players[1] = new NimComputer(2, gameColor, player2Strat);
        }

        this.turnCounter = 0;

        this.turnDisplay.setText("Player " + (turnCounter+1) + "'s turn !");
        String playersColor = String.format("#%02x%02x%02x",
                (int) (255 * this.players[turnCounter].getColor().getRed()),
                (int) (255 * this.players[turnCounter].getColor().getGreen()),
                (int) (255 * this.players[turnCounter].getColor().getBlue()));
        this.turnDisplay.setStyle("-fx-fill:"+playersColor+";");

        this.pane.getChildren().add(ground);

        for(Node n : this.graphNodes) {
            n.swapToGenericPlayMode();
            this.pane.getChildren().add(n);

        }
        for(NimEdge e : this.graphEdges) {
            this.pane.getChildren().add(e);
            //e.swapToPlayMode();
        }

        this.speedSlider.setTooltip(
                new Tooltip("0 : Pauses the game \n" +
                        "~1 : Normal speed \n" +
                        "3 : 3 times slower than normal speed \n")
        );
        //Launch the game if both players are computers :
        if(allComputerGame) {
            computersPlaying();

        } else if(player1.equals("Computer")) {

            computerMove();
            this.speedSlider.setDisable(true);
            this.speedSlider.setVisible(false);
            this.speedLabel.setVisible(false);
        } else if(!allComputerGame) {
            this.speedSlider.setDisable(true);
            this.speedSlider.setVisible(false);
            this.speedLabel.setVisible(false);
        }

    }


    public int[] getHeap() {
        int[] heap = new int[this.ground.getNodesOnTheGround().size()];
        int counter = 0;
        for(Node n : this.ground.getNodesOnTheGround()){
            ArrayList<Node> alreadyVisited = new ArrayList<>();
            //Counts the number of EDGES in each column / Remove -1 for Nodes
            heap[counter] = countNodesAbove(n, alreadyVisited, 0) - 1;
            counter++;
        }
        return heap;
    }

    private int countNodesAbove(Node considered, ArrayList<Node> alreadyVisited, int count) {

        count++;
        alreadyVisited.add(considered);

        for(NimEdge e : this.graphEdges) {
            if(e.getFirstNode() == considered && !alreadyVisited.contains(e.getSecondNode())) {
                count = countNodesAbove(e.getSecondNode(), alreadyVisited, count);
            }
            if(e.getSecondNode() == considered && !alreadyVisited.contains(e.getFirstNode())) {
                count = countNodesAbove(e.getFirstNode(), alreadyVisited, count);
            }
        }
        return count;
    }

    public boolean playAMove(NimEdge toRemove) {

        if(toRemove == null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            if(turnCounter%2 ==0) {
                if(gameType.equals("Regular")){
                    alert = new Alert(Alert.AlertType.CONFIRMATION, "Player 2 won !", ButtonType.YES);
                } else if(gameType.equals("Misery")) {
                    alert = new Alert(Alert.AlertType.CONFIRMATION, "Player 1 won !", ButtonType.YES);
                }
            } else {
                if(gameType.equals("Regular")){
                    alert = new Alert(Alert.AlertType.CONFIRMATION, "Player 1 won !", ButtonType.YES);
                } else if(gameType.equals("Misery")) {
                    alert = new Alert(Alert.AlertType.CONFIRMATION, "Player 2 won !", ButtonType.YES);
                }
            }
            alert.show();
            alert.setOnHidden(event -> {

                try{
                    returnToEditing();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            //Edge toRemove = ((GenericComputer)players[turnCounter]).playARandomMove(this.graphEdges);
            this.pane.getChildren().remove(toRemove);
            this.graphEdges.remove(toRemove);

            ArrayList<Node> nodesToErase = checkGraph();

            ArrayList<NimEdge> edgesToErase = new ArrayList<>();
            for(NimEdge e : this.graphEdges) {
                for(Node n : nodesToErase) {
                    if(e.getFirstNode() == n || e.getSecondNode() == n) {
                        edgesToErase.add(e);
                    }
                }
            }
            this.graphEdges.removeAll(edgesToErase);
            this.pane.getChildren().removeAll(edgesToErase);
            this.graphNodes.removeAll(nodesToErase);
            this.pane.getChildren().removeAll(nodesToErase);

            turnCounter = (turnCounter+1)%2;
            this.turnDisplay.setText("Player " + (turnCounter+1) + "'s turn !");
            String playersColor = String.format("#%02x%02x%02x",
                    (int) (255 * this.players[turnCounter].getColor().getRed()),
                    (int) (255 * this.players[turnCounter].getColor().getGreen()),
                    (int) (255 * this.players[turnCounter].getColor().getBlue()));
            this.turnDisplay.setStyle("-fx-fill:"+playersColor+";");


            //Win condition
            if(this.graphEdges.size() == 0) {
                if (gameType.equals("Regular")) {

                    if(turnCounter%2 == 0) {
                        return true;
                    } else {
                        return true;
                    }

                } else if(gameType.equals("Misery")) {
                    if(turnCounter%2 == 0) {
                        return true;
                    } else {
                        return true;
                    }
                }

            }
            return false;
        }
        return false;
    }
    public void computerMove() {
        int[] heap = getHeap();
        NimEdge toPlay = players[this.turnCounter%2].playStratMove(this.graphEdges, this.ground, heap, this.isRegularGame);
        if(playAMove(toPlay)) {
            if(timeline != null){
                timeline.pause();
                timeline.stop();

            }
            timeline = new Timeline();


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            if(turnCounter%2 ==0) {
                if(gameType.equals("Regular")){
                    alert = new Alert(Alert.AlertType.CONFIRMATION, "Player 2 won !", ButtonType.YES);
                } else if(gameType.equals("Misery")) {
                    alert = new Alert(Alert.AlertType.CONFIRMATION, "Player 1 won !", ButtonType.YES);
                }
            } else {
                if(gameType.equals("Regular")){
                    alert = new Alert(Alert.AlertType.CONFIRMATION, "Player 1 won !", ButtonType.YES);
                } else if(gameType.equals("Misery")) {
                    alert = new Alert(Alert.AlertType.CONFIRMATION, "Player 2 won !", ButtonType.YES);
                }
            }
            alert.show();
            alert.setOnHidden(event -> {

                try{
                    returnToEditing();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void computersPlaying() {
        //create a timeline for moving the circle
       /* timeline = new Timeline();
        //You can add a specific action when each frame is started.
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                computerMove();
            }

        };*/

        timeline = new Timeline(new KeyFrame(Duration.seconds(0), ev -> {
            computerMove();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }


    private ArrayList<Node> checkGraph() {


        ArrayList<Node> nowDead = new ArrayList<>();
        for(Node n : this.graphNodes) {
            ArrayList<Node> alreadyVisited = new ArrayList<>();
            if(isNodeConnectedToGround(n, alreadyVisited)) {
                //Good to go
            } else {
                nowDead.add(n);
            }
        }
        return nowDead;
    }

    private boolean isNodeConnectedToGround(Node toCheck, ArrayList<Node> alreadyVisited) {

        if(this.ground.getNodesOnTheGround().contains(toCheck)) {
            return true;
        }

        alreadyVisited.add(toCheck);
        for(NimEdge e : this.graphEdges) {
            if(e.getFirstNode() == toCheck) {
                if(!alreadyVisited.contains(e.getSecondNode())) {
                    if(isNodeConnectedToGround(e.getSecondNode(), alreadyVisited))
                        return true;
                } else {
                }
            } else if(e.getSecondNode() == toCheck) {
                if(!alreadyVisited.contains(e.getFirstNode())) {
                    if(isNodeConnectedToGround(e.getFirstNode(), alreadyVisited))
                        return true;
                } else {
                }
            }
        }
        return false;
    }


    public void returnToMainMenu() throws IOException {
        Stage stage;
        Parent root;

        stage=(Stage) this.turnDisplay.getScene().getWindow();

        root = FXMLLoader.load(getClass().getResource("res/homeScreen.fxml"));
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        stage.setScene(scene);
        scene.getStylesheets().add
                (Main.class.getResource("style.css").toExternalForm());
        stage.show();
    }
    public void returnToEditing() throws IOException {
        Stage stage;
        Parent root;

        stage=(Stage) this.turnDisplay.getScene().getWindow();

        root = FXMLLoader.load(getClass().getResource("res/nymEditing.fxml"));
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        stage.setScene(scene);
        scene.getStylesheets().add
                (Main.class.getResource("style.css").toExternalForm());
        stage.show();
    }


}
