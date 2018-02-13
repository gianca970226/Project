package LogicaJuego;

import java.io.Serializable;

public class JugadorHumano extends Jugador implements Serializable{

    public JugadorHumano(String nombre,Ficha jugador,int numeroPiezasPorJugador)throws JuegoException{
        super(jugador,numeroPiezasPorJugador);
        this.nombre=nombre;
    }
    
    @Override
    public boolean isIA() {
        return false;
    }
  
}
