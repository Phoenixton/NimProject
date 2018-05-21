package main;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.IA.GenericComputer;
import main.entities.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by perri on 17/05/2018.
 */
public class GenericPlayingController {

    private ArrayList<Node> graphNodes;
    private ArrayList<Edge> graphEdges;
    private ArrayList<ControlPoint> graphControlPoints;
    private ArrayList<Ellipsis> graphEllipsis;

    @FXML
    private Pane pane;

    private Ground ground;

    private String gameType;

    @FXML
    private Text turnDisplay;

    private int turnCounter;

    private Player[] players;

    private Color[] gameColors;

    private String opponent;

    @FXML
    public void initialize() {
        this.pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        this.pane.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {

            if(me.getButton().equals(MouseButton.SECONDARY)) {

                javafx.scene.Node clickedOn = me.getPickResult().getIntersectedNode();

                if(clickedOn instanceof Edge) {
                    if(((Edge) clickedOn).getBelongs() == players[(turnCounter%2)].getId() || ((Edge) clickedOn).getBelongs() == 0) {

                        playAMove((Edge)clickedOn);

                        //COmputer playing if relevant
                        if(opponent.equals("Computer")) {
                            Edge toPlay = (players[turnCounter]).playARandomMove(this.graphEdges);
                            if(toPlay == null) {
                            } else {
                                playAMove(toPlay);
                            }
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "You have to click on one of your edges !", ButtonType.YES);
                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.YES) {
                            //keep going
                        }
                    }

                } else if(clickedOn instanceof Ellipsis) {
                    if(((Ellipsis) clickedOn).getBelongs() == players[(turnCounter%2)].getId() || ((Ellipsis) clickedOn).getBelongs() == 0) {
                        playAMove((Ellipsis)clickedOn);
                        //COmputer playing if relevant
                        if(opponent.equals("Computer")) {
                            Edge toPlay = (players[turnCounter]).playARandomMove(this.graphEdges);
                            if(toPlay == null) {
                                Ellipsis toPlayEllipsis = (players[turnCounter]).playARandomMoveEllipsis(this.graphEllipsis);
                                if(toPlayEllipsis == null ){
                                    //no more choice
                                } else {
                                    playAMove(toPlayEllipsis);
                                }
                            } else {
                                playAMove(toPlay);
                            }
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "You have to click on one of your ellipse !", ButtonType.YES);
                        alert.showAndWait();

                        if (alert.getResult() == ButtonType.YES) {
                            //keep going
                        }
                    }
                }
            }
        });


    }

    public void retrievePlayerData(ArrayList<Node> graphNodes, ArrayList<Edge> graphEdges, ArrayList<Ellipsis> graphEllipsis,
                                   ArrayList<ControlPoint> graphControlPoints, Ground ground, String gameType, Color[] gameColors, String opponent) {

        this.graphNodes = graphNodes;
        this.graphEdges = graphEdges;
        this.graphEllipsis = graphEllipsis;
        this.graphControlPoints = graphControlPoints;
        this.ground = ground;
        this.gameType = gameType;
        this.players = new Player[2];
        this.gameColors = gameColors;
        this.opponent = opponent;
        this.players[0] = new Player(1, gameColors[1]);
        if(this.opponent.equals("Human")) {
            this.players[1] = new Player(2, gameColors[2]);

        } else {
            this.players[1] = new GenericComputer(2, gameColors[2]);
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
        for(Edge e : this.graphEdges) {
            this.pane.getChildren().add(e);
            e.swapToPlayMode();
        }
        for(Ellipsis e : this.graphEllipsis) {
            this.pane.getChildren().add(e);
        }
        /*
        for(ControlPoint n : this.graphNodes) {

        }*/

    }

    public void playAMove(Ellipsis toRemove) {

        //Edge toRemove = ((GenericComputer)players[turnCounter]).playARandomMove(this.graphEdges);
        this.pane.getChildren().remove(toRemove);
        this.graphEllipsis.remove(toRemove);


        turnCounter = (turnCounter+1)%2;
        this.turnDisplay.setText("Player " + (turnCounter+1) + "'s turn !");
        String playersColor = String.format("#%02x%02x%02x",
                (int) (255 * this.players[turnCounter].getColor().getRed()),
                (int) (255 * this.players[turnCounter].getColor().getGreen()),
                (int) (255 * this.players[turnCounter].getColor().getBlue()));
        this.turnDisplay.setStyle("-fx-fill:"+playersColor+";");

        checkWinCon();

    }

    public void playAMove(Edge toRemove) {

        //Edge toRemove = ((GenericComputer)players[turnCounter]).playARandomMove(this.graphEdges);
        this.pane.getChildren().remove(toRemove);
        this.graphEdges.remove(toRemove);

        ArrayList<Node> nodesToErase = checkGraph();

        ArrayList<Edge> edgesToErase = new ArrayList<>();
        ArrayList<Ellipsis> ellipsisToErase = new ArrayList<>();
        for(Edge e : this.graphEdges) {
            for(Node n : nodesToErase) {
                if(e.getFirstNode() == n || e.getSecondNode() == n) {
                    edgesToErase.add(e);
                }
            }
        }
        for(Ellipsis e : this.graphEllipsis) {
            for(Node n : nodesToErase) {
                if(e.getNode() == n) {
                    ellipsisToErase.add(e);
                }
            }
        }
        FadeTransition ft;
        for(Edge e : edgesToErase) {
        //TODO : Make the animation for the node
            ft= new FadeTransition();
            ft.setNode(e);
            ft.setDuration(new Duration(10000));
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setCycleCount(Animation.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();
        }
        this.graphEdges.removeAll(edgesToErase);
        this.pane.getChildren().removeAll(edgesToErase);
        this.graphEllipsis.removeAll(ellipsisToErase);
        this.pane.getChildren().removeAll(ellipsisToErase);
        this.graphNodes.removeAll(nodesToErase);
        this.pane.getChildren().removeAll(nodesToErase);

        turnCounter = (turnCounter+1)%2;
        this.turnDisplay.setText("Player " + (turnCounter+1) + "'s turn !");
        String playersColor = String.format("#%02x%02x%02x",
                (int) (255 * this.players[turnCounter].getColor().getRed()),
                (int) (255 * this.players[turnCounter].getColor().getGreen()),
                (int) (255 * this.players[turnCounter].getColor().getBlue()));
        this.turnDisplay.setStyle("-fx-fill:"+playersColor+";");

        checkWinCon();

    }

    private void checkWinCon() {
        //Win condition
        if((this.graphEdges.size() == 0 && this.graphEllipsis.size() ==0) ||
                (!stillAvailableEdges(players[turnCounter]))) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Replay ?", ButtonType.YES, ButtonType.NO);

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

    private boolean stillAvailableEdges(Player player) {
        for(Edge e : this.graphEdges){
            if(e.getBelongs() == 0 ||e.getBelongs() == player.getId()) {
                return true;
            }
        }


        for(Ellipsis e : this.graphEllipsis) {
            if(e.getBelongs() == 0 ||e.getBelongs() == player.getId()) {
                return true;
            }
        }
        return false;
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
        for(Edge e : this.graphEdges) {
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

        root = FXMLLoader.load(getClass().getResource("res/genericEditing.fxml"));
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        stage.setScene(scene);
        scene.getStylesheets().add
                (Main.class.getResource("style.css").toExternalForm());
        stage.show();
    }
}
