package main.entities;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * Created by perri on 16/05/2018.
 */
public class Ground extends Line {

    private ArrayList<Node> nodesOnTheGround;
    private Point2D start;
    private Point2D end;

    public Ground () {
        this.nodesOnTheGround = new ArrayList<>();
        this.start = new Point2D(0,600);
        this.end = new Point2D(800, 600);
        super.setStrokeWidth(10);
        super.setStartX(0.0);
        super.setStartY(600);
        super.setEndX(800);
        super.setEndY(600);
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
    }

    EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {

        }
    };


    public ArrayList<Node> getNodesOnTheGround() {
        return nodesOnTheGround;
    }

    public void setNodesOnTheGround(ArrayList<Node> nodesOnTheGround) {
        this.nodesOnTheGround = nodesOnTheGround;
    }

    public Point2D getStart() {
        return start;
    }

    public void setStart(Point2D start) {
        this.start = start;
    }

    public Point2D getEnd() {
        return end;
    }

    public void setEnd(Point2D end) {
        this.end = end;
    }
}
