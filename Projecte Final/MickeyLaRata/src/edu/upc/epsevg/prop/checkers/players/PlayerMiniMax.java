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
        List<Point> moviment = miniMax(s);
        return new PlayerMove(moviment, nodesExplorats, profunditat, SearchType.MINIMAX);
    }

    /**
     * Implementació de l'algorisme MiniMax per determinar el millor moviment
     * possible.
     *
     * @param s L'estat actual del joc.
     * @return Una llista de punts que representa el millor moviment.
     */
    private List<Point> miniMax(GameStatus s) {
        // Inicialització de les variables
        int heuristicaActual = -20000;  // Valor inicial de la heurística
        int alpha = Integer.MIN_VALUE;   // Valor inicial d'alpha
        int beta = Integer.MAX_VALUE;    // Valor inicial de beta

        // Obtenir la llista de moviments convertits a punts
        List<List<Point>> moviments = obtenirMoviments(s.getMoves());

        // Llista que emmagatzemarà el millor moviment trobat
        List<Point> points = new ArrayList<>();

        // Iterar a través dels moviments
        for (List<Point> moviment : moviments) {
            // Crear una còpia de l'estat del joc per simular el moviment
            GameStatus aux = new GameStatus(s);
            aux.movePiece(moviment);

            // Calcular el valor heurístic del moviment
            int valorHeuristic = minValor(aux, profunditat - 1, alpha, beta);

            // Actualitzar el millor moviment si es troba un valor heurístic millor
            if (valorHeuristic > heuristicaActual) {
                heuristicaActual = valorHeuristic;
                points = moviment;
            }

            // Actualitzar el valor d'alpha
            alpha = Math.max(alpha, heuristicaActual);
        }

        // Retornar el millor moviment trobat
        return points;
    }

    private int minValor(GameStatus s, int depth, int alpha, int beta) {
        // Valor heurístic inicial per a la funció min (MAX_INT)
        int valorHeuristic = 10000;

        // Comprovar si la partida ha acabat
        if (s.isGameOver()) {
            // Si el guanyador és el jugador màxim, retornar valor heurístic alt
            if (s.GetWinner() == jugadorMaxim) {
                return valorHeuristic;
            }
            // Si no hi ha guanyador, retornar 0 (empat)
            if (s.GetWinner() == null) {
                return 0;
            }
        }

        // Comprovar la profunditat màxima i retornar l'avaluació de l'estat actual
        if (depth == 0) {
            return evaluarEstat(s);
        }

        // Obtindre la llista de moviments convertits a punts
        List<List<Point>> camins = obtenirMoviments(s.getMoves());

        // Iterar a través dels camins possibles
        for (List<Point> cami : camins) {
            // Crear una còpia de l'estat del joc per simular el moviment
            GameStatus aux = new GameStatus(s);
            aux.movePiece(cami);

            // Calcular el valor heurístic per al jugador màxim
            int heuristicaActual = maxValor(aux, depth - 1, alpha, beta);

            // Actualitzar el valor heurístic mínim
            valorHeuristic = Math.min(valorHeuristic, heuristicaActual);
            // Actualitzar beta amb el valor heurístic mínim
            beta = Math.min(valorHeuristic, beta);

            // Realitzar la poda alpha-beta
            if (alpha >= beta) {
                break;
            }
        }

        // Retornar el valor heurístic mínim trobat
        return valorHeuristic;
    }

    private int maxValor(GameStatus s, int depth, int alpha, int beta) {
        // Valor heurístic inicial per a la funció max (MIN_INT)
        int valorHeuristic = -10000;

        // Comprovar si la partida ha acabat
        if (s.isGameOver()) {
            // Si el guanyador és el jugador mínim, retornar valor heurístic baix
            if (s.GetWinner() == jugadorMinim) {
                return valorHeuristic;
            }
            // Si no hi ha guanyador, retornar 0 (empat)
            if (s.GetWinner() == null) {
                return 0;
            }
        }

        // Comprovar la profunditat màxima i retornar l'avaluació de l'estat actual
        if (depth == 0) {
            return evaluarEstat(s);
        }

        // Obtindre la llista de moviments convertits a punts
        List<List<Point>> camins = obtenirMoviments(s.getMoves());

        // Iterar a través dels camins possibles
        for (List<Point> cami : camins) {
            // Crear una còpia de l'estat del joc per simular el moviment
            GameStatus aux = new GameStatus(s);
            aux.movePiece(cami);

            // Calcular el valor heurístic per al jugador mínim
            int heuristicaActual = minValor(aux, depth - 1, alpha, beta);

            // Actualitzar el valor heurístic màxim
            valorHeuristic = Math.max(valorHeuristic, heuristicaActual);
            // Actualitzar alpha amb el valor heurístic màxim
            alpha = Math.max(valorHeuristic, alpha);

            // Realitzar la poda alpha-beta
            if (alpha >= beta) {
                break;
            }
        }

        // Retornar el valor heurístic màxim trobat
        return valorHeuristic;
    }

    private List<List<Point>> obtenirMoviments(List<MoveNode> moviments) {
        // Llista que emmagatzemarà els camins resultants
        List<List<Point>> resultats = new ArrayList<>();

        // Iterar a través dels moviments
        for (MoveNode moviment : moviments) {
            // Crear un nou camí que comença amb el punt del moviment actual
            List<Point> cami = new ArrayList<>();
            cami.add(moviment.getPoint());
            // Cridar a la funció auxiliar per obtenir els altres punts del camí
            obtenirMovimentsAuxiliars(moviment, cami, resultats);
        }

        // Retornar els camins resultants
        resultats.sort(Comparator.comparingInt((List<Point> points) -> points.size()).reversed());
        return resultats;
    }

    private void obtenirMovimentsAuxiliars(MoveNode moviment, List<Point> cami, List<List<Point>> resultats) {
        List<MoveNode> fills = moviment.getChildren();
        if (fills.isEmpty()) {
            // Afegir el camí al resultat si no hi ha més moviments possibles
            resultats.add(new ArrayList<>(cami));
            return;
        }

        for (MoveNode fill : fills) {
            cami.add(fill.getPoint());
            obtenirMovimentsAuxiliars(fill, cami, resultats);
            cami.remove(cami.size() - 1);  // Desfer l'últim moviment per explorar altres opcions
        }
    }

    private int evaluarEstat(GameStatus s) {
        nodesExplorats++;
        return evaluarEstatAux(s, jugadorMaxim) - evaluarEstatAux(s, jugadorMinim);
    }

    private int evaluarEstatAux(GameStatus s, PlayerType jugador) {
        nodesExplorats += 1;
        int heuristica = 0;
        int pawnPieces = 0;
        int kingPieces = 0;
        int backRowPieces = 0;
        int middleBoxPieces = 0;
        int middleRowPieces = 0;
        int vulnerablePieces = 0;
        int safePieces = 0;
        int opponentPieces = 0;

        for (int i = 0; i < s.getSize(); ++i) {
            for (int j = 0; j < s.getSize(); ++j) {
                CellType casilla = s.getPos(i, j);
                if (casilla.getPlayer() == jugador) {
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
                    if (i > 0 && j > 0 && i < s.getSize() - 1 && j < s.getSize() - 1) {
                        PlayerType opponentPlayer = (jugador == PlayerType.PLAYER1) ? PlayerType.PLAYER2 : PlayerType.PLAYER1;

                        if ((s.getPos(i + 1, j - 1).getPlayer() == opponentPlayer && s.getPos(i - 1, j + 1) == CellType.EMPTY)
                                || (s.getPos(i + 1, j + 1).getPlayer() == opponentPlayer && s.getPos(i - 1, j - 1) == CellType.EMPTY)) {
                            vulnerablePieces++;
                        } else {
                            safePieces++;
                        }
                    }

                } else if (casilla.getPlayer() != jugador) {
                    opponentPieces++;
                }
            }
        }

        // Ajusta la heurística según tus necesidades
        heuristica += pawnPieces * 5; // Añade el bonus que quieras para las piezas regulares
        heuristica += kingPieces * 7; // Añade el bonus que quieras para las reinas
        if (opponentPieces <= 3) {
            heuristica -= vulnerablePieces * 10; // Resta el bonus que quieras para las piezas que pueden ser tomadas por el oponente en el próximo turno

        } else {
            heuristica += pawnPieces * 5; // Añade el bonus que quieras para las piezas regulares
            heuristica += kingPieces * 9; // Añade el bonus que quieras para las reinas
            heuristica += backRowPieces * 4; // Añade el bonus que quieras para las piezas en la última fila
            heuristica += safePieces * 3; // Añade el bonus que quieras para las piezas que no pueden ser tomadas hasta que las piezas detrás de ella (o ella misma) se muevan
            heuristica -= vulnerablePieces * 4; // Resta el bonus que quieras para las piezas que pueden ser tomadas por el oponente en el próximo turno
            heuristica += middleBoxPieces * 3; // Añade el bonus que quieras para las piezas en las 4 columnas del medio de las 2 filas del medio
            heuristica += middleRowPieces * 2; // Añade el bonus que quieras para las piezas en las 2 filas del medio pero no en las 4 columnas del medio

        }

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
