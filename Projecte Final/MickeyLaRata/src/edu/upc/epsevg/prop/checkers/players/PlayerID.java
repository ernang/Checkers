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
import java.util.List;
import java.util.Random;

/**
 * Estratègia de jugador automàtic que implementa l'algorisme MiniMax Iterative
 * Deepening Search per a les dames. El jugador busca la millor jugada possible
 * tenint en compte una heurística específica. Les opcions de moviment es
 * generen fins a una determinada profunditat de l'arbre de cerca. Es pot
 * especificar la profunditat en el constructor.
 *
 * @author Ernest Anguera Aixalà
 * @author Naïm Barba Morilla
 *
 * @version 1.0
 */
public class PlayerID implements IPlayer, IAuto {

    private String name = "Mickey La Rata";
    private PlayerType jugadorMaxim;
    private PlayerType jugadorMinim;
    private int profunditat = 0;
    private int nodesExplorats = 0;
    private boolean timeout = false;
    private int profMax = 64;

    private class Pair {

        List<Point> llista;
        int value;

        public Pair(List<Point> mov, int h) {
            this.llista = mov;
            this.value = h;
        }

        private Pair() {
            this.llista = new ArrayList<>();
            this.value = -20000;
        }

        public List<Point> getLlista() {
            return llista;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Constructor de la classe MickeyLaRata.
     *
     */
    public PlayerID() {
    }

    @Override
    public void timeout() {
        timeout = true;
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
        jugadorMaxim = s.getCurrentPlayer();
        jugadorMinim = PlayerType.opposite(jugadorMaxim);
        timeout = false;
        List<Point> ll = ids(s);
        return new PlayerMove(ll, nodesExplorats, profunditat, SearchType.MINIMAX);
    }

    private List<Point> ids(GameStatus s) {
        Pair millorObtingut = new Pair();
        int depth = 1;
        nodesExplorats = 0;
        while (!timeout && depth < profMax) {
            Pair moviment = miniMax(s, depth);
            if (moviment.getValue() > millorObtingut.getValue()) {
                millorObtingut = moviment;
            }
            depth++;
        }
        return millorObtingut.getLlista();
    }

    /**
     * Implementació de l'algorisme MiniMax per determinar el millor moviment
     * possible.
     *
     * @param s L'estat actual del joc.
     * @return Una llista de punts que representa el millor moviment.
     */
    private Pair miniMax(GameStatus s, int depth) {
        int heuristicaActual = -20000, alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
        List<MoveNode> llistaMoviments = s.getMoves();
        List<List<Point>> moviments = obtenirMoviments(llistaMoviments);
        Collections.sort(moviments, (list1, list2) -> Integer.compare(list1.size(), list2.size()));
        List<Point> points = new ArrayList<>();
        for (List<Point> moviment : moviments) {
            if (timeout) {
                break;
            }
            GameStatus aux = new GameStatus(s);
            aux.movePiece(moviment);

            int valorHeuristic = minValor(aux, depth - 1, alpha, beta);

            if (valorHeuristic > heuristicaActual) {
                heuristicaActual = valorHeuristic;
                points = moviment;
            }
            alpha = Math.max(alpha, heuristicaActual);
        }

        return new Pair(points, heuristicaActual);
    }

    private int minValor(GameStatus s, int depth, int alpha, int beta) {
        int valorHeuristic = 10000;
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
        List<MoveNode> moviments = s.getMoves();
        List<List<Point>> camins = obtenirMoviments(moviments);
        Collections.sort(camins, (list1, list2) -> Integer.compare(list1.size(), list2.size()));
        for (List<Point> cami : camins) {
            if (timeout) {
                break;
            }
            GameStatus aux = new GameStatus(s);
            aux.movePiece(cami);
            int heuristicaActual = maxValor(aux, depth - 1, alpha, beta);
            valorHeuristic = Math.min(valorHeuristic, heuristicaActual);
            beta = Math.min(valorHeuristic, beta);

            if (alpha >= beta) {
                break;
            }
        }

        return valorHeuristic;
    }

    private int maxValor(GameStatus s, int depth, int alpha, int beta) {
        int valorHeuristic = -10000;
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

        List<MoveNode> moviments = s.getMoves();
        List<List<Point>> camins = obtenirMoviments(moviments);
        Collections.sort(camins, (list1, list2) -> Integer.compare(list1.size(), list2.size()));
        for (List<Point> cami : camins) {
            if (timeout) {
                break;
            }
            GameStatus aux = new GameStatus(s);
            aux.movePiece(cami);

            int heuristicaActual = minValor(aux, depth - 1, alpha, beta);
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
            if (timeout) {
                break;
            }
            List<Point> cami = new ArrayList<>();
            cami.add(moviment.getPoint());
            obtenirMovimentsAuxiliars(moviment, cami, resultats);
        }

        return resultats;
    }

    private void obtenirMovimentsAuxiliars(MoveNode moviment, List<Point> cami, List<List<Point>> resultats) {
        List<MoveNode> següentsMoviments = moviment.getChildren();
        if (timeout) {
            return;
        }
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

    private int evaluarEstat(GameStatus s) {
        nodesExplorats++;
        if (timeout) {
            return 0;
        }
        return evaluarEstatAux(s, jugadorMaxim) - evaluarEstatAux(s, jugadorMinim);
    }

    private int evaluarEstatAux(GameStatus s, PlayerType jugador) {
        int heuristica = 0;
        int backRowPieces = 0;
        int middleBoxPieces = 0;
        int middleRowPieces = 0;
        if (timeout) {
            return 0;
        }

        for (int i = 0; i < s.getSize(); ++i) {
            for (int j = 0; j < s.getSize(); ++j) {
                Point p = new Point(i, j);
                CellType casella = s.getPos(i, j);
                MoveNode n = s.getMoves(p, jugador);
                if (casella == (jugador == PlayerType.PLAYER1 ? CellType.P1 : CellType.P2)) {
                    if (casella.isQueen()) {
                        heuristica += 7;
                    } else {
                        heuristica += 5;
                    }
                    // Cuenta las piezas en la última fila
                    if (i == 0 || i == s.getSize() - 1) {
                        backRowPieces++;
                    }
                    // Cuenta las piezas en las 4 columnas del medio de las 2 filas del medio
                    if ((i >= s.getSize() / 2 - 1 && i <= s.getSize() / 2) && (j >= s.getSize() / 2 - 2 && j <= s.getSize() / 2 + 1)) {
                        middleBoxPieces++;
                    } // Cuenta las piezas en las 2 filas del medio pero no en las 4 columnas del medio
                    else if (i >= s.getSize() / 2 - 1 && i <= s.getSize() / 2) {
                        middleRowPieces++;
                    }
                }

            }
        }

        // Ajusta la heurística según tus necesidades
        heuristica += backRowPieces * 4;
        heuristica += middleBoxPieces * 3;
        heuristica += middleRowPieces * 1;
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
