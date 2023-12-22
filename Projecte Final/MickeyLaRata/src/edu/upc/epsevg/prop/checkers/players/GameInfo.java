/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.checkers.players;

import edu.upc.epsevg.prop.checkers.PlayerType;
import java.awt.Point;
import java.util.List;

/**
 * Classe que representa la informació d'una partida, incloent detalls com el
 * moviment realitzat, el nombre de nivells per sota, el valor heurístic i el
 * tipus de jugador.
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

    public int getIndexMoviment() {
        return indexMoviment;
    }

    public void setIndexMoviment(int indexMoviment) {
        this.indexMoviment = indexMoviment;
    }

    public int getNivellsPerSota() {
        return nivellsPerSota;
    }

    public void setNivellsPerSota(int nivellsPerSota) {
        this.nivellsPerSota = nivellsPerSota;
    }
}
