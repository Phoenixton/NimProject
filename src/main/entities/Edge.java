package main.entities;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.QuadCurve;
import main.GenericEditingController;

/**
 * Created by perri on 09/05/2018.
 */
public class Edge extends QuadCurve{

    private Node firstNode;
    private Node secondNode;
    private ControlPoint controlNode;

    private boolean isSelected;

    private int belongs;

    public Edge(Node firstNode, Node secondNode, ControlPoint controlNode){
        super(firstNode.getCenterX(), firstNode.getCenterY(),
                controlNode.getCenterX(), controlNode.getCenterY(),
                secondNode.getCenterX(), secondNode.getCenterY());
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.controlNode = controlNode;
        this.controlNode.setFill(javafx.scene.paint.Color.BLACK);
        this.controlNode.setVisible(false);
        this.isSelected = false;
        //neutral at first
        this.belongs = 0;

        this.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedEditing);
        this.addEventFilter(ScrollEvent.SCROLL, mouseScrolledEditing);

    }

    public void swapToPlayMode() {
        this.removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedEditing);
        this.removeEventFilter(ScrollEvent.SCROLL, mouseScrolledEditing);

        this.setStroke(GenericEditingController.GAME_COLORS[this.getBelongs()]);
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedPlaying);

    }

    EventHandler<ScrollEvent> mouseScrolledEditing = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent e) {
            Edge edge = (Edge) e.getSource();
            edge.setBelongs((edge.getBelongs()+1)%3);
            edge.setStroke(GenericEditingController.GAME_COLORS[edge.getBelongs()]);
        }
    };

    EventHandler<MouseEvent> mousePressedEditing = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {

            Edge edge = (Edge) e.getSource();

            if(e.getButton().equals(MouseButton.PRIMARY)) {
                if(edge.getIsSelected()) {

                    edge.getControlNode().setVisible(false);
                    edge.controlNode.setIsVisible(false);
                    edge.setIsSelected(false);
                } else {
                    edge.getControlNode().setVisible(true);
                    edge.controlNode.setIsVisible(true);
                    edge.setIsSelected(true);
                }
            }


        }
    };

    EventHandler<MouseEvent> mousePressedPlaying = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            Edge edge = (Edge) e.getSource();

            if(e.getButton().equals(MouseButton.PRIMARY)) {
                //Could show the connected node
            } else if(e.getButton().equals(MouseButton.SECONDARY)){
            }
        }
    };


    public void resetEdge() {
        this.controlNode.setIsVisible(false);
        this.controlNode.setVisible(false);
        this.isSelected = false;
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(Node firstNode) {
        this.firstNode = firstNode;
    }

    public Node getSecondNode() {
        return secondNode;
    }

    public void setSecondNode(Node secondNode) {
        this.secondNode = secondNode;
    }

    public ControlPoint  getControlNode() {
        return controlNode;
    }

    public void setControlNode(ControlPoint controlNode) {
        this.controlNode = controlNode;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }

    public int getBelongs() {
        return belongs;
    }

    public void setBelongs(int belongs) {
        this.belongs = belongs;
    }
}
