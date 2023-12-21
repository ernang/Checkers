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
    private List<Point> moviment;

    /**
     * Nombre de nivells per sota del joc.
     */
    private int nivellsPerSota;

    /**
     * Valor heurístic associat a la situació del joc.
     */
    private double heuristica;

    /**
     * Tipus de jugador associat a la informació de la partida.
     */
    private PlayerType jugador;

    /**
     * Constructor de la classe GameInfo.
     *
     * @param moviment Llista de punts que representa el moviment realitzat en
     * la partida.
     * @param nivellsPerSota Nombre de nivells per sota del joc.
     * @param heuristica Valor heurístic associat a la situació del joc.
     * @param jugador Tipus de jugador associat a la informació de la partida.
     */
    public GameInfo(List<Point> moviment, int nivellsPerSota, double heuristica, PlayerType jugador) {
        this.moviment = moviment;
        this.nivellsPerSota = nivellsPerSota;
        this.heuristica = heuristica;
        this.jugador = jugador;
    }

    /**
     * Obté el moviment realitzat en la partida.
     *
     * @return Llista de punts que representa el moviment realitzat en la
     * partida.
     */
    public List<Point> getMoviment() {
        return moviment;
    }

    /**
     * Estableix el moviment realitzat en la partida.
     *
     * @param moviment Llista de punts que representa el moviment realitzat en
     * la partida.
     */
    public void setMoviment(List<Point> moviment) {
        this.moviment = moviment;
    }

    /**
     * Obté el nombre de nivells per sota del joc.
     *
     * @return Nombre de nivells per sota del joc.
     */
    public int getNivellsPerSota() {
        return nivellsPerSota;
    }

    /**
     * Estableix el nombre de nivells per sota del joc.
     *
     * @param nivellsPerSota Nombre de nivells per sota del joc.
     */
    public void setNivellsPerSota(int nivellsPerSota) {
        this.nivellsPerSota = nivellsPerSota;
    }

    /**
     * Obté el valor heurístic associat a la situació del joc.
     *
     * @return Valor heurístic associat a la situació del joc.
     */
    public double getHeuristica() {
        return heuristica;
    }

    /**
     * Estableix el valor heurístic associat a la situació del joc.
     *
     * @param heuristica Valor heurístic associat a la situació del joc.
     */
    public void setHeuristica(double heuristica) {
        this.heuristica = heuristica;
    }

    /**
     * Obté el tipus de jugador associat a la informació de la partida.
     *
     * @return Tipus de jugador associat a la informació de la partida.
     */
    public PlayerType getJugador() {
        return jugador;
    }

    /**
     * Estableix el tipus de jugador associat a la informació de la partida.
     *
     * @param jugador Tipus de jugador associat a la informació de la partida.
     */
    public void setJugador(PlayerType jugador) {
        this.jugador = jugador;
    }

}
