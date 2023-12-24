package edu.upc.epsevg.prop.checkers.players;

/**
 * Classe que representa la informació d'una partida, incloent detalls com el
 * moviment realitzat, el nombre de nivells per sota, el valor heurístic i el
 * tipus de jugador.
 *
 * Aquesta classe proporciona una estructura per emmagatzemar informació
 * relativa a una partida, incloent detalls com el moviment realitzat, el nombre
 * de nivells per sota del joc, i altres aspectes relacionats amb la gestió de
 * la partida.
 *
 * @author Ernest Anguera
 * @author Naïm Barba
 * @version 1.0
 */
public class GameInfo {

    /**
     * Llista de punts que representa el moviment realitzat en la partida.
     */
    private int indexMoviment;

    /**
     * Nombre de nivells per sota del joc.
     */
    private int nivellsPerSota;

    /**
     * Constructor de la classe GameInfo.
     *
     * @param indexMoviment Index que indica la posició del millorMoviment
     * @param nivellsPerSota Nombre de nivells per sota del joc.
     */
    public GameInfo(int indexMoviment, int nivellsPerSota) {
        this.indexMoviment = indexMoviment;
        this.nivellsPerSota = nivellsPerSota;
    }

    /**
     * Obté l'índex del moviment realitzat.
     *
     * @return L'índex del moviment realitzat.
     */
    public int getIndexMoviment() {
        return indexMoviment;
    }

    /**
     * Estableix l'índex del moviment realitzat.
     *
     * @param indexMoviment L'índex del moviment realitzat.
     */
    public void setIndexMoviment(int indexMoviment) {
        this.indexMoviment = indexMoviment;
    }

    /**
     * Obté el nombre de nivells per sota del joc.
     *
     * @return El nombre de nivells per sota del joc.
     */
    public int getNivellsPerSota() {
        return nivellsPerSota;
    }

    /**
     * Estableix el nombre de nivells per sota del joc.
     *
     * @param nivellsPerSota El nombre de nivells per sota del joc.
     */
    public void setNivellsPerSota(int nivellsPerSota) {
        this.nivellsPerSota = nivellsPerSota;
    }
}
