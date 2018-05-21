package main.entities;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

/**
 * Created by perri on 16/05/2018.
 */
public class ControlPoint extends Circle {

    private Node firstNode;
    private Node secondeNode;
    double dragX, dragY;
    private boolean isVisible;

    public ControlPoint(Node firstNode, Node secondeNode) {

        this.firstNode = firstNode;
        this.secondeNode = secondeNode;
        super.setCenterX((firstNode.getCenterX() + secondeNode.getCenterX()) / 2);
        super.setCenterY((firstNode.getCenterY() + secondeNode.getCenterY()) / 2);
        this.isVisible = false;
        this.dragX = 0;
        this.dragY = 0;
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
        this.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDragged);
    }

    public void setIsVisible(boolean b) {
        this.isVisible = b;
    }

    public boolean getIsVisible() {
        return this.isVisible;
    }

    EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ControlPoint cp = (ControlPoint) event.getSource();

            if(cp.getIsVisible()) {
                dragX = cp.getCenterX() - event.getSceneX();
                dragY = cp.getCenterY() - event.getSceneY();
            } else {
            }

            event.consume();
        }
    };

    EventHandler<MouseEvent> mouseDragged = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if( event.getSource() instanceof ControlPoint) {

                ControlPoint cp = (ControlPoint) event.getSource();
                cp.setCenterX( dragX + event.getSceneX());
                cp.setCenterY( dragY + event.getSceneY());
            }

        }
    };

}
