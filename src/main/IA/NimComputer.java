package main.IA;

import javafx.scene.paint.Color;
import main.Nim;
import main.entities.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by perri on 18/05/2018.
 */
public class NimComputer extends Player {

    private String strategy;

    public NimComputer(int id, Color color, String strategy) {
        super(id, color);
        this.strategy = strategy;

    }
    public NimEdge playStratMove(ArrayList<NimEdge> availableEdges, Ground ground, int[] heap, boolean isRegularGame) {
        Nim nim = new Nim(isRegularGame);
        if(this.strategy.equals("Winning")) {
            Nim.Move newMove = nim.winningMove(heap);
            if(newMove != Nim.Move.EMPTY) {

                Node origin = ground.getNodesOnTheGround().get(newMove.getIndex());
                ArrayList<Node> alreadyVisited = new ArrayList<>();
                int size = newMove.getSize();
                return getCorrespondingEdge(origin, availableEdges,  alreadyVisited, size);
            } else {
                return playARandomNimMove(availableEdges);
            }
        } else if(this.strategy.equals("Random")) {
            return playARandomNimMove(availableEdges);
        } else if(this.strategy.equals("Losing")) {

        }
        return null;
    }

    public NimEdge getCorrespondingEdge(Node n, ArrayList<NimEdge> edges, ArrayList<Node> alreadyVisited, int size) {

        NimEdge toReturn = null;
        if(size == 0) {
            for(NimEdge e : edges) {
                if(e.getFirstNode() == n) {
                    toReturn = e;
                } else if(e.getSecondNode() == n) {
                    toReturn = e;
                }
            }
            return toReturn;
        }
        size--;
        for(NimEdge e : edges) {
            if(e.getFirstNode() == n && !alreadyVisited.contains(e.getSecondNode())) {
                toReturn = getCorrespondingEdge(e.getSecondNode(), edges, alreadyVisited, size);
            }
            if(e.getSecondNode() == n && !alreadyVisited.contains(e.getFirstNode())) {
                toReturn = getCorrespondingEdge(e.getSecondNode(), edges, alreadyVisited, size);
            }
        }
        return toReturn;
    }

    public NimEdge playARandomNimMove(ArrayList<NimEdge> availableEdges) {
        Random rand = new Random();
        //ArrayList<Edge> possibleMoves = new ArrayList<>();
        /*for(NimEdge e : availableEdges) {
            if(e.getBelongs() == 0 || e.getBelongs() == this.id) {
                possibleMoves.add(e);
            }
        }*/
        if(availableEdges.size() <= 0) {
            return null;
        }
        int randomNumber = rand.nextInt(availableEdges.size());
        return availableEdges.get(randomNumber);
    }
}
