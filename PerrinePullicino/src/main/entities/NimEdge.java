package main.entities;

import javafx.scene.shape.Line;

/**
 * Created by perri on 18/05/2018.
 */
public class NimEdge extends Line {

    private Node firstNode, secondNode;

    private boolean isSelected;

    public NimEdge(Node firstNode, Node secondNode) {

        super(firstNode.getCenterX(), firstNode.getCenterY(), secondNode.getCenterX(), secondNode.getCenterY());
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.isSelected = false;
    }

    public void resetEdge() {

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
}
