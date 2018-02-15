
package LogicaJuego;

import java.io.Serializable;


public class Jugada implements Serializable {
    static public final int PONER = 1;
    static public final int JUGADA = 2;
    static public final int BORRAR=3;
    
    public int origen,destino,borrar;
    public final int tipoJugada;
    
    public Jugada(int origen,int destino,int eliminar,int tipo){
        if(tipo!=PONER && tipo!=JUGADA && tipo!=BORRAR){
            System.out.println("Tipo de movimiento invalido");
        }
        this.origen=origen;
        this.destino=destino;
        this.borrar=eliminar;
        this.tipoJugada=tipo;
    }
    
}
