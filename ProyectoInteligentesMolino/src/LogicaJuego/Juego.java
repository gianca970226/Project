package LogicaJuego;

import java.io.Serializable;

public class Juego implements Serializable {

    static public final int NUMERO_FICHAS_POR_JUGADOR = 9;
    static public final int FASE_COLOCAR = 1;
    static public final int FASE_MOVIMIENTO = 2;
    static public final int FASE_VUELO = 3;
    static public final int FASE_ELIMINAR = 4;

    static public final int POSICION_INVALIDA = -1;
    static public final int POSICION_NO_DISPONIBLE = -2;
    static public final int MOVIMIENTO_INVALIDO = -3;
    static public final int MOVIMIENTO_VALIDO = 0;

    static public final int NUMERO_MINIMO_FICHAS = 2;

    protected Tablero tableroJuego;//tablero del molino
    protected int faseJuego;//variable que me controla la fase del juego

    boolean miTurno = true;

    //constructor, inicializo el tablero y la fase inicial
    //es la de poner fichas
    public Juego() {
        tableroJuego = new Tablero();
        faseJuego = Juego.FASE_COLOCAR;
    }

    //metodo que devuelve la fase actual del juego
    public int getFaseActualDelJuego() {
        return this.faseJuego;
    }

    //metodo que devuelve e tablero del juego
    public Tablero getTableroDelJuego() {
        return this.tableroJuego;
    }

    public int getFaseJuego() {
        return faseJuego;
    }

    public boolean isMiTurno() {
        return miTurno;
    }

    public void setMiTurno(boolean Activo) {
        this.miTurno = Activo;
    }

    public void setFaseJuego(int faseJuego) {
        this.faseJuego = faseJuego;
    }

