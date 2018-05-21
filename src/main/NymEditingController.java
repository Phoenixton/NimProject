package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
public class NymEditingController {

    public Button returnToMainMenu;

    public Pane pane;

    public static Ground ground;

    private ArrayList<Node> graphNodes;
    private ArrayList<NimEdge> graphEdges;

    private ObservableList<String> gameOptions;
    private ObservableList<String> playerOptions;
    private ObservableList<String> playerStrategies;

    @FXML
    private ColorPicker neutralColor;
    private Color selectedColor;
    @FXML
    private ComboBox gameType;
    @FXML
    private ComboBox player1Type, player2Type;
    @FXML
    private ComboBox player1Strategy, player2Strategy;
    @FXML
    private TextField tige1, tige2, tige3, tige4, tige5, tige6, tige7;
    private TextField[] tiges;

    private int nodeCounter;

    public static int numberOfNodesSelected;

    public static int RADIUS = 10;


    @FXML
    public void initialize() {
        this.graphNodes = new ArrayList<>();
        this.graphEdges = new ArrayList<>();
        this.gameOptions = FXCollections.observableArrayList(
                "Regular",
                "Misery"
        );
        this.playerStrategies = FXCollections.observableArrayList(
                "Winning",
                "Random"
        );
        this.playerOptions = FXCollections.observableArrayList(
                "Human",
                "Computer"
        );
        this.player1Type.setItems(this.playerOptions);
        this.player1Type.setValue("Human");

        this.player2Type.setItems(this.playerOptions);
        this.player2Type.setValue("Human");

        this.player1Strategy.setItems(this.playerStrategies);
        this.player1Strategy.setValue("Winning");
        this.player1Strategy.setVisible(false);
        this.player2Strategy.setItems(this.playerStrategies);
        this.player2Strategy.setValue("Winning");
        this.player2Strategy.setVisible(false);


        this.gameType.setItems(this.gameOptions);
        this.gameType.setValue("Regular");


        this.neutralColor.setValue(javafx.scene.paint.Color.GREEN);
        this.selectedColor = this.neutralColor.getValue();
        this.nodeCounter = 0;


        NymEditingController.numberOfNodesSelected = 0;
        ground = new Ground();
        this.pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        this.tige1.setTooltip(new Tooltip("Input here the number of edges you want in each row"));
        this.tige2.setTooltip(new Tooltip("Input here the number of edges you want in each row"));
        this.tige3.setTooltip(new Tooltip("Input here the number of edges you want in each row"));
        this.tige4.setTooltip(new Tooltip("Input here the number of edges you want in each row"));
        this.tige5.setTooltip(new Tooltip("Input here the number of edges you want in each row"));
        this.tige6.setTooltip(new Tooltip("Input here the number of edges you want in each row"));
        this.tige7.setTooltip(new Tooltip("Input here the number of edges you want in each row"));
        this.pane.getChildren().add(ground);
        pane.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {

            if(me.getButton().equals(MouseButton.PRIMARY)) {

                double temp_mouse_x = me.getX();
                double temp_mouse_y = me.getY();

                double x_max = RADIUS;
                double y_max = RADIUS;

                boolean nodeUnderneath = false;
                javafx.scene.Node clickedOn = me.getPickResult().getIntersectedNode();

                if(clickedOn instanceof NimEdge) {
                    //Doesn't allow for a node on an edge
                    for (NimEdge e : this.graphEdges) {
                        if (e != clickedOn) {
                            e.resetEdge();
                        }
                    }
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
                        if(NymEditingController.numberOfNodesSelected == 2) {

                            addAnEdge();
                            cleanupNodes();
                        }

                    } else {

                        this.nodeCounter++;
                        Node circle;
                        if (clickedOn instanceof Ground) {
                            circle = new Node(this.nodeCounter, me.getX(), me.getY(), 10);
                            circle.swapToNimEditing();
                            circle.setFill(javafx.scene.paint.Color.GREEN);
                            if(circle.getIsSelected()) {

                                circle.setSelected(false);
                                NymEditingController.numberOfNodesSelected--;
                            }
                            NymEditingController.ground.getNodesOnTheGround().add(circle);
                        } else {

                            circle = new Node(this.nodeCounter, me.getX(), me.getY(), 10);
                            circle.swapToNimEditing();
                        }

                        this.graphNodes.add(circle);

                        pane.getChildren().add(circle);
                    }
                }

            } else if (me.getButton().equals(MouseButton.SECONDARY)) {
                javafx.scene.Node clickedOn = me.getPickResult().getIntersectedNode();

                if(clickedOn instanceof NimEdge) {


                    this.pane.getChildren().remove(clickedOn);
                    this.graphEdges.remove(clickedOn);


                } else if(clickedOn instanceof Node) {
                    if(((Node) clickedOn).getIsSelected()) {
                        ((Node) clickedOn).reset();
                    } else {
                        ArrayList<NimEdge> toErase = new ArrayList<>();
                        for(NimEdge e : this.graphEdges) {
                            if(e.getFirstNode() == clickedOn || e.getSecondNode() == clickedOn) {
                                toErase.add(e);
                            }
                        }

                        this.graphEdges.removeAll(toErase);
                        this.graphNodes.remove(clickedOn);

                        this.pane.getChildren().removeAll(toErase);
                        this.pane.getChildren().remove(clickedOn);
                    }

                }

            }
        });

    }

    private void cleanupNodes() {
        for(Node n : this.graphNodes){
            n.nimReset();
        }
        NymEditingController.numberOfNodesSelected = 0;
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
        if(!edgeAlreadyExists(temp[0], temp[1])) {

            if(!multipleEdges(temp[0]) && !multipleEdges(temp[1])) {

                NimEdge edge = new NimEdge(temp[0], temp[1]);
                edge.setStrokeWidth(10);
                edge.setFill(null);
                edge.setStroke(this.selectedColor);

                //binding
                edge.endXProperty().bind(temp[1].centerXProperty());
                edge.endYProperty().bind(temp[1].centerYProperty());
                edge.startXProperty().bind(temp[0].centerXProperty());
                edge.startYProperty().bind(temp[0].centerYProperty());


                this.graphEdges.add(edge);
                pane.getChildren().add(edge);

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Can't have more than two edges from a node !", ButtonType.YES);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {

                }
            }
        }


    }

    private boolean multipleEdges(Node n) {

        int counter = 0;
        for(NimEdge e : this.graphEdges) {
            if(e.getFirstNode() == n || e.getSecondNode() == n) {
                counter++;
            }
        }
        return (counter > 1);
    }


    private boolean edgeAlreadyExists(Node n1, Node n2) {
        for(NimEdge e : this.graphEdges) {
            if(e.getFirstNode() == n1 && e.getSecondNode() == n2
                    || e.getFirstNode() == n2 && e.getSecondNode() == n1) {

                return true;
            }
        }

        return false;
    }

    public void selectColor() {
        this.selectedColor = this.neutralColor.getValue();
        for(NimEdge e : this.graphEdges) {
            e.setStroke(this.selectedColor);
        }
    }

    private boolean checkGraph() {
        int i = 0;
        for(Node n : this.graphNodes) {
            ArrayList<Node> alreadyVisited = new ArrayList<>();
            if(isNodeConnectedToGround(n, alreadyVisited) && nodeOnlyHasTwoEdges(n)) {
                i++;
            } else {
                return false;
            }
        }

        return (i == this.graphNodes.size());
    }

    private boolean nodeOnlyHasTwoEdges(Node n) {
        int counter = 0;
        for(NimEdge e : this.graphEdges) {
            if(e.getFirstNode() == n || e.getSecondNode() == n) {
                counter++;
            }
        }
        return (counter <= 2);
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
        toCheck.setFill(javafx.scene.paint.Color.RED);
        return false;
    }



    public void launchGame() throws IOException {

        this.tiges = new TextField[7];

        this.tiges[0] = tige1;
        this.tiges[1] = tige2;
        this.tiges[2] = tige3;
        this.tiges[3] = tige4;
        this.tiges[4] = tige5;
        this.tiges[5] = tige6;
        this.tiges[6] = tige7;

        if(checkGraph()) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "res/nimPlaying.fxml"
                    )
            );
            Stage stage=(Stage) this.pane.getScene().getWindow();
            stage.setScene(
                    new Scene(
                            (Pane) loader.load()
                    )
            );
            String player1Strat;

            if(player1Type.getValue().toString().equals("Computer")) {
                player1Strat = this.player1Strategy.getValue().toString();
            } else {
                player1Strat = "Winning";
            }
            String player2Strat;

            if(player2Type.getValue().toString().equals("Computer")) {
                player2Strat = this.player2Strategy.getValue().toString();
            } else {
                player2Strat = "Winning";
            }

            if(!tigeTextViewsEmpty()) {
                //TODO:check if input is technically correct
                this.graphNodes.clear();
                this.graphEdges.clear();
                this.ground.getNodesOnTheGround().clear();
                this.nodeCounter = 0;
                int countNonNul = 0;
                int[] counter = new int[7];
                for(int i = 0; i < tiges.length; i++) {
                    if(tiges[i].getText().equals("")) {
                        counter[i] = 0;
                    } else {
                        counter[i] = Integer.parseInt(tiges[i].getText());
                        countNonNul++;
                    }
                }
                for(int i = 0; i < counter.length; i++){
                    if(counter[i] != 0) {
                        Node origin = new Node(this.nodeCounter, (this.pane.getWidth()/(countNonNul+1)) * (i+1), this.pane.getHeight(), RADIUS);
                        this.nodeCounter++;
                        origin.toFront();
                        origin.nodeLinkedToNimGround();
                        this.graphNodes.add(origin);
                        Node junction = origin;
                        for(int j = 0; j < counter[i]; j++) {
                            Node toJoin = new Node(this.nodeCounter, (this.pane.getWidth()/(countNonNul+1)) * (i+1),
                                    this.pane.getHeight() - (this.pane.getHeight() / counter[i]) * (j+1), RADIUS);
                            this.nodeCounter++;
                            this.graphNodes.add(toJoin);
                            NimEdge edge = new NimEdge(junction, toJoin);
                            edge.setStrokeWidth(10);
                            edge.setFill(null);
                            edge.setStroke(this.selectedColor);
                            this.graphEdges.add(edge);
                            junction = toJoin;
                        }

                    }
                }
                NimPlayingController controller =
                        loader.<NimPlayingController>getController();
                controller.retrieveGameData(this.graphNodes, this.graphEdges, ground, this.gameType.getValue().toString(),
                        this.selectedColor, player1Type.getValue().toString(), player2Type.getValue().toString(), player1Strat, player2Strat);
            } else {


                NimPlayingController controller =
                        loader.<NimPlayingController>getController();
                controller.retrieveGameData(this.graphNodes, this.graphEdges, ground, this.gameType.getValue().toString(),
                        this.selectedColor, player1Type.getValue().toString(), player2Type.getValue().toString(), player1Strat, player2Strat);
            }


            stage.show();

        }


    }

    private boolean tigeTextViewsEmpty() {

        for(int i = 0; i < tiges.length; i++) {
            if(tiges[i].getText().isEmpty()) {
            } else {
                return false;
            }
        }
        return true;
    }

    public void setStrategiesVisible() {
        if(this.player1Type.getValue().toString().equals("Computer")) {
            this.player1Strategy.setVisible(true);
        } else {
            this.player1Strategy.setVisible(false);
        }

        if(this.player2Type.getValue().toString().equals("Computer")) {
            this.player2Strategy.setVisible(true);
        } else {
            this.player2Strategy.setVisible(false);
        }


    }


    public void returnToMainMenu() throws IOException {
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
