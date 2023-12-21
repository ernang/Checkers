/**
 * ProfeNoFuncaStatus és una subclasse de GameStatus que representa l'estat
 * d'un joc de dames amb funcionalitats addicionals relacionades amb la funció de hash Zobrist.
 */
package edu.upc.epsevg.prop.checkers.players;

import edu.upc.epsevg.prop.checkers.CellType;
import edu.upc.epsevg.prop.checkers.GameStatus;
import edu.upc.epsevg.prop.checkers.PlayerType;

/**
 * ProfeNoFuncaStatus amplia la funcionalitat de GameStatus incorporant la
 * funció de hash Zobrist per calcular eficientment valors de hash únics per
 * estats de joc diferents.
 *
 * @author Ernest Anguera
 * @author Naïm Barba
 */
public class ProfeNoFuncaStatus extends GameStatus {

    /**
     * Valors de hash Zobrist per a diferents peces i les seves posicions al
     * tauler de joc.
     */
    private long[][][] zobrist;
    private long black_to_move;

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
     * Estableix els valors de hash Zobrist per a la instància actual.
     *
     * @param zobrist Els valors de hash Zobrist per a diferents peces i les
     * seves posicions.
     */
    public void setZobrist(long[][][] zobrist) {
        this.zobrist = zobrist;
    }

    public void setBlack_to_move(long black_to_move) {
        this.black_to_move = black_to_move;
    }

    /**
     * Calcula el hash Zobrist per a l'estat de joc donat.
     *
     * @return El valor de hash Zobrist per a l'estat de joc donat.
     */
    public long getHash() {
        long hash = 0;
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
        return hash;
    }
}
