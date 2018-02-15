/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaJuego;

import java.io.Serializable;

/**
 *
 * @author LENOVO
 */
public class Tablero implements Serializable {

    //constante de posiciones que tiene el tablero
    static public final int NUM_POSICIONES_DEL_TABLERO = 24;
    //constante todas las formas posibles de hacer molino
    static public final int NUM_COMBINACIONES_MOLINO = 16;
    //constante numero de posiciones necesarias para hacer molino
    static public final int NUM_POSICIONES_EN_CADA_MOLINO = 3;

    //arreglo que contiene todas las posicones
    //del tablero
    private Posicion[] posicionesDelTablero;
    //matriz que contiene todos las posibles 
    //formas de hacer molino
    private Posicion[][] combinacionesMolino;
    //numero de fichas del jugador 1
    private int numeroFichasJ1;
    //numero de fichas del jugador 2
    private int numeroFichasJ2;
    //numero de fichas colocadas en el tablero
    private int numeroPiezasColocadas;

    //constructor
    public Tablero() {
        //inicializo el arreglo que tiene las posiciones
        posicionesDelTablero = new Posicion[Tablero.NUM_POSICIONES_DEL_TABLERO];
        //inicializo el numero de fichas
        //del jugador 1 en el tablero en 0
        numeroFichasJ1 = 0;
        //inicializo el numero de fichas en el tablero
        //del jugador 2 en 0
        numeroFichasJ2 = 0;
        //inicializamos la cantidad de fichas
        //puesta en el tablero en 0
        numeroPiezasColocadas = 0;
        //metodo con el que creo el tablero
        crearTablero();
        crearCombincionesMolino();
    }

    //metodo que devuelve una posicion del tablero
    public Posicion getPosicion(int indice) throws Exception {
        if (indice >= 0 && indice < Tablero.NUM_POSICIONES_DEL_TABLERO) {
            return posicionesDelTablero[indice];
        } else {
            throw new Exception("" + getClass().getName() + " - indice invalido:" + indice);
        }
    }

    //metodo que me indica si una posicion esta disponible para
    //poner una ficha
    public boolean posicionEstaDisponible(int indice) throws Exception {
        if (indice >= 0 && indice < Tablero.NUM_POSICIONES_DEL_TABLERO) {
            return !posicionesDelTablero[indice].estaOcupado();
        } else {
            throw new Exception("" + getClass().getName() + " - indice invalido:" + indice);
        }
    }

    //metodo con el cual pongo una ficha en una posicion de un jugador
    public void setPosicionDeJugador(int indice, JugadorC jugador) throws Exception {
        if (indice >= 0 && indice < Tablero.NUM_POSICIONES_DEL_TABLERO) {
            if (jugador == JugadorC.AGENTE1 || jugador == JugadorC.AGENTE2) {
                this.posicionesDelTablero[indice].setJugadorOcupando(jugador);
            } else {
                throw new Exception("" + getClass().getName() + " - ficha invalida: " + jugador);
            }
        } else {
            throw new Exception("" + getClass().getName() + " - indice invalido:" + indice);
        }
    }

    //metodo que incrementa las fichas colocadas en el tablero
    public int incremetarPiezasColocadasTablero() {
        return ++this.numeroPiezasColocadas;
    }

    //metodo que incremeta el numero de fichas de un jugador en el tablero
    //y ademas retorna este numero
    public int incremetarNumeroFichasJugador(JugadorC jugador) throws Exception {
        if (jugador == JugadorC.AGENTE1) {
            return ++this.numeroFichasJ1;
        } else if (jugador == JugadorC.AGENTE2) {
            return ++this.numeroFichasJ2;
        } else {
            throw new Exception("" + getClass().getName() + " - Ficha de juegador invalida: " + jugador);
        }
    }

    //metodo que decrementa el numero de fichas de un jugador en el 
    //tablero y retorna este numero
    public int decrementarNumeroFichasJugador(JugadorC jugador) throws Exception {
        if (jugador == JugadorC.AGENTE1) {
            return --this.numeroFichasJ1;
        } else if (jugador == JugadorC.AGENTE2) {
            return --this.numeroFichasJ2;
        } else {
            throw new Exception("" + getClass().getName() + " - Ficha de juegador invalida: " + jugador);
        }
    }

    //metodo que devuelve el numero de fichas de un jugador en el tablero
    public int getNumeroFichasJugador(JugadorC jugador) throws Exception {
        if (jugador == JugadorC.AGENTE1) {
            return this.numeroFichasJ1;
        } else if (jugador == JugadorC.AGENTE2) {
            return this.numeroFichasJ2;
        } else {
            throw new Exception("" + getClass().getName() + " - Ficha de juegador invalida: " + jugador);
        }
    }

