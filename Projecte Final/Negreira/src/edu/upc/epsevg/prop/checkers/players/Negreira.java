package edu.upc.epsevg.prop.checkers.players;

import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.IAuto;
import edu.upc.epsevg.prop.checkers.IPlayer;
import edu.upc.epsevg.prop.checkers.MoveNode;
import edu.upc.epsevg.prop.checkers.PlayerMove;
import edu.upc.epsevg.prop.checkers.PlayerType;
import edu.upc.epsevg.prop.checkers.SearchType;
import java.awt.Point;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import java.util.Random;

/**
 * Jugador aleatori
 *
 * @author bernat
 */
public class Negreira implements IPlayer, IAuto {

    private String name;
    private GameStatus s;
    private PlayerType jugadorMaxim;
    private PlayerType jugadorMinim;
    private int profunditat = 4;
    private List<Point> millorJugada = new ArrayList<>();

    public Negreira(String name, int profunditat) {
        this.name = name;
        this.profunditat = profunditat;
        if (profunditat < 1) {
            throw new RuntimeException("La profunditat ha de ser més gran o igual a 1.");
        }
    }

    @Override
    public void timeout() {
        // Nothing to do! I'm so fast, I never timeout 8-)
    }

    /**
     * Decideix el moviment del jugador donat un tauler i un color de peça que
     * ha de posar.
     *
     * @param s Tauler i estat actual de joc.
     * @return el moviment que fa el jugador.
     */
    @Override
    public PlayerMove move(GameStatus s) {
        return new PlayerMove(miniMax(s), 0L, 0, SearchType.MINIMAX);

    }

    private List<Point> miniMax(GameStatus s) {
        int heuristicaActual = -20000;
        jugadorMaxim = s.getCurrentPlayer();
        jugadorMinim = PlayerType.opposite(s.getCurrentPlayer());
        int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
        List<MoveNode> moves = s.getMoves();
        List<Point> points = new ArrayList<>();
        if (moves.isEmpty()) {
            return points;
        }
        for (MoveNode moviment : moves) {
            GameStatus aux = new GameStatus(s);
            List<MoveNode> fillsMoviment = moviment.getChildren();
            for (MoveNode j : fillsMoviment) {
                points = llistaDelMoviment(j);
                points.addFirst(moviment.getPoint());
            }
            aux.movePiece(points);
            int valorHeuristic = minValor(aux, profunditat - 1, alpha, beta);
            if (valorHeuristic > heuristicaActual) {
                heuristicaActual = valorHeuristic;
                points = millorJugada;
            }
            alpha = Math.max(alpha, heuristicaActual);
        }
        return points;
    }

    private int maxValor(GameStatus s, int depth, int alpha, int beta) {
        int valorHeuristic = -10000;
        if (s.GetWinner() == jugadorMinim) {
            return valorHeuristic;
        }
        if (depth == 0 || !s.currentPlayerCanMove()) {
            return heuristica(s);
        }

        List<MoveNode> moviments = s.getMoves();
        for (MoveNode moviment : moviments) {

            GameStatus aux = new GameStatus(s);
            List<MoveNode> fillsMoviment = moviment.getChildren();
            for (MoveNode fill : fillsMoviment) {
                millorJugada = llistaDelMoviment(fill);
                millorJugada.add(0, moviment.getPoint());
            }
            aux.movePiece(millorJugada);
            valorHeuristic = minValor(aux, depth - 1, alpha, beta);
            alpha = Math.max(valorHeuristic, alpha);
            if (alpha >= beta) {
                break;
            }
        }
        return valorHeuristic;
    }

    private int minValor(GameStatus s, int depth, int alpha, int beta) {
        int valorHeuristic = 10000;
        if (s.GetWinner() == jugadorMaxim) {
            return valorHeuristic;
        }
        if (depth == 0 || !s.currentPlayerCanMove()) {
            return heuristica(s);
        }

        List<MoveNode> moviments = s.getMoves();
        for (MoveNode moviment : moviments) {

            GameStatus aux = new GameStatus(s);
            List<MoveNode> fillsMoviment = moviment.getChildren();
            for (MoveNode fill : fillsMoviment) {
                millorJugada = llistaDelMoviment(fill);
                millorJugada.add(0, moviment.getPoint());
            }
            aux.movePiece(millorJugada);
            valorHeuristic = maxValor(aux, depth - 1, alpha, beta);
            beta = Math.min(valorHeuristic, beta);
            if (alpha >= beta) {
                break;
            }
        }
        return valorHeuristic;
    }

    private int heuristica(GameStatus s) {
        return 0;
    }

    private List<Point> llistaDelMoviment(MoveNode moviment) {
        List<Point> result = new ArrayList<>();
        result.add(moviment.getPoint()); // Add the starting point of the move

        if (moviment.getChildren().size() > 0) {
            // Recursively add points for each child move
            for (MoveNode child : moviment.getChildren()) {
                result.addAll(llistaDelMoviment(child));
            }
        }

        return result;
    }

    private int calcularValorHeuristic(List<MoveNode> arbre) {
        return 0;
    }

    /**
     * Ens avisa que hem de parar la cerca en curs perquè s'ha exhaurit el temps
     * de joc.
     */
    @Override
    public String getName() {
        return name;
    }

}
