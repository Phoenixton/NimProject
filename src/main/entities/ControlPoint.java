package main.entities;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import main.Main;

import javax.naming.ldap.Control;
import java.util.Random;

/**
 * Created by perri on 16/05/2018.
 */
public class ControlPoint extends Circle {

    private Node firstNode;
    private Node secondeNode;
    double dragX, dragY;
    private boolean isVisible;

    public ControlPoint(Node firstNode, Node secondeNode) {
       /* Random rand = new Random();

        int  rand_x = rand.nextInt(Main.WIDTH);
        int rand_y = rand.nextInt(Main.HEIGHT);
        super.setCenterX(rand_x);
        super.setCenterY(rand_y);
        */

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
                /*
                orgSceneX = e.getSceneX();
                orgSceneY = e.getSceneY();
                orgTranslateX = ((ControlPoint)(e.getSource())).getTranslateX();
                orgTranslateY = ((ControlPoint)(e.getSource())).getTranslateY();*/
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
                /*
                double offsetX = event.getSceneX() - orgSceneX;
                double offsetY = event.getSceneY() - orgSceneY;
                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;

                ((ControlPoint)(event.getSource())).setTranslateX(newTranslateX);
                ((ControlPoint)(event.getSource())).setTranslateY(newTranslateY);
                */
            }

        }
    };

}