    //metodo que me crea el tablero
    //organizando las  posiciones de forma
    //que quede como el tablero del molino
    private void crearTablero() {
        for (int i = 0; i < Tablero.NUM_POSICIONES_DEL_TABLERO; i++) {
            posicionesDelTablero[i] = new Posicion(i);
        }
        //cuadro exterior
        //la posicion 0 es la posicion de la esquina superio izquierda
        posicionesDelTablero[0].añadirIndecesPosicionesAdjacentes(1, 9);
        //la posicion 1 es la posicion del medio de la fila superior del cuadro exterior
        posicionesDelTablero[1].añadirIndecesPosicionesAdjacentes(0, 2, 4);
        //la posicion 2 es la posicion de la esquina superior derecha
        posicionesDelTablero[2].añadirIndecesPosicionesAdjacentes(1, 14);
        //la posicion 9 es la posicion del medio de la columna izquierda del cuadro exterior
        posicionesDelTablero[9].añadirIndecesPosicionesAdjacentes(0, 10, 21);
        //la posicion 14 es la posicion del medio de la columna derecha del cuadro exterior
        posicionesDelTablero[14].añadirIndecesPosicionesAdjacentes(2, 13, 23);
        //la posicion 21 es la posicion de la esquina inferior izquierda del cuadro exterior
        posicionesDelTablero[21].añadirIndecesPosicionesAdjacentes(9, 22);
        //la posicion 22 es la posicion del medio de la fila inferior del cuadro exterior
        posicionesDelTablero[22].añadirIndecesPosicionesAdjacentes(19, 21, 23);
        //la posicion 23 es la posicion de la esquina inferior derecha del cuadro exterior
        posicionesDelTablero[23].añadirIndecesPosicionesAdjacentes(14, 22);

        //cuadro del medio
        //la posicion 3 es la posicion de la esquina superior izquierda del cuadro del medio
        posicionesDelTablero[3].añadirIndecesPosicionesAdjacentes(4, 10);
        posicionesDelTablero[4].añadirIndecesPosicionesAdjacentes(1, 3, 5, 7);
        posicionesDelTablero[5].añadirIndecesPosicionesAdjacentes(4, 13);
        posicionesDelTablero[10].añadirIndecesPosicionesAdjacentes(3, 9, 11, 18);
        posicionesDelTablero[13].añadirIndecesPosicionesAdjacentes(5, 12, 14, 20);
        posicionesDelTablero[18].añadirIndecesPosicionesAdjacentes(10, 19);
        posicionesDelTablero[19].añadirIndecesPosicionesAdjacentes(16, 18, 20, 22);
        posicionesDelTablero[20].añadirIndecesPosicionesAdjacentes(13, 19);

        //cuadro interior
        posicionesDelTablero[6].añadirIndecesPosicionesAdjacentes(7, 11);
        posicionesDelTablero[7].añadirIndecesPosicionesAdjacentes(4, 6, 8);
        posicionesDelTablero[8].añadirIndecesPosicionesAdjacentes(7, 12);
        posicionesDelTablero[11].añadirIndecesPosicionesAdjacentes(6, 10, 15);
        posicionesDelTablero[12].añadirIndecesPosicionesAdjacentes(8, 13, 17);
        posicionesDelTablero[15].añadirIndecesPosicionesAdjacentes(11, 16);
        posicionesDelTablero[16].añadirIndecesPosicionesAdjacentes(15, 17, 19);
        posicionesDelTablero[17].añadirIndecesPosicionesAdjacentes(12, 16);

    }

    //metodo que deuelve una posicion del molino segun un indice
    public Posicion[] getCombinacionMolino(int indice) throws Exception {
        if (indice >= 0 && indice < Tablero.NUM_COMBINACIONES_MOLINO) {
            return this.combinacionesMolino[indice];
        } else {
            throw new Exception("" + getClass().getName() + " - indice invalido:" + indice);
        }
    }

