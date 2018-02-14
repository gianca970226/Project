
package LogicaJuego;

import java.io.Serializable;


public class Movimiento implements Serializable {
    static public final int COLOCAR = 1;
    static public final int MOVER = 2;
    static public final int ELIMINAR=3;
    
    public int indiceOrigen,indiceDestino,eliminarFichaEnIndice;
    public final int tipoDeMovimiento;
    
    public Movimiento(int origen,int destino,int eliminar,int tipo){
        if(tipo!=COLOCAR && tipo!=MOVER && tipo!=ELIMINAR){
            System.out.println("Tipo de movimiento invalido");
        }
        this.indiceOrigen=origen;
        this.indiceDestino=destino;
        this.eliminarFichaEnIndice=eliminar;
        this.tipoDeMovimiento=tipo;
    }
    
}
