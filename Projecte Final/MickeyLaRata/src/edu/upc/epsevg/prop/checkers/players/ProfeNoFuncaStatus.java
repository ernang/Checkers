package edu.upc.epsevg.prop.checkers.players;

import edu.upc.epsevg.prop.checkers.CellType;
import static edu.upc.epsevg.prop.checkers.CellType.P1;
import static edu.upc.epsevg.prop.checkers.CellType.P1Q;
import static edu.upc.epsevg.prop.checkers.CellType.P2;
import static edu.upc.epsevg.prop.checkers.CellType.P2Q;
import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.PlayerType;
import java.awt.Point;
import java.util.List;
import java.util.Random;

/**
 * ProfeNoFuncaStatus amplia la funcionalitat de GameStatus incorporant la
 * funció de hash Zobrist per calcular eficientment valors de hash únics per
 * estats de joc diferents.
 *
 * Aquesta classe utilitza una representació eficient del tauler de joc
 * mitjançant la funció de hash Zobrist per optimitzar la comparació d'estats de
 * joc i millorar l'eficiència en la gestió de col·lisions en estructures de
 * dades com ara taules de hash.
 *
 * @author Ernest Anguera
 * @author Naïm Barba
 */
public class ProfeNoFuncaStatus extends GameStatus {

    /**
     * Valors de hash Zobrist per a diferents peces i les seves posicions al
     * tauler de joc.
     */
    private static int[][][] zobrist;
    private static int black_to_move;
    private int hash;
    private boolean hash_updated = false;

    /**
     * Inicialitza els valors de hash Zobrist i la variable que indica el torn
     * del jugador.
     */
    static {
        zobrist = new int[8][8][4];
        Random rand = new Random();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 4; k++) {
                    zobrist[i][j][k] = rand.nextInt();
                }
            }
        }
        black_to_move = rand.nextInt();
    }

    /**
     * Construeix un objecte ProfeNoFuncaStatus basat en el tauler de joc donat.
     *
     * @param tauler El tauler de joc representat com una matriu 2D d'enters.
     */
    public ProfeNoFuncaStatus(int[][] tauler) {
        super(tauler);
    }

    /**
     * Construeix un objecte ProfeNoFuncaStatus basat en un objecte GameStatus
     * existent.
     *
     * @param gs L'objecte GameStatus a utilitzar com a base per a la nova
     * instància.
     */
    public ProfeNoFuncaStatus(GameStatus gs) {
        super(gs);
    }

    /**
     * Calcula el valor de hash Zobrist per a l'estat actual del tauler de joc.
     *
     * @return El valor de hash Zobrist per a l'estat actual del tauler de joc.
     */
    @Override
    public int hashCode() {
        if (hash_updated) {
            return hash;
        }
        hash = 0;
        if (this.getCurrentPlayer() == PlayerType.PLAYER1) {
            hash ^= black_to_move;
        }
        for (int i = 0; i < this.getSize(); ++i) {
            for (int j = 0; j < this.getSize(); ++j) {
                CellType casella = this.getPos(i, j);
                switch (casella) {
                    case P1 ->
                        hash ^= zobrist[i][j][0];
                    case P2 ->
                        hash ^= zobrist[i][j][1];
                    case P1Q ->
                        hash ^= zobrist[i][j][2];
                    case P2Q ->
                        hash ^= zobrist[i][j][3];
                    default -> {
                        // No cal fer res per a les cel·les buides
                    }
                }
            }
        }
        hash_updated = true;
        return hash;
    }

    /**
     * Compara aquest objecte ProfeNoFuncaStatus amb un altre objecte per
     * determinar si són iguals.
     *
     * @param obj L'objecte a comparar amb aquest ProfeNoFuncaStatus.
     * @return Cert si els objectes són iguals, fals altrament.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProfeNoFuncaStatus other = (ProfeNoFuncaStatus) obj;
        return this.hashCode() == other.hashCode();
    }

    /**
     * Mou les peces del joc segons la llista de punts proporcionada i
     * actualitza el valor de hash per reflectir els canvis.
     *
     * @param list La llista de punts que descriu el moviment de les peces.
     */
    @Override
    public void movePiece(List<Point> list) {
        super.movePiece(list);
        hash_updated = false;
    }

}
