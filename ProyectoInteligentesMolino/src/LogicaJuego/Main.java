/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaJuego;

/**
 *
 * @author Ander
 */
public class Main {

    public static void main(String[] args) throws JuegoException {
        Juego molino = new Juego();
        molino.ponerFichaJugador(0, Ficha.JUGADOR_1);
        molino.getTableroDelJuego().imprimirTablero();

        JugadorIAMinimax minimax = new JugadorIAMinimax(Ficha.JUGADOR_2, molino.getTableroDelJuego().getNumeroFichasJugador(Ficha.JUGADOR_1), 3);
        int indice = minimax.getIndiceParaPonerFicha(molino.getTableroDelJuego());
        molino.ponerFichaJugador(indice, Ficha.JUGADOR_2);
        molino.getTableroDelJuego().imprimirTablero();
        molino.ponerFichaJugador(9, Ficha.JUGADOR_1);
        molino.getTableroDelJuego().imprimirTablero();
        indice = minimax.getIndiceParaPonerFicha(molino.getTableroDelJuego());
        molino.ponerFichaJugador(indice, Ficha.JUGADOR_2);
        molino.getTableroDelJuego().imprimirTablero();

    }
}
