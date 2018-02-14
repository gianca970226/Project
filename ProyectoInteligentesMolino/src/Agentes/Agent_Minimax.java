package Agentes;

import GUI.*;
import LogicaJuego.*;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Agent_Minimax extends Agent {

    JugadorIAMinimax jugadorIA;
    Juego juego;
    PhysicalBoardPlayer tablero;
    boolean inicioJuego;

    @Override
    public void setup() {

        System.out.println("INICIANDO AGENTE MINIMAX");

        this.juego = new Juego();

        Object[] args = getArguments();

        Ficha jugador = (Ficha) args[0];

        iniciarTablero(this);

        int numeroFichasPorJugador = (int) args[1];
        int profundidad = (int) args[2];
        inicioJuego = (boolean) args[3];
        inicializarJugadorIA(jugador, numeroFichasPorJugador, profundidad);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Agent_Minimax.class.getName()).log(Level.SEVERE, null, ex);
        }
        addBehaviour(new JugarMolino(inicioJuego));
    }

    private void iniciarTablero(Agent_Minimax agente) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                tablero = new PhysicalBoardPlayer();
                tablero.setPlayerM(agente);
                tablero.setTitle(getLocalName());
                tablero.setVisible(true);
            }
        });
    }

    public void inicializarJugadorIA(Ficha ficha, int numeroDeFichas, int profundidad) {
        try {
            this.jugadorIA = new JugadorIAMinimax(ficha, numeroDeFichas, profundidad);
        } catch (JuegoException ex) {
            System.out.println("FALLO AL INICIAR JUGADOR MINIMAX");
            ex.printStackTrace();
        }
    }

    public void setIniciaJuego(boolean inicioJuego) {
        this.inicioJuego = inicioJuego;
    }

    public boolean isInicioJuego() {
        return inicioJuego;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public PhysicalBoardPlayer getTablero() {
        return tablero;
    }

    public void setTablero(PhysicalBoardPlayer Tablero) {
        this.tablero = Tablero;
    }

    class JugarMolino extends SimpleBehaviour {

        boolean isGameOver;

        public JugarMolino(boolean inicioJuego) {
            System.out.println("INICIALIZANDO EL COMPORTAMIENTO  JUEGO MOLINO");
            isGameOver = false;
            if (inicioJuego) {
                hacerMovimientoFaseColocar();
            }
        }

        private void hacerMovimientoFaseColocar() {
            int indiceTablero = jugadorIA.getIndiceParaPonerFicha(juego.getTableroDelJuego());
            try {
                if (juego.ponerFichaJugador(indiceTablero, jugadorIA.getFichaJugador())) {
                    jugadorIA.aumentarNumeroFichasEnTablero();
                    if (juego.hizoUnMolino(indiceTablero, jugadorIA.getFichaJugador())) {
                        Ficha fichaOponente = jugadorIA.getFichaJugador() == Ficha.JUGADOR_1 ? Ficha.JUGADOR_2 : Ficha.JUGADOR_1;

                        int indiceEliminar = jugadorIA.getIndiceParaQuitarFichaDeOponente(juego.getTableroDelJuego());
                        if (juego.quitarFicha(indiceEliminar, fichaOponente)) {
                            System.out.println("ELIMINO FICHA DEL OPONENTE DE LA POSICION:" + indiceEliminar);
                        } else {
                            System.out.println("ERROR AL ELIMINAR FICHA DEL OPONENTE");
                        }
                    }
                    tablero.ActualizarTableroMinimax();
                    enviar();
                } else {
                    System.out.println("NO SE PUEDE PONER UNA FICHA EN LA POSICICON" + indiceTablero);
                }

            } catch (JuegoException ex) {
                System.out.println("ERROR AL PONER UNA FICHA EN EL JUGADOR MINIMAX");
                ex.printStackTrace();
            }
        }

        private void moverFicha() {
            int indiceOrigen, indiceDestino;
            Movimiento movimiento = null;
            try {
                movimiento = jugadorIA.getFichaAMover(juego.getTableroDelJuego(), juego.getFaseActualDelJuego());
                indiceOrigen = movimiento.indiceOrigen;
                indiceDestino = movimiento.indiceDestino;

                int resultado;
                if ((resultado = juego.moverFichaDe(indiceOrigen, indiceDestino, jugadorIA.getFichaJugador())) == Juego.MOVIMIENTO_VALIDO) {

                    if (juego.hizoUnMolino(indiceDestino, jugadorIA.getFichaJugador())) {
                        Ficha fichaOponente = jugadorIA.getFichaJugador() == Ficha.JUGADOR_1 ? Ficha.JUGADOR_2 : Ficha.JUGADOR_1;

                        int indiceEliminar = jugadorIA.getIndiceParaQuitarFichaDeOponente(juego.getTableroDelJuego());
                        if (juego.quitarFicha(indiceEliminar, fichaOponente)) {
                            System.out.println("ELIMINO FICHA EN POSICICON :" + indiceEliminar);
                        } else {
                            System.out.println("ERROR AL ELIMINAR FICHA DEL OPONENTE");
                        }
                    }
                    tablero.ActualizarTableroMinimax();
                    enviar();
                } else {
                    System.out.println("MOVIMIENTO INVALIDO :" + indiceOrigen + " a:" + indiceDestino);
                }
            } catch (JuegoException ex) {
                System.out.println("ERROR AL BUSCAR MOVIMIENTO!!");
                ex.printStackTrace();
            }
        }

        private void hacerMovimiento() throws JuegoException {
            if (juego.getFaseActualDelJuego() == Juego.FASE_COLOCAR) {
                System.out.println("FASE_COLOCAR MINIMAX");
                hacerMovimientoFaseColocar();
            } else if (!juego.finalizoElJuego()) {
                System.out.println("JUEGO FINALIZADO:" + juego.finalizoElJuego());
                System.out.println("FASE_MOVIMIENTO MINIMAX");
                moverFicha();
            } else {
                int jugador=juego.quienGano();
                if(jugador==0){
                    JOptionPane.showMessageDialog(null,"JUEGO TERMINO!!, ES UN EMPATE");
                }else if(jugador==1){
                    JOptionPane.showMessageDialog(null,"JUEGO TERMINO!!, GANADOR JUGADOR 2");
                }else{
                    JOptionPane.showMessageDialog(null,"JUEGO TERMINO!!, GANADOR JUGADOR 1");
                }
                isGameOver = true;
                enviar();
                System.out.println("EL JUEGO TERMINO");
            }
        }

        private void enviar() {
            System.out.println("ENVIANDO RESPUESTA");
            try {
                AID idReceptor = new AID();
                if (getLocalName().equals("Player_1")) {
                    idReceptor.setLocalName("Player_2");;
                } else {
                    idReceptor.setLocalName("Player_1");
                }
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.setSender(getAID());
                msg.addReceiver(idReceptor);
                msg.setContentObject(juego);
                send(msg);
                juego.setMiTurno(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERROR AL ENVIAR MENSAJE");
            }
        }

        @Override
        public void action() {
            ACLMessage mensaje = blockingReceive();
            if (mensaje != null) {
                try {
                    Juego juegoTemporal = (Juego) mensaje.getContentObject();
                    System.out.println("RECIBI TABLERO DE:" + mensaje.getSender().getLocalName());
                    juegoTemporal.getTableroDelJuego().imprimirTablero();
                    if (juegoTemporal.isMiTurno()) {

                        setJuego(juegoTemporal);
                        try {
                            getTablero().ActualizarTableroMinimax();
                            hacerMovimiento();
                        } catch (JuegoException ex) {
                            System.out.println("ERROR AL ACTUALIZAR TABLERO");
                            ex.printStackTrace();
                        }

                    } else {
                        System.out.println("EL JUEGO NO ESTA ACTIVO!!");
                    }
                    
                } catch (UnreadableException ex) {
                    System.out.println("NO PUDE RECIBIR EL MENSAJE!!");
                    ex.printStackTrace();
                }
            }
        }

        @Override
        public boolean done() {
            return isGameOver;
        }
    }
}
