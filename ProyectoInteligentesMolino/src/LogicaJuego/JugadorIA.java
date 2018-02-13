package LogicaJuego;

import java.io.Serializable;
import java.util.*;

public abstract class JugadorIA extends Jugador implements Serializable{

    protected Random aleatorio;
    public int numeroMovimientos;
    public int movimientosQueEliminan = 0;
    private static String[] nombresAleatorios = {"Albert Einstein",
        "Stephen Hawking", "Sheldon Cooper", "Dr.House", "Michael Jackson",
        "Michael Bay", "Mark Zuckerberg", "Alfred Hitchcock", "Amy Whinehouse",
        "Angelina Jolie", "Arnold Schwarzenegger", "Barak Obama", "Batman",
        "David Beckham", "Bruce Willis", "Charlie Chaplin", "Clint Eastwood",
        "Conan O' Brien", "Condoleezza Rice", "Charles Darwin", "Dexter Morgan",
        "Frodo", "Sauron", "George W Bush", "Hannibal", "Harrison Ford", "Harry Potter",
        "John Locke", "Johnny Depp", "John Wayne", "Karl Marx", "Larry King", "Leonardo Dicaprio",
        "Manny Pacquiao", "Marilyn Manson", "Matt Damon", "Meryl Streep", "Mr Bean",
        "Paris Hilton", "Prince Charles", "Quentin Tarantino", "Robert Pattinson",
        "Samuel L. Jackson", "Simon Cowell", "Snoop Lion", "Spielberg", "Steven Seagal",
        "Terminator", "Tom Cruise", "Will Smith", "Nelson Mandela", "Iron Man", "Hulk", "Thor",
        "Loki", "Captain America", "Black Widow", "Phil Coulson"};
    
    public JugadorIA(Ficha jugador,int numeroFichas) throws JuegoException{
        super(jugador,numeroFichas);
        aleatorio= new Random();
        setNombre();
    }
    
    protected void setNombre(){
        nombre= nombresAleatorios[aleatorio.nextInt(nombresAleatorios.length)];
        System.out.println("nombre CPU:"+nombre);
    }
    
    @Override
    public boolean isIA(){
        return true;
    }
    
    public abstract int getIndiceParaPonerFicha(Tablero tableroJuego);
    public abstract int getIndiceParaQuitarFichaDeOponente(Tablero tableroJuego);
    public abstract Movimiento getFichaAMover(Tablero tableroJuego,int faseDelJuego) throws JuegoException;
}
