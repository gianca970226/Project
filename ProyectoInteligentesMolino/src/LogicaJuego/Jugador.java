package LogicaJuego;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Jugador implements Serializable {

    protected String nombre;
    protected int numeroFichas;
    protected int numeroFichasEnTablero;
    protected JugadorC fichaJugador;
    protected boolean poderTerminar;
    public int numeroMovimientos;
    public int movimientosQueEliminan = 0;
    protected Jugador() {

        numeroFichasEnTablero = 0;
        poderTerminar = false;
    }

    protected Jugador(JugadorC jugador, int numeroFichasPorJugador) {
        this();
        if (jugador != JugadorC.AGENTE1 && jugador != JugadorC.AGENTE2) {
            try {
                throw new Exception("" + getClass().getName() + " - ficha invalida del jugador: " + jugador);
            } catch (Exception ex) {
                Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            numeroFichas = numeroFichasPorJugador;
            fichaJugador = jugador;
        }
    }

    public void reiniciar() {
        numeroFichasEnTablero = 0;
        poderTerminar = false;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumeroFichas() {
        return this.numeroFichas;
    }

    public int getNumeroFichasEnTablero() {
        return this.numeroFichasEnTablero;
    }

    public int getNumeroFichasQueFaltanPorPoner() {
        return (numeroFichas - numeroFichasEnTablero);
    }

    public int aumentarNumeroFichasEnTablero() {
        poderTerminar = false;
        ++numeroFichasEnTablero;
        return numeroFichasEnTablero;
    }

    public int disminuirNumeroFichasEnTablero() {
        if (--numeroFichasEnTablero == 3) {
            poderTerminar = true;
        }
        return numeroFichasEnTablero;
    }

    public JugadorC getFichaJugador() {
        return fichaJugador;
    }

    public boolean poderTerminar() {
        return poderTerminar;
    }

   
    public abstract int getIndiceParaPonerFicha(Tablero tableroJuego);

    public abstract int getIndiceParaQuitarFichaDeOponente(Tablero tableroJuego);

    public abstract Jugada getFichaAMover(Tablero tableroJuego);
}
