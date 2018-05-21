package main.entities;

import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by perri on 17/05/2018.
 */
public class Player {

    //0 first player, 1 second player
    protected int id;
    protected Color color;

    public Player(int id, Color color) {
        this.id = id;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public Edge playARandomMove(ArrayList<Edge> edges) {
        return null;
    }
    public Ellipsis playARandomMoveEllipsis(ArrayList<Ellipsis> ellipses) {
        return null;
    }

    public NimEdge playARandomNimMove(ArrayList<NimEdge> edges) {
        return null;
    }

    public NimEdge playStratMove(ArrayList<NimEdge> availableEdges, Ground ground, int[] heap, boolean isRegularGame) {
        return null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
