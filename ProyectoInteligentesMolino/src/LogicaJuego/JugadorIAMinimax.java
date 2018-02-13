package LogicaJuego;

import java.io.Serializable;
import java.util.*;

public class JugadorIAMinimax extends JugadorIA implements Serializable {

    private int profundidad;//maneja la profundidad del arbol de busqueda del algoritmo minimax
    private Ficha oponente;//el tipo de ficha que maneja el enemigo
    private Movimiento mejorMovimientoActual;//el mejor movimiento que se puede hacer actualmente
    public int mejorPuntaje = 0;//variables para guardar los mejores puntajes de un movimiento
    static final int maxPuntaje = 1000000;//maximo puntaje

    
    
    //contructor del jugador
    public JugadorIAMinimax(Ficha jugador, int numeroFichasPorJugador, int profundidad) throws JuegoException {
        super(jugador, numeroFichasPorJugador);
        if (profundidad < 1) {
            throw new JuegoException("" + getClass().getName() + " - profundidad invalidad para el jugador minimax");
        }
        this.profundidad = profundidad;
        oponente = jugador == Ficha.JUGADOR_1 ? Ficha.JUGADOR_2 : Ficha.JUGADOR_1;
    }

    
    
    //metodo con el cual se aplica un movimiento
    private void aplicarMovimiento(Movimiento movimiento, Ficha jugador, Tablero tableroJuego, int faseJuego) throws JuegoException {
        Posicion posicion = tableroJuego.getPosicion(movimiento.indiceDestino);
        posicion.setJugadorOcupando(jugador);
        if (faseJuego == Juego.FASE_COLOCAR) {
            tableroJuego.incremetarNumeroFichasJugador(jugador);
        } else {
            tableroJuego.getPosicion(movimiento.indiceOrigen).QuitarFicha();
        }

        if (movimiento.eliminarFichaEnIndice != -1) {
            Posicion eliminar = tableroJuego.getPosicion(movimiento.eliminarFichaEnIndice);
            eliminar.QuitarFicha();
            tableroJuego.decrementarNumeroFichasJugador(getFichaOponente(jugador));
        }
    }

