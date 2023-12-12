package edu.upc.epsevg.prop.checkers.players;

import edu.upc.epsevg.prop.checkers.CellType;
import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.IAuto;
import edu.upc.epsevg.prop.checkers.IPlayer;
import edu.upc.epsevg.prop.checkers.MoveNode;
import edu.upc.epsevg.prop.checkers.PlayerMove;
import edu.upc.epsevg.prop.checkers.PlayerType;
import edu.upc.epsevg.prop.checkers.SearchType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Estratègia de jugador automàtic que implementa l'algorisme MiniMax per a les
 * dames. El jugador busca la millor jugada possible tenint en compte una
 * heurística específica. Les opcions de moviment es generen fins a una
 * determinada profunditat de l'arbre de cerca. Es pot especificar la
 * profunditat en el constructor.
 *
 * @author Ernest Anguera Aixalà
 * @author Naïm Barba Morilla
 *
 * @version 1.0
 */
public class PlayerMiniMax implements IPlayer, IAuto {

    private String name = "Mickey La Rata";
    private PlayerType jugadorMaxim;
    private PlayerType jugadorMinim;
    private int profunditat = 4;
    private int nodesExplorats = 0;

    /**
     * Constructor de la classe MickeyLaRata.
     *
     * @param profunditat La profunditat màxima de l'arbre de cerca.
     * @throws RuntimeException Si la profunditat és menor que 1.
     */
    public PlayerMiniMax(int profunditat) {
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
     * @return El moviment que fa el jugador.
     */
    @Override
    public PlayerMove move(GameStatus s) {
        nodesExplorats = 0;
        jugadorMaxim = s.getCurrentPlayer();
        jugadorMinim = PlayerType.opposite(jugadorMaxim);
        List<Point> ll = miniMax(s);
        return new PlayerMove(ll, nodesExplorats, profunditat, SearchType.MINIMAX);
    }

    /**
     * Implementació de l'algorisme MiniMax per determinar el millor moviment
     * possible.
     *
     * @param s L'estat actual del joc.
     * @return Una llista de punts que representa el millor moviment.
     */
    private List<Point> miniMax(GameStatus s) {
        double heuristicaActual = -20000, alpha = Double.NEGATIVE_INFINITY, beta = Double.POSITIVE_INFINITY;

        List<List<Point>> camins = obtenirMoviments(s.getMoves());
        List<Point> points = new ArrayList<>();
        for (List<Point> cami : camins) {

            GameStatus aux = new GameStatus(s);
            aux.movePiece(cami);

            double valorHeuristic = minValor(aux, profunditat - 1, alpha, beta);

            if (valorHeuristic > heuristicaActual) {
                heuristicaActual = valorHeuristic;
                points = cami;
            }

            alpha = Math.max(alpha, heuristicaActual);
        }

        return points;
    }

    private double minValor(GameStatus s, int depth, double alpha, double beta) {
        double valorHeuristic = 10000;
        if (s.isGameOver()) {
            if (s.GetWinner() == jugadorMaxim) {
                return valorHeuristic;
            }
            if (s.GetWinner() == null) {
                return 0;
            }
        }

        if (depth == 0) {
            return evaluarEstat(s);
        }
        List<List<Point>> camins = obtenirMoviments(s.getMoves());
        for (List<Point> cami : camins) {
            GameStatus aux = new GameStatus(s);
            aux.movePiece(cami);

            double heuristicaActual = maxValor(aux, depth - 1, alpha, beta);
            valorHeuristic = Math.min(valorHeuristic, heuristicaActual);
            beta = Math.min(valorHeuristic, beta);

            if (alpha >= beta) {
                break;
            }
        }

        return valorHeuristic;
    }

    private double maxValor(GameStatus s, int depth, double alpha, double beta) {
        double valorHeuristic = -10000;

        if (s.isGameOver()) {
            if (s.GetWinner() == jugadorMinim) {
                return valorHeuristic;
            }
            if (s.GetWinner() == null) {
                return 0;
            }
        }

        if (depth == 0) {
            return evaluarEstat(s);
        }

        List<List<Point>> camins = obtenirMoviments(s.getMoves());
        for (List<Point> cami : camins) {
            GameStatus aux = new GameStatus(s);
            aux.movePiece(cami);

            double heuristicaActual = minValor(aux, depth - 1, alpha, beta);
            valorHeuristic = Math.max(valorHeuristic, heuristicaActual);
            alpha = Math.max(valorHeuristic, alpha);

            if (alpha >= beta) {
                break;
            }
        }
        return valorHeuristic;
    }

    private List<List<Point>> obtenirMoviments(List<MoveNode> moviments) {
        List<List<Point>> resultats = new ArrayList<>();

        for (MoveNode moviment : moviments) {
            List<Point> cami = new ArrayList<>();
            cami.add(moviment.getPoint());
            obtenirMovimentsAuxiliars(moviment, cami, resultats);
        }
        resultats.sort(Comparator.comparingInt((List<Point> points) -> points.size()).reversed());
        return resultats;
    }

    private void obtenirMovimentsAuxiliars(MoveNode moviment, List<Point> cami, List<List<Point>> resultats) {
        List<MoveNode> següentsMoviments = moviment.getChildren();
        if (següentsMoviments.isEmpty()) {
            // Afegir el camí al resultat si no hi ha més moviments possibles
            resultats.add(new ArrayList<>(cami));
            return;
        }

        for (MoveNode següentMoviment : següentsMoviments) {
            cami.add(següentMoviment.getPoint());
            obtenirMovimentsAuxiliars(següentMoviment, cami, resultats);
            cami.remove(cami.size() - 1);  // Desfer l'últim moviment per explorar altres opcions
        }
    }

    private double evaluarEstat(GameStatus s) {
        nodesExplorats++;
        return evaluarEstatAux(s, jugadorMaxim) - evaluarEstatAux(s, jugadorMinim);
    }

    private double evaluarEstatAux(GameStatus s, PlayerType jugador) {
        double heuristica = 0;
        int pawnPieces = 0;
        int kingPieces = 0;
        int backRowPieces = 0;
        int middleBoxPieces = 0;
        int middleRowPieces = 0;
        int vulnerablePieces = 0;
        int safePieces = 0;
        int opponentPieces = 0;
        List<Point> friendpos = new ArrayList<>();
        List<Point> enemypos = new ArrayList<>();

        for (int i = 0; i < s.getSize(); ++i) {
            for (int j = 0; j < s.getSize(); ++j) {
                CellType casilla = s.getPos(i, j);
                if (casilla.getPlayer() == jugador) {
                    Point p0 = new Point(i, j);
                    friendpos.add(p0);
                    if (casilla.isQueen()) {
                        kingPieces++; // Cuenta las reinas
                    } else {
                        pawnPieces++; // Cuenta las piezas regulares
                    }
                    if ((jugador == PlayerType.PLAYER1 && i == 0) || (jugador == PlayerType.PLAYER2 && i == s.getSize() - 1)) {
                        backRowPieces++; // Cuenta las piezas en la última fila
                    }
                    if (i >= s.getSize() / 2 - 1 && i <= s.getSize() / 2 && j >= s.getSize() / 2 - 1 && j <= s.getSize() / 2) {
                        middleBoxPieces++; // Cuenta las piezas en las 4 columnas del medio de las 2 filas del medio
                    } else if (i >= s.getSize() / 2 - 1 && i <= s.getSize() / 2) {
                        middleRowPieces++; // Cuenta las piezas en las 2 filas del medio pero no en las 4 columnas del medio
                    }
                    // Cuenta las piezas que pueden ser tomadas por el oponente en el próximo turno
                    if (jugador == PlayerType.PLAYER1) {
                        if (i > 0 && j > 0 && i < s.getSize() - 1 && j < s.getSize() - 1) {
                            if (s.getPos(i + 1, j - 1).getPlayer() == PlayerType.PLAYER2 && s.getPos(i - 1, j + 1) == CellType.EMPTY) {
                                vulnerablePieces++;
                            } else if (s.getPos(i + 1, j + 1).getPlayer() == PlayerType.PLAYER2 && s.getPos(i - 1, j - 1) == CellType.EMPTY) {
                                vulnerablePieces++;
                            } else {
                                safePieces++;
                            }
                        }

                    } else {

                        if (i > 0 && j > 0 && i < s.getSize() - 1 && j < s.getSize() - 1) {
                            if (s.getPos(i - 1, j + 1).getPlayer() == PlayerType.PLAYER1 && s.getPos(i + 1, j - 1) == CellType.EMPTY) {
                                vulnerablePieces++;
                            } else if (s.getPos(i - 1, j - 1).getPlayer() == PlayerType.PLAYER1 && s.getPos(i + 1, j + 1) == CellType.EMPTY) {
                                vulnerablePieces++;
                            } else {
                                safePieces++;
                            }

                        }

                    }
                } else if (casilla.getPlayer() != jugador) {
                    opponentPieces++;
                    Point p = new Point(i, j);
                    enemypos.add(p);
                }
            }
        }

        // Ajusta la heurística según tus necesidades
        if (opponentPieces <= 10) {
            double proximityToEnemies = 0;
            Point enemyPos = enemypos.get(0);
            for (Point friendPos : friendpos) {

                int distance = Math.abs(friendPos.x - enemyPos.x) + Math.abs(friendPos.y - enemyPos.y);
                proximityToEnemies += 1.0 / (distance + 1); // Añade un bonus inversamente proporcional a la distancia

            }

            heuristica += proximityToEnemies * 10; // Añade el bonus que quieras para la proximidad a las piezas enemigas
        }
        heuristica += pawnPieces * 5; // Añade el bonus que quieras para las piezas regulares
        heuristica += kingPieces * 7.75; // Añade el bonus que quieras para las reinas
        heuristica += backRowPieces * 4; // Añade el bonus que quieras para las piezas en la última fila
        heuristica += safePieces * 3; // Añade el bonus que quieras para las piezas que no pueden ser tomadas hasta que las piezas detrás de ella (o ella misma) se muevan
        heuristica += vulnerablePieces * -3; // Resta el bonus que quieras para las piezas que pueden ser tomadas por el oponente en el próximo turno
        heuristica += middleBoxPieces * 2.5; // Añade el bonus que quieras para las piezas en las 4 columnas del medio de las 2 filas del medio
        heuristica += middleRowPieces * 0.5; // Añade el bonus que quieras para las piezas en las 2 filas del medio pero no en las 4 columnas del medio

        return heuristica;
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
