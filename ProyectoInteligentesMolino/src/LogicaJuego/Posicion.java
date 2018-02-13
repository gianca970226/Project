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
public class Posicion implements Serializable {
    
    //me indica si una posicion del tablero esta ocupada
    private boolean estaOcupado;
    //indice de la posicion, identificador dentro del tablero
    private int indicedePosicion;
    //me indica si la posicion esta siendo ocupada por una ficha de un jugador, o esta vacio
    private Ficha jugadorOcupando;
    //me indica las posiciones que estan adyacentes
    //a esta posicion  y son a las cuales se pueden mover fichas
    private int[]indicesdePosicionesAdyacentes;
    
    
    //constructor al cual le entra por parametro
    //el numero de posicion o el indice que le correponde
    public Posicion(int posicion){
        //al inicio la posicion no esta ocupada
        estaOcupado=false;
        //a�ado el valor que entra por parametro
        //a la variable indicedePosicion
        this.indicedePosicion=posicion;
        //como la posicion esta vacia
        //le asignamos a jugadorOcupando como sin jugador
        jugadorOcupando=Ficha.SIN_JUGADOR;
        
    }
    
    //metodo que me dice si la posicion esta ocupada
    //o vacia
    public boolean estaOcupado(){
        return this.estaOcupado;
    }
    
    //metodo que me devuelve el indice de la posicion
    public int getIndicePosicion(){
        return this.indicedePosicion;
    }
    
    //metodo que me devuelve la ficha del 
    //jugador que esta ocupando la posicion
    //en caso de estar ocupada
    public Ficha getJugadorOcupandola(){
        return this.jugadorOcupando;
    }
    
    //metodo que me sirve
    //para cambiar la ficha del jugador que esta ocupando
    //esta ocupando esta posicion
    public void setJugadorOcupando(Ficha jugador){
        //cambio el valor de ocupado a true
        this.estaOcupado=true;
        this.jugadorOcupando=jugador;
    }
    
    //metodo que quita la ficha  de la posicion
    //y returna la ficha del jugador
    //que estuvo ocupando esta posicion
    public Ficha QuitarFicha(){
        this.estaOcupado=false;
        Ficha jugadorAnterior=this.jugadorOcupando;
        this.jugadorOcupando=Ficha.SIN_JUGADOR;
        return jugadorAnterior;
    }
    
    //metodo que a�ade las posicioines adjacentes
    //a esta posicion cuan la posicion solo tiene 2 posicion adjacentes
    // osea cuando la posicion esta en una esquina
    public void añadirIndecesPosicionesAdjacentes(int posicion1,int posicion2){
        this.indicesdePosicionesAdyacentes = new int [2];
        this.indicesdePosicionesAdyacentes[0]=posicion1;
        this.indicesdePosicionesAdyacentes[1]=posicion2;
    }
    
    //a�ade las posiciones adjacentes a esta posicion
    //cuando la posicion tiene 3 posiciones adjacentes y  esta en el medio del 
    //los cuadro exterior y interior
    public void añadirIndecesPosicionesAdjacentes(int posicion1,int posicion2,int posicion3){
        this.indicesdePosicionesAdyacentes = new int [3];
        this.indicesdePosicionesAdyacentes[0]=posicion1;
        this.indicesdePosicionesAdyacentes[1]=posicion2;
        this.indicesdePosicionesAdyacentes[2]=posicion3;
    }
    
     //a�ade las posiciones adjacentes a esta posicion
    //cuando la posicion tiene 4 posiciones adjacentes y esta en el medio del 
    //cuadro del medio
    public void añadirIndecesPosicionesAdjacentes(int posicion1,int posicion2,int posicion3,int posicion4){
        this.indicesdePosicionesAdyacentes = new int [4];
        this.indicesdePosicionesAdyacentes[0]=posicion1;
        this.indicesdePosicionesAdyacentes[1]=posicion2;
        this.indicesdePosicionesAdyacentes[2]=posicion3;
        this.indicesdePosicionesAdyacentes[3]=posicion4;
    }
    
    //metodo que retorna las posicionas adjacentes a esta posicion
    public int [] getIndicesPosicionesAdjacentes(){
        return this.indicesdePosicionesAdyacentes;
    }
    
    //metodo que me dice si esta posicion es 
    //adjacente a una que entra por parametro
    public boolean esAdjacenteAEsta(int posicion){
        for(int i=0;i<this.indicesdePosicionesAdyacentes.length;i++){
            if(indicesdePosicionesAdyacentes[i]==posicion){
                return true;
            }
        }
        return false;
    }
}