    //metodo que me dice que ficha de que jugador esta ocupando una posicion del tablero
    public Ficha getJugadorEnPosicionDelTablero(int posicionTablero) {
        try {
            return this.tableroJuego.getPosicion(posicionTablero).getJugadorOcupandola();
        } catch (JuegoException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return Ficha.SIN_JUGADOR;
    }

    //metodo que me dice si una posicion del tablero esta disponible
    public boolean estaDisponiblePosicion(int indiceTablero) throws JuegoException {
        return this.tableroJuego.posicionEstaDisponible(indiceTablero);
    }

    //metodo que me dice si un movimiento de una ficha
    // es valido
    public boolean movimientoValido(int indicePosicionActual, int indiceSiguientePosicion) throws JuegoException {
        Posicion posicionActual = this.tableroJuego.getPosicion(indicePosicionActual);
        //en caso de que las posiciones sean adyacentes y la posicion siguiente
        //no este ocupada el movimiento es valido
        if (posicionActual.esAdjacenteAEsta(indiceSiguientePosicion) && !this.tableroJuego.getPosicion(indiceSiguientePosicion).estaOcupado()) {
            return true;
        }
        return false;
    }

    //metodo utilizado en la fase del juego de moviemiento de fechas en el cual se mueve una ficha de una posicion a otra
    public int moverFichaDe(int indiceInicial, int indiceDestino, Ficha jugador) throws JuegoException {
        if (posicionTieneFichaDeJugador(indiceInicial, jugador)) {
            if (estaDisponiblePosicion(indiceDestino)) {
                if (movimientoValido(indiceInicial, indiceDestino) || (this.tableroJuego.getNumeroFichasJugador(jugador) == Juego.NUMERO_MINIMO_FICHAS + 1)) {
                    this.tableroJuego.getPosicion(indiceInicial).QuitarFicha();
                    this.tableroJuego.getPosicion(indiceDestino).setJugadorOcupando(jugador);
                    return Juego.MOVIMIENTO_VALIDO;
                } else {
                    return Juego.MOVIMIENTO_INVALIDO;
                }
            } else {
                return Juego.POSICION_NO_DISPONIBLE;
            }
        } else {
            return Juego.POSICION_INVALIDA;
        }
    }

    //metodo que me dice si en una determinada posicion hay una ficha de un determinado jugador
    public boolean posicionTieneFichaDeJugador(int indiceTablero, Ficha jugador) throws JuegoException {
        return (this.tableroJuego.getPosicion(indiceTablero).getJugadorOcupandola() == jugador);
    }

    //metodo utilizado en la fase de poner fichas, pone un ficha de un jugador en una posicion determinada
    //si es posible
    public boolean ponerFichaJugador(int indiceTablero, Ficha jugador) throws JuegoException {
        if (this.tableroJuego.posicionEstaDisponible(indiceTablero)) {
            tableroJuego.getPosicion(indiceTablero).setJugadorOcupando(jugador);
            tableroJuego.incremetarNumeroFichasJugador(jugador);
            if (tableroJuego.incremetarPiezasColocadasTablero() == (Juego.NUMERO_FICHAS_POR_JUGADOR * 2)) {
                this.faseJuego = Juego.FASE_MOVIMIENTO;
            }
            return true;
        }
        return false;
    }

    //metodo que me dice si al poner una ficha el jugador hizo un molino
    public boolean hizoUnMolino(int indiceDestino, Ficha jugador) throws JuegoException {
        int numeroMaximoDeFichasdelJugadorEnFila = 0;
        boolean encontroMolino = false;//esta linea es adicional puede ser eliminada si daña el funcionamiento
        for (int i = 0; i < Tablero.NUM_COMBINACIONES_MOLINO && !encontroMolino; i++) {
            Posicion[] fila = this.tableroJuego.getCombinacionMolino(i);
            for (int j = 0; j < Tablero.NUM_POSICIONES_EN_CADA_MOLINO && !encontroMolino; j++) {
                if (fila[j].getIndicePosicion() == indiceDestino) {
                    int fichasJugadorEnEstaFila = numeroDeFichasDelJugadorEnFila(fila, jugador);
                    if (fichasJugadorEnEstaFila > numeroMaximoDeFichasdelJugadorEnFila) {
                        numeroMaximoDeFichasdelJugadorEnFila = fichasJugadorEnEstaFila;
                    }
                    if (numeroMaximoDeFichasdelJugadorEnFila == Tablero.NUM_POSICIONES_EN_CADA_MOLINO) {
                        encontroMolino = true;
                    }
                }

            }
        }
        return encontroMolino;
    }

    //metodo que me dice cuantas fichas de un jugador estan en una fila
    private int numeroDeFichasDelJugadorEnFila(Posicion[] fila, Ficha jugador) {

        int contador = 0;
        for (int i = 0; i < fila.length; i++) {
            if (fila[i].getJugadorOcupandola() == jugador) {
                contador++;
            }

        }
        return contador;
    }

    //metodo que imprime el tablero del juego
    public void imprimirTableroDeJuego() {
        this.tableroJuego.imprimirTablero();
    }

    //metodo que quita una ficha del tablero de un jugador si es posible
    public boolean quitarFicha(int indiceTablero, Ficha jugador) throws JuegoException {
        if (!this.tableroJuego.posicionEstaDisponible(indiceTablero) && this.posicionTieneFichaDeJugador(indiceTablero, jugador)) {
            this.tableroJuego.getPosicion(indiceTablero).QuitarFicha();
            this.tableroJuego.decrementarNumeroFichasJugador(jugador);
            if (this.faseJuego == Juego.FASE_MOVIMIENTO && this.tableroJuego.getNumeroFichasJugador(jugador) == Juego.NUMERO_MINIMO_FICHAS + 1) {
                this.faseJuego = Juego.FASE_VUELO;
                System.out.println("fase del juego es la fase:" + this.faseJuego);
            }
            return true;
        }
        return false;
    }

    //metodo que mira si el juego ya se termino
    public boolean finalizoElJuego() {
        try {
            if ((tableroJuego.getNumeroFichasJugador(Ficha.JUGADOR_1) == Juego.NUMERO_MINIMO_FICHAS
                    || tableroJuego.getNumeroFichasJugador(Ficha.JUGADOR_2) == Juego.NUMERO_MINIMO_FICHAS)
                    && this.getFaseActualDelJuego() != Juego.FASE_COLOCAR) {
                return true;
            } else {
                boolean j1TieneMovValido = false, j2TieneMovValido = false;
                Ficha jugador;

                // mirar si cada jugador tiene al menos un movimiento valido
                for (int i = 0; i < Tablero.NUM_POSICIONES_DEL_TABLERO; i++) {
                    Posicion posicion = tableroJuego.getPosicion(i);
                    if ((jugador = posicion.getJugadorOcupandola()) != Ficha.SIN_JUGADOR) {
                        int[] adjacent = posicion.getIndicesPosicionesAdjacentes();
                        for (int j = 0; j < adjacent.length; j++) {
                            Posicion adjacentPos = tableroJuego.getPosicion(adjacent[j]);
                            if (!adjacentPos.estaOcupado()) {
                                if (!j1TieneMovValido) { // cambio si la variable es false
                                    j1TieneMovValido = (jugador == Ficha.JUGADOR_1);
                                }
                                if (!j2TieneMovValido) {
                                    j2TieneMovValido = (jugador == Ficha.JUGADOR_2);
                                }
                                break;
                            }
                        }
                    }
                    if (j1TieneMovValido && j2TieneMovValido) {
                        return false;
                    }
                }
            }
        } catch (JuegoException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return true;
    }

    public int quienGano() throws JuegoException {
        if (this.tableroJuego.getNumeroFichasJugador(Ficha.JUGADOR_1) == Juego.NUMERO_MINIMO_FICHAS) {
            return 1;
        } else if (this.tableroJuego.getNumeroFichasJugador(Ficha.JUGADOR_2) == Juego.NUMERO_MINIMO_FICHAS) {
            return 2;
        } else {
             boolean j1TieneMovValido = false, j2TieneMovValido = false;
             Ficha jugador;
            for (int i = 0; i < Tablero.NUM_POSICIONES_DEL_TABLERO; i++) {
                Posicion posicion = tableroJuego.getPosicion(i);
                if ((jugador = posicion.getJugadorOcupandola()) != Ficha.SIN_JUGADOR) {
                    int[] adjacent = posicion.getIndicesPosicionesAdjacentes();
                    for (int j = 0; j < adjacent.length; j++) {
                        Posicion adjacentPos = tableroJuego.getPosicion(adjacent[j]);
                        if (!adjacentPos.estaOcupado()) {
                            if (!j1TieneMovValido) { // cambio si la variable es false
                                j1TieneMovValido = (jugador == Ficha.JUGADOR_1);
                            }
                            if (!j2TieneMovValido) {
                                j2TieneMovValido = (jugador == Ficha.JUGADOR_2);
                            }
                            break;
                        }
                    }
                }
            }
            
            if(!j1TieneMovValido && !j2TieneMovValido){
                return 0;
            }else
            if(!j1TieneMovValido){
                return 1;
            }else if(!j2TieneMovValido){
                return 2;
            }
            return 0;
        }
    }
}