package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.entities.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by perri on 08/05/2018.
 */
public class GenericEditingController {

    public Button returnToMainMenu;

    public Pane pane;

    private ArrayList<Node> graphNodes;
    private ArrayList<Edge> graphEdges;
    private ArrayList<ControlPoint> graphControlPoints;
    private ArrayList<Ellipsis> graphEllipsis;

    public static Ground ground;


    public static final int RADIUS = 20;
    private int nodeCounter = 0;
    private int ellipsisCounter = 0;
    public static int numberOfNodesSelected = 0;

    public static Color[] GAME_COLORS = {Color.GREEN, javafx.scene.paint.Color.BLUE, Color.BROWN};

    @FXML
    private ColorPicker neutralColor, player1Color, player2Color;

    private boolean dragging;

    ObservableList<String> colorOptions;
    ObservableList<String> opponentOptions;
    @FXML
    private ComboBox gameType;
    @FXML
    private ComboBox opponentType;

    @FXML
    public void initialize() {

        this.dragging = false;

        this.colorOptions = FXCollections.observableArrayList(
                "Regular",
                "Misery"
        );

        this.opponentOptions = FXCollections.observableArrayList(
                "Human",
                "Computer"
        );
        this.gameType.setItems(this.colorOptions);
        this.gameType.setValue("Regular");
        this.opponentType.setItems(this.opponentOptions);
        this.opponentType.setValue("Human");

        this.neutralColor.setValue(GAME_COLORS[0]);
        this.player1Color.setValue(GAME_COLORS[1]);
        this.player2Color.setValue(GAME_COLORS[2]);

        this.graphNodes = new ArrayList<>();
        this.graphEdges = new ArrayList<>();
        this.graphControlPoints = new ArrayList<>();
        this.graphEllipsis = new ArrayList<>();
        ground = new Ground();
        this.pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));


        this.pane.getChildren().add(ground);
        pane.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {

            if(me.getButton().equals(MouseButton.PRIMARY)) {

                double temp_mouse_x = me.getX();
                double temp_mouse_y = me.getY();

                double x_max = RADIUS;
                double y_max = RADIUS;

                boolean nodeUnderneath = false;
                javafx.scene.Node clickedOn = me.getPickResult().getIntersectedNode();

                if(clickedOn instanceof Edge) {
                    //Doesn't allow for a node on an edge
                    for (Edge e : this.graphEdges) {
                        if (e != clickedOn) {
                            e.resetEdge();
                        }
                    }
                } else if(clickedOn instanceof ControlPoint) {
                    //Just to be sure, but treated by consume in Control Point

                } else {

                    //Checks if there's a node next to
                    for(Node n : this.graphNodes) {

                        nodeUnderneath = (temp_mouse_x >= n.getX() - 20 && temp_mouse_x <= (n.getX() + x_max + 20)
                                &&
                                temp_mouse_y >= n.getY() - 20 && temp_mouse_y <= (n.getY()+ y_max) + 20);
                        if(nodeUnderneath)
                            break;
                    }

                    if(nodeUnderneath) {
                        //There's a node too close

                        //Adds an edge and deselects
                        if(GenericEditingController.numberOfNodesSelected == 2) {
                            addAnEdge();
                            cleanupNodes();
                        }

                    } else {

                        this.nodeCounter++;
                        Node circle;
                        if (clickedOn instanceof Ground) {
                            circle = new Node(this.nodeCounter, me.getX(), me.getY(), 10);
                            circle.swapToGenericEditingMode();
                            circle.setFill(javafx.scene.paint.Color.GREEN);
                            if(circle.getIsSelected()) {

                                circle.setSelected(false);
                                GenericEditingController.numberOfNodesSelected--;
                            }
                            this.ground.getNodesOnTheGround().add(circle);
                        } else {

                            circle = new Node(this.nodeCounter, me.getX(), me.getY(), 10);
                            circle.swapToGenericEditingMode();
                        }

                        this.graphNodes.add(circle);

                        pane.getChildren().add(circle);
                    }
                }

            } else if (me.getButton().equals(MouseButton.SECONDARY)) {
                javafx.scene.Node clickedOn = me.getPickResult().getIntersectedNode();

                if(clickedOn instanceof Edge) {

                    ControlPoint toErase = ((Edge) clickedOn).getControlNode();
                    this.pane.getChildren().remove(toErase);
                    this.pane.getChildren().remove(clickedOn);
                    this.graphControlPoints.remove(toErase);
                    this.graphEdges.remove(clickedOn);


                } else if (clickedOn instanceof Ellipsis) {
                    this.graphEllipsis.remove(clickedOn);
                    this.pane.getChildren().remove(clickedOn);
                } else if(clickedOn instanceof Node) {
                    if(((Node) clickedOn).getIsSelected()) {
                        ((Node) clickedOn).reset();
                    } else {
                        ArrayList<Edge> toErase = new ArrayList<>();
                        ArrayList<Ellipsis> toErase2 = new ArrayList<>();
                        for(Edge e : this.graphEdges) {
                            if(e.getFirstNode() == clickedOn || e.getSecondNode() == clickedOn) {
                                toErase.add(e);
                            }
                        }

                        for(Ellipsis e : this.graphEllipsis) {
                            if(e.getNode() == clickedOn) {
                                toErase2.add(e);
                            }
                        }
                        this.graphEdges.removeAll(toErase);
                        this.graphEllipsis.removeAll(toErase2);
                        this.graphNodes.remove(clickedOn);

                        this.pane.getChildren().removeAll(toErase);
                        this.pane.getChildren().removeAll(toErase2);
                        this.pane.getChildren().remove(clickedOn);
                    }

                }

            }
        });


    }

    private void addAnEdge() {

        Node[] temp = new Node[2];
        int i = 0;
        for(Node n : this.graphNodes) {
            if(n.getIsSelected()) {
                n.setSelected(false);
                temp[i] = n;
                i++;
            }
        }

        if(temp[1] == null) {
            temp[1] = temp[0];
            Ellipsis ellipse = new Ellipsis(temp[0], this.ellipsisCounter);
            ellipse.setStroke(this.neutralColor.getValue());
            ellipse.setFill(javafx.scene.paint.Color.WHITE);
            ellipse.centerXProperty().bind(temp[0].centerXProperty());
            ellipse.centerYProperty().bind(temp[0].centerYProperty().subtract(RADIUS));
            this.graphEllipsis.add(ellipse);
            this.pane.getChildren().add(ellipse);
            ellipse.toBack();

        } else {

            if(!edgeAlreadyExists(temp[0], temp[1])) {

                ControlPoint control = new ControlPoint(temp[0], temp[1]);
                control.setRadius(25);
                Edge quadCurve = new Edge(temp[0], temp[1], control);
                quadCurve.setStrokeWidth(10);
                quadCurve.setFill(null);
                quadCurve.setStroke(this.neutralColor.getValue());

                //binding
                quadCurve.controlXProperty().bind(control.centerXProperty());
                quadCurve.controlYProperty().bind(control.centerYProperty());
                quadCurve.endXProperty().bind(temp[1].centerXProperty());
                quadCurve.endYProperty().bind(temp[1].centerYProperty());
                quadCurve.startXProperty().bind(temp[0].centerXProperty());
                quadCurve.startYProperty().bind(temp[0].centerYProperty());

                this.graphEdges.add(quadCurve);
                this.graphControlPoints.add(control);
                pane.getChildren().add(control);
                pane.getChildren().add(quadCurve);
            }
        }


    }

    private boolean edgeAlreadyExists(Node n1, Node n2) {
        for(Edge e : this.graphEdges) {
            if(e.getFirstNode() == n1 && e.getSecondNode() == n2
                    || e.getFirstNode() == n2 && e.getSecondNode() == n1) {
                return true;
            }
        }

        return false;
    }

    public void launchGame() throws IOException {
        if(checkGraph()) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "res/genericPlaying.fxml"
                    )
            );
            Stage stage=(Stage) this.pane.getScene().getWindow();
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );

            Color[] gameColors = {this.neutralColor.getValue(), this.player1Color.getValue(), this.player2Color.getValue()};

            GenericPlayingController controller =
                    loader.<GenericPlayingController>getController();
            controller.retrievePlayerData(this.graphNodes, this.graphEdges, this.graphEllipsis,
                    this.graphControlPoints, ground, this.gameType.getValue().toString(), gameColors, opponentType.getValue().toString());

            stage.show();

        }


    }

    private boolean checkGraph() {

        int i = 0;
        for(Node n : this.graphNodes) {
            ArrayList<Node> alreadyVisited = new ArrayList<>();
            if(isNodeConnectedToGround(n, alreadyVisited)) {
                i++;
            }
        }

        return (i == this.graphNodes.size());
    }

    private boolean isNodeConnectedToGround(Node toCheck, ArrayList<Node> alreadyVisited) {

        if(this.ground.getNodesOnTheGround().contains(toCheck)) {
            for(Node n : alreadyVisited) {
                n.setFill(javafx.scene.paint.Color.GREEN);
            }
            toCheck.setFill(javafx.scene.paint.Color.GREEN);
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
        toCheck.setFill(javafx.scene.paint.Color.RED);
        return false;
    }



    private void cleanupNodes() {
        for(Node n : this.graphNodes){
            n.reset();
        }
        numberOfNodesSelected = 0;
    }

    @FXML
    private void changeEdgesColors() {
        GAME_COLORS[0] = this.neutralColor.getValue();
        GAME_COLORS[1] = this.player1Color.getValue();
        GAME_COLORS[2] = this.player2Color.getValue();
        for(Edge e : this.graphEdges) {
            if(e.getBelongs() == 0) {
                e.setStroke(GAME_COLORS[0]);
            } else if(e.getBelongs() == 1) {
                e.setStroke(GAME_COLORS[1]);
            } else if(e.getBelongs() == 2) {
                e.setStroke(GAME_COLORS[2]);
            }
        }
        for(Ellipsis e : this.graphEllipsis) {
            if(e.getBelongs() == 0) {
                e.setStroke(GAME_COLORS[0]);
            } else if(e.getBelongs() == 1) {
                e.setStroke(GAME_COLORS[1]);
            } else if(e.getBelongs() == 2) {
                e.setStroke(GAME_COLORS[2]);
            }
        }
    }


    public void returnToMainMenu() throws IOException{
        Stage stage;
        Parent root;

        stage=(Stage) returnToMainMenu.getScene().getWindow();

        root = FXMLLoader.load(getClass().getResource("res/homeScreen.fxml"));
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        stage.setScene(scene);
        scene.getStylesheets().add
                (Main.class.getResource("style.css").toExternalForm());
        stage.show();
    }





}
