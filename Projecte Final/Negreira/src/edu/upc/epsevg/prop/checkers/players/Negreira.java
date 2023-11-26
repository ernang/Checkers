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
        return new PlayerMove(ll, 0L, 0, SearchType.RANDOM);
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

        for (MoveNode moviment : llistaMoviments) {
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
                    System.out.println("Camí fill -> " + camiFill.size());
                    if (camiFill.size() > points_aux.size()) {
                        System.out.println("I'm larger than my father");
                        points_aux.addAll(camiFill);
                        if (moviment.isJump()) {
                            points_aux.add(0,moviment.getJumpedPoint());
                        } else {
                            points_aux.add(0,moviment.getPoint());
                        }
                    }
                }
            }
            if (points.size() < points_aux.size()) {
                points = points_aux;
            }
        }
        return points;
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

    /*private int heuristica(GameStatus s) {
        return 0;
    }

    private List<Point> llistaDelMoviment(PlayerType joga, MoveNode moviment) {
        List<Point> result = new ArrayList<>();
        result.add(moviment.getPoint()); // Add the starting point of the move

        if (!moviment.getChildren().isEmpty()) {
            // Recursively add points for each child move
            for (MoveNode child : moviment.getChildren()) {
                System.out.println("Moviment de" + joga.toString() + "->" + child.getPoint().toString());
            }
        }

        return result;
    }*/
    /**
     * Ens avisa que hem de parar la cerca en curs perquè s'ha exhaurit el temps
     * de joc.
     */
    @Override
    public String getName() {
        return name;
    }
}
