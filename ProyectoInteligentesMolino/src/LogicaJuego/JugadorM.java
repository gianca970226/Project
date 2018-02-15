package LogicaJuego;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JugadorM extends Jugador implements Serializable {

    private int profundidad;//maneja la profundidad del arbol de busqueda del algoritmo minimax
    private JugadorC oponente;//el tipo de ficha que maneja el enemigo
    private Jugada mejorMovimientoActual;//el mejor movimiento que se puede hacer actualmente
    public int mejorPuntaje = 0;//variables para guardar los mejores puntajes de un movimiento
    static final int maxPuntaje = 1000000;//maximo puntaje

    
    
    //contructor del jugador
    public JugadorM(JugadorC jugador, int numeroFichasPorJugador, int profundidad) throws Exception {
        super(jugador, numeroFichasPorJugador);
        if (profundidad < 1) {
            throw new Exception("" + getClass().getName() + " - profundidad invalidad para el jugador minimax");
        }
        this.profundidad = profundidad;
        oponente = jugador == JugadorC.AGENTE1 ? JugadorC.AGENTE2 : JugadorC.AGENTE1;
    }

    
    
    //metodo con el cual se aplica un movimiento
    private void aplicarMovimiento(Jugada movimiento, JugadorC jugador, Tablero tableroJuego, int faseJuego) {
        try {
            Posicion posicion = tableroJuego.getPosicion(movimiento.destino);
            posicion.setJugadorOcupando(jugador);
            if (faseJuego == ReglasMolino.ETAPAPONER) {
                tableroJuego.incremetarNumeroFichasJugador(jugador);
            } else {
                tableroJuego.getPosicion(movimiento.origen).QuitarFicha();
            }
            
            if (movimiento.borrar != -1) {
                Posicion eliminar = tableroJuego.getPosicion(movimiento.borrar);
                eliminar.QuitarFicha();
                tableroJuego.decrementarNumeroFichasJugador(getFichaOponente(jugador));
            }
        } catch (Exception ex) {
            Logger.getLogger(JugadorM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //metodo que deshace un movimiento hecho
    private void desHacerMovimiento(Jugada movimiento, JugadorC jugador, Tablero tableroJuego, int faseJuego) {
        
        try {
            //deshace el movimiento
            Posicion posicion = tableroJuego.getPosicion(movimiento.destino);
            posicion.QuitarFicha();
            
            //si la fase es de colocar fichas simplementa decrementa la cantidad de
            //fichas del jugador
            if (faseJuego == ReglasMolino.ETAPAPONER) {
                tableroJuego.decrementarNumeroFichasJugador(jugador);
            } else {
                
                //si la fase del juego es movimiento
                //tiene que mover la ficha a la posicion original
                tableroJuego.getPosicion(movimiento.origen).setJugadorOcupando(jugador);
            }
            //si elimino una ficha en el movimiento tiene que volverla a poner
            if (movimiento.borrar != -1) {
                JugadorC opp = getFichaOponente(jugador);
                tableroJuego.getPosicion(movimiento.borrar).setJugadorOcupando(opp);
                tableroJuego.incremetarNumeroFichasJugador(opp);
            }
        } catch (Exception ex) {
            Logger.getLogger(JugadorM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    //metodo que me dice con que ficha juega e oponente
    private JugadorC getFichaOponente(JugadorC jugador) {
        if (jugador == fichaJugador) {
            return oponente;
        } else {
            return fichaJugador;
        }
    }

    @Override
    public int getIndiceParaPonerFicha(Tablero tableroJuego) {
        this.numeroMovimientos = 0;
        this.movimientosQueEliminan = 0;

        try {
            List<Jugada> movimientos = generarMovimientos(tableroJuego, this.fichaJugador, ReglasMolino.ETAPAPONER);
            for (Jugada movimiento : movimientos) {
                this.aplicarMovimiento(movimiento, fichaJugador, tableroJuego, ReglasMolino.ETAPAPONER);
                desHacerMovimiento(movimiento, fichaJugador, tableroJuego, ReglasMolino.ETAPAPONER);
            }
            this.mejorMovimientoActual = movimientos.get(new Random().nextInt(movimientos.size()));
            return mejorMovimientoActual.destino;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return -1;
    }

    private int verificarJuegoTerminado(Tablero tableroJuego) {
        if (tableroJuego.getNumeroPiezasColocadas() == (ReglasMolino.NUMERO_FICHAS_POR_JUGADOR * 2)) {
            try {
                if (tableroJuego.getNumeroFichasJugador(fichaJugador) <= ReglasMolino.NUMERO_MINIMO_FICHAS) {
                    return -maxPuntaje;
                } else if (tableroJuego.getNumeroFichasJugador(oponente) <= ReglasMolino.NUMERO_MINIMO_FICHAS) {
                    return maxPuntaje;
                } else {
                    return 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int getFaseJuego(Tablero tableroJuego, JugadorC jugador) {
        int faseJuego = ReglasMolino.ETAPAPONER;

        try {
            if (tableroJuego.getNumeroPiezasColocadas() == (ReglasMolino.NUMERO_FICHAS_POR_JUGADOR * 2)) {
                faseJuego = ReglasMolino.ETAPAMOVIMIENTO;
                if (tableroJuego.getNumeroFichasJugador(jugador) <= 3) {
                    faseJuego = ReglasMolino.FASE_VUELO;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();;
            System.exit(-1);
        }
        return faseJuego;
    }

    
    
    //metodo que genera unos posibles movimientos de un jugador
    //a partir de un estado del tablero y una fase del juego
    private List<Jugada> generarMovimientos(Tablero tableroJuego, JugadorC jugador, int faseJuego) {
        //creo la lista donde voy a guardar los movimientos posibles
        List<Jugada> movimientos = new ArrayList<>();
        //variables de posicion temporales para manejarlas
        Posicion posicion, posicionAdyacente;
        try {
            
            //si la fase del juego es de poner fichas
            if (faseJuego == ReglasMolino.ETAPAPONER) {
                //itero por todas las posiciones del tablero
                for (int i = 0; i < Tablero.NUM_POSICIONES_DEL_TABLERO; i++) {
                    //creo un movimiento
                    Jugada mov = new Jugada(-7, -1, -1, Jugada.PONER);
                    //si la posicion del tablero no esta ocupada
                    if (!(posicion = tableroJuego.getPosicion(i)).estaOcupado()) {
                        //hago a la posicion ocuparla por el jugador para mirar si se puede acercar a hacer molinos
                        posicion.setJugadorOcupando(jugador);
                        //pongo como indice destino de poner la ficha la posicion
                        mov.destino = i;
                        //miro el movimiento pasa saber si al hacerlo
                        //se hace molino y añadir los movimientos
                        //con todas las posibles fichas a eliminar
                        mirarMovimiento(tableroJuego, jugador, movimientos, mov);
                        
                        //quito la ficha de la posicion
                        posicion.QuitarFicha();
                    }
                }
                
            } 
            //si la fase de juego es la de mover fichas
            else if (faseJuego == ReglasMolino.ETAPAMOVIMIENTO) {
                
                
                //itero por todas las posiciones del tablero
                for (int i = 0; i < Tablero.NUM_POSICIONES_DEL_TABLERO; i++) {
                    //si en la posicion hay una ficha del jugador
                    if ((posicion = tableroJuego.getPosicion(i)).getJugadorOcupandola() == jugador) {
                        
                        //tomo sus posiciones adyacentes
                        int[] posAdyacentes = posicion.getIndicesPosicionesAdjacentes();
                        //itero por las posiciones adyacentes de la posicion
                        for (int j = 0; j < posAdyacentes.length; j++) {
                            //por cada posicion adyacente creo un movimiento
                            Jugada mov = new Jugada(i, -1, -1, Jugada.JUGADA);
                            //tomo la posicion adyacente en una variable temporal
                            posicionAdyacente = tableroJuego.getPosicion(posAdyacentes[j]);
                            //si la posicion adyacente no esta ocupada
                            if (!posicionAdyacente.estaOcupado()) {
                                //ocupo la posicion adyacente para hacer analisis de posibles molinos
                                posicionAdyacente.setJugadorOcupando(jugador);
                                //pongo como indice destino del movimiento a la posicion adyacente
                                mov.destino = posAdyacentes[j];
                                //quito la ficha de posicion para simular que hizo el movimiento
                                posicion.QuitarFicha();
                                //analiza el movimiento para saber si hace molino y añadir los movimientos
                                //con las fichas a eliminar
                                mirarMovimiento(tableroJuego, jugador, movimientos, mov);
                                //deshace los movimientos pues ya hizo los analisis
                                posicion.setJugadorOcupando(jugador);
                                posicionAdyacente.QuitarFicha();
                            }
                        }
                    }
                }
            } 
            //si la fase del juego es la fase final
            else if (faseJuego == ReglasMolino.FASE_VUELO) {
                //variable en la que guardo los indices donde no hay fichas
                List<Integer> espaciosLibres = new ArrayList<>();
                //lista donde guardo las posiciones donde hay fichas del actual jugador
                List<Integer> espaciosJugador = new ArrayList<>();

                
                //itero por todas las posiciones del tablero
                for (int i = 0; i < Tablero.NUM_POSICIONES_DEL_TABLERO; i++) {
                    //si la posicion del tablero la esta ocupando el jugador actual
                    if ((posicion = tableroJuego.getPosicion(i)).getJugadorOcupandola() == jugador) {
                        espaciosJugador.add(i);//añado ese indice de posicion a la lista de espaciosjugador
                    } else if (!posicion.estaOcupado()) {//si la posicion no esta ocupada es un espacio vacio y la añado a 
                        //la lista de espacios vacios
                        espaciosLibres.add(i);
                    }
                }
                //itero pos cada posicion donde hay una ficha del jugador
                for (Integer espacioJugador : espaciosJugador) {
                    
                    //tomo la posicion
                    Posicion posOrigen = tableroJuego.getPosicion(espacioJugador);
                    //quito la posicion
                    posOrigen.QuitarFicha();
                    
                    //cada posicion vacia es una posicion valida para mover
                    for (Integer espacioLibre : espaciosLibres) {
                        //creo el movimiento que va desde la posicion origen y fase movimiento
                        Jugada mov = new Jugada(posOrigen.getIndicePosicion(), -1, -1, Jugada.JUGADA);
                        //el espacio libre es una posicion destino
                        Posicion posDestino = tableroJuego.getPosicion(espacioLibre);
                        //ocupo la posicion destino para posterior analisis
                        posDestino.setJugadorOcupando(jugador);
                        //añado el espacio libre como el indice destino del movimiento
                        mov.destino = espacioLibre;
                        //miro el movimiento para analizar si con el hago molino
                        //y cuantas posibles movimientos para eliminar ficha tengo
                        mirarMovimiento(tableroJuego, jugador, movimientos, mov);
                        //despues de hacer el analisis
                        //removemos el movimiento
                        posDestino.QuitarFicha();
                    }
                    //vuelvo a ocupar la posicion que estaba ocupada
                    posOrigen.setJugadorOcupando(jugador);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        //cuando la profundidad es mayor a 3 evaluar los movimientos y ordenarlos
        //cuando la profundidad es igual o menor a 3 el gasto generado por este metodo no vale la pena
        if (profundidad > 3) {
            //itero por todos los movimientos añadidos arriba
            for (Jugada movimiento : movimientos) {
                
                try {
                    JugadorC jugadorEliminado = JugadorC.NOAGENTE;
                    
                    posicion = tableroJuego.getPosicion(movimiento.destino);
                    
                    //intenta el movimiento para hacer un analisis del puntajes del movimiento
                    posicion.setJugadorOcupando(jugador);
                    
                    if (faseJuego == ReglasMolino.ETAPAPONER) {
                        tableroJuego.incremetarNumeroFichasJugador(jugador);
                    } else {
                        tableroJuego.getPosicion(movimiento.origen).QuitarFicha();
                    }
                    
                    if (movimiento.borrar != -1) {
                        Posicion eliminada = tableroJuego.getPosicion(movimiento.borrar);
                        jugadorEliminado = eliminada.getJugadorOcupandola();
                        eliminada.QuitarFicha();
                        tableroJuego.decrementarNumeroFichasJugador(jugadorEliminado);
                    }
                    

                    
                    //deshace todos los movimientos hechos para el analisis
                    posicion.QuitarFicha();
                    
                    if (faseJuego == ReglasMolino.ETAPAPONER) {
                        tableroJuego.decrementarNumeroFichasJugador(jugador);
                    } else {
                        tableroJuego.getPosicion(movimiento.origen).setJugadorOcupando(jugador);
                    }
                    
                    if (movimiento.borrar != -1) {
                        
                        tableroJuego.getPosicion(movimiento.borrar).setJugadorOcupando(jugadorEliminado);
                        tableroJuego.incremetarNumeroFichasJugador(jugadorEliminado);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(JugadorM.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        //actualiza el numero de movimientos
        this.numeroMovimientos += movimientos.size();
        //retorna la lista de movimientos
        return movimientos;
    }


    @Override
    public int getIndiceParaQuitarFichaDeOponente(Tablero tableroJuego) {
        return this.mejorMovimientoActual.borrar;
    }

    @Override
    public Jugada getFichaAMover(Tablero tableroJuego) {
        numeroMovimientos = 0;
        movimientosQueEliminan = 0;

        try {
            List<Jugada> movimientos = generarMovimientos(tableroJuego, this.fichaJugador, getFaseJuego(tableroJuego,fichaJugador));
            for (Jugada movimiento : movimientos) {
                this.aplicarMovimiento(movimiento, fichaJugador, tableroJuego, ReglasMolino.ETAPAPONER);
                desHacerMovimiento(movimiento, fichaJugador, tableroJuego, ReglasMolino.ETAPAPONER);
            }
            this.mejorMovimientoActual = movimientos.get(new Random().nextInt(movimientos.size()));
            return mejorMovimientoActual;
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    
    
    //metodo que mira si con un movimiento puedo hacer molino
    //y me guarda los movimientos en los cuales puedo eliminar una
    //ficha del enemigo
    //y si no hace molino solo guarda el movimiento
    private void mirarMovimiento(Tablero tableroJuego, JugadorC jugador, List<Jugada> jugadas, Jugada jugada) throws Exception {
        boolean hizoMolino = false;// variable para saber si el jugador con ese movimiento hace molino 
        
        //tenemos que iterar por todas las posibles formas de hacer molino
        for (int i = 0; i < Tablero.NUM_COMBINACIONES_MOLINO; i++) {
            int fichasJugador = 0;//variable para contar cuantas fichas del jugador se encontraron
            boolean fichaSelecionada = false;//variable para saber si encontro la posicion donde va poner la ficha
            Posicion fila[] = tableroJuego.getCombinacionMolino(i);
            for (int j = 0; j < Tablero.NUM_POSICIONES_EN_CADA_MOLINO; j++) {
                
                //si encontro una ficha del jugador aumento la cantidad encontrada
                if (fila[j].getJugadorOcupandola() == jugador) {
                    fichasJugador++;
                }
                //si encontro la posicion donde va a poner ficha el movimiento lo marco como true
                if (fila[j].getIndicePosicion() == jugada.destino) {
                    fichaSelecionada = true;
                }
            }
            //si hay 3 fichas del jugador y ademas tambien se puso en la posicoin del movimiento
            //podemos eliminar una ficha del oponente
            if (fichasJugador == 3 && fichaSelecionada) {
                hizoMolino = true;
                
                //iteramos por todas las posiciones del tablero para 
                //saber en cuales a fichas del enemigo para poder quitar del tablero
                for (int j = 0; j < Tablero.NUM_POSICIONES_DEL_TABLERO; j++) {
                    //obtengo una posicion del tablero
                    Posicion pos = tableroJuego.getPosicion(j);
                    //miro si la posicion no esta siendo ocupada por una ficha del jugador actual y si
                    //la posicion no tiene ficha, en tal caso es una posicion donde puedo eliminar ficha
                    if (pos.getJugadorOcupandola() != jugador && pos.getJugadorOcupandola() != JugadorC.NOAGENTE) {
                        jugada.borrar = j;//añado el indice de la posicion como para eliminar ficha
                        jugadas.add(jugada);//añado el movimiento
                        this.movimientosQueEliminan++;//aumento e contador de los movimientos que eliminan
                    }
                    
                }
            }

            fichaSelecionada = false;
        }
        
        //si no hizo molino añado el movimiento de lo contrario ya estaria añadido
        if (!hizoMolino) {
            jugadas.add(jugada);
        } else {
            hizoMolino = false;
        }
    }
    

}
