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
import java.util.List;
import java.util.Random;

/**
 * La classe Negreira implementa un jugador que utilitza l'algorisme MiniMax per
 * decidir el seu pròxim moviment en el joc Checkers. Aquesta classe implementa
 * les interfícies IPlayer i IAuto.
 *
 * @author Ernest
 * @author Naïm
 */
public class Negreira implements IPlayer, IAuto {

    private String name;
    private PlayerType jugadorMaxim;
    private PlayerType jugadorMinim;
    private int profunditat = 4;
    private List<Point> millorJugada = new ArrayList<>();
    private int calls = 0;

    /**
     * Constructor de la classe Negreira.
     *
     * @param name El nom del jugador.
     * @param profunditat La profunditat màxima de l'arbre de cerca.
     */
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
        List<Point> ll = miniMax(s);
        for (Point point : ll) {
            System.out.println("Punt -> " + point.toString());
        }
        System.out.println("----------------------------");
        return new PlayerMove(ll, calls, calls, SearchType.MINIMAX);
    }

    /**
     * Implementació de l'algorisme MiniMax.
     *
     * @param s L'estat actual del joc.
     * @return Una llista de punts que representa el millor moviment.
     */
    private List<Point> miniMax(GameStatus s) {
        List<MoveNode> llistaMoviments = s.getMoves();
        List<Point> points = new ArrayList<>();
        int heuristicaActual = -20000, alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;

        for (MoveNode moviment : llistaMoviments) {
            List<Point> points_aux = obtenirPuntsAuxiliars(moviment);

            GameStatus aux = new GameStatus(s);
            aux.movePiece(points_aux);

            int valorHeuristic = minValor(aux, profunditat - 1, alpha, beta);

            if (valorHeuristic > heuristicaActual) {
                heuristicaActual = valorHeuristic;
            }

            if (points.size() < points_aux.size()) {
                points = points_aux;
            }

            alpha = Math.max(alpha, heuristicaActual);
        }

        return points;
    }

    private int minValor(GameStatus s, int depth, int alpha, int beta) {
        calls++;
        int valorHeuristic = 10000;
        if (s.isGameOver() && s.GetWinner() == jugadorMaxim) {
            return valorHeuristic;
        }

        if (depth == 0 || !s.currentPlayerCanMove()) {
            return evaluarEstat(s);
        }
        List<MoveNode> moviments = s.getMoves();

        for (MoveNode moviment : moviments) {
            List<Point> points_aux = obtenirPuntsAuxiliars(moviment);
            System.out.println("Mida points_aux: " + points_aux.size());
            for (Point punt : points_aux) {
                System.out.println(punt.x + "," + punt.y);
            }
            GameStatus aux = new GameStatus(s);
            aux.movePiece(points_aux);

            valorHeuristic = maxValor(aux, depth - 1, alpha, beta);
            beta = Math.min(valorHeuristic, beta);

            if (alpha >= beta) {
                break;
            }
        }

        return valorHeuristic;
    }

    private int maxValor(GameStatus s, int depth, int alpha, int beta) {
        int valorHeuristic = -10000;

        if (s.isGameOver() && s.GetWinner() == jugadorMinim) {
            return valorHeuristic;
        }

        if (depth == 0 || !s.currentPlayerCanMove()) {
            return evaluarEstat(s);
        }

        List<MoveNode> moviments = s.getMoves();

        for (MoveNode moviment : moviments) {
            List<Point> points_aux = obtenirPuntsAuxiliars(moviment);

            GameStatus aux = new GameStatus(s);
            aux.movePiece(points_aux);

            valorHeuristic = minValor(aux, depth - 1, alpha, beta);
            alpha = Math.max(valorHeuristic, alpha);

            if (alpha >= beta) {
                break;
            }
        }

        return valorHeuristic;
    }

    private List<Point> obtenirPuntsAuxiliars(MoveNode moviment) {
        List<Point> points_aux = new ArrayList<>();

        if (moviment.isJump()) {
            points_aux.add(moviment.getJumpedPoint());
        } else {
            points_aux.add(moviment.getPoint());
        }

        for (MoveNode fill : moviment.getChildren()) {
            List<Point> camiFill = obtenirCamiMesLlarg(fill);

            if (fill.getChildren().isEmpty()) {
                points_aux.add(fill.getPoint());
            } else {
                if (camiFill.size() > points_aux.size()) {
                    points_aux.addAll(camiFill);

                    if (moviment.isJump()) {
                        points_aux.add(0, moviment.getJumpedPoint());
                    } else {
                        points_aux.add(0, moviment.getPoint());
                    }
                }
            }
        }

        return points_aux;
    }

    /**
     * Busca el camí més profund de l'arbre de moviments.
     *
     * @param moviment El node de moviment actual.
     * @return Una llista de punts que representa el camí més llarg.
     */
    private List<Point> obtenirCamiMesLlarg(MoveNode moviment) {
        List<Point> camiMesLlarg = new ArrayList<>();
        if (moviment.isJump()) {
            camiMesLlarg.add(moviment.getJumpedPoint());
        } else {
            camiMesLlarg.add(moviment.getPoint());
        }

        List<MoveNode> fills = moviment.getChildren();
        if (!fills.isEmpty()) {
            for (MoveNode fill : fills) {
                List<Point> camiFill = obtenirCamiMesLlarg(fill);
                if (camiFill.size() > camiMesLlarg.size()) {
                    camiMesLlarg = camiFill;
                }
            }
        }
        return camiMesLlarg;
    }

    private int evaluarEstat(GameStatus s) {
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
