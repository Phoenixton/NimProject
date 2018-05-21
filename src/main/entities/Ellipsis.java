package main.entities;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Ellipse;
import main.GenericEditingController;

/**
 * Created by perri on 16/05/2018.
 */
public class Ellipsis extends Ellipse{

    private int id;
    //translate
    private double dragX, dragY;
    private Node node;
    private boolean dragging;
    private int belongs;

    public Ellipsis (Node node, int id) {

        this.id = id;
        this.node = node;
        this.dragging = false;
        this.belongs = 0;
        this.setStrokeWidth(5);

        super.setRadiusX(GenericEditingController.RADIUS);
        super.setRadiusY(GenericEditingController.RADIUS);
        super.setCenterX(node.getCenterX() - this.getRadiusX());
        super.setCenterY(node.getCenterY() - this.getRadiusY());

        this.addEventFilter(ScrollEvent.SCROLL, mouseScrolledEditing);
    }

    public void swapToPlayMode() {
        this.removeEventFilter(ScrollEvent.SCROLL, mouseScrolledEditing);
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getBelongs() {
        return belongs;
    }

    public void setBelongs(int belongs) {
        this.belongs = belongs;
    }

    EventHandler<ScrollEvent> mouseScrolledEditing = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent e) {
            Ellipsis ellipsis = (Ellipsis) e.getSource();
            ellipsis.setBelongs((ellipsis.getBelongs()+1)%3);
            ellipsis.setStroke(GenericEditingController.GAME_COLORS[ellipsis.getBelongs()]);
        }
    };


    EventHandler<MouseEvent> mouseDraggedEditing = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            dragging = true;
            Ellipsis e = (Ellipsis) event.getSource();
            e.setCenterX( dragX + event.getSceneX());
            e.setCenterY( dragY + event.getSceneY());

        }
    };
}
