package main.entities;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import main.GenericEditingController;
import main.NymEditingController;

/**
 * Created by perri on 09/05/2018.
 */
public class Node extends Circle implements EventHandler<MouseEvent>{

    private double x, y;
    private int idOfNode;
    private boolean isSelected;

    private boolean dragging;

    //translate
    private double dragX, dragY;

    public Node(int id, double x, double y, int radius) {
        super(x, y, radius);
        this.idOfNode = id;
        this.isSelected = false;
        this.dragging = false;

    }

    public void swapToGenericEditingMode() {

        this.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDraggedEditing);
        this.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedGenericEditing);
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedGenericEditing);
    }

    public void swapToGenericPlayMode() {
        this.removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedGenericEditing);
        this.removeEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDraggedEditing);
        this.removeEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedGenericEditing);

        this.setFill(javafx.scene.paint.Color.BLACK);

        this.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedGenericPlaying);

    }


    public void swapToNimEditing() {


        this.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDraggedEditing);
        this.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedNimEditing);
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedNimEditing);

    }

    public void swapToNimPlaying() {

        this.removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedNimEditing);
        this.removeEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDraggedEditing);
        this.removeEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedNimEditing);

        this.setFill(javafx.scene.paint.Color.BLACK);

        this.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedNimPlaying);
    }

    public void nodeLinkedToGenericGround() {
        super.setFill(javafx.scene.paint.Color.GREEN);
        if(this.getIsSelected()) {

            this.setSelected(false);
            GenericEditingController.numberOfNodesSelected--;
        } else {

        }
        if(!GenericEditingController.ground.getNodesOnTheGround().contains(this)) {

            GenericEditingController.ground.getNodesOnTheGround().add(this);
        }
    }

    public void nodeLinkedToNimGround() {
        super.setFill(javafx.scene.paint.Color.GREEN);
        if(this.getIsSelected()) {

            this.setSelected(false);
            NymEditingController.numberOfNodesSelected--;
        } else {

        }
        if(!NymEditingController.ground.getNodesOnTheGround().contains(this)) {

            NymEditingController.ground.getNodesOnTheGround().add(this);
        }
    }


    public void nodeUnlinkedToGenericGround() {
        super.setFill(javafx.scene.paint.Color.BLACK);
        if(this.getIsSelected()) {

            this.setSelected(false);
            GenericEditingController.numberOfNodesSelected--;
        } else {

        }
        Node temp = null;
        for(Node n : GenericEditingController.ground.getNodesOnTheGround()) {
            if(this == n) {
                temp = n;
            }
        }
        if(temp != null)
            GenericEditingController.ground.getNodesOnTheGround().remove(temp);
    }

    public void nodeUnlinkedToNimGround() {
        super.setFill(javafx.scene.paint.Color.BLACK);
        if(this.getIsSelected()) {

            this.setSelected(false);
            NymEditingController.numberOfNodesSelected--;
        } else {

        }
        Node temp = null;
        for(Node n : NymEditingController.ground.getNodesOnTheGround()) {
            if(this == n) {
                temp = n;
            }
        }
        if(temp != null)
            NymEditingController.ground.getNodesOnTheGround().remove(temp);
    }


    EventHandler<MouseEvent> mousePressedGenericEditing = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            Node n = (Node) event.getSource();
            dragX = n.getCenterX() - event.getSceneX();
            dragY = n.getCenterY() - event.getSceneY();
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if(n.getIsSelected()) {
                    GenericEditingController.numberOfNodesSelected++;
                } else {
                    n.isSelected = true;
                    Node.super.setFill(javafx.scene.paint.Color.BLUE);
                    GenericEditingController.numberOfNodesSelected++;

                }
            } else if(event.getButton().equals(MouseButton.SECONDARY)) {
                if(n.getIsSelected()) {
                    n.isSelected = false;
                    Node.super.setFill(javafx.scene.paint.Color.BLACK);
                    GenericEditingController.numberOfNodesSelected--;
                    event.consume();
                } else {
                    //suppress the node taken care of in Generic Editing Controller


                }
            }
        }

    };


    EventHandler<MouseEvent> mousePressedNimEditing = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            Node n = (Node) event.getSource();
            dragX = n.getCenterX() - event.getSceneX();
            dragY = n.getCenterY() - event.getSceneY();
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if(n.getIsSelected()) {
                    NymEditingController.numberOfNodesSelected--;
                    n.isSelected = false;
                    if(n.getBoundsInParent().intersects(NymEditingController.ground.getBoundsInParent())) {
                        n.setFill(javafx.scene.paint.Color.GREEN);
                    } else {
                        n.setFill(javafx.scene.paint.Color.BLACK);

                    }
                } else {
                    n.isSelected = true;
                    NymEditingController.numberOfNodesSelected++;
                    Node.super.setFill(javafx.scene.paint.Color.BLUE);

                }
            }
        }
    };

    EventHandler<MouseEvent> mouseDraggedEditing = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            dragging = true;
            Node n = (Node) event.getSource();
            n.setCenterX( dragX + event.getSceneX());
            n.setCenterY( dragY + event.getSceneY());

        }
    };

    EventHandler<MouseEvent> mouseReleasedGenericEditing = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Node n = (Node) event.getSource();
            if(dragging) {
                if(n.getBoundsInParent().intersects(GenericEditingController.ground.getBoundsInParent())) {
                    n.nodeLinkedToGenericGround();
                } else {
                    nodeUnlinkedToGenericGround();
                    n.setFill(javafx.scene.paint.Color.BLACK);
                }
            }
            dragging = false;
        }
    };

    EventHandler<MouseEvent> mouseReleasedNimEditing = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Node n = (Node) event.getSource();
            if(dragging) {
                if(n.getBoundsInParent().intersects(NymEditingController.ground.getBoundsInParent())) {
                    n.nodeLinkedToNimGround();
                } else {
                    nodeUnlinkedToNimGround();
                    n.setFill(javafx.scene.paint.Color.BLACK);
                }
            }
            dragging = false;
        }
    };



    EventHandler<MouseEvent> mousePressedGenericPlaying = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            //Could highlight the connected nodes
            Node n = (Node) event.getSource();

        }
    };


    EventHandler<MouseEvent> mousePressedNimPlaying = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Node n = (Node) event.getSource();

        }
    };

    public void reset() {
        this.isSelected = false;
        if(this.getBoundsInParent().intersects(GenericEditingController.ground.getBoundsInParent())) {
            super.setFill(javafx.scene.paint.Color.GREEN);
        } else {
            super.setFill(javafx.scene.paint.Color.BLACK);

        }
    }

    public void nimReset() {
        this.isSelected = false;
        if(this.getBoundsInParent().intersects(NymEditingController.ground.getBoundsInParent())) {
            super.setFill(javafx.scene.paint.Color.GREEN);
        } else {
            super.setFill(javafx.scene.paint.Color.BLACK);

        }
    }


    public void setId(int id) {
        this.idOfNode = id;
    }

    public void setX(double x) {
        super.setCenterX(x);
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getIdOfNode() {
        return this.idOfNode;
    }

    public double getX() {
        return super.getCenterX();
    }

    public double getY() {
        return super.getCenterY();
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public String toString() {
        return "This Node is at (" + this.x +", " + this.y + ")";
    }

    @Override
    public void handle(MouseEvent event) {
        System.out.println("Handle stuff here :)");
    }
}