    //metodo que crea todas las posibes combinaciones del molino
    private void crearCombincionesMolino() {
        this.combinacionesMolino = new Posicion[Tablero.NUM_COMBINACIONES_MOLINO][Tablero.NUM_POSICIONES_EN_CADA_MOLINO];
        //cuador exterior
        combinacionesMolino[0][0] = posicionesDelTablero[0];
        combinacionesMolino[0][1] = posicionesDelTablero[1];
        combinacionesMolino[0][2] = posicionesDelTablero[2];
        combinacionesMolino[1][0] = posicionesDelTablero[0];
        combinacionesMolino[1][1] = posicionesDelTablero[9];
        combinacionesMolino[1][2] = posicionesDelTablero[21];
        combinacionesMolino[2][0] = posicionesDelTablero[2];
        combinacionesMolino[2][1] = posicionesDelTablero[14];
        combinacionesMolino[2][2] = posicionesDelTablero[23];
        combinacionesMolino[3][0] = posicionesDelTablero[21];
        combinacionesMolino[3][1] = posicionesDelTablero[22];
        combinacionesMolino[3][2] = posicionesDelTablero[23];
        //cuadro del medio
        combinacionesMolino[4][0] = posicionesDelTablero[3];
        combinacionesMolino[4][1] = posicionesDelTablero[4];
        combinacionesMolino[4][2] = posicionesDelTablero[5];
        combinacionesMolino[5][0] = posicionesDelTablero[3];
        combinacionesMolino[5][1] = posicionesDelTablero[10];
        combinacionesMolino[5][2] = posicionesDelTablero[18];
        combinacionesMolino[6][0] = posicionesDelTablero[5];
        combinacionesMolino[6][1] = posicionesDelTablero[13];
        combinacionesMolino[6][2] = posicionesDelTablero[20];
        combinacionesMolino[7][0] = posicionesDelTablero[18];
        combinacionesMolino[7][1] = posicionesDelTablero[19];
        combinacionesMolino[7][2] = posicionesDelTablero[20];
        //cuadro interior
        combinacionesMolino[8][0] = posicionesDelTablero[6];
        combinacionesMolino[8][1] = posicionesDelTablero[7];
        combinacionesMolino[8][2] = posicionesDelTablero[8];
        combinacionesMolino[9][0] = posicionesDelTablero[6];
        combinacionesMolino[9][1] = posicionesDelTablero[11];
        combinacionesMolino[9][2] = posicionesDelTablero[15];
        combinacionesMolino[10][0] = posicionesDelTablero[8];
        combinacionesMolino[10][1] = posicionesDelTablero[12];
        combinacionesMolino[10][2] = posicionesDelTablero[17];
        combinacionesMolino[11][0] = posicionesDelTablero[15];
        combinacionesMolino[11][1] = posicionesDelTablero[16];
        combinacionesMolino[11][2] = posicionesDelTablero[17];
        //otros
        combinacionesMolino[12][0] = posicionesDelTablero[1];
        combinacionesMolino[12][1] = posicionesDelTablero[4];
        combinacionesMolino[12][2] = posicionesDelTablero[7];
        combinacionesMolino[13][0] = posicionesDelTablero[9];
        combinacionesMolino[13][1] = posicionesDelTablero[10];
        combinacionesMolino[13][2] = posicionesDelTablero[11];
        combinacionesMolino[14][0] = posicionesDelTablero[12];
        combinacionesMolino[14][1] = posicionesDelTablero[13];
        combinacionesMolino[14][2] = posicionesDelTablero[14];
        combinacionesMolino[15][0] = posicionesDelTablero[16];
        combinacionesMolino[15][1] = posicionesDelTablero[19];
        combinacionesMolino[15][2] = posicionesDelTablero[22];
    }

    public void imprimirTablero() {
        System.out.println(mostrarPos(0) + " - - - - - " + mostrarPos(1) + " - - - - - " + mostrarPos(2));
        System.out.println("|           |           |");
        System.out.println("|     " + mostrarPos(3) + " - - " + mostrarPos(4) + " - - " + mostrarPos(5) + "     |");
        System.out.println("|     |     |     |     |");
        System.out.println("|     | " + mostrarPos(6) + " - " + mostrarPos(7) + " - " + mostrarPos(8) + " |     |");
        System.out.println("|     | |       | |     |");
        System.out.println(mostrarPos(9) + " - - " + mostrarPos(10) + "-" + mostrarPos(11) + "       " + mostrarPos(12) + "-" + mostrarPos(13) + " - - " + mostrarPos(14));
        System.out.println("|     | |       | |     |");
        System.out.println("|     | " + mostrarPos(15) + " - " + mostrarPos(16) + " - " + mostrarPos(17) + " |     |");
        System.out.println("|     |     |     |     |");
        System.out.println("|     " + mostrarPos(18) + " - - " + mostrarPos(19) + " - - " + mostrarPos(20) + "     |");
        System.out.println("|           |           |");
        System.out.println(mostrarPos(21) + " - - - - - " + mostrarPos(22) + " - - - - - " + mostrarPos(23));
    }

    private String mostrarPos(int i) {
        switch (this.posicionesDelTablero[i].getJugadorOcupandola()) {
            case AGENTE1:
                return "X";
            case AGENTE2:
                return "O";
            case NOAGENTE:
                return "*";
            default:
                return null;
        }
    }

    public Posicion[] getPosicionesDelTablero() {
        return posicionesDelTablero;
    }

    public void setPosicionesDelTablero(Posicion[] posicionesDelTablero) {
        this.posicionesDelTablero = posicionesDelTablero;
    }

    public int getNumeroPiezasColocadas() {
        return this.numeroPiezasColocadas;
    }
}