    //metodo que deshace un movimiento hecho
    private void desHacerMovimiento(Movimiento movimiento, Ficha jugador, Tablero tableroJuego, int faseJuego) throws JuegoException {
        
        //deshace el movimiento
        Posicion posicion = tableroJuego.getPosicion(movimiento.indiceDestino);
        posicion.QuitarFicha();
        
        //si la fase es de colocar fichas simplementa decrementa la cantidad de 
        //fichas del jugador
        if (faseJuego == Juego.FASE_COLOCAR) {
            tableroJuego.decrementarNumeroFichasJugador(jugador);
        } else {
            
            //si la fase del juego es movimiento 
            //tiene que mover la ficha a la posicion original
            tableroJuego.getPosicion(movimiento.indiceOrigen).setJugadorOcupando(jugador);
        }
        //si elimino una ficha en el movimiento tiene que volverla a poner
        if (movimiento.eliminarFichaEnIndice != -1) {
            Ficha opp = getFichaOponente(jugador);
            tableroJuego.getPosicion(movimiento.eliminarFichaEnIndice).setJugadorOcupando(opp);
            tableroJuego.incremetarNumeroFichasJugador(opp);
        }
    }

    
    //metodo que me dice con que ficha juega e oponente
    private Ficha getFichaOponente(Ficha jugador) {
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
            List<Movimiento> movimientos = generarMovimientos(tableroJuego, this.fichaJugador, Juego.FASE_COLOCAR);
            for (Movimiento movimiento : movimientos) {
                this.aplicarMovimiento(movimiento, fichaJugador, tableroJuego, Juego.FASE_COLOCAR);
                movimiento.puntaje = alphaBeta(oponente, tableroJuego, profundidad - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                desHacerMovimiento(movimiento, fichaJugador, tableroJuego, Juego.FASE_COLOCAR);
            }

            Collections.sort(movimientos, new HeuristicaComparacionMax());

            List<Movimiento> mejoresMovimientos = new ArrayList<>();
            int mejorPuntaje = movimientos.get(0).puntaje;
            mejoresMovimientos.add(movimientos.get(0));

            for (int i = 1; i < movimientos.size(); i++) {
                if (movimientos.get(i).puntaje == mejorPuntaje) {
                    mejoresMovimientos.add(movimientos.get(i));
                } else {
                    break;
                }
            }
            this.mejorMovimientoActual = mejoresMovimientos.get(aleatorio.nextInt(mejoresMovimientos.size()));
            return mejorMovimientoActual.indiceDestino;
        } catch (JuegoException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return -1;
    }

    private int alphaBeta(Ficha jugador, Tablero tableroJuego, int profundidad, int alpha, int beta) {

        int juegoTerminado;
        List<Movimiento> movHijos;

        try {
            int faseJuego = getFaseJuego(tableroJuego, jugador);

            if (profundidad == 0) {
                return evaluar(tableroJuego, faseJuego);
            } else if ((juegoTerminado = verificarJuegoTerminado(tableroJuego)) != 0) {
                return juegoTerminado;
            } else if ((movHijos = generarMovimientos(tableroJuego, jugador, faseJuego)).isEmpty()) {
                if (jugador == fichaJugador) { 
                    return -maxPuntaje;
                } else {
                    return maxPuntaje;
                }
            } else {
                for (Movimiento move : movHijos) {

                    aplicarMovimiento(move, jugador, tableroJuego, faseJuego);

                    if (jugador == fichaJugador) {  // maximizar jugador
                        alpha = Math.max(alpha, alphaBeta(oponente, tableroJuego, profundidad - 1, alpha, beta));

                        if (beta <= alpha) {
                            desHacerMovimiento(move, jugador, tableroJuego, faseJuego);
                            break; // podar
                        }
                    } else {  //  minimizar jugador
                        beta = Math.min(beta, alphaBeta(fichaJugador, tableroJuego, profundidad - 1, alpha, beta));
                        if (beta <= alpha) {
                            desHacerMovimiento(move, jugador, tableroJuego, faseJuego);;
                            break; // podar
                        }
                    }
                    desHacerMovimiento(move, jugador, tableroJuego, faseJuego);;
                }

                if (jugador == fichaJugador) {
                    return alpha;
                } else {
                    return beta;
                }
            }

        } catch (JuegoException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return -1;
    }

    private int verificarJuegoTerminado(Tablero tableroJuego) {
        if (tableroJuego.getNumeroPiezasColocadas() == (Juego.NUMERO_FICHAS_POR_JUGADOR * 2)) {
            try {
                if (tableroJuego.getNumeroFichasJugador(fichaJugador) <= Juego.NUMERO_MINIMO_FICHAS) {
                    return -maxPuntaje;
                } else if (tableroJuego.getNumeroFichasJugador(oponente) <= Juego.NUMERO_MINIMO_FICHAS) {
                    return maxPuntaje;
                } else {
                    return 0;
                }
            } catch (JuegoException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int getFaseJuego(Tablero tableroJuego, Ficha jugador) {
        int faseJuego = Juego.FASE_COLOCAR;

        try {
            if (tableroJuego.getNumeroPiezasColocadas() == (Juego.NUMERO_FICHAS_POR_JUGADOR * 2)) {
                faseJuego = Juego.FASE_MOVIMIENTO;
                if (tableroJuego.getNumeroFichasJugador(jugador) <= 3) {
                    faseJuego = Juego.FASE_VUELO;
                }
            }

        } catch (JuegoException e) {
            e.printStackTrace();;
            System.exit(-1);
        }
        return faseJuego;
    }

    
    
    //metodo que genera unos posibles movimientos de un jugador
    //a partir de un estado del tablero y una fase del juego
    private List<Movimiento> generarMovimientos(Tablero tableroJuego, Ficha jugador, int faseJuego) throws JuegoException {
        //creo la lista donde voy a guardar los movimientos posibles
        List<Movimiento> movimientos = new ArrayList<>();
        //variables de posicion temporales para manejarlas
        Posicion posicion, posicionAdyacente;
        try {
            
            //si la fase del juego es de poner fichas
            if (faseJuego == Juego.FASE_COLOCAR) {
                //itero por todas las posiciones del tablero
                for (int i = 0; i < Tablero.NUM_POSICIONES_DEL_TABLERO; i++) {
                    //creo un movimiento
                    Movimiento mov = new Movimiento(-7, -1, -1, Movimiento.COLOCAR);
                    //si la posicion del tablero no esta ocupada
                    if (!(posicion = tableroJuego.getPosicion(i)).estaOcupado()) {
                        //hago a la posicion ocuparla por el jugador para mirar si se puede acercar a hacer molinos
                        posicion.setJugadorOcupando(jugador);
                        //pongo como indice destino de poner la ficha la posicion
                        mov.indiceDestino = i;
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
            else if (faseJuego == Juego.FASE_MOVIMIENTO) {
                
                
                //itero por todas las posiciones del tablero
                for (int i = 0; i < Tablero.NUM_POSICIONES_DEL_TABLERO; i++) {
                    //si en la posicion hay una ficha del jugador
                    if ((posicion = tableroJuego.getPosicion(i)).getJugadorOcupandola() == jugador) {
                        
                        //tomo sus posiciones adyacentes
                        int[] posAdyacentes = posicion.getIndicesPosicionesAdjacentes();
                        //itero por las posiciones adyacentes de la posicion
                        for (int j = 0; j < posAdyacentes.length; j++) {
                            //por cada posicion adyacente creo un movimiento
                            Movimiento mov = new Movimiento(i, -1, -1, Movimiento.MOVER);
                            //tomo la posicion adyacente en una variable temporal
                            posicionAdyacente = tableroJuego.getPosicion(posAdyacentes[j]);
                            //si la posicion adyacente no esta ocupada
                            if (!posicionAdyacente.estaOcupado()) {
                                //ocupo la posicion adyacente para hacer analisis de posibles molinos
                                posicionAdyacente.setJugadorOcupando(jugador);
                                //pongo como indice destino del movimiento a la posicion adyacente
                                mov.indiceDestino = posAdyacentes[j];
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
            else if (faseJuego == Juego.FASE_VUELO) {
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
                        Movimiento mov = new Movimiento(posOrigen.getIndicePosicion(), -1, -1, Movimiento.MOVER);
                        //el espacio libre es una posicion destino
                        Posicion posDestino = tableroJuego.getPosicion(espacioLibre);
                        //ocupo la posicion destino para posterior analisis
                        posDestino.setJugadorOcupando(jugador);
                        //añado el espacio libre como el indice destino del movimiento
                        mov.indiceDestino = espacioLibre;
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
        } catch (JuegoException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        //cuando la profundidad es mayor a 3 evaluar los movimientos y ordenarlos
        //cuando la profundidad es igual o menor a 3 el gasto generado por este metodo no vale la pena
        if (profundidad > 3) {
            //itero por todos los movimientos añadidos arriba
            for (Movimiento movimiento : movimientos) {
                
                Ficha jugadorEliminado = Ficha.SIN_JUGADOR;

                posicion = tableroJuego.getPosicion(movimiento.indiceDestino);
                
                //intenta el movimiento para hacer un analisis del puntajes del movimiento
                posicion.setJugadorOcupando(jugador);

                if (faseJuego == Juego.FASE_COLOCAR) {
                    tableroJuego.incremetarNumeroFichasJugador(jugador);
                } else {
                    tableroJuego.getPosicion(movimiento.indiceOrigen).QuitarFicha();
                }

                if (movimiento.eliminarFichaEnIndice != -1) {
                    Posicion eliminada = tableroJuego.getPosicion(movimiento.eliminarFichaEnIndice);
                    jugadorEliminado = eliminada.getJugadorOcupandola();
                    eliminada.QuitarFicha();
                    tableroJuego.decrementarNumeroFichasJugador(jugadorEliminado);
                }
                
                //evalua el movimiento dandole un puntaje
                movimiento.puntaje = evaluar(tableroJuego, faseJuego);
                
                //deshace todos los movimientos hechos para el analisis
                posicion.QuitarFicha();

                if (faseJuego == Juego.FASE_COLOCAR) {
                    tableroJuego.decrementarNumeroFichasJugador(jugador);
                } else {
                    tableroJuego.getPosicion(movimiento.indiceOrigen).setJugadorOcupando(jugador);
                }

                if (movimiento.eliminarFichaEnIndice != -1) {

                    tableroJuego.getPosicion(movimiento.eliminarFichaEnIndice).setJugadorOcupando(jugadorEliminado);
                    tableroJuego.incremetarNumeroFichasJugador(jugadorEliminado);
                }

            }
            //si el jugador es el jugadro actual ordena en orden a la mejor ganancia
            if (jugador == fichaJugador) {
                Collections.sort(movimientos, new HeuristicaComparacionMax());
            } else {
                //sino ordena a la menor perdida
                Collections.sort(movimientos, new HeuristicaComparacionMin());
            }
        }
        //actualiza el numero de movimientos
        this.numeroMovimientos += movimientos.size();
        //retorna la lista de movimientos
        return movimientos;
    }

    
    
    
    //metodo que utilizo para evaluar el puntaje de un estado del tablero en una fase del mismo juego
    private int evaluar(Tablero tableroJuego, int faseJuego) throws JuegoException {
        //variable donde guardare el puntaje
        int puntaje = 0;
         
        //variables para contar cuantos molinos tiene el jugador y el oponente
        int R1_numeroMolinosJugador = 0, R1_numeroMolinosOponente = 0;
        //varialbes para contar cuantas posisbles configuraciones de 2 fichas tiene el jugador y el oponente
        int R2_numeroConfiguracionDosFichasJugador = 0, R2_numeroConfiguracionDosFichasOponente = 0;
        
        
        //itero por todas las posibles conminaciones de molino
        for (int i = 0; i < Tablero.NUM_COMBINACIONES_MOLINO; i++) {
            
            int fichasJugador = 0, posicionesVacias = 0, fichasOponente = 0;

            try {
                
                Posicion fila[] = tableroJuego.getCombinacionMolino(i);
                for (int j = 0; j < Tablero.NUM_POSICIONES_EN_CADA_MOLINO; j++) {
                    if (fila[j].getJugadorOcupandola() == fichaJugador) {
                        fichasJugador++;
                    } else if (fila[j].getJugadorOcupandola() == Ficha.SIN_JUGADOR) {
                        posicionesVacias++;
                    } else {
                        fichasOponente++;
                    }
                }
            } catch (JuegoException e) {
                e.printStackTrace();
            }

            if (fichasJugador == 3) {
                R1_numeroMolinosJugador++;
            } else if (fichasJugador == 2 && posicionesVacias == 1) {
                R2_numeroConfiguracionDosFichasJugador++;
            } else if (fichasJugador == 1 && posicionesVacias == 2) {
                puntaje += 1;
            } else if (fichasOponente == 3) {
                R1_numeroMolinosOponente++;
            } else if (fichasOponente == 2 && posicionesVacias == 1) {
                R2_numeroConfiguracionDosFichasOponente++;
            } else if (fichasOponente == 1 && posicionesVacias == 2) {
                puntaje += -1;
            }

            Ficha jugadorEnPosicion = tableroJuego.getPosicion(i).getJugadorOcupandola();
            
            if (i == 4 || i == 10 || i == 13 || i == 19) {
                if (jugadorEnPosicion == fichaJugador) {
                    puntaje += 2;
                } else if (jugadorEnPosicion != Ficha.SIN_JUGADOR) {
                    puntaje -= 2;
                }
            } else if (i == 1 || i == 9 || i == 14 || i == 22
                    || i == 7 || i == 11 || i == 12 || i == 16) {
                if (jugadorEnPosicion == fichaJugador) {
                    puntaje += 1;
                } else if (jugadorEnPosicion != Ficha.SIN_JUGADOR) {
                    puntaje -= 1;
                }
            }
            
            
        }

        int coef;

        if (faseJuego == Juego.FASE_COLOCAR) {
            coef = 80;
        } else if (faseJuego == Juego.FASE_MOVIMIENTO) {
            coef = 120;
        } else {
            coef = 180;
        }

        puntaje += coef * R1_numeroMolinosJugador;
        puntaje -= coef * R1_numeroMolinosOponente;

        if (faseJuego == Juego.FASE_COLOCAR) {
            coef = 10;
        } else if (faseJuego == Juego.FASE_MOVIMIENTO) {
            coef = 8;
        } else {
            coef = 6;
        }
        puntaje += coef * tableroJuego.getNumeroFichasJugador(fichaJugador);
        puntaje -= coef * tableroJuego.getNumeroFichasJugador(oponente);

        // number of 2 pieces and 1 free spot configuration
        if (faseJuego == Juego.FASE_COLOCAR) {
            coef = 12;
        } else {
            coef = 10;
        }
        puntaje += coef * R2_numeroConfiguracionDosFichasJugador;
        puntaje -= coef * R2_numeroConfiguracionDosFichasOponente;

        if (faseJuego == Juego.FASE_COLOCAR) {
            coef = 10;
        } else {
            coef = 25;
        }

        return puntaje;
    }

    private class HeuristicaComparacionMax implements Comparator<Movimiento> {

        @Override
        public int compare(Movimiento m1, Movimiento m2) {
            return m2.puntaje - m1.puntaje;
        }
    }

    private class HeuristicaComparacionMin implements Comparator<Movimiento> {

        @Override
        public int compare(Movimiento m1, Movimiento m2) {
            return m1.puntaje - m2.puntaje;
        }
    }

    @Override
    public int getIndiceParaQuitarFichaDeOponente(Tablero tableroJuego) {
        return this.mejorMovimientoActual.eliminarFichaEnIndice;
    }

    @Override
    public Movimiento getFichaAMover(Tablero tableroJuego, int faseDelJuego) throws JuegoException {
        numeroMovimientos = 0;
        movimientosQueEliminan = 0;

        try {
            List<Movimiento> movimientos = generarMovimientos(tableroJuego, this.fichaJugador, getFaseJuego(tableroJuego,fichaJugador));
            for (Movimiento movimiento : movimientos) {
                this.aplicarMovimiento(movimiento, fichaJugador, tableroJuego, Juego.FASE_COLOCAR);
                movimiento.puntaje = alphaBeta(oponente, tableroJuego, profundidad - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                desHacerMovimiento(movimiento, fichaJugador, tableroJuego, Juego.FASE_COLOCAR);
            }

            Collections.sort(movimientos, new HeuristicaComparacionMax());
             List<Movimiento> mejoresMovimientos = new ArrayList<>();
            int mejorPuntaje = movimientos.get(0).puntaje;
            mejoresMovimientos.add(movimientos.get(0));

            for (int i = 1; i < movimientos.size(); i++) {
                if (movimientos.get(i).puntaje == mejorPuntaje) {
                    mejoresMovimientos.add(movimientos.get(i));
                } else {
                    break;
                }
            }
            this.mejorMovimientoActual = mejoresMovimientos.get(aleatorio.nextInt(mejoresMovimientos.size()));
            return mejorMovimientoActual;
            
            
        } catch (JuegoException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
        
        return null;
    }

    
    
    //metodo que mira si con un movimiento puedo hacer molino
    //y me guarda los movimientos en los cuales puedo eliminar una
    //ficha del enemigo
    //y si no hace molino solo guarda el movimiento
    private void mirarMovimiento(Tablero tableroJuego, Ficha jugador, List<Movimiento> movimientos, Movimiento movimiento) throws JuegoException {
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
                if (fila[j].getIndicePosicion() == movimiento.indiceDestino) {
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
                    if (pos.getJugadorOcupandola() != jugador && pos.getJugadorOcupandola() != Ficha.SIN_JUGADOR) {
                        movimiento.eliminarFichaEnIndice = j;//añado el indice de la posicion como para eliminar ficha
                        movimientos.add(movimiento);//añado el movimiento
                        this.movimientosQueEliminan++;//aumento e contador de los movimientos que eliminan
                    }
                    
                }
            }

            fichaSelecionada = false;
        }
        
        //si no hizo molino añado el movimiento de lo contrario ya estaria añadido
        if (!hizoMolino) {
            movimientos.add(movimiento);
        } else {
            hizoMolino = false;
        }
    }
    

}
