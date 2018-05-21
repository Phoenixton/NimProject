package main.IA;

import javafx.scene.paint.Color;
import main.entities.Edge;
import main.entities.Ellipsis;
import main.entities.Player;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by perri on 18/05/2018.
 */
public class GenericComputer extends Player{


    public GenericComputer(int id, Color color) {
        super(id, color);

    }

    public Edge playARandomMove(ArrayList<Edge> availableEdges) {
        Random rand = new Random();
        ArrayList<Edge> possibleMoves = new ArrayList<>();
        for(Edge e : availableEdges) {
            if(e.getBelongs() == 0 || e.getBelongs() == this.id) {
                possibleMoves.add(e);
            }
        }
        if(possibleMoves.size() == 0) {
            return null;
        }
        int randomNumber = rand.nextInt(possibleMoves.size());
        return possibleMoves.get(randomNumber);
    }
    public Ellipsis playARandomMoveEllipsis(ArrayList<Ellipsis> availableEllipsis) {
        Random rand = new Random();
        ArrayList<Ellipsis> possibleMoves = new ArrayList<>();
        for(Ellipsis e : availableEllipsis) {
            if(e.getBelongs() == 0 || e.getBelongs() == this.id) {
                possibleMoves.add(e);
            }
        }
        if(possibleMoves.size() == 0) {
            return null;
        }
        int randomNumber = rand.nextInt(possibleMoves.size());
        return possibleMoves.get(randomNumber);
    }
}
